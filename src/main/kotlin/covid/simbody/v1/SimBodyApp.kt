package covid.simbody.v1

object SimBodyApp {
    @JvmStatic
    fun main(args: Array<String>) {
        println("SimBody starting ...")
        Engine(Board.sample1).showWindow()
    }
}

data class Point(val x: Int, val y: Int) {

    private val asString = "$x/$y"
    override fun toString() = asString

    fun move(direction: Direction): Point = direction.manipulate(this)

}
