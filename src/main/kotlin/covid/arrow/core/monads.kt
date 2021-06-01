package covid.arrow.core

import arrow.core.None
import arrow.core.Option
import arrow.core.computations.nullable
import arrow.core.computations.option
import kotlinx.coroutines.runBlocking

// as seen in: https://arrow-kt.io/docs/patterns/monads/

fun main() {
//    `sample NullableMonad`()
    `sample OptionMonad`()
}


class Speaker(var talk: Talk? = null) {
    fun giveTalk(): Talk? {
        println("Speaker.giveTalk: $talk")
        return talk
    }
}

class Talk(var conference: Conference? = null) {
    fun locatedAt(): Conference? {
        println("Talk.locatedAt: $conference")
        return conference
    }
}

class Conference(var city: City? = null) {
    fun happeningIn(): City? {
        println("Conference.happeningIn: $city")
        return city
    }
}

data class City(val name: String = "Vienna")

//fun interface Effect<F> {
//    fun control(): DelimitedScope<F>
//}
//interface DelimitedScope<R> {
//    suspend fun <A> shift(r: R): A
//}
//fun interface NullableEffect<A> : Effect<A?> {
//    suspend fun <B> B?.bind(): B = this ?: control().shift(null)
//}
//object nullable {
//    operator fun <A> invoke(func: suspend NullableEffect<*>.() -> A?): A? =
//        Effect.restricted(eff = { NullableEffect { it } }, f = func, just = { it })
//}

fun `sample NullableMonad`() {
    val city = null//City()
    val conf = null//Conference(city)
    val talk = Talk(conf)
    val speaker = Speaker(talk)
    println("city result: " + runBlocking { doNullableMonad(speaker) })
}

suspend fun doNullableMonad(maybeSpeaker: Speaker?): City? = nullable {
    val speaker = maybeSpeaker.bind()
    val talk = speaker.giveTalk().bind()
    val conf = talk.locatedAt().bind()
    val city = conf.happeningIn().bind()
    city
}

// ================================

data class Customer(val addressId: Int)
data class Address(val id: Int, val lastOrder: Option<Order> = None)
data class Order(val id: Int, val shipper: Shipper = Shipper)
object Shipper

interface OptionRepository {
    fun getCustomer(id: Int): Option<Customer> {
        println("get customer: $id")
        return when(id) {
            1 -> Option(Customer(2))
            else -> None
        }
    }
    fun getAddress(id: Int): Option<Address> {
        println("get address: $id")
        return None
    }
    fun getOrder(id: Int): Option<Order> {
        println("get order: $id")
        return None
    }
}

object OptionInMemoryRepository : OptionRepository

suspend fun OptionRepository.shipperOfLastOrderOnCurrentAddress(customerId: Int): Option<Shipper> = option {
    val customer = getCustomer(customerId).bind()
    val address = getAddress(customer.addressId).bind()
    val lastOrder = address.lastOrder.bind()
    val order = getOrder(lastOrder.id).bind()
    order.shipper
}

fun `sample OptionMonad`() {
    val shipper = runBlocking { OptionInMemoryRepository.shipperOfLastOrderOnCurrentAddress(1) }
    println("shipper: $shipper")
}
