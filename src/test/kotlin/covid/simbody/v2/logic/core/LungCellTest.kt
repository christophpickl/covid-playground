package covid.simbody.v2.logic.core

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isInstanceOf
import org.testng.annotations.Test

@Test
class LungCellTest {
    fun foo() {
        val lung = LungCell()
        lung.onTick()
        // FIXME
        assertThat(lung.particles).hasSize(1)
        assertThat(lung.particles.first()).isInstanceOf(ErythroParticle::class)
    }
}