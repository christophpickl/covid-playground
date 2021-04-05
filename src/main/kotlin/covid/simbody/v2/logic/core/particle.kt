package covid.simbody.v2.logic.core

sealed class Particle {
    abstract val symbol: String
}

class OxygenParticle() : Particle() {
    companion object {
        private var counter = 1
    }
    object Info : ParticleInfo {
//        override val symbol = "💧"
        override val symbol = "O"
    }
    override val symbol: String = Info.symbol
    private val idCounter = counter++
    override fun toString() = "OxygenParticle@$idCounter"
}

class SugarParticle : Particle() {
    object Info : ParticleInfo {
//        override val symbol = "🍬"
        override val symbol = "S"
    }
    override val symbol: String = OxygenParticle.Info.symbol
}

interface ParticleInfo {
    val symbol: String
}