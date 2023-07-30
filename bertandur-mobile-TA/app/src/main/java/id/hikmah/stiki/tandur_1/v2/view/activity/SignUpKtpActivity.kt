package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import id.hikmah.stiki.tandur_1.databinding.ActivitySignUpKtpBinding
import id.hikmah.stiki.tandur_1.v2.model.RegisterModel
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.viewmodel.SignUpKtpViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.launch

class SignUpKtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpKtpBinding
    private lateinit var viewModel: SignUpKtpViewModel
    private lateinit var dialog : ProgressDialog
    private var registerData: RegisterModel? = null
    private var photoType: Int = 0
    private var ktp: Uri? = null
    private var selfieKtp: Uri? = null
    private var profile: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpKtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SignUpKtpViewModel::class.java)
        dialog = ProgressDialog(this)

        registerData = intent.getParcelableExtra(KeyIntent.KEY_DATA_REGISTER)
        profile = intent.getParcelableExtra(KeyIntent.KEY_IMG_PROFILE_REGISTER)

        viewModel.state.observe(this) {
            when (it) {
                State.LOADING -> {
                    showProgressDialog()
                }
                State.COMPLETE -> {
                    dialog.dismiss()
                    showAlertDialog("Sukses melakukan pendaftaran, silahkan periksa email anda untuk melakukan aktivasi atau hubungi admin untuk melakukan aktivasi",true)
                }
                State.ERROR -> {
                    dialog.dismiss()

                }
            }
        }

        viewModel.errorMassage.observe(this) {
            showAlertDialog(it.toString(),false)
        }

        binding.cardViewFotoKTP.setOnClickListener {
            photoType = 0
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding.cardViewFotoSelfieKTP.setOnClickListener {
            photoType = 1
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding.materialButtonDaftarSekarang.setOnClickListener {
            if (ktp == null || selfieKtp == null) {
                createToast("Mohon ambil gambar terlebih dahulu")
            } else {
                viewModel.register(
                    this,
                    registerData!!,
                    profile!!.toFile(),
                    ktp!!.toFile(),
                    selfieKtp!!.toFile()
                )
            }
        }
    }

    fun createToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showAlertDialog(message: String, isFinish: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
            if (isFinish) {
                val intent = Intent(this, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri = data?.data!!
                uri.toFile().let {
                    lifecycleScope.launch {
                        Compressor.compress(this@SignUpKtpActivity, it) {
                            default(quality = 80, height = 480)
                            destination(it)
                        }
                    }
                }

                if (photoType == 0) {
                    binding.imageViewFotoKTP.setImageURI(uri)
                    ktp = uri
                } else {
                    binding.imageViewFotoSelfieKTP.setImageURI(uri)
                    selfieKtp = uri
                }
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}