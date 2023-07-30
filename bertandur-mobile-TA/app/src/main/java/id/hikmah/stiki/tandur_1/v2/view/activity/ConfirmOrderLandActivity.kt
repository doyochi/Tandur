package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.models.SlideModel
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityConfirmOrderLandBinding
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.model.PaymentRentLandModel
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.ConfirmOrderLandViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.DetailLandViewModel
import java.util.*

class ConfirmOrderLandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmOrderLandBinding
    private lateinit var viewModel: ConfirmOrderLandViewModel
    private lateinit var dialog : ProgressDialog
    private lateinit var idLand: String
    private lateinit var land: LandData
    private var startDate: String = ""
    private var endDate: String = ""
//    private var lengthLand: String = ""
//    private var widthLand: String = ""
//    private var nameLand: String = ""
//    private var priceLand: Int = 0
    private var totalPriceLand: Int = 0
    private var durasi = 1

    private var mCalendar = Calendar.getInstance()
    private var mCalendarAfter = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmOrderLandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idLand = intent.getStringExtra(KeyIntent.KEY_ID_LAND).toString()
        land = intent.getParcelableExtra(KeyIntent.KEY_DATA_DETAIL_LAND)!!

        viewModel = ViewModelProvider(this).get(ConfirmOrderLandViewModel::class.java)
        dialog = ProgressDialog(this)

        Glide.with(this)
            .load(land.urlGalleryLand?.first())
            .placeholder(R.drawable.item_default)
            .error(R.drawable.item_default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.imageView)

        binding.textViewJudul.text = land.nameLand
        binding.textViewHarga.text = Utils.changePrice(land.priceLand.toString()) + "/Bln"
        binding.textViewDeskripsi.text = "Luas ${land.lengthLand}x${land.widthLand} meter\nFasilitas: ${land.facilityLand.toString().replace(";",", ")}"
        binding.textViewRating.text = land.ratingLand.toString()
        binding.textViewLokasi.text = land.addressLand.toString()

        binding.textViewNamaUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).nameUser.toString()
        binding.textViewEmailUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).emailUser.toString()
        binding.textViewTelpUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).telpUser.toString()
        totalPriceLand = land.priceLand.toString().toInt() * durasi

