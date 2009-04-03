package sc.brainflow.image.data
/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 1, 2009
 * Time: 9:07:29 PM
 * To change this template use File | Settings | File Templates.
 */

trait MixTrait {

  var data:Int = 0


  def add() {
    data = data + 1
  }
}

object Test {
  def main(args: Array[String]) = {
    val t:Test = new Test()
    t.add()
    Console.println(t.data)
  }
}

class Test extends MixTrait {
  data = 1
}

