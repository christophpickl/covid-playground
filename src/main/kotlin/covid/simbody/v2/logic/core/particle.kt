package covid.simbody.v2.logic.core

import kotlin.reflect.KMutableProperty

sealed class Particle {
    abstract val symbol: String
}

class ErythroParticle() : Particle() {
    companion object {
        private var counter = 1
    }

    object Info : ParticleInfo {
        //        override val symbol = "💧"
        override val symbol = "E"
    }

    private val hemo1 = Hemo()
    private val hemo2 = Hemo()
    val hemos: Collection<Hemo> = listOf(hemo1, hemo2)
    val hemosElements get() = hemos.map { it.elements }.flatten()

    /** returns actual oxygen uptake */
    fun takeUpOxygen(amount: Int) = changeHemoElements(amount, HemoElement.Empty, HemoElement.Oxygen)

    /** returns actual carbondioxid release */
    fun releaseCarbondioxid(amount: Int) = changeHemoElements(amount, HemoElement.Carbondioxid, HemoElement.Empty)

    private fun changeHemoElements(amount: Int, search: HemoElement, replace: HemoElement): Int {
        var amountStill = amount
        while (amountStill > 0 && countHemoElements(search) > 0) {
            val emptyElement = hemos.mapNotNull { it.elementPropertyFor(search) }.firstOrNull() ?: break
            emptyElement.setter.call(replace)
            amountStill--
        }
        return amount - amountStill
    }

    private fun allHemoElementsAre(expected: HemoElement): Boolean =
        hemos.all { it.elements.all { it == expected } }

    private fun countHemoElements(filter: HemoElement): Int =
        hemos.map { it.elements.count { it == filter } }.sum()

    override val symbol: String = Info.symbol
    private val idCounter = counter++
    override fun toString() = "ErythroParticle@$idCounter"
}

class Hemo {
    var element1 = HemoElement.Empty
    var element2 = HemoElement.Empty
    var element3 = HemoElement.Empty
    var element4 = HemoElement.Empty
    val elements get() = listOf(element1, element2, element3, element4)
    val elementsProp
        get() = mapOf<HemoElement, KMutableProperty<HemoElement>>(
            element1 to ::element1,
            element2 to ::element2,
            element3 to ::element3,
            element4 to ::element4,
        )

    fun elementPropertyFor(find: HemoElement) =
        elementsProp.entries.firstOrNull {
            it.key == find
        }?.value

}

enum class HemoElement {
    Empty,
    Oxygen,
    Carbondioxid,
}


class SugarParticle : Particle() {
    object Info : ParticleInfo {
        //        override val symbol = "🍬"
        override val symbol = "S"
    }

    override val symbol: String = ErythroParticle.Info.symbol
}

interface ParticleInfo {
    val symbol: String
}