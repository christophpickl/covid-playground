package covid.simbody.v2

import covid.simbody.v2.logic.core.ArteryCell
import covid.simbody.v2.logic.core.Body
import covid.simbody.v2.logic.core.LungCell
import covid.simbody.v2.view.Board
import covid.simbody.v2.view.Position
import covid.simbody.v2.view.ui.MainWindow

object SimBodyV2App {
    @JvmStatic
    fun main(args: Array<String>) {
        println("SimBody starting ...")

        val (body, board) = buildBodyAndBoard()
        MainWindow(board, body).showYourself()
    }

    private fun buildBodyAndBoard(): Pair<Body, Board> {
        val lung = LungCell()
        val artery1 = ArteryCell()
        val artery2 = ArteryCell()
        val artery3 = ArteryCell()
        lung.wireOutput(artery1)
        artery1.wireOutput(artery2)
        artery2.wireOutput(artery3)
        artery3.wireOutput(lung)

        val board = Board(
            3, 3, mapOf(
                Position(0, 0) to lung,
                Position(1, 0) to artery1,
                Position(1, 1) to artery2,
                Position(0, 1) to artery3,
            )
        )
        val body = Body(setOf(lung, artery1, artery2, artery3))

        return body to board
    }
}
