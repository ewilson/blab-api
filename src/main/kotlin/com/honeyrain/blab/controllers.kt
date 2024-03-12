package com.honeyrain.blab

import org.springframework.web.bind.annotation.*
import java.util.*


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
