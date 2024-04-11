package com.example.connect.data.repository

import com.example.connect.data.local.Tags
import kotlinx.coroutines.flow.Flow

interface TagsRepository {

    suspend fun insert(tag: Tags)
    suspend fun insert(tag: List<Tags>)
    suspend fun update(tag: Tags)
    suspend fun delete(tag: Tags)
    fun getAllTags(): Flow<List<Tags>>
    fun getTag(id:Int): Flow<Tags>
}