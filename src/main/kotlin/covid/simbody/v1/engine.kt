package covid.simbody.v1

class Engine(
    val board: Board
) : UserActionListener {

    private val window by lazy {
        MainWindow(board, this)
    }

    fun showWindow() {
        window.showYourself()
    }

    override fun onTick() {
        board.tick()
        window.repaint()
    }

    override fun onAddParticle() {
        board.entryVesselCell.moveInto(1)
        board.entryVesselCell.accumulateParticles()
        window.repaint()
    }
}

interface TickManager {
    fun onVesselCell(cell: VesselCell)
}
