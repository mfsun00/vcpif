package com.example.vcpif.service

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.SendTo


@KafkaListener(topics=["test"])
@SendTo
fun consume(message:String): String {
    println("Server received $message")
    return message.toUpperCase()
}