package dev.furq.vinyls.utils

import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

//4 bytes for "OggS", 2 unused bytes, 8 bytes for length
private const val OGG_OFFSET = 8 + 2 + 4
private val OGGS_BYTES = "OggS".map(Char::toByte).toByteArray()
private val VORBIS_BYTES = "vorbis".map(Char::toByte).toByteArray()
fun ByteArray.slice(start: Int, len: Int) = copyOfRange(start, start + len)

@Throws(IOException::class)
fun calculateOggDuration(oggFile: File): Pair<Int, Int> {
    var rate = -1
    var length = -1

    val size = oggFile.length().toInt()
    val t = ByteArray(size)

    oggFile.inputStream().use { stream ->
        stream.read(t)
        val endIndex = size - OGG_OFFSET - 1
        for (i in endIndex downTo 0) {
            if (OGGS_BYTES contentEquals t.sliceArray(i..(i + 3))) {
                length  = t
                    .slice(i + 6, 8)
                    .let(ByteBuffer::wrap)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .getInt(0)
                break
            }
        }
        for (i in 0..endIndex) {
            if (VORBIS_BYTES contentEquals t.sliceArray(i..(i + 5))) {
                rate = t
                    .slice(i + 11, 4)
                    .let(ByteBuffer::wrap)
                    .order(ByteOrder.LITTLE_ENDIAN)
                    .getInt(0)
                break
            }
        }
    }

    return length to rate
}