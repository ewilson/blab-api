package com.honeyrain.blab

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime


@SpringBootApplication
class BlabApplication

fun main(args: Array<String>) {
	runApplication<BlabApplication>(*args)
}

@Configuration
class Config {

	private val log = LoggerFactory.getLogger(javaClass)

	@Bean
	fun initDatabase(blabService: BlabService, userService: UserService): CommandLineRunner {
		return CommandLineRunner { _ ->
			val users = userService.findAll()
			if (users.isEmpty()) {
				log.info("Empty DB, adding some data")
				val user = User(id = null, username = "the system")
				userService.save(user)
				log.info("Preloading user {}", user.id)
				val blab = Blab(id = null, title = "test blab", userId = user.id!!, createdTime = LocalDateTime.now())
				blabService.save(blab)
				log.info("Preloading blab {}", blab.id)
			} else {
				log.info("leave it alone, found {} users", users.size)
			}
		}
	}
}

