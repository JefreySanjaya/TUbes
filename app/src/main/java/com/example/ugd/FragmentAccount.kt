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
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd.api.PesananApi
import com.example.ugd.models.User
//import com.example.ugd.room.NoteDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class FragmentAccount: Fragment() {

//    val db by lazy { activity?.let { NoteDB(it) } }
//    private val myPreference = "login"
//    private val id = "idKey"
//    var sharedPreferences: SharedPreferences? = null
    private var queue: RequestQueue? = null
    var SP: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queue = Volley.newRequestQueue(requireActivity())
//        sharedPreferences = activity?.getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        val btnTambah : Button = view.findViewById(R.id.btnTambah)
        val btnLokasi : Button = view.findViewById(R.id.btnLokasi)
        val btnCamera : Button = view.findViewById(R.id.btnCamera)
//        sharedPreferences = activity?.getSharedPreferences("id", Context.MODE_PRIVATE)
        SP = activity?.getSharedPreferences("UserId", Context.MODE_PRIVATE)
        val id = SP!!.getInt("id", -1)
        getUserById(id)

        btnTambah.setOnClickListener {
            val intent = Intent(activity, Activity_Tambah::class.java)
            startActivity(intent)
        }

        btnCamera.setOnClickListener {
            val intent = Intent(activity, PesananActivity::class.java)
            startActivity(intent)
        }
        btnLokasi.setOnClickListener{
            val intent = Intent(activity, LokasiView::class.java)
            activity?.startActivity(intent)
        }

    }

    private fun getUserById(id: Int) {

        val username: TextView =  requireView().findViewById(R.id.etSessionUsername)
        val email: TextView =  requireView().findViewById(R.id.etSessionEmail)
        val date: TextView =  requireView().findViewById(R.id.etSessionDateBirth)
        val phone: TextView =  requireView().findViewById(R.id.etSessionPhone)


        val stringRequest: StringRequest = object :
            StringRequest(Request.Method.GET, PesananApi.getUserID + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                var user = gson.fromJson(jsonObject.getJSONObject("data").toString(), User::class.java)
                username.setText(user.username)
                email.setText(user.email)
                date.setText(user.dateBirth)
                phone.setText(user.phoneNumber)

            },Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(requireActivity(), errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Toast.makeText(requireActivity(), e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

        }
        queue!!.add(stringRequest)
    }
}