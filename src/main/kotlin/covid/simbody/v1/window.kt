package covid.simbody.v1

import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JPanel

interface UserActionListener {
    fun onTick()
    fun onAddParticle()
}

class MainWindow(
    val board: Board,
    val listener: UserActionListener
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
