package covid.arrow.core

import arrow.core.combineAll
import arrow.typeclasses.Monoid

fun main() {
//    `sample Monoid`()
    `sample Arrow and Apply`()
}

fun `sample Monoid`() {
    // like an interface, but not...
    // implement: empty (returning default value) + combine (takes two, returns one)
    println("combined int: " + AdditionIntMonoid.combineAll(listOf(1, 2, 3)))
    println("combined string: " + StringConcatMonoid.combine("a", "b"))

    println("monoid int: " + Monoid.int().combineAll(listOf(1, 2, 3)))
    println("monoid string: " + Monoid.string().combineAll(listOf("a", "b")))
    println("monoid collection: " + listOf(1, 2).combineAll(Monoid.int()))
}

interface MyMonoid<A> {
    fun empty(): A
    fun combine(x: A, y: A): A
    fun combineAll(elements: Collection<A>): A =
        elements.fold(empty()) { acc, x -> combine(acc, x) }
}

object AdditionIntMonoid : MyMonoid<Int> {
    override fun empty() = 0
    override fun combine(x: Int, y: Int) = x + y
}

object StringConcatMonoid : MyMonoid<String> {
    override fun empty() = ""
    override fun combine(x: String, y: String) = x + y
}

// Functor ... wrapping/unrwapping (boxing) of value
// Applicative(Functor) ... wrapping/unrwapping (boxing) of function
// Monad : Applicative


// ==============================================================================
// thanks to jacob bass: https://www.youtube.com/watch?v=ERM0mBPNLHc

interface Kind<F, A> // some kind of wrapper
class ForId private constructor() // no one else outside this file can access it ;)
typealias IdOf<T> = Kind<ForId, T> // kotlin loves sugar

// unchecked cast as the price we have to pay (having not a proper functional language at hand)
inline fun <T> IdOf<T>.fix(): Id<T> = this as Id<T>

data class Id<A>(val a: A) : IdOf<A> {
    companion object

    fun <B> map(f: (A) -> B): IdOf<B> = Id(f(a))
}

interface Functor<F> {
    fun <A, B> Kind<F, A>.map(f: (A) -> B): Kind<F, B>
}

interface IdFunctorInstance : Functor<ForId> {
    override fun <A, B> Kind<ForId, A>.map(f: (A) -> B) = fix().map(f)
}

fun Id.Companion.functor(): IdFunctorInstance = object : IdFunctorInstance {}

fun `sample Arrow and Apply`() {
    val idStr: Id<String> = Id("1")
    val idInt: IdOf<Int> = idStr.map { it.toInt() }
    println("from str $idStr to int $idInt")

    // witness?! meh :(
    fun <F> addOne(witness: Functor<F>, a: Kind<F, Int>): Kind<F, Int> = witness.run { a.map { it + 1 } }
    val idTwo: IdOf<Int> = addOne(Id.functor(), Id(1))
    println("idTwo witness: $idTwo")
}