package blog.repository

import blog.model.User
import blog.repository.exceptions.BadParamsException

interface IUsersRepository {
    @Throws(BadParamsException::class)
    fun register(
        nickname: String,
        fullName: String,
        password: String
    ): User

    fun findUser(nickname: String): User?
}