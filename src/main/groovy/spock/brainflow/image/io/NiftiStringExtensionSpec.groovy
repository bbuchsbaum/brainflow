/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 6, 2009
 * Time: 12:23:02 PM
 * To change this template use File | Settings | File Templates.
 */
import org.junit.runner.RunWith
import spock.lang.*
import brainflow.image.io.NiftiImageInfo


public class NIftiStringExtensionSpec extends Specification {


  def "getting an image file name from a header name, where header ends with .hdr"() {
    when:
    def header = "joseph.hdr"
    def image = NiftiImageInfo.getImageName(header, ".nii")

    then:
    image.equals("joseph.img")


  }


def "getting an image file name from a header name, where header ends with .img"() {
    when:
    def header = "joseph.img"
    def image = NiftiImageInfo.getImageName(header, ".nii")

    then:
    image.equals("joseph.img")


  }

  def "getting an image file name from a header name, where header ends with .nii"() {
    when:
    def header = "joseph.nii"
    def image = NiftiImageInfo.getImageName(header, ".nii")

    then:
    image.equals("joseph.nii")


  }

  def "getting an image file name from a header name, where header is already stripped"() {
    when:
    def header = "joseph"
    def image = NiftiImageInfo.getImageName(header, ".nii")

    then:
    image.equals("joseph.nii")


  }

def "getting a header file name from an image name, where header ends with .hdr"() {
    when:
    def input = "joseph.hdr"
    def header = NiftiImageInfo.getHeaderName(input, ".nii")

    then:
    header.equals("joseph.hdr")


  }


def "getting an header file name from an image name, where header ends with .img"() {
    when:
    def input = "joseph.img"
    def header = NiftiImageInfo.getHeaderName(input, ".nii")

    then:
    header.equals("joseph.hdr")


  }

  def "getting a header file name from an image name, where header ends with .nii"() {
    when:
    def input = "joseph.nii"
    def header = NiftiImageInfo.getHeaderName(input, ".nii")

    then:
    header.equals("joseph.nii")


  }

  def "getting a header file name from an image name, where header is already stripped"() {
    when:
    def input = "joseph"
    def header = NiftiImageInfo.getImageName(input, ".nii")

    then:
    header.equals("joseph.nii")


  }





}
