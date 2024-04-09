package com.example.demo

import java.util.UUID
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}

@RestController
class MessageController(val service: MessageService) {
    @GetMapping("/") fun index(): List<Message> = service.findMessages()

    @PostMapping("/")
    fun post(@RequestBody message: Message) {
        service.save(message)
    }
}

data class Message(val id: String?, val text: String)

@Service
class MessageService(val db: JdbcTemplate) {
    fun findMessages(): List<Message> =
            db.query("select * from messages") { response, _ ->
                Message(response.getString("id"), response.getString("text"))
            }

    fun save(message: Message) {
        val id = message.id ?: UUID.randomUUID().toString()
        db.update("insert into messages values ( ?, ? )", id, message.text)
    }
}
