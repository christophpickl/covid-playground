package covid.simbody.v1

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import javax.swing.JPanel

sealed class Cell(
    val baseColor: Color,
    val cellSize: Int = CELL_SIZE,
) : JPanel() {

    init {
        size = Dimension(cellSize, cellSize)
        minimumSize = Dimension(cellSize, cellSize)
        preferredSize = Dimension(cellSize, cellSize)
    }

    abstract fun tick(manager: TickManager)
    abstract fun cellPaint(g: Graphics)

    override fun paint(g: Graphics) {
        cellPaint(g)
    }

    protected fun Graphics.drawCellBorder() {
        color = Color.BLACK
        drawRect(0, 0, cellSize, cellSize)
    }

    protected fun Graphics.fillCell(fillColor: Color = baseColor) {
        color = fillColor
        fillRect(0, 0, cellSize, cellSize)
    }

}

enum class Direction {
    Up {
        override fun manipulate(p: Point) = Point(p.x, p.y - 1)
    },
    Down {
        override fun manipulate(p: Point) = Point(p.x, p.y + 1)
    },
    Left {
        override fun manipulate(p: Point) = Point(p.x - 1, p.y)
    },
    Right {
        override fun manipulate(p: Point) = Point(p.x + 1, p.y)
    };

    abstract fun manipulate(p: Point): Point
}

class VoidCell : Cell(Color.RED) {
    override fun cellPaint(g: Graphics) {
        g.fillCell()
        g.drawCellBorder()
    }

    override fun tick(manager: TickManager) {
        // no-op
    }
}


class VesselCell(
    val position: Point,
    val direction: Direction,
) : Cell(Color.GREEN) {

    var newParticles = 0
    var currentParticles = 0
    val nextPosition = direction.manipulate(position)
    val altColor = baseColor.darker().darker()

    override fun tick(manager: TickManager) {
        manager.onVesselCell(this)
    }

    override fun cellPaint(g: Graphics) {
        g.fillCell(if (currentParticles > 0) altColor else baseColor)

        g.color = Color.BLACK
        g.drawString("$currentParticles", 10, 20)
        g.drawString("${direction.name.first()}", 10, 30)

        g.drawCellBorder()
    }

    fun moveInto(count: Int) {
        println("$position => moveInto $count (particles: $count)")
        newParticles += count
    }
//    fun initiallySetOldParticles(count: Int) {
//        oldParticles = count
//    }

    fun clearOldParticles(): Int = currentParticles.also {
        println("$position => clearOldParticles (count: $it)")
        currentParticles = newParticles
        newParticles = 0
    }

    fun accumulateParticles() {
        println("$position accumulates!")
        currentParticles += newParticles
        newParticles = 0
    }

    override fun toString() = "VesselCell[pos=$position, dir=$direction]"
}
