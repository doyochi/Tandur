package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import id.hikmah.stiki.tandur_1.databinding.FragmentLoginBinding
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.viewmodel.SignInViewModel


class SignInActivity : AppCompatActivity() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: SignInViewModel
    private lateinit var dialog : ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
        dialog = ProgressDialog(this)

        binding.buttonLogin.setOnClickListener {
            viewModel.signIn(
                this,
                binding.editEmail.text.toString(),
                binding.editPassword.text.toString()
            )
        }

        binding.btnToregist.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        viewModel.jwt.observe(this) {
//            Prefs(this).jwt = it.toString()
//            startActivity(Intent(this, MainActivity::class.java))
//            finish()
            logRegToken()
        }

        viewModel.state.observe(this) {
            when (it) {
                State.LOADING -> {
                    showProgressDialog()
                }
                State.COMPLETE -> {
                    dialog.dismiss()
                }
                State.ERROR -> {
                    dialog.dismiss()
                }
            }
        }

        viewModel.stateToken.observe(this) {
            when (it) {
                State.LOADING -> {
                    showProgressDialog()
                }
                State.COMPLETE -> {
                    dialog.dismiss()
                    Prefs(this).jwt = viewModel.jwt.value.toString()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                State.ERROR -> {
                    dialog.dismiss()
                }
            }
        }

        viewModel.errorMessage.observe(this) {
            showAlertDialog(it.toString())
        }
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun logRegToken() {
        // [START log_reg_token]
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("logRegToken", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                val msg = "FCM Registration token: $token"
                Log.e("logRegToken", msg)

                viewModel.updateToken(this, token.toString())
            })
        // [END log_reg_token]
    }
}