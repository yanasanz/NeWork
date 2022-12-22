package com.example.nework.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.nework.entity.EventEntity

@Dao
interface EventDao {
    @Query("SELECT * FROM EventEntity ORDER BY id DESC")
    fun getAllEvents(): PagingSource<Int, EventEntity>

    @Query("SELECT COUNT(*) == 0 FROM EventEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(events: List<EventEntity>)

    @Query("DELETE FROM EventEntity WHERE id = :id")
    suspend fun removeEventById(id: Int)

    @Query("SELECT COUNT() FROM EventEntity")
    suspend fun getSize(): Int

    @Query("DELETE FROM EventEntity")
    suspend fun removeAllEvents()
}