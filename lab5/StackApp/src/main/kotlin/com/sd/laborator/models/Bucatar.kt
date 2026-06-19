package com.sd.laborator.models

import com.sd.laborator.interfaces.IProcesareComanda
import kotlin.math.min

class Bucatar (
    var id: String,
    var comenziPrimite: MutableList<Comanda>,
    var lucreazaLaComanda: Boolean = false
) {
    lateinit var procesareComandaService: IProcesareComanda
    fun poatePrimiComanda() : Boolean {
        return (comenziPrimite.size < 5)
    }

    fun primesteComanda(comanda: Comanda){
        if (poatePrimiComanda())
            comenziPrimite.add(comanda)
    }

    fun preparaComanda() {
        while (true) {
            if (comenziPrimite.isNotEmpty() == true && lucreazaLaComanda == false) {
                lucreazaLaComanda = true;
                var comanda = comenziPrimite[0]
                var minute = comanda.preparationTime
                while (minute > 0) {
                    minute -= 1
                    Thread.sleep(1000)
                }
                lucreazaLaComanda = false
                comenziPrimite.remove(comanda)
                println("$id a terminat comanda ${comanda.id}")

                procesareComandaService.finalizeazaComanda(comanda)
            }
            else
            {
                Thread.sleep(1000)
            }
        }
    }

    fun calculeazaTimpTotal(): Int {
        var suma = 0
        for (comanda in comenziPrimite) {
            suma += comanda.preparationTime
        }
        return suma
    }
}