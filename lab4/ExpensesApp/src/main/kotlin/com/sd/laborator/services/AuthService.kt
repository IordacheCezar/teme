package com.sd.laborator.services

import com.sd.laborator.interfaces.IAuthService
import com.sd.laborator.interfaces.IUserService
import com.sd.laborator.pojo.User
import com.sd.laborator.services.UserService
import org.springframework.stereotype.Service

@Service
class AuthService (
    private var userService: IUserService
) : IAuthService {
    override fun login(username: String, password: String) : Boolean {
        val user = userService.searchByUsername(username) ?: return false
        return user.password == password
    }

    override fun register(username: String, password: String) {
        if (userService.searchByUsername(username) != null) {
            throw IllegalArgumentException("Username already exists")
        }
        userService.createUser(User(0, username, password))
    }
}