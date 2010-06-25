package sc.brainflow.image.io

import java.nio.{ByteOrder, ByteBuffer}
import org.apache.commons.vfs.provider.local.LocalFile
import java.io.{FileInputStream, File, InputStream}
import java.nio.channels.FileChannel.MapMode
import org.apache.commons.vfs.{Selectors, VFS, FileObject}
import brainflow.utils.ProgressListener

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 16, 2010
 * Time: 11:44:20 AM
 * To change this template use File | Settings | File Templates.
 */

trait BinaryReader {

  val offset: Int

  val byteOrder: ByteOrder

  def createInputStream: InputStream


  def readFloats(numElements: Int, numChunks: Int=1) = {
    read(numElements*4, numChunks).asFloatBuffer
  }

  def readDoubles(numElements: Int, numChunks: Int=1) = {
    read(numElements*8, numChunks).asDoubleBuffer
  }

  def readInts(numElements: Int, numChunks: Int=1) = {
    read(numElements*4, numChunks).asIntBuffer
  }

  def readShorts(numElements: Int, numChunks: Int=1) = {
    read(numElements*2, numChunks).asShortBuffer
  }

  def readLongs(numElements: Int, numChunks: Int=1) = {
    read(numElements*2, numChunks).asLongBuffer
  }

  //def read(numBytes: Int): ByteBuffer = read(numBytes, 1, None)


  def read(numBytes: Int, numChunks: Int = 1, listener: Option[ProgressListener] = None): ByteBuffer = {
    val (chunkSize, lastChunk) = if (numChunks == 1) {
      (numBytes,0)

    } else {
      (numBytes / numChunks, numBytes % numChunks)
    }

    listener.map( l => { l.setMinimum(0); l.setMaximum(numBytes) } )

    val istream = createInputStream
    var nread: Long = istream.skip(offset)
    var wholeBuffer: ByteBuffer = ByteBuffer.allocate(numBytes)
    wholeBuffer.order(byteOrder)

    var holder: Array[Byte] = new Array[Byte](chunkSize)

    for (i <- 0 until numChunks) {
          istream.read(holder)
          wholeBuffer.put(holder)
          listener.map( _.setValue( (i+1) * chunkSize))
    }

    if (lastChunk > 0) {
      var lastData: Array[Byte] = new Array[Byte](lastChunk)
      nread = istream.read(lastData)
      wholeBuffer.put(lastData)
      listener.map( _.setValue( numBytes))
    }

    listener.map( _.finished)

    wholeBuffer.rewind
    wholeBuffer

  }

}

class FileObjectBinaryReader(val file: FileObject, override val offset: Int=0, override val byteOrder: ByteOrder = ByteOrder.nativeOrder) extends BinaryReader {

  def createInputStream = file.getContent.getInputStream

  private def isLocalFile : Boolean = file.isInstanceOf[LocalFile]

  private def readFromChannel(file: File, numBytes: Int) : ByteBuffer = {
    val istream = new FileInputStream(file)
    val channel = istream.getChannel
    channel.map(MapMode.READ_ONLY, offset, numBytes)
    val buffer = ByteBuffer.allocateDirect(numBytes)

    channel.read(buffer)
    buffer.rewind
    buffer
  }


  private def readFully(numBytes: Int, listener: Option[ProgressListener] = None): ByteBuffer = {
    if (isLocalFile) {
      val locFile = file.getFileSystem().replicateFile(file, Selectors.SELECT_SELF);
      readFromChannel(locFile, numBytes)
    }
    else super.read(numBytes, 1, listener)
  }

  override def read(numBytes: Int, numChunks: Int = 1, listener: Option[ProgressListener] = None): ByteBuffer = {
    require(numBytes > 0 && numChunks >= 1)
    if (numChunks > 1 || !isLocalFile) super.read(numBytes, numChunks, listener)
    else readFully(numBytes, listener)
  }
}


sealed trait DataReader[T] {
  val byteReader: BinaryReader

  def read(numChunks: Int = 1, progressListener: Option[ProgressListener] = None) : Array[T]

}

case class ByteReader(val byteReader: BinaryReader, val elements: Int) extends DataReader[Byte] {

  def read(numChunks: Int=1, progressListener: Option[ProgressListener]=None) = {
    byteReader.read(elements*2, numChunks, progressListener).array
  }
}


case class ShortReader(val byteReader: BinaryReader, val elements: Int) extends DataReader[Short] {

  def read(numChunks: Int=1, progressListener: Option[ProgressListener]=None) = {
    byteReader.read(elements*2, numChunks, progressListener).asShortBuffer.array
  }
}

case class IntReader(val byteReader: BinaryReader, val elements: Int) extends DataReader[Int] {

  def read(numChunks: Int=1, progressListener: Option[ProgressListener]=None) = {
    byteReader.read(elements*4, numChunks, progressListener).asIntBuffer.array
  }
}

case class FloatReader(val byteReader: BinaryReader, val elements: Int) extends DataReader[Float] {

  def read(numChunks: Int=1, progressListener: Option[ProgressListener]=None) = {
    byteReader.read(elements*4, numChunks, progressListener).asFloatBuffer.array
  }
}

case class DoubleReader(val byteReader: BinaryReader, val elements: Int) extends DataReader[Double] {

  def read(numChunks: Int=1, progressListener: Option[ProgressListener]=None) = {
    byteReader.read(elements*8, numChunks, progressListener).asDoubleBuffer.array
  }
}

case class LongReader(val byteReader: BinaryReader, val elements: Int) extends DataReader[Long] {

  def read(numChunks: Int=1, progressListener: Option[ProgressListener]=None) = {
    byteReader.read(elements*8, numChunks, progressListener).asLongBuffer.array
  }
}







