package simbody

import java.awt.Color
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JPanel

class Board(
    val cols: Int,
    val rows: Int,
    val entryVesselCell: VesselCell,
    val definedCells: Map<Point, Cell>
) : TickManager, JPanel(GridBagLayout()) {

    companion object;

    private val cellsMap: Map<Point, Cell>

    init {
        background = Color.BLUE
        val c = GridBagConstraints()
        c.gridx = 0
        c.gridy = 0
        c.fill = GridBagConstraints.BOTH
        val map = mutableMapOf<Point, Cell>()
        1.rangeTo(rows).forEach { row ->
            1.rangeTo(cols).forEach { col ->
                val point = Point(col - 1, row - 1)
                val cell = definedCells[point] ?: VoidCell()
                map[point] = cell
                add(cell, c)
                c.gridx++
            }
            c.gridx = 0
            c.gridy++
        }
        cellsMap = map
    }

    fun cellAt(x: Int, y: Int) = cellsMap[Point(x, y)]

    fun tick() {
        definedCells.values.forEach { cell ->
            println("ticking: $cell")
            cell.tick(this)
        }
    }

    override fun onVesselCell(cell: VesselCell) {
        val followVessel = cellsMap[cell.nextPosition] as? VesselCell
        if (followVessel != null) {
            val particles = cell.clearOldParticles()
            if (particles == 0) return
            followVessel.moveInto(particles)
        } else {
            cell.accumulateParticles()
        }
    }
}
