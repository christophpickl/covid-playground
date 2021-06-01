package covid.arrow.core

import arrow.core.*

fun main() {
    `sample Option`()
}

fun `sample Option`() {
    val some = Some(1)
    val nope = None
    val maybe = Option.fromNullable(2)

    when (maybe) {
        is Some -> maybe.value
        is None -> maybe.isEmpty()
    }
    maybe.fold({ println("empty") }, { println("some: $it") })

    maybe.getOrElse { 3 }
    println("maybe doubled: ${maybe.map(::doubled).orElse { Some(42) }}")

    val nullable: String? = null
    val nullableResult = nullable?.length ?: -1

    val option: Option<String> = None
    val optionResult = option.map { it.length }.getOrElse { -1 }

    val mapper: (Option<String>) -> Option<Int> = Option.lift { it.toInt() }
    val maybeInt: Option<String> = Option("1")
    val sureInt: Option<Int> = mapper(maybeInt)
    println(sureInt)
}

fun doubled(value: Int) = value * 2
