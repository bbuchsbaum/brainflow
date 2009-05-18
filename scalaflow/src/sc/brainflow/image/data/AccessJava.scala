package sc.brainflow.image.data


import _root_.brainflow.image.io.BrainIO

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 27, 2009
 * Time: 9:21:44 AM
 * To change this template use File | Settings | File Templates.
 */

object AccessJava {
  def hello() = {
    val bf = BrainIO.createInfoReader("BBB")
    bf
  }

  def main(args:Array[String]) {
    hello()

  }
}