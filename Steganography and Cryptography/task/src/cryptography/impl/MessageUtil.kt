package cryptography.impl

class MessageUtil {
    companion object {
        const val hideCommand = "hide"
        const val showCommand = "show"
        const val exitCommand = "exit"
        const val menu = "Task (hide, show, exit):"
        const val inputFileMes = "Input image file:"
        const val outputFileMes = "Output image file:"
        const val inHideMes = "Message to hide:"
        const val byeMes = "Bye!"

        const val noEnoughLarge = "The input image is not large enough to hold this message."
        const val cantRead = "Can't read input file!"
        const val badFileName = "FileName is bad"
        const val badMessage = "Message cant be null or Blank"
        const val badPas = "Password cant be null or Blank"
        const val mes = "Message:"
        const val pas = "Password:"

        fun wrongTask(task: String): String = "Wrong task: $task"
        fun savedMes(fileName: String): String = "Message saved in $fileName image."

    }


}