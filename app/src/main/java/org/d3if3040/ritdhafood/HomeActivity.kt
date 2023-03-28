package org.d3if3040.ritdhafood

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.d3if3040.ritdhafood.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Bind view dengan View Binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Atur tampilan untuk gambar header
        Glide.with(this)
            .load(R.drawable.header_image)
            .centerCrop()
            .into(binding.headerImage)

        // Atur tampilan untuk tombol start
        binding.startButton.setOnClickListener {
            // Lakukan aksi ketika tombol start di-klik
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
