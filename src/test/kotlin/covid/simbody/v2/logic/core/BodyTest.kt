package covid.simbody.v2.logic.core

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test
class BodyTest {

    private lateinit var particle: OxygenParticle
    private lateinit var organ: OrganCell
    private lateinit var artery: ArteryCell
    private lateinit var body: Body

    @BeforeMethod
    fun reset() {
        particle = OxygenParticle()
        organ = OrganCell()
        artery = ArteryCell()
        organ.wireOutput(artery)
        artery.wireOutput(organ)
        organ.particles += particle
        body = Body(setOf(organ, artery))
    }

    fun `tick, move particle`() {
        body.tick()

        assertThat(organ.particles).isEmpty()
        assertThat(artery.particles).containsOnly(particle)
    }
}