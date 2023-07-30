package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivitySellProductBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ImageGalleryAdapter
import id.hikmah.stiki.tandur_1.v2.model.CategoryData
import id.hikmah.stiki.tandur_1.v2.model.ProvinceData
import id.hikmah.stiki.tandur_1.v2.model.SellProductModel
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.util.Utils
import id.hikmah.stiki.tandur_1.v2.viewmodel.SellProductViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.launch

class SellProductActivity : AppCompatActivity() {
    private val listener = object : ItemClickListener<Boolean> {
        override fun onClickItem(item: Boolean) {
            if (item) {
                binding.linearLayoutGallery.visibility = View.VISIBLE
                binding.nestedScrollView.visibility = View.GONE
            } else {
                binding.imageViewAdd.visibility = View.VISIBLE
            }
        }
    }

    private lateinit var binding: ActivitySellProductBinding
    private lateinit var viewModel: SellProductViewModel
    private lateinit var dialog : ProgressDialog
    private var imageGalleryAdapter = ImageGalleryAdapter(listener)
    private var categories: List<CategoryData> = emptyList()
    private var productType: Int = 0
    private var idPcat: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productType = intent.getIntExtra(KeyIntent.KEY_PRODUCT_TYPE,0)

        viewModel = ViewModelProvider(this)[SellProductViewModel::class.java]
        dialog = ProgressDialog(this)

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

        binding.autoCompleteTextViewKategori.setOnItemClickListener { adapterView, view, i, l ->
            idPcat = categories.get(i).idPcat.toString()
        }

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

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SellProductActivity, LinearLayoutManager.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            adapter = imageGalleryAdapter
        }

        binding.linearLayoutGallery.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(Utils.getOutputDirectory(this))
                .start()
        }

        binding.imageViewAdd.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(Utils.getOutputDirectory(this))
                .start()
        }

        binding.materialButtonKirim.setOnClickListener {
            val data = SellProductModel(
                binding.editTextNamaBarang.text.toString(),
                productType.toString(),
                idPcat.toString(),
                binding.editTextHarga.text.toString(),
                binding.editTextBerat.text.toString(),
                binding.editTextStokBarang.text.toString(),
                binding.autoCompleteTextViewKondisiBarang.text.toString(),
                binding.editTextDeskripsiBarang.text.toString(),
                binding.editTextCatatanPenjual.text.toString(),
                "",
                "",
                "",
                "",
                "",
            )

            when (imageGalleryAdapter.list.size) {
                5 -> {
                    data.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    data.gallery2 = imageGalleryAdapter.list.get(1).toString()
                    data.gallery3 = imageGalleryAdapter.list.get(2).toString()
                    data.gallery4 = imageGalleryAdapter.list.get(3).toString()
                    data.gallery5 = imageGalleryAdapter.list.get(4).toString()
                }
                4 -> {
                    data.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    data.gallery2 = imageGalleryAdapter.list.get(1).toString()
                    data.gallery3 = imageGalleryAdapter.list.get(2).toString()
                    data.gallery4 = imageGalleryAdapter.list.get(3).toString()
                    data.gallery5 = null
                }
                3 -> {
                    data.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    data.gallery2 = imageGalleryAdapter.list.get(1).toString()
                    data.gallery3 = imageGalleryAdapter.list.get(2).toString()
                    data.gallery4 = null
                    data.gallery5 = null
                }
                2 -> {
                    data.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    data.gallery2 = imageGalleryAdapter.list.get(1).toString()
                    data.gallery3 = null
                    data.gallery4 = null
                    data.gallery5 = null
                }
                1 -> {
                    data.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    data.gallery2 = null
                    data.gallery3 = null
                    data.gallery4 = null
                    data.gallery5 = null
                }
                0 -> {
                    data.gallery1 = null
                    data.gallery2 = null
                    data.gallery3 = null
                    data.gallery4 = null
                    data.gallery5 = null
                }
            }

            if (binding.editTextNamaBarang.text.isNullOrEmpty()
                || binding.autoCompleteTextViewKategori.text.isNullOrEmpty()
                || binding.editTextHarga.text.isNullOrEmpty()
                || binding.editTextStokBarang.text.isNullOrEmpty()
                || binding.autoCompleteTextViewKondisiBarang.text.isNullOrEmpty()
                || binding.editTextDeskripsiBarang.text.isNullOrEmpty()
                || binding.editTextCatatanPenjual.text.isNullOrEmpty()) {
                Toast.makeText(this, "Mohon lengkapi data produk", Toast.LENGTH_SHORT).show()
            } else {
                if (imageGalleryAdapter.list.isNullOrEmpty()) {
                    Toast.makeText(this, "Mohon tambahkan foto produk (minimal 1 foto)", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insertProduct(this, data)
                }
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
                        Compressor.compress(this@SellProductActivity, it) {
                            default(quality = 80, height = 480)
                            destination(it)
                        }
                    }
                }
                if (imageGalleryAdapter.list.isEmpty()) {
                    binding.linearLayoutGallery.visibility = View.GONE
                    binding.nestedScrollView.visibility = View.VISIBLE
                }

                imageGalleryAdapter.list.add(uri)
                imageGalleryAdapter.notifyDataSetChanged()

                if (imageGalleryAdapter.list.size == 5) {
                    binding.imageViewAdd.visibility = View.GONE
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