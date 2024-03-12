package com.honeyrain.blab

import org.slf4j.LoggerFactory
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
class BlabController(val service: BlabService) {
    @GetMapping("/blab")
    fun all(): CollectionModel<EntityModel<Blab>> {
        val blabs = service.findAll().stream().map { blab ->
            EntityModel.of(blab,
                    linkTo<BlabController> { one(blab.id!!) }.withSelfRel(),
                    linkTo<BlabController> { all() }.withRel("blabs"),
                    linkTo<UserController> { one(blab.userId) }.withRel("user"),
            )
        }.toList()
        return CollectionModel.of(blabs,
                linkTo<BlabController> { all() }.withSelfRel(),
        )
    }

    @GetMapping("/blab/{id}")
    fun one(@PathVariable id: String): EntityModel<Blab> {
        val blab = service.findById(id).orElseThrow { ObjectNotFoundException(id) }
        return EntityModel.of(blab,
                linkTo<BlabController> { one(id) }.withSelfRel(),
                linkTo<BlabController> { all() }.withRel("blabs"),
                linkTo<UserController> { one(blab.userId) }.withRel("user")
        )
    }

    @DeleteMapping("/blab/{id}")
    fun delete(@PathVariable id: String) =
            service.deleteById(id)

    @PostMapping("/blab")
    fun post(@RequestBody blab: Blab): EntityModel<Blab> {
        service.save(blab)
        return EntityModel.of(blab,
                linkTo<BlabController> { one(blab.id!!) }.withSelfRel(),
                linkTo<BlabController> { all() }.withRel("blabs"),
                linkTo<UserController> { one(blab.userId) }.withRel("user")
        )
    }

}

@RestController
class UserController(val service: UserService) {

    private val log = LoggerFactory.getLogger(javaClass)
    @GetMapping("/user")
    fun all(): CollectionModel<EntityModel<User>> {
        val users = service.findAll().stream().map { user ->
            EntityModel.of(user,
                    linkTo<UserController> { one(user.id!!) }.withSelfRel(),
                    linkTo<UserController> { all() }.withRel("users"),
            )
        }.toList()
        return CollectionModel.of(users,
                linkTo<UserController> { all() }.withSelfRel()
        )
    }


    @GetMapping("/user/{id}")
    fun one(@PathVariable id: String): EntityModel<User> {
        val user = service.findById(id).orElseThrow { ObjectNotFoundException(id) }
        return EntityModel.of(user,
                linkTo<UserController> { one(id) }.withSelfRel(),
                linkTo<UserController> { all() }.withRel("users"),
        )
    }
    @DeleteMapping("/user/{id}")
    fun delete(@PathVariable id: String) {
        return service.deleteById(id)
    }

    @PostMapping("/user")
    fun post(@RequestBody user: User): EntityModel<User> {
        service.save(user)
        return EntityModel.of(user,
                linkTo<UserController> { one(user.id!!) }.withSelfRel(),
                linkTo<UserController> { all() }.withRel("users"),
        )
    }
}

@ControllerAdvice
internal class ObjectNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ObjectNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun objectNotFoundHandler(ex: ObjectNotFoundException): String? {
        return ex.message
    }
}
internal class ObjectNotFoundException(id: String) : RuntimeException("Could not find object $id")

