package org.d3if3040.logindanqrcode

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.d3if3040.logindanqrcode.databinding.ActivityMainBinding

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
    }
}
