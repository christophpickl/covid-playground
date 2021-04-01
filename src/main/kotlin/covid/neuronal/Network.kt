package covid.neuronal

import cpickl.neuronal.LearnConfig
import cpickl.neuronal.Node
import cpickl.neuronal.Ref
import cpickl.neuronal.TestSet
import org.apache.commons.math3.analysis.function.Sigmoid

data class Network(
    val inputs: List<Node>,
    val hiddenLayers: List<List<Node>>,
    val outputs: List<Node>,
    val learnConfig: LearnConfig,
) {
    companion object {
        fun build(
            inputCount: Int,
            layersCount: Int,
            layersNodeSize: Int, // < 2x in; betweein in/out (if wide apart) -OR- 2/3 in + output
            outputCount: Int,
            learnConfig: LearnConfig,

            ) = Network(
            inputs = 1.rangeTo(inputCount).map { Node("I$it") },
            hiddenLayers = 1.rangeTo(layersCount).map { layer ->
                1.rangeTo(layersNodeSize).map { node -> Node("H$layer-$node") }
            },
            outputs = 1.rangeTo(outputCount).map { Node("O$it") },
            learnConfig = learnConfig,
        ).wireReferences()

        private fun Network.wireReferences() = apply {
            inputs.forEach { input ->
                hiddenLayers.first().forEach { hidden ->
                    val ref = Ref(input, hidden)
                    input.refs += ref
                    hidden.refs += ref
                }
            }
            hiddenLayers.zipWithNext { xs, ys ->
                xs.forEach { x ->
                    ys.forEach { y ->
                        val ref = Ref(x, y)
                        x.refs += ref
                        y.refs += ref
                    }
                }
            }
            outputs.forEach { output ->
                hiddenLayers.last().forEach { hidden ->
                    val ref = Ref(hidden, output)
                    output.refs += ref
                    hidden.refs += ref
                }
            }
        }
    }

    fun learn(iterations: Int, data: List<TestSet>) { // desiredErrorLevel: Double
        require(data.all { it.inputs.size == inputs.size })
        require(data.all { it.answerPosition in 0..(outputs.size - 1) })


        1.rangeTo(iterations).forEach { iteration ->
            println("Iteration $iteration:")
            data.forEach { test ->
                println("\tLearning ... $test")

                hiddenLayers.forEach { nodes ->
                    nodes.forEach { node ->
                        // sum of { InputNode.value * Reference.value } + HiddenNode.value
                        val e = node.incomingRefs.map { ref -> ref.from.value * ref.value }.sum() + node.value
                        val e2 = activationFunction(e) // TODO what to do?
                    }
                }
                outputs.forEachIndexed { index,  output ->
                    val e = output.incomingRefs.map { ref -> ref.from.value * ref.value }.sum() + output.value
                    val currentGuess = activationFunction(e)

                    val correctAnswer = if(index == test.answerPosition) 1.0 else 0.0
                    val delta = correctAnswer - currentGuess // "delta" equals error rate

                    // adjust weights+biases a "bit"!
                    output.incomingRefs.forEach { ref ->
                        val pastChange = 1.0 // TODO simply from previous "change" down below?
                        val change =(learnConfig.learningRate * delta * ref.value) + (learnConfig.momentum * pastChange)
                        // change = (learningRate * delta * value) + (momentum * pastChange)
                        // FIXME propagate back
//                        ref.value += change
                    }
                }
            }
        }
    }
    private val sig = Sigmoid() // sigmoid, tanh
    private fun activationFunction(e: Double): Double {
        return sig.value(e) // 1 / (1 + exp(-x))
    }

    fun dumpString() = "${inputs.joinToString { "${it.id}/${it.valueInt}" }}\n" +
            "${hiddenLayers.joinToString("\n") { it.joinToString { "${it.id}/${it.valueInt}" } }}\n" +
            "${outputs.joinToString { "${it.id}/${it.valueInt}" }}\n"


    fun toGraphString() =
        "\tInput:\n" +
                inputs.joinToString("\n") { "\t\t$it" } + "\n" +
                "\tHidden:\n" +
                hiddenLayers.joinToString() { layer -> layer.joinToString("\n") { "\t\t$it" } + "\n" } +
                "\tOutput:\n" +
                outputs.joinToString("\n") { "\t\t$it" }
}