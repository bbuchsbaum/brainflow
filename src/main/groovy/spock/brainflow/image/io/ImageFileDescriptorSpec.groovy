package spock.brainflow.image.io


import org.junit.runner.RunWith

import brainflow.app.toplevel.DataSourceManager
import brainflow.image.io.BrainIO
import spock.lang.Specification

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 5, 2010
 * Time: 11:06:37 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageFileDescriptorSpec extends Specification {



  def "nifti descriptor correctly matches data.nii"() {
    when:
      def res = BrainIO.NIFTI.isDataMatch("data.nii")
    then:
      res == true
  }

  def "nifti descriptor correctly matches header.nii"() {
    when:
      def res = BrainIO.NIFTI.isHeaderMatch("header.nii")
    then:
      res == true
  }

  def "nifti descriptor correctly matches header.nii, data.nii"() {
    when:
      def res = BrainIO.NIFTI.isMatch("header.nii", "data.nii")
    then:
      res == true
  }

  def "nifti descriptor correctly matches c://a//b//c//header.nii, c:/a/b/c/data.nii"() {
    when:
      def res = BrainIO.NIFTI.isMatch("header.nii", "data.nii")
    then:
      res == true
  }

  def "nifti gz descriptor correctly matches header.nii.gz, data.nii.gz"() {
    when:
      def res = BrainIO.NIFTI_GZ.isMatch("header.nii.gz", "data.nii.gz")
    then:
      res == true
  }

  def "stripping extension works properly for c:/xxx/data.nii"() {
    when:
      def res = BrainIO.NIFTI.stripExtension("c:/xxx/data.nii")
    then:
      res == "c:/xxx/data"
  }
}
