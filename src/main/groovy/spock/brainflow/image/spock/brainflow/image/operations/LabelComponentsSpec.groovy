package spock.brainflow.image.spock.brainflow.image.operations

import spock.lang.Speck
import org.junit.runner.RunWith
import brainflow.image.data.IImageData3D
import brainflow.image.operations.ComponentLabeler
import brainflow.image.data.IMaskedData3D
import brainflow.image.data.MaskedData3D
import brainflow.image.data.MaskPredicate
import spock.lang.Sputnik
import brainflow.image.io.BrainIO

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Aug 24, 2009
 * Time: 3:43:57 PM
 * To change this template use File | Settings | File Templates.
 */


@Speck
@RunWith (Sputnik)
class LabelComponentsSpec {


  def "clustering a big anatomical file is possible"() {

    when:
    IImageData3D  image = BrainIO.readNiftiImage("src/main/groovy/testdata/207_anat_alepi.nii")
    def max = image.maxValue()
    IMaskedData3D mask = new MaskedData3D(image, { it > max/2 } as MaskPredicate)
    ComponentLabeler labeler = new ComponentLabeler(mask, image.createWriter(true))

    then:
    true

  }



}