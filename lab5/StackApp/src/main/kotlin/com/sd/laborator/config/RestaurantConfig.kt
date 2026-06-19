package com.sd.laborator.config

import com.sd.laborator.models.Bucatar
import com.sd.laborator.models.Chelner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RestaurantConfig {

    @Bean
    open fun chelneri(): MutableList<Chelner> {
        return mutableListOf(
            Chelner("Chelner72XX45E", mutableListOf()),
            Chelner("Chelner31AB12C", mutableListOf()),
            Chelner("Chelner99ZZ88K", mutableListOf())
        )
    }

    @Bean
    open fun bucatari(): MutableList<Bucatar> {
        return mutableListOf(
            Bucatar("Bucatar37x56AF", mutableListOf()),
            Bucatar("Bucatar11QQ22W", mutableListOf())
        )
    }
}