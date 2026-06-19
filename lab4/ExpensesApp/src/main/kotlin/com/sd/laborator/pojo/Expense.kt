package com.sd.laborator.pojo

data class Expense(
    var id: Int = 0,
    var amount: Int = 0,
    var category: String = "",
    var description: String = "",
    var userId: Int = 0
)