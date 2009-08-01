package spock.brainflow.image.io
/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 5, 2009
 * Time: 10:15:24 PM
 * To change this template use File | Settings | File Templates.
 */

import brainflow.image.io.NiftiInfoReader
import org.junit.runner.RunWith
import spock.lang.*
import static spock.lang.Predef.*
import brainflow.image.anatomy.Anatomy3D
import brainflow.utils.DataType
import java.nio.ByteOrder
import brainflow.core.BrainFlowException
import brainflow.image.io.NiftiImageInfo
import org.apache.commons.vfs.VFS
import brainflow.image.io.NiftiInfoWriter
import org.apache.commons.vfs.FileObject
import brainflow.image.io.BrainIO
import brainflow.image.io.NiftiImageWriter
import brainflow.image.data.ImageData

@Speck
@RunWith (Sputnik)
public class NIftiIOSpec {

  def infoReader


  def readHeader(filename) {
    def infoReader = new NiftiInfoReader(filename as String)
    def infolist = infoReader.readInfo()
    def header = infolist.get(0)
    header

  }


  def setupSpeck() {
    def infoReader = new NiftiInfoReader("src/main/groovy/testdata/207_anat_alepi.nii")
    def infolist = infoReader.readInfo()
   
  }

  def "reading a single nifti header"() {
    when:
    def infoReader = new NiftiInfoReader("src/main/groovy/testdata/207_anat_alepi.nii")
    def infolist = infoReader.readInfo()

    then:
    infolist != null
    infolist.size() == 1
  }


  def "reading 207_anat_alepi.nii"() {
    when:
    def info = readHeader("src/main/groovy/testdata/207_anat_alepi.nii") as NiftiImageInfo

    then:
    info.getImageLabel() == "207_anat_alepi.nii"
    info.anatomy == Anatomy3D.AXIAL_RPI
    info.arrayDim.toArray() == [256, 256, 128]
    info.getDataOffset(0) == 3088
    info.getDataType() == DataType.FLOAT
    info.getDimensionality() == 3
    info.getEndian() == ByteOrder.LITTLE_ENDIAN
    info.getImageIndex() == 0
    info.getIntercept() == 0
    info.getScaleFactor() == 0
    info.hasExtensions()

  }

  def "reading a bogus nifti header"() {
    when:
    def info = readHeader("src/main/groovy/testdata/nonexistent.nii")

    then:
    thrown(BrainFlowException)
    info == null
  }


  def "comparing a copied NiftiImageInfo"() {
    when:
    def NiftiImageInfo info = readHeader("src/main/groovy/testdata/207_anat_alepi.nii") as NiftiImageInfo
    def tmpheader = VFS.getManager().resolveFile("tmp://joseph.nii")
    def tmpdata = VFS.getManager().resolveFile("tmp://joseph.nii")
    def cinfo = info.copy(tmpheader, tmpdata)
    cinfo.dump()

    then:
    cinfo.hasExtensions()
    info.equals(cinfo)

  }


  def "writing a copied header and rereading it yields something similar"() {
    when:
    def NiftiImageInfo info = readHeader("src/main/groovy/testdata/207_anat_alepi.nii") as NiftiImageInfo
    def tmpheader = VFS.getManager().resolveFile("tmp://joseph3.nii")
    tmpheader.createFile()
    def tmpdata = tmpheader
    def cinfo = info.copy(tmpheader, tmpdata)
    def NiftiInfoWriter writer = new NiftiInfoWriter()
    def streamPos = writer.writeInfo(cinfo)
    //def children = ramfs.getFileSystem().getRoot().getChildren();
    def reader = new NiftiInfoReader(cinfo.getHeaderFile(), cinfo.getDataFile())
    def recon = reader.readInfo().get(0)

    then:
    streamPos == 3088
    recon.arrayDim == cinfo.arrayDim
    recon.anatomy == cinfo.anatomy
    recon.spacing == cinfo.spacing
    recon.dataOffset == cinfo.dataOffset
    recon.dimensionality == cinfo.dimensionality
    recon.getDataType() == cinfo.getDataType()
    recon.getEndian() == cinfo.getEndian()
    recon.getScaleFactor() == cinfo.getScaleFactor()

    cleanup:
    tmpheader.close()
    tmpheader.delete()
  }

  def "reading a nifti image and writing it yields the same image"() {
    when:
    def NiftiImageInfo info = readHeader("src/main/groovy/testdata/207_anat_alepi.nii") as NiftiImageInfo
    def nifile = VFS.getManager().resolveFile("tmp://copy_207_anat_alepi.nii")
    def cinfo = info.copy(nifile, nifile)
    def idata = BrainIO.readNiftiImage("src/main/groovy/testdata/207_anat_alepi.nii")
    System.out.println("idata max " + idata.maxValue());
    def writer = new NiftiImageWriter()
    //todo copied info object has wrong header offset. need to handle extensions properly
    writer.writeImage(cinfo, idata)
    println "reread size: " + nifile.getContent().size
    println "reread offset" + cinfo.getDataOffset()
    def reread = BrainIO.readNiftiImage(cinfo.getHeaderFile())

    println reread.getDataType()
    println reread.maxValue()
    then:
    ImageData.elementsEquals(idata, reread, 0.001 as Float)



  }




}

