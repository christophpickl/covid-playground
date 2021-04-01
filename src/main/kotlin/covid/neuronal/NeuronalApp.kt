package covid.neuronal

import cpickl.neuronal.LearnConfig
import cpickl.neuronal.TestSet

/*

USECASE IDEEN:
1) athletische figure? quasi BMI: input=height cm, weight kg; output= 1(true), 0(false)
2) formen erkennen: triangle/square/circle; output 0,1,2 (pixel zerlegen)
3) beliebige single char erkennen, anhand von font (test input selbst generieren); kann neuen erkennen?!

 */
object NeuronalApp {
    @JvmStatic
    fun main(args: Array<String>) {

        val network = Network.build(
            inputCount = 2,
            layersCount = 1,
            layersNodeSize = 3,
            outputCount = 2,
            learnConfig = LearnConfig(
                learningRate = 1.0,
                momentum = 1.0, // TODO
            )
        )
        println(network.toGraphString())
//    println(network.dumpString())
        network.learn(2,
            listOf(
                TestSet(listOf(0, 1, 0, 1), 0),
                TestSet(listOf(1, 1, 0, 1), 1),
                TestSet(listOf(1, 0, 1, 1), 1),
                TestSet(listOf(0, 0, 1, 1), 0),
            )
            // ASK? TestSet(listOf(1, 0, 0, 0), 0),
        )
    }
}
