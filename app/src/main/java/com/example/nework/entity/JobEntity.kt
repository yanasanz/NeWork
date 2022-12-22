package com.example.nework.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nework.dto.Job

@Entity
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val position: String,
    val start: String,
    val finish: String?,
    val link: String?,
    val ownedByMe: Boolean
) {
    fun toDto() = Job(id, name, position, start, finish, link, ownedByMe)

    companion object {
        fun fromDto(dto: Job) =
            JobEntity(dto.id, dto.name, dto.position, dto.start, dto.finish, dto.link, dto.ownedByMe)
    }
}

fun List<Job>.toEntity(): List<JobEntity> = map(JobEntity::fromDto)
fun List<JobEntity>.toDto(): List<Job> = map(JobEntity::toDto)