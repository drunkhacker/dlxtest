package com.example.dlxtest

import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.annotation.EnableBinding
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.Output
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.SubscribableChannel
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.support.GenericMessage
import org.springframework.stereotype.Service

@Service
@EnableBinding(TopicMessageSink::class)
class TopicSubscription {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @StreamListener(TopicMessageSink.DLX_TEST_INPUT)
    fun message(message: GenericMessage<ByteArray>) {
        logger.info("[DLX_TEST_INPUT] payload=${String(message.payload)}, headers=${message.headers}")
    }

    @StreamListener(TopicMessageSink.DLX_TEST_INPUT2)
    fun message2(message: GenericMessage<ByteArray>) {
        logger.info("[DLX_TEST_INPUT2] payload=${String(message.payload)}, headers=${message.headers}")
    }
}

interface TopicMessageSink {
    companion object {
        const val DLX_TEST_INPUT = "dlxTestInput"
        const val DLX_TEST_INPUT2 = "dlxTestInput2"
    }

    @Input(DLX_TEST_INPUT)
    fun dlxTestInput(): SubscribableChannel

    @Input(DLX_TEST_INPUT2)
    fun dlxTestInput2(): SubscribableChannel
}

interface TopicOutput {
    companion object {
        const val DLX_TEST_OUTPUT = "dlxTestOutput"
        const val DLX_TEST_OUTPUT2 = "dlxTestOutput2"
    }

    @Output(DLX_TEST_OUTPUT)
    fun dlxTestOutput(): MessageChannel

    @Output(DLX_TEST_OUTPUT2)
    fun dlxTestOutput2(): MessageChannel
}
