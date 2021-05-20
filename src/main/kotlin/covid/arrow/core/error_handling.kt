package covid.arrow

import arrow.core.Either
import arrow.core.computations.either
import kotlinx.coroutines.runBlocking

fun main() {
    when(val lunch = runBlocking { prepareLunch() }) {
        is Either.Right -> println("yummy salad: ${lunch.value.calories} cal")
        is Either.Left -> println("error: ${lunch.value}")
    }
}

sealed class CookingFailedError {
    object NoKnifeError : CookingFailedError()
    object EmptyFridgeError : CookingFailedError()
    object CutFingerError : CookingFailedError()
}
typealias CookingFailed = CookingFailedError
typealias NoKnife = CookingFailedError.NoKnifeError
typealias EmptyFridge = CookingFailedError.EmptyFridgeError
typealias CutFinger = CookingFailedError.CutFingerError

data class Salad(
    val calories: Int
)

object Lettuce

fun takeFoodFromRefrigerator(): Either<EmptyFridge, Lettuce> =
    Either.Left(EmptyFridge)
//    Either.Right(Lettuce)

object Knife
fun getKnife(): Either<NoKnife, Knife> =
    Either.Left(NoKnife)
//    Either.Right(Knife)

fun prepare(knife: Knife, lettuce: Lettuce): Either<CutFinger, Salad> {
    println("prepare: $knife, $lettuce")
    return Either.Left(CutFinger)
//    return Either.Right(Salad(20))
}

suspend fun prepareLunch(): Either<CookingFailed, Salad> =
    either {
        val lettuce = takeFoodFromRefrigerator().bind()
        val knife = getKnife().bind()
        val salad = prepare(knife, lettuce).bind()
        salad
    }
