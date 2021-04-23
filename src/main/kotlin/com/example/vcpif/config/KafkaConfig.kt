package com.example.vcpif.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate
import org.apache.kafka.clients.admin.NewTopic

@Configuration
class KafkaConfig {
    @Bean
    fun replyingTemplate(
        pf: ProducerFactory<String, String>,
        repliesContainer: ConcurrentMessageListenerContainer<String, String>
    ): ReplyingKafkaTemplate<String, String, String> {
        return ReplyingKafkaTemplate(pf, repliesContainer)
    }

    @Bean
    fun repliesContainer(containerFactory: ConcurrentKafkaListenerContainerFactory<String, String>)
        : ConcurrentMessageListenerContainer<String, String> {
        return containerFactory.createContainer("replies").apply {
            containerProperties.groupId = "repliesGroup"
            isAutoStartup = false
        }
    }

    @Bean
    fun kRequests():NewTopic {
        return TopicBuilder.name("kRequests")
            .partitions(1)
            .replicas(1)
            .build()
    }

    @Bean
    fun kReplies(): NewTopic {
        return TopicBuilder.name("kReplies")
            .partitions(1)
            .replicas(1)
            .build()
    }

}