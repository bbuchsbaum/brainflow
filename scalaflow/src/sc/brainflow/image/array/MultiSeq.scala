package sc.brainflow.image.array

import reflect.ClassManifest
import collection.Seq
import collection.generic.CanBuildFrom

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 24, 2010
 * Time: 10:02:16 PM
 * To change this template use File | Settings | File Templates.
 */

trait MultiSeq[T] extends Seq[T] {
  def numDim() = {
    dim.length
  }

  def dim: Array[Int]

}

trait MultiSeq2D[T] extends MultiSeq[T] {
  self =>

  def indexOf(i: Int, j: Int) = {
    j * dim(0) + i
  }

  def apply(i: Int, j: Int): T = {
    this(indexOf(i, j))
  }

  def chop(axis: Int): Seq[Seq[T]] = axis match {
    case 0 => for (i <- 0 until dim(0)) yield (row(i))
    case 1 => for (i <- 0 until dim(1)) yield (column(i))
    case _ => error("illegal dimension " + axis)
  }

  def map(axis: Int, f: (Seq[T] => T)) = {
     for (x <- chop(axis)) yield f(x)
  }

  def row(row: Int) = new RowView(row)

  def column(col: Int) = new ColumnView(col)


  def along(d: Int): Seq[Int] = d match {
    case 0 => 0 until dim(0)
    case 1 => 0 until dim(1)
    case _ => error("illegal dimension " + d)
  }


  def length = dim(0)*dim(1)

  def iterator = new Iterator[T] {
    var i = -1
    var len = length-1

    def next() = {
      i = i+1
      apply(i)
    }

    def hasNext = i < len
  }


  class ColumnView(val colnum: Int) extends Seq[T] {

    def iterator = new Iterator[T] {
      var i = -1
      var len = dim(1) - 1

      def next() = {
        i = i + 1
        self(i, colnum)
      }

      def hasNext = i < len
    }

    def apply(v1: Int): T = self.apply(colnum, v1)

    def length = self.dim(0)
  }

  class RowView(val rownum: Int) extends Seq[T] {

    def iterator = new Iterator[T] {
      var i = -1
      var len = dim(0) - 1

      def next() = {
        i = i + 1
        self.apply(rownum, i)
      }

      def hasNext = i < len
    }

    def apply(v1: Int): T = self(rownum, v1)

    def length = self.dim(0)
  }

}

trait MultiSeq3D[T] extends MultiSeq[T] {
  self =>
  lazy val d01 = dim(0) * dim(1)
  lazy val d12 = dim(1) * dim(2)
  lazy val d02 = dim(0) * dim(2)

  def indexOf(i: Int, j: Int, k: Int) = {
    k * d01 + j * dim(0) + i
  }

  def apply(i: Int, j: Int, k: Int): T = {
    this(indexOf(i, j, k))
  }

  class CutView02(val slicenum: Int) extends MultiSeq2D[T] {
    require(slicenum >= 0 && slicenum < self.dim(1))
    val dim = Array(self.dim(0), self.dim(2))
    def apply(v1: Int) = self(v1/self.dim(0) * d01 + v1 % self.dim(0) + self.dim(0) * slicenum)
  }

  class CutView12(val slicenum: Int) extends MultiSeq2D[T] {
    require(slicenum >= 0 && slicenum < self.dim(0))
    val dim = Array(self.dim(1), self.dim(2))
    def apply(v1: Int) = self(v1/self.dim(1) * d01 + v1 % self.dim(1) * self.dim(0) + slicenum)
  }

  class CutView01(val slicenum: Int) extends MultiSeq2D[T] {
    require(slicenum >= 0 && slicenum < self.dim(2))
    val dim = Array(self.dim(0), self.dim(1))
    def apply(v1: Int) = self(d01*slicenum + v1)
  }

  def cut(axis : Int, i: Int) = axis match {
    case 0 => new CutView12(i)
    case 1 => new CutView02(i)
    case 2 => new CutView01(i)
    case _ => error("illegal dimension " + axis)
  }

  def along(d: Int): Seq[Int] = d match {
    case 0 => 0 until dim(0)
    case 1 => 0 until dim(1)
    case 2 => 0 until dim(2)
    case _ => error("illegal dimension " + d)
  }

}



class MultiArray2D[T: ClassManifest](val d0: Int, val d1: Int, data: Array[T]) extends MultiSeq2D[T] {
  require(d0 * d1 == data.length)

  def this(d0: Int, d1: Int) = this (d0, d1, new Array[T](d0 * d1))

  val dim = Array[Int](d0, d1)

  override def iterator = data.iterator

  override def length = data.length

  override def apply(v1: Int) = data(v1)

  //def apply(v1: Seq[Int])


}


class MultiArray3D[T: ClassManifest](val d0: Int, val d1: Int, val d2: Int, data: Array[T]) extends MultiSeq3D[T] {
  require(d0 * d1 * d2 == data.length)

  def this(d0: Int, d1: Int, d2: Int) = this (d0, d1, d2, new Array[T](d0 * d1 * d2))

  val dim = Array[Int](d0, d1, d2)

  def iterator = data.iterator

  def length = data.length

  def apply(v1: Int) = data(v1)
}



