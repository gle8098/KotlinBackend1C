package blog.model

import kotlinx.serialization.Serializable

@Serializable
data class Publication(
    val id: String,
    val text: String,
    val timeCreated: String,
    val timeEdited: String,
    val authorId: String
)