package com.sd.laborator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
@SpringBootApplication
open class ExpensesApplication
fun main(args: Array<String>) {
    runApplication<ExpensesApplication>(*args)
}