package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Message
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivitySignUpBinding
import id.hikmah.stiki.tandur_1.v2.model.CityData
import id.hikmah.stiki.tandur_1.v2.model.DistrictData
import id.hikmah.stiki.tandur_1.v2.model.ProvinceData
import id.hikmah.stiki.tandur_1.v2.model.RegisterModel
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var dialog : ProgressDialog
    private var provinces: List<ProvinceData> = emptyList()
    private var cities: List<CityData> = emptyList()
    private var districts: List<DistrictData> = emptyList()
    private var profileUri: Uri? = null
    private var idProvince: Int = 0
    private var idCity: Int = 0
    private var idDistrict: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        dialog = ProgressDialog(this)

        viewModel.getProvince(this)
        viewModel.provinceData.observe(this) {
            provinces = it.sortedBy { it.nameProvince }.toMutableList()
            val data = ArrayList<String>()
            provinces.forEach {
                data.add(it.nameProvince.toString())
            }
            val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, data)
            binding.autoCompleteTextViewProvinsi.setAdapter(adapter)
        }
        binding.autoCompleteTextViewProvinsi.setOnItemClickListener { adapterView, view, i, l ->
            binding.autoCompleteTextViewKota.text.clear()
            binding.autoCompleteTextViewKecamatan.text.clear()
            viewModel.getCity(
                this,
                provinces.get(i).idProvince!!
            )

            idProvince = provinces.get(i).idProvince!!
        }

        viewModel.cityData.observe(this) {
            cities = it.sortedBy { it.nameCity }.toMutableList()
            val data = ArrayList<String>()
            cities.forEach {
                data.add(it.nameCity.toString())
            }
            val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, data)
            binding.autoCompleteTextViewKota.setAdapter(adapter)
        }
        binding.autoCompleteTextViewKota.setOnItemClickListener { adapterView, view, i, l ->
            binding.autoCompleteTextViewKecamatan.text.clear()
            viewModel.getDistrict(
                this,
                cities.get(i).idCity!!
            )

            idCity = cities.get(i).idCity!!
        }

        viewModel.districtData.observe(this) {
            districts = it.sortedBy { it.nameDistrict }.toMutableList()
            val data = ArrayList<String>()
            districts.forEach {
                data.add(it.nameDistrict.toString())
            }
            val adapter = ArrayAdapter(this, R.layout.item_dropdown_text, data)
            binding.autoCompleteTextViewKecamatan.setAdapter(adapter)
        }
        binding.autoCompleteTextViewKecamatan.setOnItemClickListener { adapterView, view, i, l ->
            idDistrict = districts.get(i).idDistrict!!
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

        binding.shapeableImageView.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                .start()
        }

        binding.btnSelanjutnya.setOnClickListener {
            if (profileUri == null) {
                createToast("Mohon ambil gambar profil terlebih dahulu")
            } else if (binding.editKtp.text.isNullOrEmpty()
                || binding.editEmail.text.isNullOrEmpty()
                || binding.editNama.text.isNullOrEmpty()
                || binding.editNomor.text.isNullOrEmpty()
                || binding.editAlamat.text.isNullOrEmpty()
                || binding.autoCompleteTextViewProvinsi.text.isNullOrEmpty()
                || binding.autoCompleteTextViewKota.text.isNullOrEmpty()
                || binding.autoCompleteTextViewKecamatan.text.isNullOrEmpty()
                || binding.editPassword.text.isNullOrEmpty()
                || binding.editKonfirmasiPassword.text.isNullOrEmpty()) {
                createToast("Mohon lengkapi data anda")
            } else if (!binding.editPassword.text.toString().trim().equals(binding.editKonfirmasiPassword.text.toString().trim())) {
                createToast("Password tidak sesuai")
            } else {
                val data = RegisterModel(
                    idDistrict,
                    idCity,
                    idProvince,
                    binding.editEmail.text.toString(),
                    binding.editKtp.text.toString(),
                    binding.editNama.text.toString(),
                    binding.editPassword.text.toString(),
                    binding.editNomor.text.toString(),
                    binding.editAlamat.text.toString(),
                )

                val intent = Intent(this, SignUpKtpActivity::class.java)
                intent.putExtra(KeyIntent.KEY_DATA_REGISTER, data)
                intent.putExtra(KeyIntent.KEY_IMG_PROFILE_REGISTER, profileUri)
                startActivity(intent)
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