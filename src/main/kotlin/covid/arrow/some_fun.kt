package covid.arrow

// thanks to philip wadler: https://www.youtube.com/watch?v=yjmKMhJOJos

fun main() {
    val term: Term =
        3.c `×` (1.c `+` 2.c)
        // Mul(Con(3), Add(Con(1), Con(2)))
    println("Evaluted term is: ${term.eval()}")
    // (19)42 the lambda calculus was invented
    println("Or shortened: ${term.eval().map { it / 2 }}")

    val error: Term =
        3.c `∕` 0.c
    println("Boom: ${error.eval()}")

}

// TODO exercise: count a specific operation in a given term (without storing global mutable state of course ;)
// ... see video minute 20

// because kotlin loves sugar 🍭
val Int.c get() = Con(this)
infix fun Term.`+`(t2: Term) = Add(this, t2)
infix fun Term.`-`(t2: Term) = Add(this, t2)
infix fun Term.`×`(t2: Term) = Mul(this, t2)
infix fun Term.`∕`(t2: Term) = Div(this, t2)

// core logic
fun Term.eval(): M<Int> = when (this) {
    is Con -> Result(x)
    is Add -> binaryEval(Operator.plus())
    is Sub -> binaryEval(Operator.minus())
    is Mul -> binaryEval(Operator.multiplication())
    is Div -> binaryEval(Operator.division())
}

// data structures
sealed class Term
class Con(val x: Int) : Term()
class Add(override val t1: Term, override val t2: Term) : Term(), BinaryTerm
class Sub(override val t1: Term, override val t2: Term) : Term(), BinaryTerm
class Mul(override val t1: Term, override val t2: Term) : Term(), BinaryTerm
class Div(override val t1: Term, override val t2: Term) : Term(), BinaryTerm
/*
in haskell:
data Term       = Con Int | Add Term Term
eval            :: Term -> Int
eval(Con x)     = x
eval(Add t1 t2) = eval t1 + eval t2
 */

typealias Error = String

// our very own monad, yay :)
sealed class M<A>
class Result<A>(val value: A) : M<A>() {
    override fun toString() = "Result[$value]"
}
class Raise<A>(val error: Error) : M<A>() {
    override fun toString() = "Error[$error]"
}

// simply reuse some code
interface BinaryTerm {
    val t1: Term
    val t2: Term
}

enum class Operator(val code: (Int, Int) -> M<Int>) {
    plus({ x, y -> Result(x + y)}),
    minus({ x, y -> Result(x - y)}),
    multiplication({ x, y -> Result(x * y)}),
    division({ x, y -> if(y == 0) Raise("division by zero") else Result(x / y)})
    ;
    operator fun invoke() = code
}

fun BinaryTerm.binaryEval(operator: (Int, Int) -> M<Int>): M<Int> =
    t1.eval().let { ted1 ->
        when (ted1) {
            is Raise -> ted1
            is Result -> t2.eval().let { ted2 ->
                when (ted2) {
                    is Raise -> ted2
                    is Result -> operator(ted1.value, ted2.value)
                }
            }
        }
    }

// utility
fun <A> M<A>.map(f: (A) -> A): M<A> = when (this) {
    is Result<A> -> Result(f(value))
    is Raise<A> -> this
}
