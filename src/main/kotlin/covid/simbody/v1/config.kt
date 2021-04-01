package covid.simbody.v1

const val CELL_SIZE = 40

private const val BOARD_COLS = 10
private const val BOARD_ROWS = 14

val Board.Companion.sample1
    get(): Board {
        val entryCell = VesselCell(Point(2, 1), Direction.Down)
        val cells2 = buildCells(entryCell) {
            move(Direction.Down, 3)
            move(Direction.Right, 2)
            move(Direction.Up, 3)
            move(Direction.Left, 1)
        }
        return Board(BOARD_COLS, BOARD_ROWS, entryCell, cells2.associateBy { it.position })
    }

class CellBuilderDsl(entry: VesselCell) {
    private val cells = mutableListOf<VesselCell>()
    private var currentPosition = entry.position
    private var lastAddedCell: VesselCell? = null

    init {
        cells += entry
    }

    fun move(direction: Direction, size: Int) {
        rewireLastCell(direction)
        addCells(direction, size)
        lastAddedCell = cells.last()
    }

    private fun addCells(direction: Direction, size: Int) {
        (1..size).forEach { _ ->
            cells += VesselCell(currentPosition.move(direction).also {
                currentPosition = it
            }, direction)
        }
    }

    private fun rewireLastCell(direction: Direction) {
        lastAddedCell?.also {
            cells.removeLast()
            lastAddedCell = VesselCell(it.position, direction).also { rewritten ->
                cells += rewritten
            }
        }
    }

    fun build(): List<VesselCell> {
        return cells
    }

}

fun buildCells(entry: VesselCell, code: CellBuilderDsl.() -> Unit): List<VesselCell> {
    return CellBuilderDsl(entry).apply { code() }.build()
}