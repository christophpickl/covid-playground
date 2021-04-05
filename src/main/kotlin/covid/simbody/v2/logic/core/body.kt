package covid.simbody.v2.logic.core

class Body(
    val cells: Set<Cell>,
) {
    fun tick() {
        val p2: MutableSet<ToBeMovedParticle> = cells.associateParticles()
        p2.forEach { moveParticle ->
            require(moveParticle.cell.particles.remove(moveParticle.delegate))
            require(moveParticle.cell.output.particles.add(moveParticle.delegate))
//            moveParticle.moved()
        }
    }

    private fun Set<Cell>.associateParticles(): MutableSet<ToBeMovedParticle> {
        val associated = mutableSetOf<ToBeMovedParticle>()
        this.forEach { c ->
            c.particles.forEach { p ->
                require(associated.add(ToBeMovedParticle(c, p)))
            }
        }
        return associated
    }
}

data class ToBeMovedParticle(
    val cell: Cell,
    val delegate: Particle,
) {
//    var isMoved: Boolean = false
//        private set
//
//    fun moved() {
//        require(!isMoved)
//        isMoved = true
//    }
}
