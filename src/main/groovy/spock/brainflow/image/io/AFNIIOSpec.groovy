package spock.brainflow.image.io

import spock.lang.Speck
import org.junit.runner.RunWith
import brainflow.image.io.AFNIInfoReader
import spock.lang.Sputnik

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 27, 2009
 * Time: 9:30:15 PM
 * To change this template use File | Settings | File Templates.
 */

@Speck
@RunWith (Sputnik)
public class AFNIIOSpec  {


  def "reading motion-reg2+orig succeeds"() {
    when:
    def reader = new AFNIInfoReader("src/main/groovy/testdata/motion-reg2+orig")
    def infolist = reader.readInfo()

    then:
    infolist != null
  }

}
