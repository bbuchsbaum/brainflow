package sc.bflow.app.toplevel

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel
import javax.swing.{UIManager, UnsupportedLookAndFeelException}
import com.jidesoft.plaf.LookAndFeelFactory

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 10, 2010
 * Time: 9:47:39 AM
 * To change this template use File | Settings | File Templates.
 */


object BrainFlowInitialization {
  def initLookAndFeel: Unit = {


    com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3")
   // com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFrame", "7.YTcWgxxjx1xjSnUqG:U1ldgGetfRn1")

    try {
      val osname: String = System.getProperty("os.name")
      osname.toUpperCase match {
        case osname if osname.contains("WINDOWS") => {
          LookAndFeelFactory.installDefaultLookAndFeel
          LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2007_STYLE)
        }
        case osname if osname.contains("LINUX") => {
          UIManager.setLookAndFeel(new NimbusLookAndFeel)
          LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE)
        }
        case osname if osname.contains("MAC") => {
          System.setProperty("apple.laf.useScreenMenuBar", "true")
          System.setProperty("com.apple.mrj.application.apple.menu.about.name", "BrainFlow")
          System.setProperty("com.apple.macos.useScreenMenuBar", "true")
          System.setProperty("apple.awt.graphics.UseQuartz", "true")
          System.setProperty("apple.awt.brushMetalLook", "true")
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)
          LookAndFeelFactory.installJideExtension()
        }
        case _ => {
          LookAndFeelFactory.installDefaultLookAndFeel
          LookAndFeelFactory.installJideExtension
        }
      }

    } catch {
      case e: UnsupportedLookAndFeelException => {
        //log.severe("could not createSource look and feel, exiting")
        System.exit(1)
      }
    }
  }
}