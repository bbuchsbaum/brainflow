package sc.brainflow.core


import boxwood.binding.Converter
import sc.brainflow.image.space.{IndexPoint3D, RealPoint3D, GridPoint3D}

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Apr 6, 2010
 * Time: 9:27:54 PM
 * To change this template use File | Settings | File Templates.
 */

object Conversions {

  implicit val gridLoc3DConverter = new Converter[GridPoint3D, RealPoint3D] {
    def reverse(b: RealPoint3D) = GridPoint3D.fromReal(b.x, b.y, b.z, b.xaxis,b.yaxis,b.zaxis)

    def forward(a: GridPoint3D) = RealPoint3D.fromGrid(a.x, a.y, a.z, a.xaxis, a.yaxis, a.zaxis)
  }

  implicit val gridIndexConverter = new Converter[GridPoint3D, IndexPoint3D] {
    def reverse(b: IndexPoint3D) = b.toGrid

    def forward(a: GridPoint3D) = a.toIndex
  }


  

}