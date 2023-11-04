package blog

import blog.repository.IPublicationsRepository
import blog.repository.impl.MemoryPublicationsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val publicationsModule = module {
    singleOf(::MemoryPublicationsRepository) bind IPublicationsRepository::class
}
