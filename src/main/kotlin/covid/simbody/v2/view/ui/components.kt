package covid.simbody.v2.view.ui

import covid.simbody.v2.logic.core.*
import covid.simbody.v2.view.Board
import covid.simbody.v2.view.BoardCell
import covid.simbody.v2.view.MaybeCell
import covid.simbody.v2.view.Position
import java.awt.*
import javax.swing.JPanel

interface InteractionListener {
    fun doTick()
    fun addOxygen()
}

class BoardPanel(
    private val board: Board,
) : JPanel(GridBagLayout()) {

    init {
        background = Color.BLUE
        val c = GridBagConstraints()
        c.gridx = 0
        c.gridy = 0
        c.fill = GridBagConstraints.BOTH
        1.rangeTo(board.height).forEach { row ->
            1.rangeTo(board.width).forEach { col ->
                val cell = board.cellAt(Position(col - 1, row - 1))
                add(rendererFor(cell).render(), c)
                c.gridx++
            }
            c.gridx = 0
            c.gridy++
        }
    }

    private fun rendererFor(bCell: BoardCell) = when (bCell.mCell) {
        is MaybeCell.VoidCell -> VoidCellRenderer
        is MaybeCell.HasCell ->
            when (bCell.mCell.cell) {
                is ArteryCell -> ArteryCellRenderer(bCell.mCell.cell)
                is OrganCell -> {
                    when (bCell.mCell.cell) {
                        is LungCell -> LungCellRenderer(bCell.mCell.cell)
                    }
                }
            }
    }
}
