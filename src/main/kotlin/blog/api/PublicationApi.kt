package blog.api

import blog.model.ApiError
import blog.model.UserIdPrincipal
import blog.repository.exceptions.BadTextException
import blog.repository.IPublicationsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.publicationApi() {
    routing {

        val publicationsRepository by inject<IPublicationsRepository>()

        route("/publication") {
            get("/get/{id}") {
                val id = call.parameters["id"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val pub = publicationsRepository.getById(id) ?: run {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(pub)
            }

            get("/getAllByPages/{pageNo?}") {
                val pageNo = call.parameters["pageNo"]?.toIntOrNull() ?: 0
                val pubs = publicationsRepository.getAllForPage(pageNo)
                call.respond(pubs)
            }

            authenticate("auth-jwt") {
                put("/create") {
                    try {
                        val userId = call.principal<UserIdPrincipal>()!!
                        val pub = publicationsRepository.create(userId.nickname, call.receiveText())
                        call.respond(pub.id)
                    } catch (ex: BadTextException) {
                        call.respond(ApiError(ex.toString()))
                    }
                }

                patch("/edit/{id}") {
                    val id = call.parameters["id"] ?: run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@patch
                    }

                    val userId = call.principal<UserIdPrincipal>()!!
                    val text = call.receiveText()

                    try {
                        publicationsRepository.editById(id, text, userId.nickname) ?: run {
                            call.respond(HttpStatusCode.NotFound)
                            return@patch
                        }
                        call.respond(HttpStatusCode.OK)
                    } catch (ex: Exception) {
                        call.respond(ApiError(ex))
                    }
                }

                delete("/delete/{id}") {
                    val id = call.parameters["id"] ?: run {
                        call.respond(HttpStatusCode.BadRequest)
                        return@delete
                    }

                    val userId = call.principal<UserIdPrincipal>()!!
                    if (publicationsRepository.getById(id)?.authorId != userId.nickname) {
                        call.respond(HttpStatusCode.Forbidden)
                        return@delete
                    }

                    if (publicationsRepository.deleteById(id)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }
                }
            }
        }

    }
}
