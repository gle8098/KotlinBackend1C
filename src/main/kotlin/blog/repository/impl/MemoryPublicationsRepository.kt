package blog.repository.impl

import blog.model.Publication
import blog.repository.BadTextException
import blog.repository.IPublicationsRepository
import java.time.Instant
import java.util.*

class MemoryPublicationsRepository : IPublicationsRepository {
    private val publications: MutableMap<String, Publication> = mutableMapOf()

    private fun ensureTextCorrect(text: String) {
        if (text.length > 500) {
            throw BadTextException(BadTextException.Code.TEXT_TOO_LONG)
        }
    }

    override fun create(text: String): Publication {
        ensureTextCorrect(text)

        val id = UUID.randomUUID().toString()
        val now = Instant.now().toString()
        val pub = Publication(id, text, now, now)

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

    override fun editById(id: String, text: String): Publication? {
        ensureTextCorrect(text)

        synchronized(publications) {
            val oldPub = publications[id] ?: return null
            val now = Instant.now().toString()
            val pub = Publication(id, text, oldPub.timeCreated, now)
            publications[id] = pub
        }

        return publications[id]
    }

    companion object {
        const val ENTRIES_IN_PAGE = 2
    }
}