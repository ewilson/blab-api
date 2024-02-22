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
	@GetMapping("/")
	fun index(): List<Blab> = service.findAll()

	@GetMapping("/{id}")
	fun index(@PathVariable id: String): List<Blab> =
		service.findById(id)

	@PostMapping("/")
	fun post(@RequestBody blab: Blab) {
		service.save(blab)
	}
}

interface BlabRepository : CrudRepository<Blab, String>

@Table("BLABS")
data class Blab(@Id var id: String?, val title: String, val userId: String)

@Service
class BlabService(val db: BlabRepository) {
	fun findAll(): List<Blab> = db.findAll().toList()

	fun findById(id: String): List<Blab> = db.findById(id).toList()

	fun save(blab: Blab) {
		db.save(blab)
	}

	fun <T : Any> Optional<out T>.toList(): List<T> =
		if (isPresent) listOf(get()) else emptyList()
}