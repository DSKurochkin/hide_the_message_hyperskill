package cryptography

import cryptography.impl.MessageUtil.Companion.byeMes
import cryptography.impl.MessageUtil.Companion.exitCommand
import cryptography.impl.MessageUtil.Companion.hideCommand
import cryptography.impl.MessageUtil.Companion.menu
import cryptography.impl.MessageUtil.Companion.showCommand
import cryptography.impl.MessageUtil.Companion.wrongTask

class Starter(private val hide: HideMessage, private val show: ShowMessage) {

    fun start() {
        var s = printMesAndGetStr()
        while (true) {
            when (s) {
                exitCommand -> {
                    println(byeMes)
                    return
                }

                hideCommand -> hide.hide()


                showCommand -> show.show()

                else -> println(wrongTask(s))

            }
            s = printMesAndGetStr()
        }

    }

    private fun printMesAndGetStr(): String {
        println(menu)
        return readln()
    }

}