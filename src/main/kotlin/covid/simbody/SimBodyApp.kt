package simbody

import java.awt.*
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

object SimBodyApp {
    @JvmStatic
    fun main(args: Array<String>) {
        println("SimBody starting ...")
        Engine(Board.sample1).showWindow()
    }
}

class Engine(
    val board: Board
) : Listener {

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

interface Listener {
    fun onTick()
    fun onAddParticle()
}

class MainWindow(
    val board: Board,
    val listener: Listener
) : JFrame("Sim Body") {
    init {
        val btnPanel = JPanel(FlowLayout()).apply {
            add(JButton("add particle").apply {
                addActionListener {
                    listener.onAddParticle()
                }
            })
            add(JButton("click to tick").apply {
                addActionListener {
                    listener.onTick()
                }
            })
        }


        val panel = JPanel(BorderLayout())
        panel.add(btnPanel, BorderLayout.SOUTH)
        panel.add(board, BorderLayout.CENTER)
        contentPane.add(panel)
    }

    fun showYourself() {
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }
}

data class Point(val x: Int, val y: Int) {
    private val asString = "$x/$y"
    override fun toString() = asString

    fun move(direction: Direction): Point = direction.manipulate(this)

}