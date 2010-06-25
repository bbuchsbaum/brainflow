package sc.brainflow.image.io

import java.lang.String
import org.apache.commons.vfs.FileObject
import java.nio.ByteOrder
import brainflow.utils.DataType
import simplex3d.math.floatm.renamed._
import simplex3d.math.floatm.FloatMath._

import brainflow.image.io.Nifti1Dataset
import brainflow.image.io.NiftiImageInfo
import brainflow.math.{Matrix3f, Matrix4f}
import brainflow.image.anatomy.Anatomy3D

/**
 * Created by IntelliJ IDEA.
 * User: brad
 * Date: Apr 8, 2010
 * Time: 6:35:49 AM
 * To change this template use File | Settings | File Templates.
 */


class NIFTIHeader(
    val dim: Array[Short],
    val qfac: Short = 1,
    val intent: Array[Float] = Array[Float](0,0,0),
    val intentCode: Short,
    val dataType: Short,
    val bitsPerPixel: Short,
    val sliceStart: Short,
    val pixdim: Array[Float],
    val voxOffset: Float,
    val sclSlope: Float,
    val sclIntercept: Float,
    val sliceEnd: Int,
    val sliceCode: Byte,
    val xyztUnits: Byte,
    val sliceDuration: Float,
    val timeOffset: Float,
    val description: String,
    val auxFile: String,
    val qformCode: Short,
    val sformCode: Short,
    val quatern: Array[Float],
    val qoffset: Array[Float],
    val srowx: Array[Float],
    val srowy: Array[Float],
    val srowz: Array[Float],
    val intentName: String,
    val magic: String,
    val extension: Array[Byte])

object NiftiMetaInfo {

  def convert(jinfo: NiftiImageInfo) = {
    val vdim = if (jinfo.getDimensions.numDim ==3) {
      Seq[Int](jinfo.getDimensions.getDim(0).intValue, jinfo.getDimensions.getDim(1).intValue, jinfo.getDimensions.getDim(2).intValue)
    } else if (jinfo.getDimensions.numDim == 4) {
      Seq[Int](jinfo.getDimensions.getDim(0).intValue, jinfo.getDimensions.getDim(1).intValue, jinfo.getDimensions.getDim(2).intValue,jinfo.getDimensions.getDim(3).intValue)
    } else {
      error("wrong number of dimensions " + jinfo.getDimensions.numDim)
    }

    val spacing = jinfo.getSpacing

    val dset = jinfo.getHeader
    val header = new NIFTIHeader(dset.dim,
                                 dset.qfac,
                                 dset.intent,
                                 dset.intent_code,
                                 dset.datatype,
                                 dset.bitpix,
                                 dset.slice_start,
                                 dset.pixdim,
                                 dset.vox_offset,
                                 dset.scl_slope,
                                 dset.scl_inter,
                                 dset.slice_end,
                                 dset.slice_code,
                                 dset.xyzt_units,
                                 dset.slice_duration,
                                 dset.toffset,
                                 dset.descrip.toString,
                                 dset.aux_file.toString,
                                 dset.qform_code,
                                 dset.sform_code,
                                 dset.quatern,
                                 dset.qoffset,
                                 dset.srow_x,
                                 dset.srow_y,
                                 dset.srow_z,
                                 dset.intent_name.toString,
                                 dset.magic.toString,
                                 dset.extension)

    new NiftiMetaInfo(jinfo.getDataFile,
                                    jinfo.getHeaderFile,
                                    vdim,
                                    Seq[Double](spacing.getDim(0).doubleValue, spacing.getDim(1).doubleValue, spacing.getDim(2).doubleValue),
                                    jinfo.getHeaderFile.getName.getBaseName,
                                    jinfo.getDataType,
                                    jinfo.getDataOffset,
                                    jinfo.getEndian,
                                    Seq(jinfo.getIntercept),
                                    Seq(jinfo.getScaleFactor),
                                    Map[String,Any](),
                                    header)


  }
  

        
}
    


class NiftiMetaInfo(val dataFile: FileObject, val headerFile: FileObject,
                    val dimensions: Seq[Int], val spacing: Seq[Double], val label: String, val dtype: DataType, val byteOffset: Int, val endian: ByteOrder,
                    val intercept: Seq[Double] = Seq(0.0), val scaleFactor: Seq[Double] = Seq(1.0),
                    val attributes: Map[String, Any], val header: NIFTIHeader) extends ImageMetaInfo {




  def createDataReader(index: Int): DataReader[_] = {
    val elements = dimensions(0) * dimensions(1) * dimensions(2)
    val reader = new FileObjectBinaryReader(dataFile, byteOffset + index*elements, endian)
    dtype match {
      case DataType.UBYTE => ByteReader(reader, elements)
      case DataType.BYTE => ByteReader(reader, elements)
      case DataType.SHORT => ShortReader(reader, elements)
      case DataType.FLOAT => FloatReader(reader, elements)
      case DataType.INTEGER => IntReader(reader, elements)
      case DataType.DOUBLE => DoubleReader(reader, elements)
      case DataType.LONG => LongReader(reader, elements)
    }
  }


  def numVolumes = if (dimensions.size == 4) dimensions(3) else 0 

  lazy val dataType = Seq(dtype)

  lazy val volumeLabels = (0 until (numVolumes-1)).map("#" + _.toString)

  def qfac = header.qfac

  lazy val quaternion : Vec3 =  {
    Vec3(header.quatern(0), header.quatern(1), header.quatern(2))
  }


}