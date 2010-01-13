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
import brainflow.core.BF
import brainflow.image.data.ClusterSet

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
    IImageData3D  image = BrainIO.readNiftiImage("src/main/groovy/testdata/cohtrend_GLT#0_Tstat.nii")
    def max = image.maxValue()
    println max
    IMaskedData3D mask = new MaskedData3D(image, { it > max/4 } as MaskPredicate)
    println mask.cardinality()
    ComponentLabeler labeler = new ComponentLabeler(mask, 12)
    labeler.label()

    then:
    println labeler.getClusterSizes()
    def cset = new ClusterSet(labeler.getLabelledComponents());
    println cset.getSortedClustersBySize()

  }



}