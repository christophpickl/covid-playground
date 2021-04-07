package covid.simbody.v2.logic.core

import covid.simbody.v2.view.ui.TickListener

sealed class Cell : TickListener {

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
    companion object {
        private val MAX_O2_PRODUCE = 6
        private val MAX_CO2_CONSUME = 5
    }

    override fun onTick() {
        val erythros = particles.filterIsInstance<ErythroParticle>()
        var co2ToConsume = MAX_CO2_CONSUME
//        erythros.forEach { erythro ->
//            val released = erythro.releaseCarbondioxid(co2ToConsume)
//            co2ToConsume -= released
//        }
        // FIXME
    }
}

class ArteryCell : Cell() {
    override fun onTick() {
        // no-op
    }
}
