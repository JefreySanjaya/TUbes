package com.example.ugd

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ugd.room.Constant
import com.example.ugd.room.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.ugd.room.NoteDB
import kotlinx.android.synthetic.main.activity_tambah.*


class Activity_Tambah : AppCompatActivity() {


    val db by lazy { NoteDB(this) }
    lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tambah)
        setupListener()
        setupRecyclerView()
    }


    private fun setupRecyclerView(){
        noteAdapter = NoteAdapter(arrayListOf(), object : NoteAdapter.OnAdapterListener{
            override fun onClick(note: Note) {
                Toast.makeText(applicationContext,note.username, Toast.LENGTH_SHORT).show()
                intentEdit(note.id, Constant.TYPE_READ)
            }

            override fun onUpdate(note: Note) {
                intentEdit(note.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(note: Note) {
                deleteDialog(note)
            }
        })
        list_item.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = noteAdapter
        }
    }

    private fun deleteDialog(note: Note){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From ${note.username}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialogInterface, i ->
                dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener{ dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote(note)
                    loadData()
                }
            })
        }
        alertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData(){
        CoroutineScope(Dispatchers.IO).launch{
            val notes = db.noteDao().getNotes()
            Log.d("MainActivity", "dbResponse: $notes")
            withContext(Dispatchers.Main){
                noteAdapter.setData(notes)
            }
        }
    }

    fun setupListener(){
        button_create.setOnClickListener {
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(noteId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", noteId)
                .putExtra("intent_type", intentType)
        )
    }


}