package com.honeyrain.blab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*
import java.util.*


@SpringBootApplication
class BlabApplication

fun main(args: Array<String>) {
	runApplication<BlabApplication>(*args)
}

@RestController
class BlabController(val service: BlabService) {
	@GetMapping("/blab")
	fun index(): List<Blab> = service.findAll()

	@GetMapping("/blab/{id}")
	fun index(@PathVariable id: String): Optional<Blab> =
		service.findById(id)

	@PostMapping("/blab")
	fun post(@RequestBody blab: Blab) {
		service.save(blab)
	}
}

@RestController
class UserController(val service: UserService) {
	@GetMapping("/user")
	fun index(): List<User> = service.findAll()

	@GetMapping("/user/{id}")
	fun index(@PathVariable id: String): Optional<User> {
		return service.findById(id)
	}

	@PostMapping("/user")
	fun post(@RequestBody user: User) {
		service.save(user)
	}
}

interface BlabRepository : CrudRepository<Blab, String>
interface UserRepository : CrudRepository<User, String>

@Table("BLABS")
data class Blab(@Id var id: String?, val title: String, val userId: String)

@Table("USERS")
data class User(@Id var id: String?, val username: String)

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