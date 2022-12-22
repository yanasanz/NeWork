package com.example.nework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nework.dao.*
import com.example.nework.entity.*

@Database(
    entities = [PostEntity::class,
        EventEntity::class,
        EventRemoteKeyEntity::class,
        PostRemoteKeyEntity::class,
        JobEntity::class,
        UserEntity::class], version = 4
)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
    abstract fun eventDao(): EventDao
    abstract fun eventRemoteKeyDao(): EventRemoteKeyDao
    abstract fun jobDao(): JobDao
    abstract fun userDao(): UserDao
}