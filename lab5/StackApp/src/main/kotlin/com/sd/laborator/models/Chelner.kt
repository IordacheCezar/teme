package com.sd.laborator.models

class Chelner (
    var id: String,
    var comenziPrimite: MutableList<Comanda>
) {
    fun poatePrimiComanda() : Boolean {
        return (comenziPrimite.size < 5)
    }

    fun primesteComanda(comanda: Comanda) {
        if (poatePrimiComanda())
            comenziPrimite.add(comanda)
    }

    fun livreazaComanda(comanda: Comanda) {
        comenziPrimite.remove(comanda)
    }
}