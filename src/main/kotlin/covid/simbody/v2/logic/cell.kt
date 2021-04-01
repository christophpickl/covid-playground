package covid.simbody.v2.logic

data class Pos/*ition*/(val x: Int, val y: Int)

interface Cell {
    val pos: Pos

}

data class VoidCell(override val pos: Pos) : Cell
data class EsophagusCell(override val pos: Pos) : Cell