//        viewModel.getDetailLand(this, idLand)
//        viewModel.lands.observe(this) {
//            val data = it.first()
//
//            nameLand = data.nameLand.toString()
//            lengthLand = data.lengthLand.toString()
//            widthLand = data.widthLand.toString()
//            priceLand = data.priceLand.toString().toInt()
//            totalPriceLand = priceLand * durasi
//
//            Glide.with(this)
//                .load(data.galleryLand?.first())
//                .placeholder(R.drawable.item_default)
//                .error(R.drawable.item_default)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(binding.imageView)
//
//            binding.textViewJudul.text = nameLand
//            binding.textViewHarga.text = Utils.changePrice(priceLand.toString()) + "/Bln"
//            binding.textViewDeskripsi.text = "Luas ${lengthLand}x${widthLand} meter\nFasilitas: ${data.facilityLand.toString().replace(";",", ")}"
//            binding.textViewRating.text = data.ratingLand.toString()
//            binding.textViewLokasi.text = data.addressLand.toString()
//        }
//
//        viewModel.state.observe(this) {
//            when (it) {
//                State.LOADING -> {
//                    showProgressDialog()
//                }
//                State.COMPLETE -> {
//                    dialog.dismiss()
//                }
//                State.ERROR -> {
//                    dialog.dismiss()
//                }
//            }
//        }

        binding.cardViewMinus.setOnClickListener {
            if (durasi > 1) {
                if (startDate.isNotEmpty()) {
                    durasi--
                    binding.textViewDurasi.text = durasi.toString()
                    mCalendarAfter.add(Calendar.MONTH, -1)
                    endDate = "${mCalendarAfter[Calendar.YEAR]}-${mCalendarAfter[Calendar.MONTH] + 1}-${mCalendarAfter[Calendar.DAY_OF_MONTH]}"
                    binding.textViewSelesaiPada.text = (mCalendarAfter[Calendar.DAY_OF_MONTH].toString() + " " + Utils.getMonthName((mCalendarAfter[Calendar.MONTH] + 1)) + " " + mCalendarAfter[Calendar.YEAR])
                    totalPriceLand = land.priceLand.toString().toInt() * durasi
                    binding.textViewDetailHargaLahan.text = Utils.changePrice(totalPriceLand.toString())
                    binding.textViewTotalBiayaSewa.text = Utils.changePrice(totalPriceLand.toString())
                }
            }
        }

        binding.cardViewPlus.setOnClickListener {
            if (durasi < 6) {
                if (startDate.isNotEmpty()) {
                    durasi++
                    binding.textViewDurasi.text = durasi.toString()
                    mCalendarAfter.add(Calendar.MONTH, 1)
                    endDate = "${mCalendarAfter[Calendar.YEAR]}-${mCalendarAfter[Calendar.MONTH] + 1}-${mCalendarAfter[Calendar.DAY_OF_MONTH]}"
                    binding.textViewSelesaiPada.text = (mCalendarAfter[Calendar.DAY_OF_MONTH].toString() + " " + Utils.getMonthName((mCalendarAfter[Calendar.MONTH] + 1)) + " " + mCalendarAfter[Calendar.YEAR])
                    totalPriceLand = land.priceLand.toString().toInt() * durasi
                    binding.textViewDetailHargaLahan.text = Utils.changePrice(totalPriceLand.toString())
                    binding.textViewTotalBiayaSewa.text = Utils.changePrice(totalPriceLand.toString())
                }
            }
        }

        binding.linearLayoutMulaiPada.setOnClickListener {
            val mDialog = DatePickerDialog(this, { _, mYear, mMonth, mDay ->
                durasi = 1
                binding.textViewDurasi.text = durasi.toString()
                binding.textViewDetailDurasiLahan.text = "${durasi} Bulan - Lahan ${land.lengthLand}x${land.widthLand} m\n${land.nameLand}"
                binding.textViewDetailHargaLahan.text = Utils.changePrice((land.priceLand.toString().toInt() * durasi).toString())
                binding.textViewTotalBiayaSewa.text = Utils.changePrice((land.priceLand.toString().toInt() * durasi).toString())

                mCalendar[Calendar.YEAR] = mYear
                mCalendar[Calendar.MONTH] = mMonth
                mCalendar[Calendar.DAY_OF_MONTH] = mDay

                mCalendarAfter[Calendar.YEAR] = mYear
                mCalendarAfter[Calendar.MONTH] = mMonth
                mCalendarAfter[Calendar.DAY_OF_MONTH] = mDay
                mCalendarAfter.add(Calendar.MONTH, 1)

                startDate = "${mYear}-${mMonth + 1}-${mDay}"
                endDate = "${mCalendarAfter[Calendar.YEAR]}-${mCalendarAfter[Calendar.MONTH] + 1}-${mCalendarAfter[Calendar.DAY_OF_MONTH]}"

                binding.textViewMulaiPada.text = (mDay.toString() + " " + Utils.getMonthName((mMonth + 1)) + " " + mYear)
                binding.textViewSelesaiPada.text = (mCalendarAfter[Calendar.DAY_OF_MONTH].toString() + " " + Utils.getMonthName((mCalendarAfter[Calendar.MONTH] + 1)) + " " + mCalendarAfter[Calendar.YEAR])
            }, mCalendar[Calendar.YEAR], mCalendar[Calendar.MONTH], mCalendar[Calendar.DAY_OF_MONTH])
            mDialog.datePicker.minDate = mCalendar.timeInMillis

            mDialog.show()
        }

        binding.materialButtonLanjutkan.setOnClickListener {
            if (startDate.isNotEmpty()) {
                val data = PaymentRentLandModel(
                    idLand,
                    durasi.toString(),
                    startDate,
                    endDate,
                    "",
                    totalPriceLand.toString()
                )

                val intent = Intent(this, PaymentLandActivity::class.java)
                intent.putExtra(KeyIntent.KEY_DATA_PAYMENT_RENT_LAND, data)
                intent.putExtra(KeyIntent.KEY_DATA_DETAIL_LAND, land)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Mohon pilih tanggal mulai sewa terlebih dahulu", Toast.LENGTH_SHORT).show()
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