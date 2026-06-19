package com.sd.laborator.components

//import RabbitMqConnectionFactoryComponent
import com.sd.laborator.interfaces.IProcesareComanda
import com.sd.laborator.models.Comanda
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class RestaurantComponent (
    private val procesareComandaService: IProcesareComanda,
    private val messageSender: LivrareComandaMesaj
) {
    @RabbitListener(queues = ["\${restaurantapp.rabbitmq.queue}"])
    fun recieveMessage(msg: String) {
        try {
            println("Mesaj primit: $msg")

            val response = comandare(msg)

            messageSender.sendMessage(response)
        } catch (ex: Exception) {
            messageSender.sendMessage("error-${ex.message}")
        }
    }

    fun comandare(msg: String): String {
        val parts = msg.split("-")

        if (parts.size != 2 || parts[0] != "order") {
            throw IllegalArgumentException("Mesaj invalid. Format asteptat: order-tipMeniu")
        }

        val menuType = parts[1].toInt()

        if (menuType !in 1..5) {
            throw IllegalArgumentException("Tipul meniului trebuie sa fie intre 1 si 5.")
        }

        val comanda = Comanda(
            id = Random.nextInt(0, 100),
            menuType = menuType,
            preparationTime = getPreparationTime(menuType),
            chelnerId = "",
            bucatarId = ""
        )

        procesareComandaService.inregistreazaComanda(comanda)

        return "accepted-${comanda.id}-${comanda.menuType}-${comanda.preparationTime}"
    }

    private fun getPreparationTime(tipMeniu: Int): Int {
        return when (tipMeniu) {
            1 -> 2
            2 -> 4
            3 -> 6
            4 -> 8
            5 -> 10
            else -> throw IllegalArgumentException("Tip meniu invalid.")
        }
    }
}