package blog

import blog.api.publicationApi
import blog.api.usersApi
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import org.koin.ktor.plugin.Koin


fun main() {
    embeddedServer(Netty, port = 8080) {
        configureServer()
    }.start(wait = true)
}

private fun Application.configureServer() {
    configurePlugins()
    publicationApi()
    usersApi()
}

private fun Application.configurePlugins() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
        })
    }

    install(Koin) {
        modules(publicationsModule)
        modules(usersModule)
    }

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(JwtConfig.verifier)
            realm = "blog"
            validate { return@validate JwtConfig.validate(it) }
        }
    }
}
