package com.sd.laborator.controllers

import com.sd.laborator.interfaces.IAuthService
import com.sd.laborator.pojo.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

data class LoginRequest(
    val username: String,
    val password: String
)

@RestController
class AuthController {
    @Autowired
    private lateinit var authService: IAuthService

    @RequestMapping(value = ["/auth/login"], method = [(RequestMethod.POST)])
    fun login(@RequestBody request: LoginRequest) : ResponseEntity<Unit> {
        val ok = authService.login(request.username, request.password)
        if (ok) {
            return ResponseEntity(Unit, HttpStatus.OK)
        }
        return ResponseEntity(Unit, HttpStatus.FORBIDDEN)
    }

    @RequestMapping(value = ["/auth/register"], method = [(RequestMethod.POST)])
    fun register(@RequestBody request: LoginRequest) : ResponseEntity<Unit> {
        authService.register(request.username, request.password)
        return ResponseEntity(Unit, HttpStatus.CREATED)
    }
}