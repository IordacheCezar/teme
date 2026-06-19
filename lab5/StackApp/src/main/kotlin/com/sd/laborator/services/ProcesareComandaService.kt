package com.sd.laborator.services

import com.sd.laborator.components.LivrareComandaMesaj
import com.sd.laborator.interfaces.IProcesareComanda
import com.sd.laborator.models.Bucatar
import com.sd.laborator.models.Chelner
import com.sd.laborator.models.Comanda
import org.springframework.stereotype.Service

@Service
class ProcesareComandaService (
    var chelneri: MutableList<Chelner>,
    var bucatari: MutableList<Bucatar>,
    private val messageSender: LivrareComandaMesaj
    ) : IProcesareComanda {
    override fun alegeChelner() : Chelner{
        val chelnerDisponibili = chelneri.filter { it.poatePrimiComanda() }

        if (chelnerDisponibili.isEmpty()) {
            throw IllegalStateException("Nu exista niciun chelner disponibil.")
        }

        var chelnerAles = chelnerDisponibili[0]
        for (chelner in chelneri)
            if (chelner.comenziPrimite.size < chelnerAles.comenziPrimite.size)
                chelnerAles = chelner
        return chelnerAles
    }

    override fun alegeBucatar(): Bucatar {
        val bucatariDisponibili = bucatari.filter { it.poatePrimiComanda() }

        if (bucatariDisponibili.isEmpty()) {
            throw IllegalStateException("Nu exista niciun bucatar disponibil.")
        }

        var bucatarAles = bucatariDisponibili[0]
        var timpMinim = bucatarAles.calculeazaTimpTotal()

        for (bucatar in bucatariDisponibili) {
            val timpCurent = bucatar.calculeazaTimpTotal()

            if (timpCurent < timpMinim) {
                timpMinim = timpCurent
                bucatarAles = bucatar
            }
        }

        return bucatarAles
    }

    override fun inregistreazaComanda(comanda: Comanda) {
        var chelner = alegeChelner()
        chelner.primesteComanda(comanda)
        comanda.chelnerId = chelner.id

        var bucatar = alegeBucatar()
        bucatar.primesteComanda(comanda)
        comanda.bucatarId = bucatar.id
    }

    override fun finalizeazaComanda(comanda: Comanda) {
        val chelner = chelneri.find { it.id == comanda.chelnerId }
            ?: throw IllegalStateException("Chelnerul a plecat acasa.")

        chelner.livreazaComanda(comanda)

        messageSender.sendMessage("done-s-a livrat-${comanda.id}-${comanda.menuType}")
    }
}