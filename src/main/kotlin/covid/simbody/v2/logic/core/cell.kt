package covid.simbody.v2.logic.core

sealed class Cell {

    lateinit var input: Cell
        private set
    lateinit var output: Cell
        private set

    fun wireInput(value: Cell) {
        input = value
        value.output = this
    }
    fun wireOutput(value: Cell) {
        output = value
        value.input = this
    }

    val particles = mutableSetOf<Particle>()

    override fun toString() = "${this.javaClass.simpleName}[particles=$particles]"
}

sealed class OrganCell : Cell() {
}
class LungCell : OrganCell() {

}

class ArteryCell: Cell()
