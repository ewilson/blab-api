package com.honeyrain.blab

import org.slf4j.LoggerFactory
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.IanaLinkRelations
import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*


@RestController
class BlabController(val service: BlabService, val assembler: BlabModelAssembler) {
    @GetMapping("/blab")
    fun all(): CollectionModel<EntityModel<Blab>> {
        val blabs = service.findAll().stream().map(assembler::toModel).toList()
        return CollectionModel.of(blabs, linkTo<BlabController> { all() }.withSelfRel())
    }

    @GetMapping("/blab/{id}")
    fun one(@PathVariable id: String): EntityModel<Blab> {
        val blab = service.findById(id).orElseThrow { ObjectNotFoundException(id) }
        return assembler.toModel(blab)
    }

    @DeleteMapping("/blab/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Any> {
        service.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/blab")
    fun post(@RequestBody blab: Blab): ResponseEntity<EntityModel<Blab>> {
        service.save(blab)
        val entityModel = assembler.toModel(blab)
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel)
    }
}

@RestController
class UserController(val service: UserService, val assembler: UserModelAssembler) {

    private val log = LoggerFactory.getLogger(javaClass)
    @GetMapping("/user")
    fun all(): CollectionModel<EntityModel<User>> {
        val users = service.findAll().stream().map(assembler::toModel).toList()
        return CollectionModel.of(users, linkTo<UserController> { all() }.withSelfRel())
    }

    @GetMapping("/user/{id}")
    fun one(@PathVariable id: String): EntityModel<User> {
        val user = service.findById(id).orElseThrow { ObjectNotFoundException(id) }
        return assembler.toModel(user)
    }
    @DeleteMapping("/user/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Any> {
        service.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/user")
    fun post(@RequestBody user: User): ResponseEntity<EntityModel<User>> {
        service.save(user)
        val entityModel = assembler.toModel(user)
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel)
    }

    @PutMapping("/user")
    fun put(@RequestBody user: User): ResponseEntity<EntityModel<User>> {
        if (user.id == null) {
            throw ObjectNotFoundException(user.toString())
        } else {
            val existingUser = service.findById(user.id!!).orElseThrow { ObjectNotFoundException(user.id!!) }
            existingUser.bio = user.bio
            existingUser.name = user.name
        }
        val entityModel = assembler.toModel(user)
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel)
    }

}

@Component
class UserModelAssembler : RepresentationModelAssembler<User, EntityModel<User>> {
    override fun toModel(user: User): EntityModel<User> {
        return EntityModel.of(user,
                linkTo<UserController> { one(user.id!!) }.withSelfRel(),
                linkTo<UserController> { all() }.withRel("users"),
        )
    }
}

@Component
class BlabModelAssembler : RepresentationModelAssembler<Blab, EntityModel<Blab>> {
    override fun toModel(blab: Blab): EntityModel<Blab> {
        return EntityModel.of(blab,
                linkTo<BlabController> { one(blab.id!!) }.withSelfRel(),
                linkTo<BlabController> { all() }.withRel("blabs"),
                linkTo<UserController> { one(blab.userId) }.withRel("user"),
        )
    }
}


@ControllerAdvice
internal class ObjectNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ObjectNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun objectNotFoundHandler(ex: ObjectNotFoundException): String? = ex.message
}
internal class ObjectNotFoundException(id: String) : RuntimeException("Could not find object $id")

