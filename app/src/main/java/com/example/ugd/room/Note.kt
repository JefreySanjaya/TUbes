package com.example.ugd.room

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val password: String,
    val email: String,
    val dateob: Int,
    val phone: Int
)