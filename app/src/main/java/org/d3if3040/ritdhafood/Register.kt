package org.d3if3040.ritdhafood

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import org.d3if3040.ritdhafood.databinding.RegisterBinding

class Register : DialogFragment() {
    private lateinit var db: DataBaseHeleperLogin
    private var _binding: RegisterBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "Register"
        fun newInstance() = Register()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = RegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DataBaseHeleperLogin(requireActivity())

        binding.btnRegister.setOnClickListener {
            val inUsername = binding.etUsername.text.toString()
            val inPassword = binding.etPassword.text.toString()
            val inrePassword = binding.etRepeatPassword.text.toString()

            if (inrePassword != inPassword) {
                binding.etRepeatPassword.error = "Password Tidak Sama"
            } else {
                val daftar = db.simpanUser(inUsername, inPassword)
                if (daftar) {
                    Toast.makeText(requireActivity(), "Daftar Berhasil", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireActivity(), "Daftar Gagal", Toast.LENGTH_LONG).show()
                }
                dismiss()
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        val activity: Activity? = activity
        if (activity is OnDialogCloseListener) {
            activity.onDialogClose(dialog)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}