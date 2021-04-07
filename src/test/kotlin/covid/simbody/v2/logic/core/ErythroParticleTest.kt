package covid.simbody.v2.logic.core

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test
class ErythroParticleTest {
    private lateinit var erythro: ErythroParticle

    @BeforeMethod
    fun init() {
        erythro = ErythroParticle()
    }

    fun `initially all empty`() {
        assertThat(erythro).allElementsAre(HemoElement.Empty)
    }

    fun `nothing to takeup because all oxygenized`() {
        erythro.changeAllHemoTo(HemoElement.Oxygen)

        val takenUp = erythro.takeUpOxygen(1)

        assertThat(erythro).allElementsAre(HemoElement.Oxygen)
        assertThat(takenUp).isEqualTo(0)
    }

    fun `nothing to takeup because all carbondioxized`() {
        erythro.changeAllHemoTo(HemoElement.Carbondioxid)

        val takenUp = erythro.takeUpOxygen(1)

        assertThat(erythro).allElementsAre(HemoElement.Carbondioxid)
        assertThat(takenUp).isEqualTo(0)
    }

    fun `takeup single oxygen`() {
        val takenUp = erythro.takeUpOxygen(1)

        assertThat(erythro.hemosElements.filter { it == HemoElement.Oxygen }.count()).isEqualTo(1)
        assertThat(takenUp).isEqualTo(1)
    }

    fun `takeup all oxygen`() {
        val takenUp = erythro.takeUpOxygen(99)

        assertThat(erythro.hemosElements.all { it == HemoElement.Oxygen }).isTrue()
        assertThat(takenUp).isEqualTo(2 * 4)
    }

    fun `nothing to release`() {
        val released = erythro.releaseCarbondioxid(1)

        assertThat(erythro).allElementsAre(HemoElement.Empty)
        assertThat(released).isEqualTo(0)
    }

    fun `release single carbondioxid`() {
        val hemo = erythro.hemos.first()
        hemo.element1 = HemoElement.Carbondioxid

        val released = erythro.releaseCarbondioxid(1)

        assertThat(hemo.element1).isEqualTo(HemoElement.Empty)
        assertThat(released).isEqualTo(1)
    }

    fun `release all carbondioxid`() {
        erythro.changeAllHemoTo(HemoElement.Carbondioxid)

        val released = erythro.releaseCarbondioxid(99)

        assertThat(erythro).allElementsAre(HemoElement.Empty)
        assertThat(released).isEqualTo(2 * 4)
    }
}

fun Assert<ErythroParticle>.allElementsAre(expected: HemoElement) {
    given {  erythro ->
        erythro.hemosElements.forEach { el ->
            assertThat(el).isEqualTo(expected)
        }
    }
}

fun ErythroParticle.changeAllHemoTo(element: HemoElement) {
    hemos.forEach { it.changeAllTo(element) }
}

fun Hemo.changeAllTo(element: HemoElement) {
    element1 = element
    element2 = element
    element3 = element
    element4 = element
}
