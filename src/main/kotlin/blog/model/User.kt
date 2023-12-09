package blog.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val nickname: String,
    val fullname: String,
    val password: String,
    val timeCreated: String
)