package covid.simbody.v2

import covid.simbody.v2.logic.Board
import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.GridBagLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

interface WindowListener {
    fun doTick()
    fun addFood()
}

class BoardPanel(
    private val board: Board
) : JPanel(GridBagLayout()) {
    // FIXME
}

class MainWindow(
    board: Board,
    listener: WindowListener
) : JFrame("SimBody") {

    private val boardPanel = BoardPanel(board)

    init {
        val btnPanel = JPanel(FlowLayout()).apply {
            add(JButton("add food").apply {
                addActionListener {
                    listener.addFood()
                }
            })
            add(JButton("click to tick").apply {
                addActionListener {
                    listener.doTick()
                }
            })
        }


        val panel = JPanel(BorderLayout())
        panel.add(btnPanel, BorderLayout.SOUTH)
        panel.add(boardPanel, BorderLayout.CENTER)
        contentPane.add(panel)
    }

    fun showYourself() {
        pack()
        setLocationRelativeTo(null)
        isVisible = true
    }
}
