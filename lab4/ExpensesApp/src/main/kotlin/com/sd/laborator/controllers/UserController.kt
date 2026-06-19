package com.sd.laborator.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import com.sd.laborator.interfaces.IUserService
import com.sd.laborator.pojo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController {

    @Autowired
    private lateinit var userService: IUserService

    @RequestMapping(value = ["/user"], method = [RequestMethod.POST])
    fun createUser(@RequestBody user: User): ResponseEntity<Unit> {
        userService.createUser(user)
        return ResponseEntity(Unit, HttpStatus.CREATED)
    }

    @RequestMapping(value = ["/user/{id}"], method = [RequestMethod.GET])
    fun getUser(@PathVariable id: Int): ResponseEntity<User> {
        return try {
            val user = userService.getUser(id)
            ResponseEntity(user, HttpStatus.OK)
        } catch (e: NoSuchElementException) {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @RequestMapping(value = ["/user/{id}"], method = [RequestMethod.DELETE])
    fun deleteUser(@PathVariable id: Int): ResponseEntity<Unit> {
        return try {
            userService.deleteUser(id)
            ResponseEntity(Unit, HttpStatus.OK)
        } catch (e: NoSuchElementException) {
            ResponseEntity(Unit, HttpStatus.NOT_FOUND)
        }
    }

    @RequestMapping(value = ["/user/username/{username}"], method = [RequestMethod.GET])
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<User> {
        val user = userService.searchByUsername(username)
            ?: return ResponseEntity(HttpStatus.NOT_FOUND)

        return ResponseEntity(user, HttpStatus.OK)
    }

    @RequestMapping(value = ["/user/{id}"], method = [RequestMethod.PATCH])
    fun patchUser(@PathVariable id: Int, @RequestBody patchOperations: JsonPatch): ResponseEntity<Unit> {
        return try {
            val user = userService.getUser(id)

            val objectMapper = ObjectMapper()
            val patchedUserJsonNode = patchOperations.apply(objectMapper.valueToTree(user))
            val patchedUser = objectMapper.treeToValue(patchedUserJsonNode, User::class.java)

            userService.updateUser(id, patchedUser)
            ResponseEntity(Unit, HttpStatus.NO_CONTENT)
        } catch (e: NoSuchElementException) {
            ResponseEntity(Unit, HttpStatus.NOT_FOUND)
        }
    }
}