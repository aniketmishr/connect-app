package com.example.connect.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag:Tags)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: List<Tags>)
    @Update
    suspend fun update(tag: Tags)
    @Delete
    suspend fun delete(tag: Tags)
    @Query("select * from tags")
    fun getAllTags(): Flow<List<Tags>>
    @Query("select * from tags where id = :id")
    fun getTag(id:Int): Flow<Tags>

}