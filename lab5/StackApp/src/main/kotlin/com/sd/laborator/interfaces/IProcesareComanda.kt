package com.sd.laborator.interfaces

import com.sd.laborator.models.Bucatar
import com.sd.laborator.models.Chelner
import com.sd.laborator.models.Comanda

interface IProcesareComanda {
    fun inregistreazaComanda(comanda: Comanda)
    fun finalizeazaComanda(comanda: Comanda)
    fun alegeChelner() : Chelner
    fun alegeBucatar() : Bucatar
}