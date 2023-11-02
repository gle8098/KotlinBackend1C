package blog.repository

import blog.model.Publication

interface IPublicationsRepository {
    @Throws(BadTextException::class)
    fun create(text: String): Publication

    fun getById(id: String): Publication?

    fun getAllForPage(pageNo: Int): Collection<Publication>

    @Throws(BadTextException::class)
    fun editById(id: String, text: String): Publication?

    fun deleteById(id: String): Boolean
}