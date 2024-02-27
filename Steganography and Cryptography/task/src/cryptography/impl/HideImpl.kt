package cryptography.impl

import cryptography.HideMessage
import cryptography.impl.MessageUtil.Companion.badFileName
import cryptography.impl.MessageUtil.Companion.badMessage
import cryptography.impl.MessageUtil.Companion.badPas
import cryptography.impl.MessageUtil.Companion.cantRead
import cryptography.impl.MessageUtil.Companion.noEnoughLarge
import cryptography.impl.MessageUtil.Companion.savedMes
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.experimental.xor

class HideImpl : HideMessage {

    override fun hide() {
        var inImage: BufferedImage
        val mesInArray: ByteArray
        val outFile: File

        println(MessageUtil.inputFileMes)
        val inFileName = readln()
        println(MessageUtil.outputFileMes)
        val outFileName = readln()
        println(MessageUtil.inHideMes)
        val message = readln()
        println(MessageUtil.pas)
        val pas = readln()


        try {
            checkInput(inFileName, outFileName, message, pas)
            inImage = initInImage(inFileName)
            mesInArray = encrypt(message, pas)
            if (isImageSizeNotEnough(inImage, mesInArray)) {
                println(noEnoughLarge)
                return
            }
            inImage = recordMes(convertToTypeIntRGB(inImage), addTail(mesInArray))
            outFile = File(outFileName)
            ImageIO.write(inImage, "png", outFile)

        } catch (e: IOException) {
            println(cantRead)
            return
        }

        println(savedMes(outFile.name))
    }

    private fun convertToTypeIntRGB(inImage: BufferedImage): BufferedImage {
        val convertImage = BufferedImage(inImage.width, inImage.height, BufferedImage.TYPE_INT_RGB)
        convertImage.graphics.drawImage(inImage, 0, 0, null)
        return convertImage
    }

    private fun recordMes(inImage: BufferedImage, mesInArray: ByteArray): BufferedImage {
        val binaryArr =
            mesInArray.map { b -> fillByZero(b.toString(2)) }.flatMap { set -> set.toCharArray().asList() }
        var k = -1
        val arrSize = binaryArr.size
        val lastBitTo1 = 1
        val lastBitTo0 = 254

        outerLoop@ for (y in 0 until inImage.height) {
            for (x in 0 until inImage.width) {
                k++
                if (k == arrSize) {
                    break@outerLoop
                }
                val color = Color(inImage.getRGB(x, y))
                var blue = color.blue

                when (binaryArr[k]) {
                    '0' -> blue = blue and lastBitTo0
                    '1' -> blue = blue or lastBitTo1
                }

                val red = color.red
                val green = color.green

                inImage.setRGB(x, y, Color(red, green, blue).rgb)

            }

        }
        return inImage
    }

    private fun isImageSizeNotEnough(inImage: BufferedImage, mes: ByteArray): Boolean {
        return inImage.width * inImage.height < ((mes.size + 3) * 8)
    }

    private fun checkInput(inFileName: String?, outFileName: String?, mes: String?, pas: String?) {
        if (outFileName.isNullOrBlank()
            || !outFileName.endsWith(".png")
            || inFileName.isNullOrBlank()
        ) throw IOException(badFileName)
        if (mes.isNullOrBlank()) {
            throw IOException(badMessage)
        }
        if (pas.isNullOrBlank()) {
            throw IOException(badPas)
        }
    }

    private fun initInImage(inFileName: String): BufferedImage {
        return ImageIO.read(File(inFileName))
    }

    private fun addTail(arr: ByteArray): ByteArray {
        val res = ByteArray(arr.size + 3)
        var lastOfInArr = arr.lastIndex
        for (i in 0 until arr.size) {
            res[i] = arr[i]
        }
        res[++lastOfInArr] = 0
        res[++lastOfInArr] = 0
        res[++lastOfInArr] = 3

        return res
    }

    private fun fillByZero(s: String): String {
        val sb = StringBuilder()
        val diff = 8 - s.length
        for (j in 0 until diff) {
            sb.append("0")
        }
        return sb.append(s).toString()
    }

    private fun encrypt(mes: String, pas: String): ByteArray {
        val mesArr = mes.encodeToByteArray()
        val pasArr = pas.encodeToByteArray()
        var j = 0
        for (i in mesArr.indices) {
            if (j == pasArr.size) {
                j = 0
            }
            mesArr[i] = mesArr[i] xor pasArr[j]
            j++
        }

        return mesArr
    }
}