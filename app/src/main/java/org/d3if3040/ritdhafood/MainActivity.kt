package org.d3if3040.ritdhafood

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import org.d3if3040.ritdhafood.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*

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

        binding.btnSave.setOnClickListener {
            val note = binding.etNote.text.toString()
            if (note.isNotEmpty()) {
                val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

                val values = ContentValues()
                values.put(DatabaseHelper.COLUMN_NOTE, note)
                values.put(DatabaseHelper.COLUMN_DATE, currentDate)

                val database = db.writableDatabase
                database.insert(DatabaseHelper.TABLE_NAME, null, values)
                database.close()

                Toast.makeText(this, "Catatan berhasil disimpan", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Catatan tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
            val intent = Intent(this, NotesActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
