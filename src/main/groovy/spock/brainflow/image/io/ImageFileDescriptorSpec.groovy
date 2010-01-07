package spock.brainflow.image.io

import spock.lang.Sputnik
import org.junit.runner.RunWith
import spock.lang.Speck
import brainflow.app.toplevel.DataSourceManager
import brainflow.image.io.BrainIO

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 5, 2010
 * Time: 11:06:37 PM
 * To change this template use File | Settings | File Templates.
 */

@Speck
@RunWith (Sputnik)
public class ImageFileDescriptorSpec {

  def "nifti descriptor correctly matches data.nii"() {
    when:
      def res = BrainIO.NIFTI_DESCRIPTOR.isDataMatch("data.nii")
    then:
      res == true
  }

  def "nifti descriptor correctly matches header.nii"() {
    when:
      def res = BrainIO.NIFTI_DESCRIPTOR.isHeaderMatch("header.nii")
    then:
      res == true
  }

  def "nifti descriptor correctly matches header.nii, data.nii"() {
    when:
      def res = BrainIO.NIFTI_DESCRIPTOR.isMatch("header.nii", "data.nii")
    then:
      res == true
  }

  def "nifti descriptor correctly matches c://a//b//c//header.nii, c:/a/b/c/data.nii"() {
    when:
      def res = BrainIO.NIFTI_DESCRIPTOR.isMatch("header.nii", "data.nii")
    then:
      res == true
  }

  def "stripping extension works properly for c:/xxx/data.nii"() {
    when:
      def res = BrainIO.NIFTI_DESCRIPTOR.stripExtension("c:/xxx/data.nii")
    then:
      res == "c:/xxx/data"
  }
}
