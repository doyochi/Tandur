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
import id.hikmah.stiki.tandur_1.databinding.ActivityUploadEvidenceProductBinding
import id.hikmah.stiki.tandur_1.databinding.ActivityUploadEvidenceRentBinding
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.viewmodel.UploadEvidenceRentViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.launch

class UploadEvidenceProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadEvidenceProductBinding
    private lateinit var viewModel: UploadEvidenceRentViewModel
    private lateinit var dialog : ProgressDialog
    private lateinit var idPurchase: String
    private lateinit var uriImage: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadEvidenceProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idPurchase = intent.getStringExtra(KeyIntent.KEY_ID_PURCHASE).toString()

        viewModel = ViewModelProvider(this).get(UploadEvidenceRentViewModel::class.java)
        dialog = ProgressDialog(this)

        binding.cardViewFotoBuktiTransfer.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding.materialButtonKirimSekarang.setOnClickListener {
            if (uriImage != null) {
                viewModel.uploadBuktiTransferProduk(
                    this,
                    idPurchase,
                    uriImage.toFile()
                )
            } else {
                Toast.makeText(this,"Mohon upload bukti transfer terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.state.observe(this) {
            when (it) {
                State.LOADING -> {
                    showProgressDialog()
                }
                State.COMPLETE -> {
                    dialog.dismiss()
                    showAlertDialog("Sukses mengirimkan bukti transfer",true)
                }
                State.ERROR -> {
                    dialog.dismiss()

                }
            }
        }
    }

    private fun showAlertDialog(message: String, isFinish: Boolean) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
            if (isFinish) {
                val intent = Intent()
                intent.putExtra(KeyIntent.KEY_ID_PURCHASE, idPurchase)
                setResult(KeyIntent.KEY_UPLOAD_EVIDENCE, intent)
                finish()
            }
        }
        builder.show()
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val uri: Uri = data?.data!!
                uri.toFile().let {
                    lifecycleScope.launch {
                        Compressor.compress(this@UploadEvidenceProductActivity, it) {
                            default(quality = 80, height = 480)
                            destination(it)
                        }
                    }
                }
                binding.imageViewFotoBuktiTransfer.setImageURI(uri)
                uriImage = uri

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