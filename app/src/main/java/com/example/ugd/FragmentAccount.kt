package com.example.ugd

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.ugd.room.NoteDB
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentAccount: Fragment() {

    val db by lazy { activity?.let { NoteDB(it) } }
    private val myPreference = "login"
    private val id = "idKey"
    var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = activity?.getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        val showUsername: TextView = view.findViewById(R.id.etSessionUsername)
        val showEmail: TextView = view.findViewById(R.id.etSessionEmail)
        val showDate: TextView = view.findViewById(R.id.etSessionDateBirth)
        val showPhone: TextView = view.findViewById(R.id.etSessionPhone)
        val btnTambah : Button = view.findViewById(R.id.btnTambah)
        val btnLokasi : Button = view.findViewById(R.id.btnLokasi)
        val btnCamera : Button = view.findViewById(R.id.btnCamera)

        CoroutineScope(Dispatchers.IO).launch {
            val user = db?.userDao()?.getUser(sharedPreferences!!.getString("id","")!!.toInt())?.get(0)
            showUsername.setText(user?.username)
            showEmail.setText(user?.email)
            showDate.setText(user?.dateBirth)
            showPhone.setText(user?.phoneNumber)
        }

        btnTambah.setOnClickListener {
            val intent = Intent(activity, Activity_Tambah::class.java)
            startActivity(intent)
        }

        btnCamera.setOnClickListener {
            val intent = Intent(activity, CameraActivity::class.java)
            startActivity(intent)
        }

        btnLokasi.setOnClickListener{
            val intent = Intent(activity,LokasiView::class.java)
            startActivity(intent)
        }
    }
}