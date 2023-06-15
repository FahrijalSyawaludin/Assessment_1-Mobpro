package org.d3if3040.logindanqrcode.ViewModel;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.d3if3040.logindanqrcode.worker.LoadDataWorker;
import org.d3if3040.logindanqrcode.Model.DataBaseHeleperLogin;
import org.d3if3040.logindanqrcode.Model.UserData;
import org.d3if3040.logindanqrcode.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

public class ViewDataActivityApi extends AppCompatActivity {
    private List<UserData> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DataAdapter dataAdapter;
    private DataBaseHeleperLogin dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_api);

        recyclerView = findViewById(R.id.recycler_view);
        dbHelper = new DataBaseHeleperLogin(this);

        loadData();

        dataAdapter = new DataAdapter(dataList);
        recyclerView.setAdapter(dataAdapter);
        Button btnAdd = findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewDataActivityApi.this, ImageApiActivity.class);
                startActivity(intent);
            }
        });

    }


    private void loadData() {
        WorkRequest loadDataRequest = new OneTimeWorkRequest.Builder(LoadDataWorker.class)
                .build();

        WorkManager.getInstance(this).enqueue(loadDataRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(loadDataRequest.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        Data outputData = workInfo.getOutputData();
                        String jsonArrayString = outputData.getString("jsonArray");

                        if (jsonArrayString != null) {
                            try {
                                JSONArray jsonArray = new JSONArray(jsonArrayString);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String username = jsonObject.getString("username");
                                    String password = jsonObject.getString("password");

                                    UserData data = new UserData(id, username, password);
                                    dataList.add(data);
                                }

                                // Menampilkan data kepada pengguna
                                dataAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(ViewDataActivityApi.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        }
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

            holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { // Ketika tombol "Edit" diklik, panggil method editData() pada activity
                    editData(data);
                }
            });

            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Ketika tombol "Hapus" diklik, panggil method deleteData() pada activity
                    deleteData(data);
                }
            });

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }

    private void editData(UserData data) {
        // Membuat Intent untuk membuka activity EditDataActivity dan mengirimkan data ke activity tersebut
        Intent intent = new Intent(this, EditDataActivity.class);
        intent.putExtra("id", data.getId());
        intent.putExtra("username", data.getUsername());
        intent.putExtra("password", data.getPassword());
        startActivity(intent);
    }

    private void deleteData(UserData data) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int result = db.delete("user", "id=?", new String[]{String.valueOf(data.getId())});
        if (result > 0) {
            dataList.remove(data);
            dataAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Data gagal dihapus", Toast.LENGTH_SHORT).show();
        }
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