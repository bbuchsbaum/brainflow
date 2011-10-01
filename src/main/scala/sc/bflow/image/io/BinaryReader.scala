package sc.bflow.image.io

import java.nio.{ByteOrder, ByteBuffer}
import org.apache.commons.vfs.provider.local.LocalFile
import java.io.{FileInputStream, File, InputStream}
import java.nio.channels.FileChannel.MapMode
import org.apache.commons.vfs.{Selectors, VFS, FileObject}
import boxwood.swing.utils.WatchableProcess
import swing.Publisher
import swing.event.Event
import sc.bflow.utils.ProcessListener

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

  def isDeterminate = true

  def readFloats(numElements: Int, numChunks: Int = 1, listener: ProcessListener=ProcessListener.default) = {
    read(numElements * 4, numChunks).asFloatBuffer
  }

  def readDoubles(numElements: Int, numChunks: Int = 1, listener: ProcessListener=ProcessListener.default) = {
    read(numElements * 8, numChunks).asDoubleBuffer
  }

  def readInts(numElements: Int, numChunks: Int = 1, listener: ProcessListener=ProcessListener.default) = {
    read(numElements * 4, numChunks).asIntBuffer
  }

  def readShorts(numElements: Int, numChunks: Int = 1, listener: ProcessListener=ProcessListener.default) = {
    read(numElements * 2, numChunks).asShortBuffer
  }

  def readLongs(numElements: Int, numChunks: Int = 1, listener: ProcessListener=ProcessListener.default) = {
    read(numElements * 2, numChunks).asLongBuffer
  }

  //def read(numBytes: Int): ByteBuffer = read(numBytes, 1, None)


  def read(numBytes: Int, numChunks: Int = 1, listener: ProcessListener=ProcessListener.default): ByteBuffer = {
    val (chunkSize, lastChunk) = if (numChunks == 1) {
      (numBytes, 0)

    } else {
      (numBytes / numChunks, numBytes % numChunks)
    }

    listener.progress(0)

    val istream = createInputStream
    var nread: Long = istream.skip(offset)
    var wholeBuffer: ByteBuffer = ByteBuffer.allocate(numBytes)
    wholeBuffer.order(byteOrder)

    var holder: Array[Byte] = new Array[Byte](chunkSize)

    for (i <- 0 until numChunks) {
      istream.read(holder)
      wholeBuffer.put(holder)
      listener.progress((i.toDouble / numChunks).toInt * 100)
    }

    if (lastChunk > 0) {
      var lastData: Array[Byte] = new Array[Byte](lastChunk)
      nread = istream.read(lastData)
      wholeBuffer.put(lastData)
      listener.progress(100)
    }


    wholeBuffer.rewind

    listener.done()

    wholeBuffer

  }

}

class FileObjectBinaryReader(val file: FileObject, override val offset: Int = 0, override val byteOrder: ByteOrder = ByteOrder.nativeOrder) extends BinaryReader {

  def createInputStream = file.getContent.getInputStream

  private def isLocalFile: Boolean = file.isInstanceOf[LocalFile]

  private def readFromChannel(file: File, numBytes: Int): ByteBuffer = {
    val istream = new FileInputStream(file)
    val channel = istream.getChannel
    val buffer = channel.map(MapMode.READ_ONLY, offset, numBytes)

    //val buffer = ByteBuffer.allocateDirect(numBytes)
    buffer.order(byteOrder)
    //channel.read(buffer)
    buffer.rewind
    buffer
  }


  private def readFully(numBytes: Int, listener: ProcessListener=ProcessListener.default): ByteBuffer = {
    if (isLocalFile) {
      val locFile = file.getFileSystem().replicateFile(file, Selectors.SELECT_SELF);
      val ret= readFromChannel(locFile, numBytes)
      listener.done()
      ret
    } else super.read(numBytes, 1, listener)
  }

  override def read(numBytes: Int, numChunks: Int = 1, listener: ProcessListener=ProcessListener.default): ByteBuffer = {
    require(numBytes > 0 && numChunks >= 1)
   // if (numChunks > 1 || !isLocalFile) super.read(numBytes, numChunks, listener)
    if (numChunks > 1 || !isLocalFile) super.read(numBytes, numChunks, listener)
    else readFully(numBytes, listener)
  }
}


sealed trait DataReader[T] {

  val byteReader: BinaryReader

  def read(numChunks: Int = 1, listener: ProcessListener=ProcessListener.default): Array[T]

}

case class ByteReader(byteReader: BinaryReader, elements: Int) extends DataReader[Byte] {

  def read(numChunks: Int = 1,listener: ProcessListener=ProcessListener.default) = {
    val arr = new Array[Byte](elements)
    byteReader.read(elements * 2, numChunks, listener).get(arr)
    arr
  }
}


case class ShortReader(byteReader: BinaryReader, elements: Int) extends DataReader[Short] {

  def read(numChunks: Int = 1,listener: ProcessListener=ProcessListener.default) = {
    val arr = new Array[Short](elements)
    byteReader.read(elements * 2, numChunks, listener).asShortBuffer.get(arr)
    arr
  }
}

case class IntReader(byteReader: BinaryReader, elements: Int) extends DataReader[Int] {

  def read(numChunks: Int = 1,listener: ProcessListener=ProcessListener.default) = {
    val arr = new Array[Int](elements)
    byteReader.read(elements * 4, numChunks, listener).asIntBuffer.get(arr)
    arr
  }
}

case class FloatReader(byteReader: BinaryReader, elements: Int) extends DataReader[Float] {

  def read(numChunks: Int = 1,listener: ProcessListener=ProcessListener.default) = {
    val arr = new Array[Float](elements)
    //byteReader.read(elements * 4, numChunks, progressListener).asFloatBuffer.get(arr)
    byteReader.read(elements * 4, numChunks, listener).asFloatBuffer.get(arr)
    arr
  }
}

case class DoubleReader(byteReader: BinaryReader, elements: Int) extends DataReader[Double] {

  def read(numChunks: Int = 1,listener: ProcessListener=ProcessListener.default) = {
    val arr = new Array[Double](elements)
    byteReader.read(elements * 8, numChunks, listener).asDoubleBuffer.get(arr)
    arr
  }
}

case class LongReader(byteReader: BinaryReader, elements: Int) extends DataReader[Long] {

  def read(numChunks: Int = 1, listener: ProcessListener=ProcessListener.default) = {
    val arr = new Array[Long](elements)
    byteReader.read(elements * 8, numChunks, listener).asLongBuffer.get(arr)
    arr
  }
}







