package com.example.dlxtest

import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.support.AmqpMessageHeaderAccessor
import org.springframework.amqp.support.SimpleAmqpHeaderMapper
import org.springframework.boot.autoconfigure.amqp.RabbitProperties
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.messaging.support.MessageBuilder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct

@RestController
@EnableBinding(TopicOutput::class)
class TestController(
    private val topicOutput: TopicOutput
) {
    @PostMapping("/test")
    fun test(@RequestBody body: Map<String, String>) {
        val payload = body["payload"] ?: ""
        val delay = (body["delay"] ?: "0")
        val target = body["target"] ?: "1"
        val headerMapper = SimpleAmqpHeaderMapper()

        val msg = MessageBuilder.withPayload(payload)
            .setHeader("amqp_expiration", delay)
            .build()

        if (target == "1") {
            topicOutput.dlxTestOutput().send(msg)
        } else {
            topicOutput.dlxTestOutput2().send(msg)
        }
    }

    @PostConstruct
    fun init() {
        val headerMapper = SimpleAmqpHeaderMapper()
        val prop = MessageProperties()
        prop.setExpiration("expire1111")
        prop.setType("type1234")
        val mappedHeaders = headerMapper.toHeaders(prop)
        println(mappedHeaders.toString())

        val msg = MessageBuilder.withPayload("aa").copyHeaders(mappedHeaders).build()
        println(msg.headers)
        val headers = AmqpMessageHeaderAccessor.wrap(msg)
        headers.expiration
        headers.type
    }
}