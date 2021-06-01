package covid.arrow.core

//import arrow.core.compose
import arrow.core.curried
import arrow.core.partially1

// thanks jacob bass: https://www.youtube.com/watch?v=tM2wEI-e80E

fun main() {
//    `sample compose`()
//    `sample partial application`()
    `sample uncurried`()
}

fun `sample compose`() {
    val big: (Int) -> Boolean = { it > 3 }
    val even: (Int) -> Boolean = { it % 2 == 0 }
    val bigAndEven: (Int) -> Boolean = { big(it) && even(it) }
    // val bigAndEven : (Int) -> Boolean = big compose even // TODO doesn't work?!
    println((1..6).toList().filter(bigAndEven))
}

fun `sample partial application`() {
    val big: (Int) -> Boolean = { it > 3 }
    val even: (Int) -> Boolean = { it % 2 == 0 }

    val funFilter: ((Int) -> Boolean, List<Int>) -> List<Int> = { f, l -> l.filter(f) }

//    val biggies: (List<Int>) -> List<Int> = funFilter.partially1(big)
//    val evenies: (List<Int>) -> List<Int> = funFilter.partially1(even)
//    val bigAndEven: (List<Int>) -> List<Int> = biggies compose evenies
//
//    println(bigAndEven((1..6).toList()))
}

fun `sample uncurried`() {
    val uncurried: ((Int) -> Boolean, List<Int>) -> List<Int> = { f: (Int) -> Boolean, list: List<Int> -> list.filter(f) }
    // first curry it, to partially apply it.
    // otherwise we can simply use arrow's partiallyX() methods to do the same on uncurried function; as did above.
    val filter: ((Int) -> Boolean) -> (List<Int>) -> List<Int> = uncurried.curried()
    val biggies = filter { it > 3 }
    val evenies = filter { it % 2 == 0 }
    // no more pipes |>
    val bigAndEven: (List<Int>) -> List<Int> = { x: List<Int> -> evenies(biggies(x)) }

    println(bigAndEven((1..6).toList()))

    val strong: (String) -> String = { body -> "<strong>$body</strong>" }
    println(strong("From a pipe"))
}