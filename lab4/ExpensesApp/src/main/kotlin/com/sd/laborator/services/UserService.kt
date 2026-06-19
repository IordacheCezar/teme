package com.sd.laborator.services

import com.sd.laborator.interfaces.IUserService
import com.sd.laborator.pojo.User
import org.springframework.stereotype.Service

@Service
class UserService : IUserService {

    private val users = mutableListOf<User>()
    private var next_id: Int= 0;

    override fun createUser(user: User) {
        user.id = next_id++
        users.add(user)
    }

    override fun deleteUser(id: Int) {
        val user = users.find { it.id == id }
            ?: throw NoSuchElementException("User with id $id not found")

        users.remove(user)
    }

    override fun getUser(id: Int): User {
        return users.find { it.id == id }
            ?: throw NoSuchElementException("User with id $id not found")
    }

    override fun updateUser(id: Int, user: User) {
        val index = users.indexOfFirst { it.id == id }

        if (index == -1) {
            throw NoSuchElementException("User with id $id not found")
        }

        users[index] = user
    }

    override fun searchByUsername(username: String): User? {
        return users.find { it.username == username }
    }
}