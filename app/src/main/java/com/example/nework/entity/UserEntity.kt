package com.example.nework.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nework.dto.UserResponse

@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val login: String = "",
    val name: String = "",
    val avatar: String? = "",
    var isChecked: Boolean = false
) {

    fun toDto() = UserResponse(id, login, name, avatar, isChecked)

    companion object {
        fun fromDto(dto: UserResponse) =
            UserEntity(dto.id, dto.login, dto.name, dto.avatar, dto.isChecked)
    }
}

fun List<UserEntity>.toDto(): List<UserResponse> = map(UserEntity::toDto)
fun List<UserResponse>.toEntity(): List<UserEntity> = map(UserEntity::fromDto)