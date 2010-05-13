package spock.brainflow.image.io



import brainflow.image.io.AFNIInfoReader
import spock.lang.Specification

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 27, 2009
 * Time: 9:30:15 PM
 * To change this template use File | Settings | File Templates.
 */


public class AFNIIOSpec extends Specification {


  def "reading motion-reg2+orig succeeds"() {
    when:
    def reader = new AFNIInfoReader("motion-reg2+orig")
    def infolist = reader.readInfoList()

    then:
    infolist != null
  }

  def "reading TT_N27+tlrc.BRIK.gz succeeds"() {
    when:
    def reader = new AFNIInfoReader("brainflow/src/main/testdata/TT_N27+tlrc.BRIK.gz")
    def infolist = reader.readInfoList()

    then:
    infolist != null

  }

  def "reading TT_icbm452+tlrc.HEAD succeeds"() {
    when:
    def reader = new AFNIInfoReader("brainflow/src/main/testdata/TT_icbm452+tlrc.HEAD")
    def infolist = reader.readInfoList()

    then:
    infolist != null

  }

}
