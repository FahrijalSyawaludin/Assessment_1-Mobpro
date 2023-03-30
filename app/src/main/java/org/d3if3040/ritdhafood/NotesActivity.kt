package org.d3if3040.ritdhafood

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if3040.ritdhafood.databinding.ActivityNotesBinding


class NotesActivity : AppCompatActivity() {
    private lateinit var db: DatabaseHelper
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var binding: ActivityNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DatabaseHelper(this)
        noteAdapter = NoteAdapter(getAllNotes())

        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        binding.rvNotes.adapter = noteAdapter
    }

    private fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val database = db.readableDatabase
        val cursor = database.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_NOTE,
                DatabaseHelper.COLUMN_DATE
            ),
            null,
            null,
            null,
            null,
            "${DatabaseHelper.COLUMN_ID} DESC"
        )

        val idColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)
        val noteColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NOTE)
        val dateColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE)
        if (noteColumnIndex >= 0) {
            while (cursor.moveToNext()) {
                notes.add(
                    Note(
                        id = cursor.getInt(idColumnIndex),
                        note = cursor.getString(noteColumnIndex),
                        date = cursor.getString(dateColumnIndex)
                    )
                )
            }
        }

        cursor.close()
        database.close()
        return notes
    }
}
