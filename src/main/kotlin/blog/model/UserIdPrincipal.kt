package blog.model

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserIdPrincipal(
    val nickname: String
): Principal
