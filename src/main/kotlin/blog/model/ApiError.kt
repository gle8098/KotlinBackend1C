package blog.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(val description: String, val error: Boolean = true)