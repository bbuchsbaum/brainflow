package sc.bflow.app.commands

import sc.bflow.app.toplevel.BrainFlowContext
import javax.swing.JFileChooser
import boxwood.io._
import java.io.File
import sc.bflow.image.io._
import brainflow.image.anatomy.Anatomy3D
import brainflow.colormap.IColorMap
import brainflow.core.{IClipRange, ClipRange}
import sc.bflow.core.{OrthoImageView, ImageView, ImageCanvasDesktop}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: 10/24/10
 * Time: 8:02 PM
 * To change this template use File | Settings | File Templates.
 */


trait CommandComponent {
  this: BrainFlowContext =>

  lazy val fileChooser: JFileChooser = new JFileChooser(fileSystemService.currentDirectory().toLocalFile)

  lazy val newCanvas = QuickCommand("new-canvas") {
    addCanvas(new ImageCanvasDesktop())
  }

  lazy val openImage = QuickCommand("open-image") {
    val res: Int = fileChooser.showOpenDialog(mainFrame)
    if (res == JFileChooser.APPROVE_OPTION) {
      val file = fileChooser.getSelectedFile

      val metaInfo = sc.bflow.image.io.readMetaInfo(file)

      if (metaInfo.isRight) {
        val imageNode = sc.bflow.image.io.makeImageSource(metaInfo.right.get)
        imageNode match {
          case x@ImageSource3D(index, meta) => displayService.openVolume(x)
          case x@ImageSourceSeq3D(label, children) => //
          case x@ImageSourceGroup(label, children) => //
        }
      } else {
        sys.error("could not read file: " + file)
      }
    }

  }

  lazy val createAxial = QuickCommand("create-axial") {
    val selView = selectedCanvas().model.selectedView
    selView.foreach {
      view =>
        val newView = ImageView(view.model, Anatomy3D.getCanonicalAxial)
        selectedCanvas() += newView
    }

  }


  lazy val createCoronal = QuickCommand("create-coronal") {
    val selView = selectedCanvas().model.selectedView
    selView.foreach {
      view =>
        val newView = ImageView(view.model, Anatomy3D.getCanonicalCoronal)
        selectedCanvas() += newView
    }

  }

  lazy val createSagittal = QuickCommand("create-sagittal") {
    val selView = selectedCanvas().model.selectedView
    selView.foreach {
      view =>
        val newView = ImageView(view.model, Anatomy3D.getCanonicalSagittal)
        selectedCanvas() += newView
    }

  }


  lazy val nextSlice = QuickCommand("next-slice") {
    val selView = selectedCanvas().model.selectedView
    selView.foreach {
      view =>
        view.sliceController.nextSlice()
    }
  }

  lazy val prevSlice = QuickCommand("previous-slice") {
    val selView = selectedCanvas().model.selectedView
    selView.foreach {
      view =>
        view.sliceController.previousSlice()
    }
  }

  lazy val pageForwardSlice = QuickCommand("page-forward-slice") {
    val selView = selectedCanvas().model.selectedView
    selView.foreach {
      view =>
        view.sliceController.pageForward()
    }

  }

  lazy val pageBackSlice = QuickCommand("page-back-slice") {
    val selView = selectedCanvas().model.selectedView
    selView.foreach {
      view =>
        view.sliceController.pageBack()
    }
  }

  def updateClip(clip: IClipRange, decrease: Boolean, colorMap: IColorMap, multiplier: Double = .07) = {
    val distance = clip.getHighClip - clip.getLowClip
    val increment: Double = (multiplier * distance) / 2

    val (newHighClip, newLowClip) = if (decrease) {
      var hc = math.min(clip.getHighClip + increment, colorMap.getMaximumValue)
      var lc = math.max(clip.getLowClip - increment, colorMap.getMinimumValue)
      if (lc >= hc) lc = hc - .0001
      (math.min(hc, colorMap.getMaximumValue), math.max(lc, colorMap.getMinimumValue))
    } else {
      var hc = math.min(clip.getHighClip - increment, colorMap.getMaximumValue)
      var lc = math.max(clip.getLowClip + increment, colorMap.getMinimumValue)
      if (lc >= hc) lc = hc - .0001
      (math.min(hc, colorMap.getMaximumValue), math.max(lc, colorMap.getMinimumValue))

    }
    new ClipRange(clip.getMin, clip.getMax, newLowClip, newHighClip)


  }

  lazy val increaseContrast = QuickCommand("increase-contrast") {
    val selView = selectedCanvas().model.selectedView
    selView.foreach {
      view =>
        view.selectedLayer.clipRange := updateClip(view.selectedLayer.clipRange(), false, view.selectedLayer.colorMap())

    }
  }

  lazy val decreaseContrast = QuickCommand("decrease-contrast") {
    val selView = selectedCanvas().model.selectedView

    selView.foreach {
      view =>
        view.selectedLayer.clipRange := updateClip(view.selectedLayer.clipRange(), true, view.selectedLayer.colorMap())

    }

  }
  
  lazy val createOrthoTriangular = QuickCommand("create-ortho-horizontal") {
    val selView = selectedCanvas().model.selectedView
    selView.foreach {
          view =>
            val newView = new OrthoImageView(view.model, Anatomy3D.getCanonicalAxial, OrthoImageView.Triangular)
            selectedCanvas() += newView
        }


  }


}