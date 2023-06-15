package org.d3if3040.logindanqrcode.ViewModel;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.d3if3040.logindanqrcode.Model.DataBaseHeleperLogin;
import org.d3if3040.logindanqrcode.R;

public class EditDataActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnUpdate, btnCancel;
    private DataBaseHeleperLogin dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_activity);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnUpdate = findViewById(R.id.btn_update);
        btnCancel = findViewById(R.id.btn_cancel);

        dbHelper = new DataBaseHeleperLogin(this);

        // Mengambil data yang dikirim dari ViewDataActivity
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        etUsername.setText(username);
        etPassword.setText(password);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengambil data yang diisi pengguna
                String newUsername = etUsername.getText().toString().trim();
                String newPassword = etPassword.getText().toString().trim();

                if (newUsername.isEmpty()) {
                    etUsername.setError("Username harus diisi");
                    etUsername.requestFocus();
                    return;
                }

                if (newPassword.isEmpty()) {
                    etPassword.setError("Password harus diisi");
                    etPassword.requestFocus();
                    return;
                }

                // Memperbarui data pada database
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("username", newUsername);
                values.put("password", newPassword);
                int result = db.update("user", values, "id=?", new String[]{String.valueOf(id)});
                if (result > 0) {
                    Toast.makeText(EditDataActivity.this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditDataActivity.this, "Data gagal diperbarui", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke halaman sebelumnya tanpa melakukan perubahan
                finish();
            }
        });
    }
}
