package com.example.connect.data.repository

import com.example.connect.data.local.Tags
import com.example.connect.data.local.TagsDao
import kotlinx.coroutines.flow.Flow

class TagsRepositoryImpl(private val tagsDao: TagsDao) : TagsRepository{
    override suspend fun insert(tag: Tags) = tagsDao.insert(tag)

    override suspend fun insert(tag: List<Tags>) = tagsDao.insert(tag)

    override suspend fun update(tag: Tags) = tagsDao.update(tag)

    override suspend fun delete(tag: Tags) = tagsDao.delete(tag)

    override fun getAllTags(): Flow<List<Tags>> = tagsDao.getAllTags()

    override fun getTag(id: Int): Flow<Tags> = tagsDao.getTag(id)
}