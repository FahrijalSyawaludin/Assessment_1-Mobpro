package org.d3if3040.logindanqrcode

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.d3if3040.logindanqrcode.ViewModel.AboutActivity
import org.d3if3040.logindanqrcode.Model.DataBaseHeleperLogin
import org.d3if3040.logindanqrcode.Model.UserData
import org.d3if3040.logindanqrcode.View.LoginActivity
import org.d3if3040.logindanqrcode.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var db: DataBaseHeleperLogin
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREF_NAME = "myPref"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DataBaseHeleperLogin(this)
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)

        val checkSession = db.checkSession("ada")
        if (!checkSession) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnKeluar.setOnClickListener {
            val updateSession = db.upgradeSession("kosong", 1)
            if (updateSession) {
                Toast.makeText(this, "Berhasil Keluar", Toast.LENGTH_LONG).show()

                val editor = sharedPreferences.edit()
                editor.putBoolean("masuk", false)
                editor.apply()

                val keluar = Intent(this, LoginActivity::class.java)
                startActivity(keluar)
                finish()
            }

        }

        binding.btnCreate.setOnClickListener {
            val text = binding.etInput.text.toString().trim()

            val writer = MultiFormatWriter()

            try {
                val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 300, 300)

                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.createBitmap(bitMatrix)

                binding.ivQrCode.setImageBitmap(bitmap)

            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
        // Menampilkan tombol Up di ActionBar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Memberikan aksi ketika tombol Up diklik
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        // Membuat objek Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/FahrijalSyawaludin/API/main/UserData/") // Tambahkan tanda "/" di akhir URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Membuat objek ApiService menggunakan Retrofit
        val apiService = retrofit.create(ApiService::class.java)

        // Memanggil endpoint yang mengambil data JSON
        val call = apiService.userData
        call.enqueue(object : Callback<List<UserData>> {
            override fun onResponse(call: Call<List<UserData>>, response: Response<List<UserData>>) {
                if (response.isSuccessful) {
                    val userDataList = response.body()
                    // Lakukan operasi dengan data yang diterima
                    // Contoh: Tampilkan data dalam TextView
                    userDataList?.let {
                        for (userData in it) {
                            val username = userData.username
                            val password = userData.password
                            // Tampilkan username dan password ke TextView atau view lainnya
                        }
                    }
                } else {
                    // Menangani respon gagal
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                // Menangani kesalahan jaringan atau koneksi
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Tombol Up ditekan, lakukan tindakan yang diinginkan, seperti kembali ke activity sebelumnya
                onBackPressed()
                true
            }
            R.id.action_about -> {
                // Tombol "About" ditekan, buka activity "AboutActivity" atau lakukan tindakan yang diinginkan
                // Ganti AboutActivity dengan activity tujuan Anda
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Share Message Here")
                startActivity(Intent.createChooser(shareIntent, "Share using"))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return true
    }

}
