package sc.bflow.image.data

import org.apache.commons.vfs.FileObject
import brainflow.image.io.BrainIO
import brainflow.image.data.{BasicImageDataVector3D, IImageData3D, IImageData}

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 14, 2010
 * Time: 8:53:04 AM
 * To change this template use File | Settings | File Templates.
 */

object BrainVector {

  def apply(flist: List[FileObject]) = {
    val ret = flist.map(x => BrainIO.loadDataSource(x).load)
    val im3d = ret.collect({
      case im: IImageData3D => im

    })

    new BasicImageDataVector3D(im3d.toArray) with BrainVectorOps


  }
}