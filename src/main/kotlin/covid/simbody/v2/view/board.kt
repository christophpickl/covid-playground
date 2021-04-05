package covid.simbody.v2.view

import covid.simbody.v2.logic.core.Cell


data class Board(
    val width: Int,
    val height: Int,
    private val definedCells: Map<Position, Cell>,
//    val cells: MutableList<BoardCell>
) {

    private val cellsMap: Map<Position, BoardCell>

    init {
        val map = mutableMapOf<Position, BoardCell>()
        1.rangeTo(height).forEach { row ->
            1.rangeTo(width).forEach { col ->
                val pos = Position(col - 1, row - 1)
                val cell = definedCells[pos]?.let { MaybeCell.HasCell(it) } ?: MaybeCell.VoidCell
                map[pos] = BoardCell(cell, pos)
            }
        }
        cellsMap = map
    }

    fun cellAt(pos: Position) = cellsMap[pos] ?: throw Exception("Cell not found at $pos!")
}

data class BoardCell(
    val mCell: MaybeCell,
    val pos: Position,
) {

}

sealed class MaybeCell {
    object VoidCell : MaybeCell() {
        override fun toString() = "EmptyCell[]"
    }
    data class HasCell(val cell: Cell) : MaybeCell() {
        override fun toString() = "HasCell[$cell]"
    }
}

data class Position(
    val x: Int,
    val y: Int,
)
//class BoardDefinition(
//    val width: Int,
//    val height: Int,
//    val definedCells: MutableList<Cell>,
//) {
//    private val definedCellsByPos = definedCells.associateBy { it.pos }
//
//    companion object {
//        val sample1 = BoardDefinition(1, 2, mutableListOf(
//            EsophagusCell(Pos(0, 1)),
//        ))
//    }
//    fun toBoard(): Board {
//        val allCells = ArrayList<Cell>(width * height)
//        (0 until width).forEach { x ->
//            (0 until height).forEach { y ->
//                val pos = Pos(x, y)
//                allCells += definedCellsByPos[pos] ?: VoidCell(pos)
//            }
//        }
//        println(allCells)
//        return Board(width, height, allCells)
//    }
//}
