package com.example.vcpif.controller

import com.example.vcpif.service.KafkaService
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class DemoController {
    @Autowired
    private lateinit var kafkaService: KafkaService

    val objectMapper = ObjectMapper()

    @PostMapping("/test")
    fun test(@RequestBody param: TestVo):ResponseEntity<Mono<String>> {
        val res = Mono.just(objectMapper.writeValueAsString(param))
            .flatMap {request ->
                val record = ProducerRecord("kRequests", request,request)
                kafkaService.sendAndReceive(record)
            }
        return ResponseEntity.status(HttpStatus.OK).body(res)
    }
}