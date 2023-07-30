package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.dhaval2404.imagepicker.ImagePicker
import id.hikmah.stiki.tandur_1.databinding.ActivityRentLandDetailBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ImageGalleryAdapter
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.model.RentLandModel
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.RentLandDetailViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.launch

class RentLandDetailActivity : AppCompatActivity() {
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

    private lateinit var binding: ActivityRentLandDetailBinding
    private lateinit var viewModel: RentLandDetailViewModel
    private lateinit var dialog : ProgressDialog
    private lateinit var rentLandModel: RentLandModel
    private var imageGalleryAdapter = ImageGalleryAdapter(listener)
    private var facility = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentLandDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RentLandDetailViewModel::class.java)
        dialog = ProgressDialog(this)

        rentLandModel = intent.getParcelableExtra(KeyIntent.KEY_DATA_RENT_LAND)!!

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@RentLandDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            isNestedScrollingEnabled = false
            adapter = imageGalleryAdapter
        }

        binding.checkBoxIrigasi.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                facility.add("Irigasi")
            } else {
                facility.remove("Irigasi")
            }
        }

        binding.checkBoxListrik.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                facility.add("Listrik")
            } else {
                facility.remove("Listrik")
            }
        }

        binding.checkBoxPeralatan.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                facility.add("Peralatan")
            } else {
                facility.remove("Peralatan")
            }
        }

        binding.checkBoxKanopi.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                facility.add("Kanopi")
            } else {
                facility.remove("Kanopi")
            }
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
            rentLandModel.price = binding.editTextHargaSewa.text.toString()
            rentLandModel.lengthLand = binding.editTextPanjang.text.toString()
            rentLandModel.widthLand = binding.editTextLebar.text.toString()
            rentLandModel.desc = binding.editTextDeskripsiLahan.text.toString()
            rentLandModel.rule = binding.editTextPeraturanLahan.text.toString()

            var strFacility = ""
            if (facility.isNotEmpty()) {
                for (i in 0..facility.lastIndex) {
                    strFacility = if (i == facility.lastIndex) {
                        strFacility + facility[i]
                    } else {
                        strFacility + facility[i] + ";"
                    }
                }
            }
            rentLandModel.facility = strFacility.toString()
            when (imageGalleryAdapter.list.size) {
                5 -> {
                    rentLandModel.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    rentLandModel.gallery2 = imageGalleryAdapter.list.get(1).toString()
                    rentLandModel.gallery3 = imageGalleryAdapter.list.get(2).toString()
                    rentLandModel.gallery4 = imageGalleryAdapter.list.get(3).toString()
                    rentLandModel.gallery5 = imageGalleryAdapter.list.get(4).toString()
                }
                4 -> {
                    rentLandModel.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    rentLandModel.gallery2 = imageGalleryAdapter.list.get(1).toString()
                    rentLandModel.gallery3 = imageGalleryAdapter.list.get(2).toString()
                    rentLandModel.gallery4 = imageGalleryAdapter.list.get(3).toString()
                    rentLandModel.gallery5 = null
                }
                3 -> {
                    rentLandModel.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    rentLandModel.gallery2 = imageGalleryAdapter.list.get(1).toString()
                    rentLandModel.gallery3 = imageGalleryAdapter.list.get(2).toString()
                    rentLandModel.gallery4 = null
                    rentLandModel.gallery5 = null
                }
                2 -> {
                    rentLandModel.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    rentLandModel.gallery2 = imageGalleryAdapter.list.get(1).toString()
                    rentLandModel.gallery3 = null
                    rentLandModel.gallery4 = null
                    rentLandModel.gallery5 = null
                }
                1 -> {
                    rentLandModel.gallery1 = imageGalleryAdapter.list.get(0).toString()
                    rentLandModel.gallery2 = null
                    rentLandModel.gallery3 = null
                    rentLandModel.gallery4 = null
                    rentLandModel.gallery5 = null
                }
                0 -> {
                    rentLandModel.gallery1 = null
                    rentLandModel.gallery2 = null
                    rentLandModel.gallery3 = null
                    rentLandModel.gallery4 = null
                    rentLandModel.gallery5 = null
                }
            }

            if (binding.editTextHargaSewa.text.isNullOrEmpty()
                || binding.editTextPanjang.text.isNullOrEmpty()
                || binding.editTextLebar.text.isNullOrEmpty()
                || binding.editTextDeskripsiLahan.text.isNullOrEmpty()
                || binding.editTextPeraturanLahan.text.isNullOrEmpty()) {
                Toast.makeText(this, "Mohon lengkapi data lahan", Toast.LENGTH_SHORT).show()
            } else {
                if (imageGalleryAdapter.list.isNullOrEmpty()) {
                    Toast.makeText(this, "Mohon tambahkan foto lahan (minimal 1 foto)", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.insertLahan(
                        this,
                        rentLandModel
                    )
                }
            }
        }

        viewModel.state.observe(this) {
            when (it) {
                State.LOADING -> {
                    showProgressDialog()
                }
                State.COMPLETE -> {
                    dialog.dismiss()
                    startActivity(Intent(this, FeedbackInsertLahanActivity::class.java))
                }
                State.ERROR -> {
                    dialog.dismiss()
                }
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
                        Compressor.compress(this@RentLandDetailActivity, it) {
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