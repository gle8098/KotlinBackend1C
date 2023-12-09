package blog.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val description: String,
    /* FIXME Hack: insert field "error = true" in resulting json */
    val error: Boolean = true
) {
    constructor(ex: Exception) : this(description = ex.toString()) {}
}