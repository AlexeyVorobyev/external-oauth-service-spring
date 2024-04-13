package org.lexxv.externaloauthservicespring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ExternalOauthServiceSpringApplication

fun main(args: Array<String>) {
	runApplication<ExternalOauthServiceSpringApplication>(*args)
}
