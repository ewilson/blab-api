package com.honeyrain.blab

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class BlabService(val db: BlabRepository) {
    fun findAll(): List<Blab> = db.findAll().toList()

    fun findById(id: String): Optional<Blab> = db.findById(id)

    fun save(blab: Blab) {
        db.save(blab)
    }

    fun <T : Any> Optional<out T>.toList(): List<T> =
            if (isPresent) listOf(get()) else emptyList()
}

@Service
class UserService(val db: UserRepository) {
    fun findAll(): List<User> = db.findAll().toList()

    fun findById(id: String): Optional<User> = db.findById(id)

    fun save(user: User) {
        db.save(user)
    }

    fun <T : Any> Optional<out T>.toList(): List<T> =
            if (isPresent) listOf(get()) else emptyList()
}

interface BlabRepository : CrudRepository<Blab, String>
interface UserRepository : CrudRepository<User, String>

