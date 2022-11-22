package com.example.ugd

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd.PesananAdapter.pesananAdapter
import com.example.ugd.api.PesananApi
import com.example.ugd.models.Pesanan
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.Exception
import java.nio.charset.StandardCharsets
import kotlin.jvm.Throws

class AddEditPesananActivity : AppCompatActivity() {
    companion object{
        private val PESANAN_LIST = arrayOf("HairCut", "Creambath", "Hair Style", "Hair Model")
    }

    private var etNama: EditText? = null
    private var etRincian: EditText? = null
    private var edPesanan: AutoCompleteTextView? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pesanan)

        //pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        etRincian = findViewById(R.id.et_rincian)
        edPesanan = findViewById(R.id.ed_jenisPesanan)

        setExposedDropDownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if (id == -1L){
            tvTitle.setText("Tambah Pesanan")
            btnSave.setOnClickListener { createPesanan() }
        } else {
            tvTitle.setText("Edit Pesanan")
            getPesananById(id)

            btnSave.setOnClickListener { updatePesanan(id) }
        }
    }

    fun setExposedDropDownMenu(){
        val adapterPesanan: ArrayAdapter<String> = ArrayAdapter<String>(this,
            R.layout.item_list, PESANAN_LIST)
        edPesanan!!.setAdapter(adapterPesanan)
    }

    private fun getPesananById(id: Long){

        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, PesananApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val pesanan = gson.fromJson(response, Pesanan::class.java)

                etNama!!.setText(pesanan.nama)
                etRincian!!.setText(pesanan.rincian)
                edPesanan!!.setText(pesanan.jenisPesanan)
                setExposedDropDownMenu()

                Toast.makeText(this@AddEditPesananActivity, "Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val error = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditPesananActivity,
                        error.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@AddEditPesananActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap <String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    private fun createPesanan(){

        val pesanan = Pesanan(
            etNama!!.text.toString(),
            etRincian!!.text.toString(),
            edPesanan!!.text.toString(),
        )
        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, PesananApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()
                val pesanan = gson.fromJson(response, Pesanan::class.java)

                if (pesanan != null)
                    Toast.makeText(this@AddEditPesananActivity, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val error = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditPesananActivity,
                        error.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@AddEditPesananActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String> ()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(pesanan)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)

                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }
        //Menambahkan request ke request queue
        queue!!.add(stringRequest)
    }

    private fun updatePesanan(id: Long){

        val pesanan = Pesanan(
            etNama!!.text.toString(),
            etRincian!!.text.toString(),
            edPesanan!!.text.toString(),
        )

        val stringRequest: StringRequest = object  :
            StringRequest(Method.PUT, PesananApi.UPDATE_URL + id, Response.Listener { response ->
                val gson = Gson()

                var pesanan = gson.fromJson(response, Pesanan::class.java)

                if (pesanan != null)
                    Toast.makeText(this@AddEditPesananActivity, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

            }, Response.ErrorListener { error ->
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val error = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditPesananActivity,
                        error.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@AddEditPesananActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(pesanan)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }

        }
        queue!!.add(stringRequest)
    }

}