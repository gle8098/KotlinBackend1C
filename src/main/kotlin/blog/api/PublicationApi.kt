package blog.api

import blog.model.ApiError
import blog.repository.BadTextException
import blog.repository.IPublicationsRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.publicationApi() {
    routing {

        val publicationsRepository by inject<IPublicationsRepository>()

        route("/publication") {
            put("/create") {
                try {
                    val pub = publicationsRepository.create(call.receiveText())
                    call.respond(pub.id)
                } catch (ex: BadTextException) {
                    call.respond(ApiError(ex.toString()))
                }
            }

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

            patch("/edit/{id}") {
                val id = call.parameters["id"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
                    return@patch
                }

                val text = call.receiveText()


                try {
                    publicationsRepository.editById(id, text) ?: run {
                        call.respond(HttpStatusCode.NotFound)
                        return@patch
                    }
                    call.respond(HttpStatusCode.OK)
                } catch (ex: BadTextException) {
                    call.respond(ApiError(ex.toString()))
                }
            }

            delete("/delete/{id}") {
                val id = call.parameters["id"] ?: run {
                    call.respond(HttpStatusCode.BadRequest)
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
