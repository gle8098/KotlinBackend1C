package blog.repository.impl

import blog.model.User
import blog.repository.IUsersRepository
import blog.repository.exceptions.BadParamsException
import java.time.Instant

class MemoryUsersRepository : IUsersRepository {
    private val users: MutableMap<String, User> = mutableMapOf()

    override fun register(nickname: String, fullName: String, password: String): User {
        if (nickname.length > 16) {
            throw BadParamsException(BadParamsException.Code.NAME_TOO_LONG)
        }

        if (password.length < 4) {
            throw BadParamsException(BadParamsException.Code.WEAK_PASSWORD)
        }

        synchronized(users) {
            if (users.contains(nickname)) {
                throw BadParamsException(BadParamsException.Code.NAME_OCCUPIED)
            }

            val user = User(nickname, fullName, password, Instant.now().toString())
            users.put(nickname, user)
            return user
        }
    }

    override fun findUser(nickname: String): User? {
        synchronized(users) {
            return users[nickname]
        }
    }
}