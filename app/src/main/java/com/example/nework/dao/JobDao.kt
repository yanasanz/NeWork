package com.example.nework.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nework.entity.JobEntity

@Dao
interface JobDao {
    @Query("SELECT * FROM JobEntity ORDER BY id DESC")
    fun getAllJobs(): LiveData<List<JobEntity>>

    @Query("SELECT COUNT(*) == 0 FROM JobEntity")
    suspend fun isEmpty(): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: JobEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(job: List<JobEntity>)

    @Query("DELETE FROM JobEntity WHERE id = :id")
    suspend fun removeJobById(id: Int)

    @Query("SELECT COUNT() FROM JobEntity")
    suspend fun getSize(): Int

    @Query("DELETE FROM JobEntity")
    suspend fun removeAllJobs()
}