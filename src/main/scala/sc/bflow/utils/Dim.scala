package sc.bflow.utils

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Apr 7, 2010
 * Time: 11:05:33 AM
 * To change this template use File | Settings | File Templates.
 */

sealed trait Dim[T] {
  val arr: Array[T]

  def size = arr.length

  def apply(index: Int) : T = arr(index)

  def reduce(f: (T, T) => T) = {
    arr.reduceLeft(f)
  }

}

case class Dim1[T](val one: T)(implicit m0: ClassManifest[T], m: Numeric[T]) extends Dim[T] {
  val arr = Array[T](one)
}

case class Dim2[T](val one:T, two: T)(implicit m0: ClassManifest[T], m: Numeric[T]) extends Dim[T] {
  val arr = Array[T](one, two)
}

case class Dim3[T](val one:T, two: T, three: T)(implicit m0: ClassManifest[T], m: Numeric[T]) extends Dim[T] {
  val arr = Array[T](one, two, three)
}

case class Dim4[T](val one:T, two: T, three: T, four: T)(implicit m0: ClassManifest[T], m: Numeric[T]) extends Dim[T] {
  val arr = Array[T](one, two, three, four)
}

case class Dim5[T](val one: T, two: T, three: T, four: T, five: T)(implicit m0: ClassManifest[T], m: Numeric[T]) extends Dim[T] {
  val arr = Array[T](one, two, three, four, five)
}

