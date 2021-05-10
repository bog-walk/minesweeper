import kotlin.system.exitProcess

fun main() {
    while (true) {
        val game = MineSweeper()
        game.play()
        println("Replay? y|n")
        when (readLine()!!.trim().lowercase()) {
            "y" -> continue
            else -> exitProcess(0)
        }
    }
}