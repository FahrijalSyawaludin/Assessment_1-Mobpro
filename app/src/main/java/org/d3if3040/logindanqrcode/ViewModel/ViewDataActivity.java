package org.d3if3040.logindanqrcode.ViewModel;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.d3if3040.logindanqrcode.ApiService;
import org.d3if3040.logindanqrcode.Model.DataBaseHeleperLogin;
import org.d3if3040.logindanqrcode.Model.UserData;
import org.d3if3040.logindanqrcode.MainActivity;
import org.d3if3040.logindanqrcode.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewDataActivity extends AppCompatActivity {
    private List<UserData> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private DataBaseHeleperLogin dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        recyclerView = findViewById(R.id.recycler_view);
        dbHelper = new DataBaseHeleperLogin(this);

        // Load data before setting the adapter
        loadData();

        dataAdapter = new DataAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);
        Button btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDataActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }


    private void loadData() {
        // Membuat objek Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/FahrijalSyawaludin/API/main/UserData")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Membuat objek ApiService menggunakan Retrofit
        ApiService apiService = retrofit.create(ApiService.class);

        // Memanggil endpoint yang mengambil data JSON dari GitHub
        Call<List<UserData>> call = apiService.getUserData();
        call.enqueue(new Callback<List<UserData>>() {
            @Override
            public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                if (response.isSuccessful()) {
                    // Clear the existing data list
                    dataList.clear();

                    // Add the new data to the list
                    dataList.addAll(response.body());

                    // Notify the adapter that the data has changed
                    dataAdapter.notifyDataSetChanged();
                } else {
                    // Handle unsuccessful response
                    String errorMessage = "Failed to load data: " + response.message();
                    Toast.makeText(ViewDataActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.e("ViewDataActivity", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<UserData>> call, Throwable t) {
                // Handle network failure
                Toast.makeText(ViewDataActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class DataViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_username, tv_password;
        private Button btnEdit, btnDelete;

        public DataViewHolder(View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_password = itemView.findViewById(R.id.tv_password);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }

    public class DataAdapter extends RecyclerView.Adapter<DataViewHolder> {
        private List<UserData> dataList;

        public DataAdapter(List<UserData> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
            return new DataViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
            UserData data = dataList.get(position);
            holder.tv_username.setText(data.getUsername());
            holder.tv_password.setText(data.getPassword());

            // Anda perlu memanggil metode editData() dan deleteData() di sini
            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Panggil method editData() pada activity
                    ((ViewDataActivity) view.getContext()).editData(data);
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Panggil method deleteData() pada activity
                    ((ViewDataActivity) view.getContext()).deleteData(data);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    public void editData(UserData data) {
        Intent intent = new Intent(ViewDataActivity.this, EditDataActivity.class);
        intent.putExtra("data", data);
        startActivity(intent);
    }


    public void deleteData(UserData data) {
        // Implement your delete data logic here
        // For example, you can show a confirmation dialog and delete the data from the database
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewDataActivity.this);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this data?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete the data from the database
                        deleteDataFromDatabase(data);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteDataFromDatabase(UserData data) {
        // Implement the actual deletion of data from the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("user", "id = ?", new String[]{String.valueOf(data.getId())});
        db.close();

        // Remove the data from the list and update the adapter
        dataList.remove(data);
        dataAdapter.notifyDataSetChanged();

        Toast.makeText(ViewDataActivity.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Share Message Here");
                startActivity(Intent.createChooser(shareIntent, "Share using"));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}