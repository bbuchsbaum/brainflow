package sc.bflow.utils


/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 9/24/11
 * Time: 9:41 PM
 * To change this template use File | Settings | File Templates.
 */


trait ProcessListener {



  def progress(percentDone: Int) : Unit

  def message(message: String) : Unit

  def done() : Unit

}

object ProcessListener {
  val default = NoOpProcessListener

}

object NoOpProcessListener extends ProcessListener {
  def progress(percentDone: Int) {}

  def done() {}

  def message(message: String) {}
}

trait ObservableFunction0[+R] extends Function0[R] {


  def apply(listener: ProcessListener) : R
}

trait ObservableFunction1[-T, +R] extends Function1[T,R] {

  def apply(in: T, listener: ProcessListener) : R

}