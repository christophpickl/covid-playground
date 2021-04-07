package covid.simbody.v2.logic.core

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@Test
class CellTest {

    private lateinit var cell1: Cell
    private lateinit var cell2: Cell

    @BeforeMethod
    fun reset() {
        cell1 = LungCell()
        cell2 = LungCell()
    }

    fun `wire input`() {
        cell1.wireInput(cell2)

        assertThat(cell1.input).isEqualTo(cell2)
        assertThat(cell2.output).isEqualTo(cell1)
    }

    fun `wire output`() {
        cell1.wireOutput(cell2)

        assertThat(cell1.output).isEqualTo(cell2)
        assertThat(cell2.input).isEqualTo(cell1)
    }

}