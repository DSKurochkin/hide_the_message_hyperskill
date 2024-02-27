package cryptography.impl

import cryptography.ShowMessage
import cryptography.impl.MessageUtil.Companion.cantRead
import cryptography.impl.MessageUtil.Companion.inputFileMes
import cryptography.impl.MessageUtil.Companion.mes
import cryptography.impl.MessageUtil.Companion.pas
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO

class ShowImpl : ShowMessage {
    override fun show() {
        val image: BufferedImage
        println(inputFileMes)
        val fileName = readLine()
        println(pas)
        val pas = readLine()

        try {
            if (fileName.isNullOrBlank()
                || !fileName.endsWith(".png")
                || pas.isNullOrBlank()

            ) throw IOException(cantRead)
        } catch (e: IOException) {
            println(e.message)
            return
        }
        val pasArr = pas.encodeToByteArray()
        var j = 0
        try {
            image = ImageIO.read(File(fileName))
        } catch (e: IOException) {
            println(cantRead)
            return
        }
        val unEncrypted = emptyList<Char>().toMutableList()
        val raw = emptyList<Int>().toMutableList()
        var i = 1
        val sb = StringBuilder(0)
        outer@ for (y in 0 until image.height) {
            for (x in 0 until image.width) {
                val color = Color(image.getRGB(x, y))
                val blue = color.blue
                if (blue % 2 == 0) {
                    sb.append("0")
                } else {
                    sb.append("1")
                }
                if (i % 8 == 0) {
                    if (j == pasArr.size) {
                        j = 0
                    }
                    val rawDec = sb.toString().toInt(2)
                    raw.add(rawDec)
                    unEncrypted.add(Char(rawDec xor pasArr[j].toInt()))
                    sb.clear()
                    if (unEncrypted.size > 3
                        && raw.last() == 3
                        && isEndOfMes(raw)
                    ) {
                        break@outer
                    }
                    j++
                }
                i++
            }
        }
        println(mes)
        println(unEncrypted.dropLast(3).joinToString(""))
    }


    private fun isEndOfMes(list: MutableList<Int>): Boolean {
        return list[list.lastIndex - 1] == 0
                && list[list.size - 2] == 0
    }


}


