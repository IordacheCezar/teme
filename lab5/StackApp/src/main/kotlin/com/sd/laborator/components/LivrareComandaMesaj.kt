package com.sd.laborator.components

import org.springframework.amqp.core.AmqpTemplate
import org.springframework.stereotype.Component

@Component
class LivrareComandaMesaj(
    private val connectionFactory: RabbitMqConnectionFactoryComponent,
    private val amqpTemplate: AmqpTemplate
) {
    fun sendMessage(msg: String) {
        println("message:")
        println(msg)

        amqpTemplate.convertAndSend(
            connectionFactory.getExchange(),
            connectionFactory.getRoutingKey(),
            msg
        )
    }
}