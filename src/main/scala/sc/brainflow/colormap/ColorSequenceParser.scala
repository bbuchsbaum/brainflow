package sc.brainflow.colormap


import java.awt.Color
import util.parsing.combinator.JavaTokenParsers
import util.parsing.combinator._




/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 21, 2009
 * Time: 6:52:53 PM
 * To change this template use File | Settings | File Templates.
 */

class ColorSequenceParser extends JavaTokenParsers {



  //def colorSeqGradient : Parser[Any] = colorSeq~colorExpr

  def colorSeq : Parser[Any] = colorSpec~(delimNumSeq)~colorSpec ^^
          { case clr1~numseq~clr2 => (clr1, clr2, numseq) }

  def colorSpec : Parser[Node] = namedColor | rgbTuple

  def numSeq : Parser[Any] = repsep(floatingPointNumber, ",") ^^
          { case numlist => numlist.map(_.toDouble).toArray }

  def numSeqFree : Parser[Any] = rep(floatingPointNumber) ^^
          { case numlist => numlist.map(_.toDouble).toArray }

  def delimNumSeq : Parser[Any] = ("/"~> numSeq)<~"/" | ("/"~>numSeqFree<~"/")

  def numSeqList : Parser[Any] = rep(delimNumSeq)

  def mixedSeqList : Parser[Any] = rep(delimNumSeq | colorSeq)

  def rgbTuple : Parser[RGBColor] = "("~> repsep(floatingPointNumber, ",")<~")" ^^
          { case numlist => RGBColor(numlist.map(_.toDouble).toArray) }

  def namedColor : Parser[NamedColor] = ident ^^ (NamedColor(_))

}

abstract class Node

case class EmptyNode extends Node {
  override def toString = "empty node"
}

case class NamedColor(name: String) extends Node  {
  def asColor:Color = {
    Color.decode(name)
  }
}
case class RGBColor(values: Array[Double]) extends Node {
  def asColor:Color = {
    new Color(values(1).toInt, values(2).toInt, values(3).toInt)      
  }
}

case class ColorNode(clr: Color) extends Node
  


object Test extends ColorSequenceParser {

  def main(args: Array[String]) {
    //println(parseAll(numSeq, "1, 2, 3, 5"))
    //println(parseAll(numSeqFree, "1 2 3 5"))
    //println(parseAll(colorSeq, "RED/1,2,3,4,5/RED"))
    //println(parseAll(colorSeq, "RED/1, 2, 3/GREEN"))
    //println(parseAll(colorSeq, "(22,33,44)/1, 2, 3/GREEN"))
    //println(parseAll(delimNumSeq, "/1, 2, 3, 4, 5, 6/"))
    //println(parseAll(delimNumSeq, "/6 5 4/"))

    //println(parseAll(mixedSeqList, "/6 5 4/ /1 2 3/ /3,2,1/"))
    //println(parseAll(mixedSeqList, "RED/6 5 4/GREEN /1 2 3/ YELLOW/3,2,1/ORANGE"))

    val x = parseAll(colorSpec, "(32,32,32)")

    val z = x match {
      case Success(result, _) => result
      case _ => EmptyNode()
    }

    def simplify(node:Node): Color = {
      node match {
        case RGBColor(values) => simplify(ColorNode(new Color(values(0).toInt, values(1).toInt, values(2).toInt)))
        case NamedColor(name) => simplify(ColorNode(Color.getColor(name)))
        case ColorNode(clr) => clr
      }
      
      
    }


    val tmp = simplify(z)
    println(tmp.toString)
   

  }
}