package sc.brainflow.image.io

import java.nio.{ByteOrder, ByteBuffer}
import org.apache.commons.vfs.{VFS, FileObject}
import org.apache.commons.vfs.provider.local.LocalFile
import java.io.{FileInputStream, File, InputStream}
import java.nio.channels.FileChannel.MapMode

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 16, 2010
 * Time: 11:44:20 AM
 * To change this template use File | Settings | File Templates.
 */

trait ByteReader {

  val offset: Int

  val byteOrder: ByteOrder

  def createInputStream(): InputStream

  def read(numBytes: Int): ByteBuffer = read(numBytes, 1)

  def read(numBytes: Int, numChunks: Int = 10): ByteBuffer = {
    val (chunkSize, lastChunk) = if (numChunks == 1) {
      (numBytes,0)

    } else {
      (numBytes / numChunks, numBytes % numChunks)
    }

    val istream = createInputStream

    var nread: Long = istream.skip(offset)
    var wholeBuffer: ByteBuffer = ByteBuffer.allocate(numBytes)
    wholeBuffer.order(byteOrder)

    var holder: Array[Byte] = new Array[Byte](chunkSize)

    for (i <- 0 until numChunks) {
          istream.read(holder)
          wholeBuffer.put(holder)
    }

    if (lastChunk > 0) {
      var lastData: Array[Byte] = new Array[Byte](lastChunk)
      nread = istream.read(lastData)
      wholeBuffer.put(lastData)
    }

    wholeBuffer.rewind
    wholeBuffer

  }

}

class FileObjectByteReader(val file: FileObject, override val offset: Int=0, override val byteOrder: ByteOrder = ByteOrder.nativeOrder) extends ByteReader {

  def createInputStream() = file.getContent.getInputStream

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


  override def read(numBytes: Int) = {
    if (isLocalFile) readFromChannel(file.asInstanceOf[LocalFile], numBytes)
    else super.read(numBytes)
  }

  override def read(numBytes: Int, numChunks: Int) = {
    if (numChunks > 1 || !isLocalFile) super.read(numBytes, numChunks)
    else 
  }
}

