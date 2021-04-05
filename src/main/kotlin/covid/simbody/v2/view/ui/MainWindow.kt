package covid.simbody.v2.view.ui

import covid.simbody.v2.view.Board
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

class MainWindow(
    board: Board,
    listener: InteractionListener
) : JFrame("SimBody") {

    private val boardPanel = BoardPanel(board)

    init {
        val btnPanel = JPanel(FlowLayout()).apply {
            add(JButton("add O2").apply {
                addActionListener {
                    listener.addOxygen()
                    this@MainWindow.repaint()
                }
            })
            add(JButton("tick").apply {
                addActionListener {
                    listener.doTick()
                    this@MainWindow.repaint()
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
