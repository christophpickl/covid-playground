package covid.simbody.v2.logic

import java.util.ArrayList

data class Board(
    val width: Int,
    val height: Int,
    val cells: MutableList<Cell>
) {

}

class BoardDefinition(
    val width: Int,
    val height: Int,
    val definedCells: MutableList<Cell>,
) {
    private val definedCellsByPos = definedCells.associateBy { it.pos }

    companion object {
        val sample1 = BoardDefinition(1, 2, mutableListOf(
            EsophagusCell(Pos(0, 1)),
        ))
    }
    fun toBoard(): Board {
        val allCells = ArrayList<Cell>(width * height)
        (0 until width).forEach { x ->
            (0 until height).forEach { y ->
                val pos = Pos(x, y)
                allCells += definedCellsByPos[pos] ?: VoidCell(pos)
            }
        }
        println(allCells)
        return Board(width, height, allCells)
    }
}
