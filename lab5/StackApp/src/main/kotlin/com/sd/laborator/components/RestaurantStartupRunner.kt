package com.sd.laborator.components

import com.sd.laborator.interfaces.IProcesareComanda
import com.sd.laborator.models.Bucatar
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class RestaurantStartupRunner(
    private val bucatari: MutableList<Bucatar>,
    private val procesareComandaService: IProcesareComanda
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        for (bucatar in bucatari) {
            bucatar.procesareComandaService = procesareComandaService
            Thread {
                bucatar.preparaComanda()
            }.start()
        }
    }
}