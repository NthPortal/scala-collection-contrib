package scala.collection
package decorators

import org.junit.{Assert, Test}
import scala.collection.immutable.{LazyList, List, Range, Map}

class IterableDecoratorTest {

  @Test
  def foldSomeLeft(): Unit = {
      val r = Range(0, 100)
      Assert.assertEquals(0, r.foldSomeLeft(0)((x, y) => None))
      Assert.assertEquals(10, r.foldSomeLeft(0)((x, y) => if (y > 10) None else Some(y)))
      Assert.assertEquals(55, r.foldSomeLeft(0)((x, y) => if (y > 10) None else Some(x + y)))
      Assert.assertEquals(4950, r.foldSomeLeft(0)((x, y) => Some(x + y)))

      Assert.assertEquals(10, List[Int]().foldSomeLeft(10)((x, y) => Some(x + y)))
    }

  @Test def lazyFoldRightIsLazy(): Unit = {
    val xs = LazyList.from(0)
    def chooseOne(x: Int): Either[Int, Int => Int]= if (x < (1 << 16)) Right(identity) else Left(x)

    Assert.assertEquals(1 << 16, xs.lazyFoldRight(0)(chooseOne))
  }

  @Test
  def hasIterableOpsWorksWithStringAndMap(): Unit = {
    val result = "foo".foldSomeLeft(0) { case (_, 'o') => None case (n, _) => Some(n + 1) }
    Assert.assertEquals(1, result)

    val result2 =
      Map(1 -> "foo", 2 -> "bar").foldSomeLeft(0) {
        case (n, (k, _)) => if (k == -1) None else Some(n + 1)
      }
    Assert.assertEquals(2, result2)
  }

}
