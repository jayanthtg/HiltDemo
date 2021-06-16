package com.kiran.data.db

import com.kiran.data.IMapper
import com.kiran.domain.entities.Blog
import javax.inject.Inject

class BlogDBMapper
    @Inject
    constructor()
    : IMapper<BlogDBEntity, Blog> {

    override fun mapFromEntity(entity: BlogDBEntity): Blog {
        return Blog(
            id = entity.id,
            title = entity.title,
            body = entity.body,
            image = entity.image,
            category = entity.category
        )
    }

    override fun mapToEntity(domainModel: Blog): BlogDBEntity {
        return BlogDBEntity(
            id = domainModel.id,
            title = domainModel.title,
            body = domainModel.body,
            image = domainModel.image,
            category = domainModel.category
        )
    }

    fun mapFromEntityList(entities: List<BlogDBEntity>): List<Blog>{
        return entities.map { mapFromEntity(it) }
    }
}