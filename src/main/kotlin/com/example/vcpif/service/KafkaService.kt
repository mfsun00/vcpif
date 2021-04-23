package com.example.vcpif.service

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@Service
class KafkaService {

    @Autowired
    private lateinit var replyingKafkaTemplate:ReplyingKafkaTemplate<String, String, String>

    fun sendAndReceive(record:ProducerRecord<String,String>): Mono<String> {
        val replyFuture = replyingKafkaTemplate.sendAndReceive(record)
        val sendResult = replyFuture.sendFuture.get(10, TimeUnit.SECONDS)
        println("Sent ok: ${sendResult.recordMetadata}")
        val consumerRecord = replyFuture.get(10, TimeUnit.SECONDS)
        val value = consumerRecord.value()
        println("Return value: $value")
        return Mono.just(value)
    }

}
