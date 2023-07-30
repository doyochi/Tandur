package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityUpdateProductBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ImageGalleryAdapter
import id.hikmah.stiki.tandur_1.v2.model.CategoryData
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.model.SellProductModel
import id.hikmah.stiki.tandur_1.v2.model.UpdateProductModel
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.util.Utils
import id.hikmah.stiki.tandur_1.v2.viewmodel.SellProductViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.UpdateLandViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.UpdateProductViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.launch

class UpdateProductActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateProductBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(UpdateProductViewModel::class.java)
    }
    private val idProduct by lazy {
        intent.getStringExtra(KeyIntent.KEY_ID_PRODUCT)
    }
    private val category by lazy {
        intent.getStringExtra(KeyIntent.KEY_CATEGORY)
    }
    private lateinit var dialog : ProgressDialog
    private var categories: List<CategoryData> = emptyList()
    private var productType: Int = 0
    private var idPcat: String = "0"
    private var typeFoto: Int = 0
    private var uriFoto1: Uri? = null
    private var uriFoto2: Uri? = null
    private var uriFoto3: Uri? = null
    private var uriFoto4: Uri? = null
    private var uriFoto5: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        dialog = ProgressDialog(this)

        binding.autoCompleteTextViewKategori.setOnItemClickListener { adapterView, view, i, l ->
            idPcat = categories.get(i).idPcat.toString()
        }

        binding.cardViewFoto1.setOnClickListener {
            typeFoto = 1
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding.cardViewFoto2.setOnClickListener {
            typeFoto = 2
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding.cardViewFoto3.setOnClickListener {
            typeFoto = 3
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding.cardViewFoto4.setOnClickListener {
            typeFoto = 4
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding.cardViewFoto5.setOnClickListener {
            typeFoto = 5
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        initIntentData()
        initKondisi()

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

        viewModel.stateInsert.observe(this) {
            when (it) {
                State.LOADING -> {
                    showProgressDialog()
                }
                State.COMPLETE -> {
                    startActivity(Intent(this, FeedbackSellProductActivity::class.java))
                    finish()
                }
                State.ERROR -> {
                    dialog.dismiss()
                }
            }
        }

        binding.materialButtonKirim.setOnClickListener {
            val data = UpdateProductModel(
                binding.editTextNamaBarang.text.toString(),
                productType.toString(),
                idProduct.toString(),
                idPcat.toString(),
                binding.editTextHarga.text.toString(),
                binding.editTextBerat.text.toString(),
                binding.editTextStokBarang.text.toString(),
                binding.autoCompleteTextViewKondisiBarang.text.toString(),
                binding.editTextDeskripsiBarang.text.toString(),
                binding.editTextCatatanPenjual.text.toString(),
                if (uriFoto1 != null) {uriFoto1.toString()} else {null},
                if (uriFoto2 != null) {uriFoto2.toString()} else {null},
                if (uriFoto3 != null) {uriFoto3.toString()} else {null},
                if (uriFoto4 != null) {uriFoto4.toString()} else {null},
                if (uriFoto5 != null) {uriFoto5.toString()} else {null},
            )

            if (binding.editTextNamaBarang.text.isNullOrEmpty()
                || binding.autoCompleteTextViewKategori.text.isNullOrEmpty()
                || binding.editTextHarga.text.isNullOrEmpty()
                || binding.editTextStokBarang.text.isNullOrEmpty()
                || binding.autoCompleteTextViewKondisiBarang.text.isNullOrEmpty()
                || binding.editTextDeskripsiBarang.text.isNullOrEmpty()
                || binding.editTextCatatanPenjual.text.isNullOrEmpty()) {
                Toast.makeText(this, "Mohon lengkapi data produk", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateProduct(this, data)
            }
        }
    }

    private fun initKondisi() {
        val dataKondisi = ArrayList<String>()
        dataKondisi.add("Baru")
        if (productType == 2) {
            dataKondisi.add("Bekas")
        }
        val adapterKondisi = ArrayAdapter(this, R.layout.item_dropdown_text, dataKondisi)
        binding.autoCompleteTextViewKondisiBarang.setAdapter(adapterKondisi)
    }

    private fun initIntentData() {
        if (!idProduct.isNullOrEmpty()) {
            viewModel.getDetailProduct(this, idProduct.toString())
            viewModel.product.observe(this) {
                if (!it.isNullOrEmpty()) {
                    it.first().let {
                        idPcat = it.idPcat.toString()

                        binding.editTextNamaBarang.setText(it.nameProduct)
                        binding.editTextHarga.setText(it.priceProduct)
                        binding.editTextBerat.setText(it.weightProduct)
                        binding.editTextStokBarang.setText(it.stockProduct.toString())
                        binding.editTextDeskripsiBarang.setText(it.descProduct)
                        binding.editTextCatatanPenjual.setText(it.noteProduct)
                        binding.autoCompleteTextViewKondisiBarang.setHint(it.conditionProduct)
                        binding.autoCompleteTextViewKategori.setHint(it.namePcat)

                        Glide.with(this)
                            .load(it.photoProduct?.get(0).toString())
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto1)
                        Glide.with(this)
                            .load(it.photoProduct?.get(1).toString())
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto2)
                        Glide.with(this)
                            .load(it.photoProduct?.get(2).toString())
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto3)
                        Glide.with(this)
                            .load(it.photoProduct?.get(3).toString())
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto4)
                        Glide.with(this)
                            .load(it.photoProduct?.get(4).toString())
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto5)
                    }
                }
            }
        }

        if (!category.isNullOrEmpty()) {
            if (category.equals("Tandur Market",true)) {
                productType = 1
            } else {
                productType = 2
            }
            viewModel.getCategory(this, productType)
            viewModel.categories.observe(this) {
                categories = it.sortedBy { it.namePcat }.toMutableList()
                val data = ArrayList<String>()
                categories.forEach {
                    data.add(it.namePcat.toString())
                }
                val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, data)
                binding.autoCompleteTextViewKategori.setAdapter(adapter)
            }
        }
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
                        Compressor.compress(this@UpdateProductActivity, it) {
                            default(quality = 80, height = 480)
                            destination(it)
                        }
                    }
                }

                when(typeFoto) {
                    1 -> {
                        uriFoto1 = uri
                        Glide.with(this)
                            .load(uri)
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto1)
                    }
                    2 -> {
                        uriFoto2 = uri
                        Glide.with(this)
                            .load(uri)
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto2)
                    }
                    3 -> {
                        uriFoto3 = uri
                        Glide.with(this)
                            .load(uri)
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto3)
                    }
                    4 -> {
                        uriFoto4 = uri
                        Glide.with(this)
                            .load(uri)
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto4)
                    }
                    5 -> {
                        uriFoto5 = uri
                        Glide.with(this)
                            .load(uri)
                            .error(R.drawable.ic_upload_image_fields)
                            .placeholder(R.drawable.ic_upload_image_fields)
                            .into(binding.imageViewFoto5)
                    }
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