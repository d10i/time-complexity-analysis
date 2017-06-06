package tech.dario.timecomplexityanalysis.timerecorder.impl.akka

import org.scalatest._

class SortingTest extends FlatSpec with Matchers {
  "A Stack" should "lol" in {
    val l1: List[MethodAction] = List.fill(2000000)(if (scala.util.Random.nextInt % 2 == 0) { MethodStarted("1", scala.util.Random.nextInt) } else { MethodFinished("2", scala.util.Random.nextInt) })
    val l2: List[MethodAction] = List.fill(4000000)(if (scala.util.Random.nextInt % 2 == 0) { MethodStarted("3", scala.util.Random.nextInt) } else { MethodFinished("4", scala.util.Random.nextInt) })
    val l3: List[MethodAction] = List.fill(8000000)(if (scala.util.Random.nextInt % 2 == 0) { MethodStarted("5", scala.util.Random.nextInt) } else { MethodFinished("6", scala.util.Random.nextInt) })
    val l4: List[MethodAction] = List.fill(16000000)(if (scala.util.Random.nextInt % 2 == 0) { MethodStarted("7", scala.util.Random.nextInt) } else { MethodFinished("8", scala.util.Random.nextInt) })

    def time[A](f: => A) = {
      val s = System.nanoTime
      val ret = f
      println("time: " + (System.nanoTime - s) / 1e6 + "ms")
      ret
    }

    def mergeSortedLists[T](a: List[T], b: List[T], cmp: (T, T) => Long): List[T] = {
      import scala.annotation.tailrec
      @tailrec
      def merge[T](res: List[T], a: List[T], b: List[T], cmp: (T, T) => Long): List[T] = {
        (a, b) match {
          case (Nil, Nil) => res
          case (_, Nil) => a.reverse ++ res
          case (Nil, _) => b.reverse ++ res
          case (ahead :: aTail, bHead :: bTail) =>
            if (cmp(ahead, bHead) <= 0) {
              merge(ahead +: res, aTail, b, cmp)
            } else {
              merge(bHead +: res, a, bTail, cmp)
            }
        }
      }

      merge(List(), a, b, cmp).reverse
    }

    time {
      implicit val ordering = Ordering.by { ma: MethodAction => ma.nanoTime }
      (l3 ++ l1 ++ l4 ++ l2).sorted
    }

    time {
      val m1 = mergeSortedLists(l3, l1, (a: MethodAction, b: MethodAction) => a.nanoTime - b.nanoTime)
      val m2 = mergeSortedLists(l4, l2, (a: MethodAction, b: MethodAction) => a.nanoTime - b.nanoTime)
      val m3 = mergeSortedLists(m1, m2, (a: MethodAction, b: MethodAction) => a.nanoTime - b.nanoTime)
    }
  }
}
