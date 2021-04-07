package covid.simbody.v2.logic.core

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test
class BodyTest {

    private lateinit var particle: ErythroParticle
    private lateinit var organ: OrganCell
    private lateinit var artery: ArteryCell
    private lateinit var body: Body

    @BeforeMethod
    fun reset() {
        particle = ErythroParticle()
        organ = LungCell()
        artery = ArteryCell()
        organ.wireOutput(artery)
        artery.wireOutput(organ)
        organ.particles += particle
        body = Body(setOf(organ, artery))
    }

    fun `tick, move particle`() {
        body.onTick()

        assertThat(organ.particles).isEmpty()
        assertThat(artery.particles).containsOnly(particle)
    }
}