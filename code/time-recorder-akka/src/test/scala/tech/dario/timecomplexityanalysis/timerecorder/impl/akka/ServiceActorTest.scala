package tech.dario.timecomplexityanalysis.timerecorder.impl.akka

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import tech.dario.timecomplexityanalysis.timerecorder.tree.{MergeableNode, MergeableTree, Measurement}

import scala.concurrent.Await
import scala.concurrent.duration._

class ServiceActorTest() extends TestKit(ActorSystem("ServiceActor")) with ImplicitSender
  with WordSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "The ServiceActor" must {
    "fail with method actions missing" in {
      implicit val timeout = Timeout(10.seconds)

      val service = system.actorOf(Props.create(classOf[ServiceActor]), "test1")

      service ! MethodActions(
        MethodStarted("Method1", 100),
        MethodStarted("Method2", 200),
        MethodStarted("Method3", 300),
        MethodFinished("Method3", 400),
        MethodFinished("Method2", 500)
      )

      val treeFuture = service ? Save()
      try {
        Await.result(treeFuture, timeout.duration).asInstanceOf[MergeableTree[Measurement]]
        fail("Should have thrown exception")
      } catch {
        case iae: IllegalArgumentException => iae.getMessage should be("Finished processing list but current node is not 'root'. Some actions are probably missing from the list.")
        case _: Exception => fail("Should have thrown IllegalArgumentException")
      }
    }

    "fail with not-matching method actions" in {
      implicit val timeout = Timeout(10.seconds)

      val service = system.actorOf(Props.create(classOf[ServiceActor]), "test2")

      service ! MethodActions(
        MethodStarted("Method1", 100),
        MethodStarted("Method2", 200),
        MethodStarted("Method3", 300),
        MethodFinished("Method2", 400),
        MethodFinished("Method3", 500),
        MethodFinished("Method1", 600)
      )

      val treeFuture = service ? Save()
      try {
        Await.result(treeFuture, timeout.duration).asInstanceOf[MergeableTree[Measurement]]
        fail("Should have thrown exception")
      } catch {
        case iae: IllegalArgumentException => iae.getMessage should be("Received MethodFinished(Method2,400) but was expecting a MethodFinished action with name 'Method3'")
        case _: Exception => fail("Should have thrown IllegalArgumentException")
      }
    }

    "tranform the received method actions into a call tree" in {
      implicit val timeout = Timeout(10.seconds)

      val service = system.actorOf(Props.create(classOf[ServiceActor]), "test3")

      service ! MethodActions(
        MethodStarted("Method1", 100),
        MethodStarted("Method2", 200),
        MethodStarted("Method3", 300),
        MethodFinished("Method3", 400),
        MethodStarted("Method4", 500),
        MethodStarted("Method5", 600),
        MethodFinished("Method5", 700),
        MethodFinished("Method4", 800),
        MethodStarted("Method3", 900)
      )
      service ! MethodActions(
        MethodFinished("Method3", 1000),
        MethodStarted("Method5", 1100),
        MethodFinished("Method5", 1199),
        MethodFinished("Method2", 1300),
        MethodFinished("Method1", 1400)
      )

      val treeFuture = service ? Save()

      val expectedTree = new MergeableTree[Measurement]
      val node1a = new MergeableNode[Measurement]("Method1", new Measurement(1.0d, 1300.0d))
      val node2a = node1a.add(new MergeableNode[Measurement]("Method2", new Measurement(1.0d, 1100.0d)))
      val node3a = node2a.add(new MergeableNode[Measurement]("Method3", new Measurement(2.0d, 200.0d)))
      val node3b = node2a.add(new MergeableNode[Measurement]("Method4", new Measurement(1.0d, 300.0d)))
      val node4a = node3b.add(new MergeableNode[Measurement]("Method5", new Measurement(1.0d, 100.0d)))
      val node3c = node2a.add(new MergeableNode[Measurement]("Method5", new Measurement(1.0d, 99.0d)))
      expectedTree.add(node1a)

      val result = Await.result(treeFuture, timeout.duration).asInstanceOf[MergeableTree[Measurement]]
      result should be(expectedTree)
    }
  }
}
