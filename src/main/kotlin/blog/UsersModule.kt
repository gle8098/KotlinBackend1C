package blog

import blog.repository.IUsersRepository
import blog.repository.impl.MemoryUsersRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val usersModule = module {
    singleOf(::MemoryUsersRepository) bind IUsersRepository::class
}