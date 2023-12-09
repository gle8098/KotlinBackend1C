package blog.repository.impl

import blog.model.Publication
import blog.repository.exceptions.BadTextException
import blog.repository.IPublicationsRepository
import blog.repository.exceptions.BadParamsException
import java.time.Instant
import java.util.*

class MemoryPublicationsRepository : IPublicationsRepository {
    private val publications: MutableMap<String, Publication> = mutableMapOf()

    private fun ensureTextCorrect(text: String) {
        if (text.length > 500) {
            throw BadTextException(BadTextException.Code.TEXT_TOO_LONG)
        }
    }

    override fun create(userId: String, text: String): Publication {
        ensureTextCorrect(text)

        val id = UUID.randomUUID().toString()
        val now = Instant.now().toString()
        val pub = Publication(id, text, now, now, userId)

        synchronized(publications) {
            publications[id] = pub
        }

        return pub
    }

    override fun deleteById(id: String): Boolean {
        synchronized(publications) {
            return publications.remove(id) != null
        }
    }

    override fun getById(id: String): Publication? {
        synchronized(publications) {
            return publications[id]
        }
    }

    override fun getAllForPage(pageNo: Int): Collection<Publication> {
        val startEntry = pageNo * ENTRIES_IN_PAGE

        synchronized(publications) {
            return publications.iterator()
                .asSequence()
                .drop(startEntry)
                .take(ENTRIES_IN_PAGE)
                .map { elem -> elem.value }
                .toList()
        }
    }

    override fun editById(id: String, text: String, expectedAuthorId: String): Publication? {
        ensureTextCorrect(text)

        synchronized(publications) {
            val oldPub = publications[id] ?: return null

            if (oldPub.authorId != expectedAuthorId) {
                throw BadParamsException(BadParamsException.Code.WRONG_USER)
            }

            val now = Instant.now().toString()
            val pub = Publication(id, text, oldPub.timeCreated, now, oldPub.authorId)
            publications[id] = pub
        }

        return publications[id]
    }

    companion object {
        const val ENTRIES_IN_PAGE = 2
    }
}