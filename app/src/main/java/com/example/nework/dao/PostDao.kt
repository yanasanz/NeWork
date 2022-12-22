package com.example.nework.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.nework.entity.PostEntity

@Dao
interface PostDao {
    @Query("SELECT * FROM PostEntity ORDER BY id DESC")
    fun getAllPosts(): PagingSource<Int, PostEntity>

    @Query("SELECT COUNT(*) == 0 FROM PostEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(posts: List<PostEntity>)

    @Query("DELETE FROM PostEntity WHERE id = :id")
    suspend fun removeById(id: Int)

    @Query("SELECT COUNT() FROM PostEntity")
    suspend fun getSize(): Int

    @Query("DELETE FROM PostEntity")
    suspend fun removeAllPosts()
}