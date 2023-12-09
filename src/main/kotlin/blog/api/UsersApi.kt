package blog.api

import blog.JwtConfig
import blog.model.ApiError
import blog.repository.IUsersRepository
import blog.repository.exceptions.BadParamsException
import blog.repository.exceptions.BadTextException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.usersApi() {
    routing {
        val usersRepository by inject<IUsersRepository>()

        route("/users") {
            post("/register") {
                val params = call.receiveParameters()
                if (!params.contains("nickname") || !params.contains("fullname") || !params.contains("password")) {
                    call.respond(HttpStatusCode.BadRequest, "No params")
                    return@post
                }

                val nickname = params["nickname"]!!
                val fullname = params["fullname"]!!
                val password = params["password"]!!

                try {
                    val user = usersRepository.register(nickname, fullname, password)
                    call.respond(JwtConfig.makeToken(user))
                } catch (ex: BadParamsException) {
                    call.respond(ApiError(ex))
                }
            }

            post("/login") {
                val params = call.receiveParameters()
                if (!params.contains("nickname") || !params.contains("password")) {
                    call.respond(HttpStatusCode.BadRequest, "No params")
                    return@post
                }

                val nickname = params["nickname"]!!
                val password = params["password"]!!

                val user = usersRepository.findUser(nickname) ?: run {
                    call.respond(HttpStatusCode.Forbidden, "No user found")
                    return@post
                }

                if (user.password != password) {
                    call.respond(HttpStatusCode.Forbidden, "Incorrect password")
                    return@post
                }

                call.respond(JwtConfig.makeToken(user))
            }
        }
    }
}