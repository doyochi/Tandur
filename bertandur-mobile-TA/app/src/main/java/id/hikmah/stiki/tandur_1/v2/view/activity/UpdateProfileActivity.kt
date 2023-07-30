package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.dhaval2404.imagepicker.ImagePicker
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityDetailProfileBinding
import id.hikmah.stiki.tandur_1.databinding.ActivityUpdateProfileBinding
import id.hikmah.stiki.tandur_1.v2.model.CityData
import id.hikmah.stiki.tandur_1.v2.model.DistrictData
import id.hikmah.stiki.tandur_1.v2.model.ProvinceData
import id.hikmah.stiki.tandur_1.v2.model.UpdateProfileModel
import id.hikmah.stiki.tandur_1.v2.util.JWTUtils
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.viewmodel.SignUpViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.UpdateProfileViewModel

class UpdateProfileActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateProfileBinding.inflate(layoutInflater)
    }

    private val user by lazy {
        JWTUtils.decoded(Prefs(this).jwt.toString())
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(UpdateProfileViewModel::class.java)
    }

    private lateinit var dialog : ProgressDialog

    private var provinces: List<ProvinceData> = emptyList()
    private var cities: List<CityData> = emptyList()
    private var districts: List<DistrictData> = emptyList()
    private var profileUri: Uri? = null
    private var idProvince: Int = 0
    private var idCity: Int = 0
    private var idDistrict: Int = 0
    private var isFisrtLoadP = true
    private var isFisrtLoadC = true
    private var isFisrtLoadD = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        observeData()
    }

    private fun initView() {
        dialog = ProgressDialog(this)

        binding.apply {
            Glide.with(this@UpdateProfileActivity)
                .load(user.imgUser)
                .placeholder(R.drawable.ic_no_profile)
                .error(R.drawable.ic_no_profile)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(shapeableImageView)

            textViewNama.text = user.nameUser
            editKtp.setText(user.ktpUser)
            editAlamat.setText(user.addressUser)
            editEmail.setText(user.emailUser)
            editNomor.setText(user.telpUser)

            autoCompleteTextViewProvinsi.setOnItemClickListener { adapterView, view, i, l ->
                autoCompleteTextViewKota.text.clear()
                autoCompleteTextViewKecamatan.text.clear()

                autoCompleteTextViewKota.setHint("Kota / Kabupaten")
                autoCompleteTextViewKecamatan.setHint("Kecamatan")

                viewModel.getCity(
                    this@UpdateProfileActivity,
                    provinces.get(i).idProvince!!
                )

                idProvince = provinces.get(i).idProvince!!
            }

            binding.autoCompleteTextViewKota.setOnItemClickListener { adapterView, view, i, l ->
                autoCompleteTextViewKecamatan.text.clear()
                autoCompleteTextViewKecamatan.setHint("Kecamatan")

                viewModel.getDistrict(
                    this@UpdateProfileActivity,
                    cities.get(i).idCity!!
                )

                idCity = cities.get(i).idCity!!
            }

            autoCompleteTextViewKecamatan.setOnItemClickListener { adapterView, view, i, l ->
                idDistrict = districts.get(i).idDistrict!!
            }

            materialButtonSimpan.setOnClickListener {
                viewModel.updateProfile(
                    this@UpdateProfileActivity,
                    UpdateProfileModel(
                        idDistrict,
                        idCity,
                        idProvince,
                        editEmail.text.toString(),
                        editKtp.text.toString(),
                        user.nameUser.toString(),
                        editNomor.text.toString(),
                        editAlamat.text.toString(),
                        user.idUser.toString()
                    ),
                    profileUri?.toFile()
                )
            }

            shapeableImageView.setOnClickListener {
                ImagePicker.with(this@UpdateProfileActivity)
                    .crop()
                    .compress(256)
                    .maxResultSize(620, 620)
                    .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                    .start()
            }
        }
    }

    private fun observeData() {
        viewModel.getProvince(this)
        viewModel.getCity(
            this@UpdateProfileActivity,
            user.idProvince.toString().toInt()
        )
        viewModel.getDistrict(
            this@UpdateProfileActivity,
            user.idCity.toString().toInt()
        )

        viewModel.provinceData.observe(this) {
            provinces = it.sortedBy { it.nameProvince }.toMutableList()
            val data = ArrayList<String>()
            provinces.forEach {
                data.add(it.nameProvince.toString())
            }
            val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, data)
            binding.autoCompleteTextViewProvinsi.setAdapter(adapter)

            if (isFisrtLoadP) {
                binding.autoCompleteTextViewProvinsi.setHint(provinces.find { it.idProvince == user.idProvince.toString().toInt() }?.nameProvince)
                idProvince = user.idProvince.toString().toInt()
                isFisrtLoadP = false
            }
        }

        viewModel.cityData.observe(this) {
            cities = it.sortedBy { it.nameCity }.toMutableList()
            val data = ArrayList<String>()
            cities.forEach {
                data.add(it.nameCity.toString())
            }
            val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, data)
            binding.autoCompleteTextViewKota.setAdapter(adapter)

            if (isFisrtLoadC) {
                binding.autoCompleteTextViewKota.setHint(cities.find { it.idCity == user.idCity.toString().toInt() }?.nameCity)
                idCity = user.idCity.toString().toInt()
                isFisrtLoadC = false
            }
        }

        viewModel.districtData.observe(this) {
            districts = it.sortedBy { it.nameDistrict }.toMutableList()
            val data = ArrayList<String>()
            districts.forEach {
                data.add(it.nameDistrict.toString())
            }
            val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, data)
            binding.autoCompleteTextViewKecamatan.setAdapter(adapter)

            if (isFisrtLoadD) {
                binding.autoCompleteTextViewKecamatan.setHint(districts.find { it.idDistrict == user.idDistrict.toString().toInt() }?.nameDistrict)
                idDistrict = user.idDistrict.toString().toInt()
                isFisrtLoadD = false
            }
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

        viewModel.stateUpdate.observe(this) {
            when (it) {
                State.LOADING -> {
                    showProgressDialog()
                }
                State.COMPLETE -> {
                    dialog.dismiss()
                    Toast.makeText(this, "Profil berhasil diubah", Toast.LENGTH_SHORT).show()
                    finish()
                }
                State.ERROR -> {
                    dialog.dismiss()
                }
            }
        }

        viewModel.errorMassage.observe(this) {
            if (it != null) {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
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
                binding.shapeableImageView.setImageURI(uri)

                profileUri = uri
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