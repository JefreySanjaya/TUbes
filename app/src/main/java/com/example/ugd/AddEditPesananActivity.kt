package com.example.ugd

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
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
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.jvm.Throws
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import com.example.ugd.databinding.ActivityAddPesananBinding

class AddEditPesananActivity : AppCompatActivity() {
    companion object{
        private val PESANAN_LIST = arrayOf("HairCut", "Creambath", "Hair Style", "Hair Model")
    }

    private var etNama: EditText? = null
    private var etRincian: EditText? = null
    private var edPesanan: AutoCompleteTextView? = null
    private var queue: RequestQueue? = null

    private var binding: ActivityAddPesananBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPesananBinding.inflate(layoutInflater)
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

        if(etNama!!.text.toString().isEmpty()){
            Toast.makeText(this@AddEditPesananActivity, "Nama Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show()
        }else if (etRincian!!.text.toString().isEmpty()){
            Toast.makeText(this@AddEditPesananActivity, "Rincian Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show()
        }else if (edPesanan!!.text.toString().isEmpty()){
            Toast.makeText(this@AddEditPesananActivity, "Pesanan Tidak Boleh Kosong!", Toast.LENGTH_SHORT).show()
        }else {
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

                    createPdf(pesanan.nama, pesanan.rincian, pesanan.jenisPesanan)
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

    //cetak pdf
    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Throws(
        FileNotFoundException::class
    )
    private fun createPdf(nama: String, rincian: String, jenisPesanan: String) {

        val pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            .toString()
        val file = File(pdfPath, "pdf_salonku.pdf")
        FileOutputStream(file)

        //inisaliasi pembuatan PDF
        val writer = PdfWriter(file)
        val pdfDocument = PdfDocument(writer)
        val document = Document(pdfDocument)
        pdfDocument.defaultPageSize = PageSize.A4
        document.setMargins(5f, 5f, 5f, 5f)
        @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.salonku)

        //penambahan gambar pada Gambar atas
        val bitmap = (d as BitmapDrawable?)!!.bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val bitmapData = stream.toByteArray()
        val imageData = ImageDataFactory.create(bitmapData)
        val image = Image(imageData)
        val namapengguna = Paragraph("Detail Pemesanan").setBold().setFontSize(24f)
            .setTextAlignment(TextAlignment.CENTER)
        val group = Paragraph(
            """
                       ..................
                       . Detail Pesanan .
                       ..................
                        """.trimIndent()
        ).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

        //proses pembuatan table
        val width = floatArrayOf(100f, 100f)
        val table = Table(width)
        //pengisian table dengan data-data
        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
        table.addCell(Cell().add(Paragraph("Nama Pemesan")))
        table.addCell(Cell().add(Paragraph(nama)))
        table.addCell(Cell().add(Paragraph("Rincian")))
        table.addCell(Cell().add(Paragraph(rincian)))
        table.addCell(Cell().add(Paragraph("Jenis Pesanan")))
        table.addCell(Cell().add(Paragraph(jenisPesanan)))
        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
        table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

        //pembuatan QR CODE secara generate dengan bantuan IText7
        val barcodeQRCode = BarcodeQRCode(
            """
                                      $nama
                                      $rincian
                                      $jenisPesanan
                                      ${LocalDate.now().format(dateTimeFormatter)}
                                      ${LocalTime.now().format(timeFormatter)}
                                      """.trimIndent()
        )
        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(
            HorizontalAlignment.CENTER
        )

        document.add(image)
        document.add(namapengguna)
        document.add(group)
        document.add(table)
        document.add(qrCodeImage)

        document.close()
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_LONG).show()
    }
}