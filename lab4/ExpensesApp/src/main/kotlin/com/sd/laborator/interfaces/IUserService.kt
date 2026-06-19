package com.sd.laborator.interfaces

import com.sd.laborator.pojo.User

interface IUserService {
    fun createUser(user: User)
    fun deleteUser(id: Int)
    fun getUser(id: Int): User
    fun updateUser(id: Int, user: User)
    fun searchByUsername(username: String): User?
}