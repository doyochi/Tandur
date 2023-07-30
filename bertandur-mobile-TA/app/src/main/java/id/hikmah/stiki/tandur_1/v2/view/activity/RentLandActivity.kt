package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.github.dhaval2404.imagepicker.ImagePicker
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityRentLandBinding
import id.hikmah.stiki.tandur_1.v2.model.CityData
import id.hikmah.stiki.tandur_1.v2.model.DistrictData
import id.hikmah.stiki.tandur_1.v2.model.ProvinceData
import id.hikmah.stiki.tandur_1.v2.model.RentLandModel
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.RentLandViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import kotlinx.coroutines.launch

class RentLandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRentLandBinding
    private lateinit var viewModel: RentLandViewModel
    private lateinit var dialog : ProgressDialog
    private var provinces: List<ProvinceData> = emptyList()
    private var cities: List<CityData> = emptyList()
    private var districts: List<DistrictData> = emptyList()
    private var idProvince: Int = 0
    private var idCity: Int = 0
    private var idDistrict: Int = 0
    private var locationLand: Int = 1
    private var isGambar1: Boolean = false
    private var gambar1Uri: Uri? = null
    private var gambar2Uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentLandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RentLandViewModel::class.java)
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

        binding.cardViewGambar1.setOnClickListener {
            isGambar1 = true
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(Utils.getOutputDirectory(this))
                .start()
        }

        binding.cardViewGambar2.setOnClickListener {
            isGambar1 = false
            ImagePicker.with(this)
                .crop()
                .compress(256)
                .maxResultSize(620, 620)
                .saveDir(Utils.getOutputDirectory(this))
                .start()
        }

        binding.checkBoxKtp.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.editTextNamaPemilikKtp.setText(JWTUtils.decoded(Prefs(this).jwt.toString()).nameUser.toString())
                binding.editTextNoKtp.setText(JWTUtils.decoded(Prefs(this).jwt.toString()).ktpUser.toString())

                binding.editTextNamaPemilikKtp.isEnabled = false
                binding.editTextNoKtp.isEnabled = false
            } else {
                binding.editTextNamaPemilikKtp.text.clear()
                binding.editTextNoKtp.text.clear()

                binding.editTextNamaPemilikKtp.isEnabled = true
                binding.editTextNoKtp.isEnabled = true
            }
        }

        binding.checkBoxAlamat.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                idProvince = JWTUtils.decoded(Prefs(this).jwt.toString()).idProvince.toString().toInt()
                idCity = JWTUtils.decoded(Prefs(this).jwt.toString()).idCity.toString().toInt()
                idDistrict = JWTUtils.decoded(Prefs(this).jwt.toString()).idDistrict.toString().toInt()

                binding.autoCompleteTextViewProvinsi.setText(provinces.find { it.idProvince.toString().equals(JWTUtils.decoded(Prefs(this).jwt.toString()).idProvince.toString()) }?.nameProvince.toString())
                binding.autoCompleteTextViewKota.setText(cities.find { it.idCity.toString().equals(JWTUtils.decoded(Prefs(this).jwt.toString()).idCity.toString()) }?.nameCity.toString())
                binding.autoCompleteTextViewKecamatan.setText(districts.find { it.idDistrict.toString().equals(JWTUtils.decoded(Prefs(this).jwt.toString()).idDistrict.toString()) }?.nameDistrict.toString())

                binding.autoCompleteTextViewProvinsi.isEnabled = false
                binding.autoCompleteTextViewKota.isEnabled = false
                binding.autoCompleteTextViewKecamatan.isEnabled = false
            } else {
                binding.autoCompleteTextViewProvinsi.text.clear()
                binding.autoCompleteTextViewKota.text.clear()
                binding.autoCompleteTextViewKecamatan.text.clear()

                binding.autoCompleteTextViewProvinsi.isEnabled = true
                binding.autoCompleteTextViewKota.isEnabled = true
                binding.autoCompleteTextViewKecamatan.isEnabled = true
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when(i) {
                R.id.radioButtonHalamanTerasRumah -> {
                    locationLand = 1

                    binding.textInputLayoutNoSertifikatLahan.visibility = View.GONE
                    binding.editTextNoSertifikatLahan.text.clear()
                    binding.textViewGambar1.text = "Foto Rumah Anda"
                    binding.textViewGambar2.text = "Foto Halaman\nRumah Anda"
                    binding.imageViewGambar1.setImageResource(R.drawable.ic_default_foto_rumah_anda)
                    binding.imageViewGambar2.setImageResource(R.drawable.ic_default_foto_halaman_rumah_anda)
                    gambar1Uri = null
                    gambar2Uri = null
                }
                R.id.radioButtonLahanBerbedaDariRumah -> {
                    locationLand = 2

                    binding.textInputLayoutNoSertifikatLahan.visibility = View.VISIBLE
                    binding.editTextNoSertifikatLahan.text.clear()
                    binding.textViewGambar1.text = "Foto Sertifikat Tanah"
                    binding.textViewGambar2.text = "Foto Lahan Anda"
                    binding.imageViewGambar1.setImageResource(R.drawable.ic_default_foto_rumah_anda)
                    binding.imageViewGambar2.setImageResource(R.drawable.ic_default_foto_halaman_rumah_anda)
                    gambar1Uri = null
                    gambar2Uri = null
                }
            }
        }

        binding.materialButtonLanjut.setOnClickListener {
            val address = binding.editTextAlamatLahan.text.trim().toString() + "," + binding.autoCompleteTextViewKecamatan.text.toString() + "," + binding.autoCompleteTextViewKota.text.toString() + "," + binding.autoCompleteTextViewProvinsi.text.toString() + ",Indonesia"
            val longLat = Utils.getLocationFromAddress(this, address.toString())

            val data = RentLandModel(
                binding.editTextNamaLahan.text.toString(),
                binding.editTextAlamatLahan.text.toString(),
                idProvince.toString(),
                idCity.toString(),
                idDistrict.toString(),
                locationLand.toString(),
                binding.editTextNoSertifikatLahan.text.toString(),
                gambar1Uri.toString(),
                gambar2Uri.toString(),
                "",
                binding.editTextNamaPemilikKtp.text.toString(),
                binding.editTextNoKtp.text.toString(),
                JWTUtils.decoded(Prefs(this).jwt.toString()).emailUser.toString(),
                JWTUtils.decoded(Prefs(this).jwt.toString()).telpUser.toString(),
                "",
                "",
                "",
                "",
                "0",
                longLat?.longitude.toString(),
                longLat?.latitude.toString(),
                "",
                "",
                "",
                "",
                "",
                ""
            )

            val intent = Intent(this, RentLandDetailActivity::class.java)
            intent.putExtra(KeyIntent.KEY_DATA_RENT_LAND, data)

            if (binding.editTextNamaLahan.text.isNullOrEmpty()
                || binding.editTextAlamatLahan.text.isNullOrEmpty()
                || binding.autoCompleteTextViewProvinsi.text.isNullOrEmpty()
                || binding.autoCompleteTextViewKota.text.isNullOrEmpty()
                || binding.autoCompleteTextViewKecamatan.text.isNullOrEmpty()
                || gambar1Uri == null
                || gambar2Uri == null
                || binding.editTextNamaPemilikKtp.text.isNullOrEmpty()
                || binding.editTextNoKtp.text.isNullOrEmpty()) {
                Toast.makeText(this, "Mohon lengkapi data lahan", Toast.LENGTH_SHORT).show()
            } else {
                if (locationLand == 1) {
                    startActivity(intent)
                } else {
                    if (binding.editTextNoSertifikatLahan.text.isNullOrEmpty()) {
                        Toast.makeText(this, "Mohon lengkapi data lahan", Toast.LENGTH_SHORT).show()
                    } else {
                        startActivity(intent)
                    }
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
                        Compressor.compress(this@RentLandActivity, it) {
                            default(quality = 80, height = 480)
                            destination(it)
                        }
                    }
                }
                if (isGambar1) {
                    binding.imageViewGambar1.setImageURI(uri)
                    gambar1Uri = uri
                } else {
                    binding.imageViewGambar2.setImageURI(uri)
                    gambar2Uri = uri
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