package covid.arrow

import arrow.core.*
import arrow.typeclasses.Semigroup

// arrow => merge of kategory and funktionale
// monad => wrapper classs (factory method, map, flatMap)
// typeclasses => Monoid, Semigroup, Semiring

// Arrow = { Core, FX, Optics, Meta }
// https://medium.com/beingprofessional/understanding-functor-and-monad-with-a-bag-of-peanuts-8fa702b3f69e

fun main() {
//    `sample Const`()
//    `sample Option`()
//    `sample Either`()
//    `sample Eval`()
//    `sample Either error handling`()
//    `sample misc`()
    `sample Applicatives`()
}

fun `sample Const`() {
    val const1 = Const<Int, String>(42)
    println("const1: $const1")
    println("  value: ${const1.value()}")
    println("  retag: ${const1.retag<Boolean>()}")
    val zipped: Const<Int, Boolean> = const1.zip<Int, Boolean>(Semigroup.int(), Const(10)) { string, int -> true }
    println("zipped: $zipped")
}

fun `sample Option`() {
    // Option can be [ Some | None ]
    // technically not a monad
    val intOpt = Option(42) // actually companion invoke method ;)
    val intOptAlt: Option<Int> = Some(42)
    println("intOpt: $intOpt")

    val nullableOpt = Option.fromNullable<Int>(null)
    val nullableOptAlt = none<Int>()
    val nonGeneric: Option<Nothing> = None
    println("nullableOpt: $nullableOpt")
    println("  mapped: ${nullableOpt.map { it + 1 }}")

    println("riskyOpt: ${Option.catch({ println("oops: $it") }, { riskyCompute(2) })}")

    val lifter = Option.lift<Int, String> { it.toString() }
    val liftedOpt: Option<String> = lifter(intOpt)
    println("liftedOpt: $liftedOpt")

    val zippedOpt: Option<Pair<Int, String>> = intOpt.zip(Option("a"))
    println("zippedOpt: $zippedOpt")

    val replicatedOpt: Option<List<Int>> = intOpt.replicate(3)
    println("replicated: $replicatedOpt")
}

private fun riskyCompute(input: Int): String {
    return if (input % 2 == 0) {
        "yes"
    } else {
        throw Exception("nope")
    }
}

fun `sample Either`() {
    // right side focused (business), left side for exceptions
    val either1: Either<Unit, String> = Either.fromNullable(null)
    println("either1 value: ${either1.getOrElse { "nope" }}")
    println("either1 length: ${either1.map { it.length }}")

    val intEither: Either<Int, Nothing> = 1.left()
    val stringEither: Either<Nothing, String> = "foobar".right()
    stringEither.toValidated()

    val maybe: Either<String, Int> = Either.conditionally(true, { "1" }, { 2 })
    println("maybe L=${maybe.isLeft()}, R=${maybe.isRight()}")
}

fun `sample Either error handling`() {
    val maybeX = parseToInt("x")
    println("parse 'x': $maybeX")
    val maybe3 = parseToInt("3")
    println("parse '3': $maybe3")

    when (val computed = maybe3.map(::compute)) {
        is Either.Right -> println("yay, result: ${computed.value}")
        is Either.Left -> when (computed.value) {
            is NotAnIntError -> println("fail: not an int '${computed.value.givenInput}'")
            is UnknownError -> println("fail: and we don't know what :(")
        }
    }
}

fun compute(x: Int): Int = x * 10 + 3

fun parseToInt(maybeInt: String): Either<InputError, Int> =
    Either.nullConditionally(maybeInt.toIntOrNull()) { NotAnIntError(maybeInt) }

fun <ERR, VAL> Either.Companion.nullConditionally(maybe: VAL?, alternate: () -> ERR): Either<ERR, VAL> =
    conditionally<ERR, VAL>(maybe != null, alternate) { maybe!! }

sealed class InputError(val givenInput: String)
class NotAnIntError(givenInput: String) : InputError(givenInput)
class UnknownError(givenInput: String) : InputError(givenInput)

fun `sample Eval`() {
    val preCalculated = Eval.now(1)
    val evalLater = Eval.later { println("later computed"); 1 }
    println("evalLater.value: ${evalLater.value()}")
    println("evalLater.value: ${evalLater.value()}")

    val evalDefer = Eval.defer { println("deferred computed"); Eval.now(2) }
    println("evalDefer.value: ${evalDefer.value()}")
    println("evalDefer.value: ${evalDefer.value()}")
    val memoizedDeferred = evalDefer.memoize()
    println("evalDefer.memoize.value: ${memoizedDeferred.value()}")
    println("evalDefer.memoize.value: ${memoizedDeferred.value()}")

    val evalAlways = Eval.always { println("always computed"); 3 }
    println("evalAlways.value: ${evalAlways.value()}")
    println("evalAlways.value: ${evalAlways.value()}")
}

fun `sample misc`() {
    val someBool = true
    val maybe: Option<String> = someBool.maybe { "yes" } // reminds me a bit of SmallTalk, nice :)
    println(
        "maybe uppered: ${
            maybe.map {
                println("(map being executed)")
                it.toUpperCase()
            }
        }"
    )
}

fun `sample flat map`() {
    val singleWrapped: Either<Nothing, Int> = Either.Right(1)
    val doubleWrapped: Either<Nothing, Either<Nothing, Int>> = Either.Right(singleWrapped)
    val singleWrappedAgain: Either<Nothing, Int> = doubleWrapped.flatten()

    val x = Either.Right(21)
    val foo1: Either<Nothing, Int> = x.map { doubleAndWrap(it) }.flatten()
    val foo2: Either<Nothing, Int> = x.flatMap { doubleAndWrap(it) }
}

fun doubleAndWrap(x: Int): Either<Nothing, Int> =
    Either.Right(x * 2)

fun `sample Applicatives`() {
    println("applicative applied: ${Some { x: Int -> x * 2 } apply Some(21)}") // => Some(42)

    val ints = arrayOf<(Int) -> Int>({ it + 1 }, { it * 2 }) apply arrayOf(1, 2, 3)
    println("double function: ${ints.contentToString()}")
    // => [2, 3, 4, 2, 4, 6]

    // big boys can use functions with any number of arguments
    println("Curried sum: ${Some(3).map(::curriedSum) apply Some(2)}")
    // Some(3).map(::curriedSum) => Some({ 3 + b})
    // Some({ 3 + b }) apply Some(2)

    val product = Some(3).map(curry(::trippleProduct)) apply Some(2) apply Some (2)
    println("product: $product") // => Some(12)
}

infix fun <A, B> Option<(A) -> B>.apply(f: Option<A>): Option<B> = when (this) {
    is None -> None
    is Some -> f.map(value)
}

inline infix fun <A, reified B> Array<(A) -> B>.apply(list: Array<A>) =
    Array(size * list.size) {
        val f = this[it / list.size]
        f(list[it % list.size])
    }

fun curriedSum(a: Int) = { b: Int -> a + b }

fun trippleProduct(a: Int, b: Int, c: Int) = a * b * c

fun <A, B, C, D> curry(f: (A, B, C) -> D): (A) -> (B) -> (C) -> D =
    { a -> { b -> { c -> f(a, b, c) } } }
