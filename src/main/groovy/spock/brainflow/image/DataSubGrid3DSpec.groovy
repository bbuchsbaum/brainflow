package spock.brainflow.image

import spock.lang.Speck
import org.junit.runner.RunWith
import brainflow.image.io.BrainIO
import spock.lang.Sputnik
import brainflow.image.data.IImageData3D
import spock.lang.Shared

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 23, 2009
 * Time: 5:31:23 PM
 * To change this template use File | Settings | File Templates.
 */

@Speck
@RunWith (Sputnik)
public class DataSubGrid3DSpec {

   @Shared IImageData3D  image =  BrainIO.readNiftiImage("src/main/groovy/testdata/207_anat_alepi.nii")

   


  def "extracted sub grid has correct dimensions"() {
    when:
    def subgrid = image.subGrid(25, 55, 50, 75, 22, 28)

    then:
    subgrid.value(0) == image.value(25, 50, 22)
    subgrid.dimensions.getDim(0) == (55-25+1)
    subgrid.dimensions.getDim(1) == (75-50+1)
    subgrid.dimensions.getDim(2) == (28-22 +1)
    subgrid.numElements() == (55-25+1) * (75-50+1) *  (28-22 +1)
   
  }

  def "subgrid iteration works as expected"() {
    when:
    def subgrid = image.subGrid(20, 40, 20, 40, 20, 40)
    def iter = subgrid.iterator()

    then:
    iter.next() == image.value(20,20,20)
    def val = 0
    while (iter.hasNext()) {
      val = iter.next()
    }
    println val
    println subgrid.dimensions.getDim(0)
    val == image.value(40,40,40)

  }

  

 


}
