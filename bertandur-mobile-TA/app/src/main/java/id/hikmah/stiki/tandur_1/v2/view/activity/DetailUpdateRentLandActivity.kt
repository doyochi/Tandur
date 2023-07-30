package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import id.hikmah.stiki.tandur_1.databinding.ActivityDetailUpdateRentLandBinding
import id.hikmah.stiki.tandur_1.v2.model.UpdateRentLandModel
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.viewmodel.UpdateLandViewModel


class DetailUpdateRentLandActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailUpdateRentLandBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(UpdateLandViewModel::class.java)
    }
    private lateinit var dialog : ProgressDialog
    private var updateRentLandModel: UpdateRentLandModel? = null
    private var facility = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        updateRentLandModel = intent.getParcelableExtra(KeyIntent.KEY_DATA_RENT_LAND)

        initView()
        observeData()
    }

    private fun initView() {
        dialog = ProgressDialog(this)
        binding.apply {
            editTextHargaSewa.setText(updateRentLandModel?.price.toString())
            editTextPanjang.setText(updateRentLandModel?.lengthLand.toString())
            editTextLebar.setText(updateRentLandModel?.widthLand.toString())
            editTextPeraturanLahan.setText(updateRentLandModel?.rule.toString())


            updateRentLandModel?.facility.toString().split(";").forEach {
                if (it.equals("Irigasi",true)) {
                    checkBoxIrigasi.isChecked = true
                    facility.add("Irigasi")
                } else if (it.equals("Listrik",true)) {
                    checkBoxListrik.isChecked = true
                    facility.add("Listrik")
                } else if (it.equals("Peralatan",true)) {
                    checkBoxPeralatan.isChecked = true
                    facility.add("Peralatan")
                } else if (it.equals("Kanopi",true)) {
                    checkBoxKanopi.isChecked = true
                    facility.add("Kanopi")
                }
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

            materialButtonKirim.setOnClickListener {
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

                updateRentLandModel?.let {
                    it.price = editTextHargaSewa.text.toString()
                    it.desc = editTextDeskripsiLahan.text.toString()
                    it.rule = editTextPeraturanLahan.text.toString()
                    it.widthLand = editTextLebar.text.toString()
                    it.lengthLand = editTextPanjang.text.toString()
                    it.facility = strFacility
                }

                viewModel.updateLahan(
                    this@DetailUpdateRentLandActivity,
                    updateRentLandModel!!
                )
            }
        }
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun observeData() {
        viewModel.apply {
            state.observe(this@DetailUpdateRentLandActivity) {
                when (it) {
                    State.LOADING -> {
                        showProgressDialog()
                    }
                    State.COMPLETE -> {
                        dialog.dismiss()
                        Toast.makeText(this@DetailUpdateRentLandActivity, "Data lahan berhasil diubah", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@DetailUpdateRentLandActivity, ManajemenLahanActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                    }
                    State.ERROR -> {
                        dialog.dismiss()
                    }
                }
            }
        }
    }
}