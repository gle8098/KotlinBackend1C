package blog.repository

import blog.model.Publication
import blog.repository.exceptions.BadParamsException
import blog.repository.exceptions.BadTextException

interface IPublicationsRepository {
    @Throws(BadTextException::class)
    fun create(userId: String, text: String): Publication

    fun getById(id: String): Publication?

    fun getAllForPage(pageNo: Int): Collection<Publication>

    @Throws(BadTextException::class, BadParamsException::class)
    fun editById(id: String, text: String, expectedAuthorId: String): Publication?

    fun deleteById(id: String): Boolean
}