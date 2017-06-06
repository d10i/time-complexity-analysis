package tech.dario.timecomplexityanalysis.timerecorder.impl.akka

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import tech.dario.timecomplexityanalysis.timerecorder.tree.{MergeableNode, MergeableTree, Metrics}

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

      val service = system.actorOf(Props.create(classOf[ServiceActor]), "service")

      service ! MethodStarted("Method1", 100)
      service ! MethodStarted("Method2", 200)
      service ! MethodStarted("Method3", 300)
      service ! MethodFinished("Method3", 400)
      service ! MethodFinished("Method2", 500)

      val treeFuture = service ? Save()
      try {
        Await.result(treeFuture, timeout.duration).asInstanceOf[MergeableTree[Metrics]]
      } catch {
        case iae: IllegalArgumentException => iae.getMessage should be ("Finished processing list but current node is not 'root'. Some actions are probably missing from the list.")
      }
    }

    "fail with not-matching method actions" in {
      implicit val timeout = Timeout(10.seconds)

      val service = system.actorOf(Props.create(classOf[ServiceActor]), "service")

      service ! MethodStarted("Method1", 100)
      service ! MethodStarted("Method2", 200)
      service ! MethodStarted("Method3", 300)
      service ! MethodFinished("Method2", 400)
      service ! MethodFinished("Method3", 500)
      service ! MethodFinished("Method1", 600)

      val treeFuture = service ? Save()
      try {
        Await.result(treeFuture, timeout.duration).asInstanceOf[MergeableTree[Metrics]]
      } catch {
        case iae: IllegalArgumentException => iae.getMessage should be ("Received MethodFinished(Method2,400) but was expecting a MethodFinished action with name 'Method3']")
      }
    }

    "tranform the received method actions into a call tree" in {
      implicit val timeout = Timeout(10.seconds)

      val service = system.actorOf(Props.create(classOf[ServiceActor]), "service")

      service ! MethodStarted("Method1", 100)
      service ! MethodStarted("Method2", 200)
      service ! MethodStarted("Method3", 300)
      service ! MethodFinished("Method3", 400)
      service ! MethodStarted("Method4", 500)
      service ! MethodStarted("Method5", 600)
      service ! MethodFinished("Method5", 700)
      service ! MethodFinished("Method4", 800)
      service ! MethodStarted("Method3", 900)
      service ! MethodFinished("Method3", 1000)
      service ! MethodStarted("Method5", 1100)
      service ! MethodFinished("Method5", 1199)
      service ! MethodFinished("Method2", 1300)
      service ! MethodFinished("Method1", 1400)

      val treeFuture = service ? Save()

      val expectedTree = new MergeableTree[Metrics]
      val node1a = new MergeableNode[Metrics]("Method1", new Metrics(1.0d, 1300.0d))
      val node2a = node1a.add(new MergeableNode[Metrics]("Method2", new Metrics(1.0d, 1100.0d)))
      val node3a = node2a.add(new MergeableNode[Metrics]("Method3", new Metrics(2.0d, 200.0d)))
      val node3b = node2a.add(new MergeableNode[Metrics]("Method4", new Metrics(1.0d, 300.0d)))
      val node4a = node3b.add(new MergeableNode[Metrics]("Method5", new Metrics(1.0d, 100.0d)))
      val node3c = node2a.add(new MergeableNode[Metrics]("Method5", new Metrics(1.0d, 99.0d)))
      expectedTree.add(node1a)

      val result = Await.result(treeFuture, timeout.duration).asInstanceOf[MergeableTree[Metrics]]
      result should be (expectedTree)
    }
  }
}
