package com.sd.laborator.interfaces

interface IAuthService {
    fun login(username: String, password: String) : Boolean
    fun register(username: String, password: String)
}