package covid.simbody.v2.view.ui

import covid.simbody.v2.logic.core.*
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

sealed class CellRenderer {
    companion object {
        const val CELL_SIZE = 40
    }

    fun render(): JPanel {
        val panel = object : JPanel() {
            override fun paint(g: Graphics) {
                prepare(g)
            }
        }
        panel.size = Dimension(CELL_SIZE, CELL_SIZE)
        panel.minimumSize = Dimension(CELL_SIZE, CELL_SIZE)
        panel.preferredSize = Dimension(CELL_SIZE, CELL_SIZE)
        return panel
    }

    abstract fun prepare(g: Graphics)

    protected fun fillRect(g: Graphics, color: Color) {
        g.color = color
        g.fillRect(0, 0, CELL_SIZE, CELL_SIZE)
    }

    protected fun drawInfo(g: Graphics, cell: Cell) {
        drawParticle<ErythroParticle>(g, cell, ErythroParticle.Info)
    }

    private inline fun <reified K : Particle> drawParticle(g: Graphics, cell: Cell, info: ParticleInfo) {
        g.color = Color.BLACK
        g.drawString("${info.symbol}: ${cell.particles.filterIsInstance<K>().count()}", 10, 10)
    }
}

object VoidCellRenderer : CellRenderer() {
    override fun prepare(g: Graphics) {
        fillRect(g, Color.GRAY)
    }
}

class ArteryCellRenderer(private val cell: ArteryCell) : CellRenderer() {
    override fun prepare(g: Graphics) {
        fillRect(g, Color.RED)
        drawInfo(g, cell)
    }
}

class LungCellRenderer(private val cell: LungCell) : CellRenderer() {
    override fun prepare(g: Graphics) {
        fillRect(g, Color.WHITE)
        drawInfo(g, cell)
    }
}
