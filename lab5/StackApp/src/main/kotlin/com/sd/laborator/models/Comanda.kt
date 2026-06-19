package com.sd.laborator.models

data class Comanda (
    var id: Int,
    var menuType: Int,
    var preparationTime: Int,
    var chelnerId: String,
    var bucatarId: String
)