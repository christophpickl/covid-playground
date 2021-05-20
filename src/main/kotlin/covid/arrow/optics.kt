package covid.arrow

import arrow.optics.Lens
import arrow.optics.optics

// Optics = abstractions to update immutable data structures in an elegant way

@optics
data class User(val address: Address) {
    companion object
}

@optics
data class Address(val street: Street) {
    companion object
}

@optics
data class Street(val number: Int) {
    companion object
}

val user = User(Address(Street(42)))

fun main() {
    `sample copy`()
//    `sample Lens`()
}

fun `sample copy`() {
    val userOldSchool =
        user.copy(address = user.address.copy(street = user.address.street.copy(number = user.address.street.number + 1)))

    val userNewSchool = User.address.street.number.modify(user) { it + 1 }

    println("old: $userOldSchool")
    println("new: $userNewSchool")
}

fun `sample Lens`() {
//    val streetNumberLens: Lens<User, Int> = Lens(
//        get = { it.address.street.number },
//        set = { u, v -> u.copy(address = u.address.copy(street = u.address.street.copy(number = v))) },
//    )
    val addressLens: Lens<User, Address> = Lens(
        get = { it.address },
        set = { u, v -> u.copy(address = v) }
    )
    val streetLens: Lens<Address, Street> = Lens(
        get = { it.street },
        set = { u, v -> u.copy(street = v) }
    )
    val streetNumberLens: Lens<Street, Int> = Lens(
        get = { it.number },
        set = { u, v -> u.copy(number = v) }
    )
    val userStreetNumberLens: Lens<User, Int> = addressLens compose streetLens compose streetNumberLens

    println("Street number: ${userStreetNumberLens.get(user)}")
    val userLensed = userStreetNumberLens.set(user, 21)
    println("Street number lensed: ${userStreetNumberLens.get(userLensed)}")
    val liftUp: (User) -> User = userStreetNumberLens.lift { it + 10 }
    println("lifted: ${liftUp(user)}")
}