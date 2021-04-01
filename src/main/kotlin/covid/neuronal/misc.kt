package cpickl.neuronal

data class TestSet(
    val inputs: List<Int>,
    val answerPosition: Int, // 0-base indexed
)
data class LearnConfig(
    val learningRate: Double,
    val momentum: Double,
)

data class Node(
    val id: String,
    val refs: MutableList<Ref> = mutableListOf(),
    override var value: Double = Math.random(), // bias
) : Valueable {
    val incomingRefs: List<Ref> by lazy { refs.filter { it.to == this@Node } }
    val incoming: List<Node> by lazy { incomingRefs.map { it.from } }
    val outgoingRefs: List<Ref> by lazy { refs.filter { it.from == this@Node } }
    val outgoing: List<Node> by lazy { outgoingRefs.map { it.to } }

    override fun toString() =
        "$id:$valueString -> " +
                "{ in => ${incomingRefs.joinToString() { "${it.from.id}:${it.valueString}" }} } - " +
                "{ out => ${outgoingRefs.joinToString() { "${it.to.id}:${it.valueString}" }} }"
}

data class Ref(
    val from: Node,
    val to: Node,
    override var value: Double = Math.random(), // weight
) : Valueable

interface Valueable {
    var value: Double
    val valueInt get() = (value * 100).toInt()
    val valueString get() = "[${(value * 100).toInt()}]"
}