package covid.simbody.v2

import covid.simbody.v2.logic.BoardDefinition
import covid.simbody.v2.logic.Engine

object SimBodyV2App {
    @JvmStatic
    fun main(args: Array<String>) {
        println("SimBody starting ...")

        val board = BoardDefinition.sample1.toBoard()
        val engine = Engine(board)
        val listener = MyWindowListener(engine)
        MainWindow(board, listener).showYourself()
    }
}

class MyWindowListener(
    private val engine: Engine
    ) : WindowListener {

    override fun doTick() {
        engine.tick()
    }

    override fun addFood() {
    }
}
