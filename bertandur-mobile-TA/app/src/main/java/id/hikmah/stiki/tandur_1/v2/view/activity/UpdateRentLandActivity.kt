package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityUpdateRentLandBinding
import id.hikmah.stiki.tandur_1.v2.model.*
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.UpdateLandViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.UrbanFarmingViewModel

class UpdateRentLandActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUpdateRentLandBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(UpdateLandViewModel::class.java)
    }
    private lateinit var dialog : ProgressDialog
    private var landData: LandData? = null
    private var provinces: List<ProvinceData> = emptyList()
    private var cities: List<CityData> = emptyList()
    private var districts: List<DistrictData> = emptyList()
    private var idProvince: Int = 0
    private var provinceText: String = ""
    private var idCity: Int = 0
    private var cityText: String = ""
    private var idDistrict: Int = 0
    private var districtText: String = ""
    private var rule: String = ""
    private var locationLand: Int = 1
    private var isFisrtLoadP = true
    private var isFisrtLoadC = true
    private var isFisrtLoadD = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        landData = intent.getParcelableExtra(KeyIntent.KEY_LAND)

        initView()
        observeData()
    }

    private fun initView() {
        dialog = ProgressDialog(this)
        binding.apply {
            editTextNamaLahan.setText(landData?.nameLand.toString())
            locationLand = landData?.locationLand.toString().toInt()
            editTextNoKtp.setText(landData?.ownKtpLand.toString())

            when(locationLand) {
                1 -> {
                    binding.radioButtonHalamanTerasRumah.isChecked = true
                    binding.radioButtonLahanBerbedaDariRumah.isChecked = false
                }
                2 -> {
                    binding.radioButtonHalamanTerasRumah.isChecked = false
                    binding.radioButtonLahanBerbedaDariRumah.isChecked = true

                    textInputLayoutNoSertifikatLahan.visibility = View.VISIBLE
                    editTextNoSertifikatLahan.setText(landData?.noCertificateLand)
                }
            }

            autoCompleteTextViewProvinsi.setOnItemClickListener { adapterView, view, i, l ->
                autoCompleteTextViewKota.text.clear()
                autoCompleteTextViewKecamatan.text.clear()

                autoCompleteTextViewKota.setHint("Kota / Kabupaten")
                autoCompleteTextViewKecamatan.setHint("Kecamatan")

                viewModel.getCity(
                    this@UpdateRentLandActivity,
                    provinces.get(i).idProvince!!
                )

                idProvince = provinces.get(i).idProvince!!
                provinceText = provinces.get(i).nameProvince.toString()
            }

            autoCompleteTextViewKota.setOnItemClickListener { adapterView, view, i, l ->
                autoCompleteTextViewKecamatan.text.clear()
                autoCompleteTextViewKecamatan.setHint("Kecamatan")
                viewModel.getDistrict(
                    this@UpdateRentLandActivity,
                    cities.get(i).idCity!!
                )

                idCity = cities.get(i).idCity!!
                cityText = cities.get(i).nameCity.toString()
            }

            autoCompleteTextViewKecamatan.setOnItemClickListener { adapterView, view, i, l ->
                idDistrict = districts.get(i).idDistrict!!
                districtText = districts.get(i).nameDistrict.toString()
            }

            checkBoxKtp.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    editTextNamaPemilikKtp.setText(JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).nameUser.toString())
                    editTextNoKtp.setText(JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).ktpUser.toString())

                    editTextNamaPemilikKtp.isEnabled = false
                    editTextNoKtp.isEnabled = false
                } else {
                    editTextNamaPemilikKtp.text.clear()
                    editTextNoKtp.text.clear()

                    editTextNamaPemilikKtp.isEnabled = true
                    editTextNoKtp.isEnabled = true
                }
            }

            checkBoxAlamat.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    idProvince = JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).idProvince.toString().toInt()
                    idCity = JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).idCity.toString().toInt()
                    idDistrict = JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).idDistrict.toString().toInt()

                    autoCompleteTextViewProvinsi.setText(provinces.find { it.idProvince.toString().equals(
                        JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).idProvince.toString()) }?.nameProvince.toString())
                    autoCompleteTextViewKota.setText(cities.find { it.idCity.toString().equals(
                        JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).idCity.toString()) }?.nameCity.toString())
                    autoCompleteTextViewKecamatan.setText(districts.find { it.idDistrict.toString().equals(
                        JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).idDistrict.toString()) }?.nameDistrict.toString())

                    autoCompleteTextViewProvinsi.isEnabled = false
                    autoCompleteTextViewKota.isEnabled = false
                    autoCompleteTextViewKecamatan.isEnabled = false
                } else {
                    autoCompleteTextViewProvinsi.text.clear()
                    autoCompleteTextViewKota.text.clear()
                    autoCompleteTextViewKecamatan.text.clear()

                    autoCompleteTextViewProvinsi.isEnabled = true
                    autoCompleteTextViewKota.isEnabled = true
                    autoCompleteTextViewKecamatan.isEnabled = true
                }
            }

            radioGroup.setOnCheckedChangeListener { radioGroup, i ->
                when(i) {
                    R.id.radioButtonHalamanTerasRumah -> {
                        locationLand = 1

                        textInputLayoutNoSertifikatLahan.visibility = View.GONE
                        editTextNoSertifikatLahan.text.clear()
                    }
                    R.id.radioButtonLahanBerbedaDariRumah -> {
                        locationLand = 2

                        textInputLayoutNoSertifikatLahan.visibility = View.VISIBLE
                        editTextNoSertifikatLahan.text.clear()
                    }
                }
            }

            materialButtonLanjut.setOnClickListener {
                val address = binding.editTextAlamatLahan.text.trim().toString() + "," + districtText + "," + cityText + "," + provinceText + ",Indonesia"
                val longLat = Utils.getLocationFromAddress(this@UpdateRentLandActivity, address.toString())

                val data = UpdateRentLandModel(
                    idLand = landData?.idLand.toString(),
                    nameLand = binding.editTextNamaLahan.text.toString(),
                    address = binding.editTextAlamatLahan.text.toString(),
                    province = idProvince.toString(),
                    city = idCity.toString(),
                    district = idDistrict.toString(),
                    locationLand = locationLand.toString(),
                    noCertificateLand = binding.editTextNoSertifikatLahan.text.toString(),
                    ownNameLand = binding.editTextNamaPemilikKtp.text.toString(),
                    ownKtp = binding.editTextNoKtp.text.toString(),
                    ownEmail = JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).emailUser.toString(),
                    ownTelp = JWTUtils.decoded(Prefs(this@UpdateRentLandActivity).jwt.toString()).telpUser.toString(),
                    latitude = longLat?.latitude.toString(),
                    longitude = longLat?.longitude.toString(),
                    rating = "0",
                    widthLand = landData?.widthLand.toString(),
                    lengthLand = landData?.lengthLand.toString(),
                    price = landData?.priceLand.toString(),
                    desc = "",
                    facility = landData?.facilityLand.toString(),
                    rule = rule,
                )

                val intent = Intent(this@UpdateRentLandActivity, DetailUpdateRentLandActivity::class.java)
                intent.putExtra(KeyIntent.KEY_DATA_RENT_LAND, data)
                startActivity(intent)
            }
        }
    }

    private fun observeData() {
        viewModel.getProvince(this)
        viewModel.getCity(
            this@UpdateRentLandActivity,
            landData?.provinceLand.toString().toInt()
        )
        viewModel.getDistrict(
            this@UpdateRentLandActivity,
            landData?.cityLand.toString().toInt()
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
                val dataNP = provinces.find { it.idProvince == landData?.provinceLand.toString().toInt() }?.nameProvince.toString()
                binding.autoCompleteTextViewProvinsi.setHint(dataNP)
                idProvince = landData?.provinceLand.toString().toInt()
                provinceText = dataNP
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
                val dataNC = cities.find { it.idCity == landData?.cityLand.toString().toInt() }?.nameCity.toString()
                binding.autoCompleteTextViewKota.setHint(dataNC)
                idCity = landData?.cityLand.toString().toInt()
                cityText = dataNC
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
                val dataND = districts.find { it.idDistrict == landData?.districtLand.toString().toInt() }?.nameDistrict.toString()
                binding.autoCompleteTextViewKecamatan.setHint(dataND)
                idDistrict = landData?.districtLand.toString().toInt()
                districtText = dataND
                isFisrtLoadD = false

                viewModel.getDetailLand(this, landData?.idLand.toString())
            }
        }

        viewModel.land.observe(this) {
            if (it != null) {
                binding.editTextNamaPemilikKtp.setText(it.ownNameLand.toString())
                binding.editTextAlamatLahan.setText(it.addressLand.toString())
                rule = it.ruleLand.toString()
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
}