package spock.brainflow.image
/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 14, 2009
 * Time: 7:09:16 PM
 * To change this template use File | Settings | File Templates.
 */

import org.junit.runner.RunWith
import spock.lang.*
import brainflow.image.io.NiftiImageInfo
import brainflow.image.space.Space
import brainflow.image.anatomy.Anatomy3D;

@Speck
@RunWith (Sputnik)
public class PermutationMatrix3DSpec {

  def setupSpeck() {


  }

  // AIL ---> LPI
  // 1 --> !2
  // 2 --> 3
  // 3 --> 1

  def "permutation from AIL to LPI works"() {
    when:
    def ispace = Space.createImageSpace(64, 100, 76, 1, 1, 1);
    println ispace.anatomy
    def out_anat = Anatomy3D.SAGITTAL_AIL
    println out_anat
    def pmat = Space.createPermutationMatrix(ispace, out_anat)
    println pmat
    println pmat.permute(0, 0, 0)

    then:
    pmat != null
    // 0,0,0 --> 0, 100, 0
    pmat.permute(0, 0, 0) == [99, 0, 0] as int[]


  }

  // LSA ---> LPI
  // 1 --> 1
  // 2 --> !3
  // 3 --> !2

  def "permutation from LSA to LPI works"() {
    when:
    def ispace = Space.createImageSpace(64, 100, 76, 1, 1, 1);
    def out_anat = Anatomy3D.CORONAL_LSA
    def pmat = Space.createPermutationMatrix(ispace, out_anat)

    then:
    // 0,0,0 --> 0, 100, 0
    pmat.permute(0, 0, 0) == [0, 75, 99] as int[]

  }

  // RAS ---> LPI
  // 1 --> !1
  // 2 --> !2
  // 3 --> !3

  def "permutation from RAS to LPI works"() {
    when:
    def ispace = Space.createImageSpace(64, 100, 76, 1, 1, 1);
    def out_anat = Anatomy3D.AXIAL_RAS;
    def pmat = Space.createPermutationMatrix(ispace, out_anat)

    then:
    // 0,0,0 --> 0, 100, 0
    pmat.permute(0, 0, 0) == [63, 99, 75] as int[]

  }

  


}