package spock.brainflow.image.data

import spock.lang.Specification
import brainflow.image.io.BrainIO
import brainflow.image.data.IImageData3D
import spock.lang.Shared
import brainflow.image.data.BasicImageDataVector3D


/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 11, 2010
 * Time: 10:32:19 PM
 * To change this template use File | Settings | File Templates.
 */
class BasicImageDataVectorSpec extends Specification {


  @Shared IImageData3D  image1 =  BrainIO.readNiftiImage("brainflow/src/main/groovy/testdata/nwreh_P1S1.nii")
  @Shared IImageData3D  image2 =  BrainIO.readNiftiImage("brainflow/src/main/groovy/testdata/nwreh_P1S2.nii")
  @Shared IImageData3D  image3 =  BrainIO.readNiftiImage("brainflow/src/main/groovy/testdata/nwreh_P1S3.nii")

  def "construction of BasicImageDataVectorSpec succeeds and has correct number of volumes"() {

    when:
    def imvec = [image1, image2, image3] as IImageData3D[]

    then:
    def res = new BasicImageDataVector3D(imvec)
    println res.getNumVolumes()
    res.getNumVolumes() == 3

  }

  def "max of BasicImageDataVector is max of its constituents"() {

    when:
    def imvec = [image1, image2, image3] as IImageData3D[]
    def maxval = [image1.maxValue(), image2.maxValue(), image3.maxValue()].max()
    then:
    def res = new BasicImageDataVector3D(imvec)
    res.maxValue() == maxval
  }

  def "min of BasicImageDataVector is min of its constituents"() {

    when:
    def imvec = [image1, image2, image3] as IImageData3D[]
    def minval = [image1.minValue(), image2.minValue(), image3.minValue()].min()
    then:
    def res = new BasicImageDataVector3D(imvec)
    res.minValue() == minval
  }
}
