package com.example.ugd.room

import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    suspend fun addNote(note: Note)
    @Update
    suspend fun updateNote(note: Note)
    @Delete
    suspend fun deleteNote(note: Note)
    @Query("SELECT * FROM note")
    suspend fun getNotes() : List<Note>
    @Query("SELECT * FROM note WHERE id =:note_id")
    suspend fun getNote(note_id: Int) : List<Note>
}