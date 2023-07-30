package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityPaymentLandBinding
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.model.PaymentRentLandModel
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.PaymentLandViewModel

class PaymentLandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentLandBinding
    private lateinit var viewModel: PaymentLandViewModel
    private lateinit var dialog : ProgressDialog
    private lateinit var paymentLand: PaymentRentLandModel
    private lateinit var land: LandData
    private var paymentMethod = "BCA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentLandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentLand = intent.getParcelableExtra(KeyIntent.KEY_DATA_PAYMENT_RENT_LAND)!!
        land = intent.getParcelableExtra(KeyIntent.KEY_DATA_DETAIL_LAND)!!

        viewModel = ViewModelProvider(this).get(PaymentLandViewModel::class.java)
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

        val startSplitDate = paymentLand.startDate.split("-")
        val endSplitDate = paymentLand.endDate.split("-")
        binding.textViewMulaiPada.text = "${startSplitDate.get(2)} ${Utils.getMonthName(startSplitDate.get(1).toInt())} ${startSplitDate.get(0)}"
        binding.textViewSelesaiPada.text = "${endSplitDate.get(2)} ${Utils.getMonthName(endSplitDate.get(1).toInt())} ${endSplitDate.get(0)}"

        binding.textViewDetailDurasiLahan.text = "${paymentLand.durationRent} Bulan - Lahan ${land.lengthLand}x${land.widthLand} m\n${land.nameLand}"
        binding.textViewDetailHargaLahan.text = Utils.changePrice(paymentLand.totalPayment)
        binding.textViewTotalBiayaSewa.text = Utils.changePrice(paymentLand.totalPayment)

        binding.cardViewMetodePembayaran.setOnClickListener {
            val intent = Intent(this, PaymentMethodActivity::class.java)
            startActivityForResult(intent, KeyIntent.KEY_PAYMENT_METHOD_CODE)
        }

        binding.materialButtonPesanSekarang.setOnClickListener {
            paymentLand.paymentMethod = paymentMethod

            viewModel.rentLahan(
                this,
                paymentLand
            )
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

        viewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                showAlertDialog(it)
            }
        }

        viewModel.paymentUrl.observe(this) {
            if (!it.toString().isNullOrEmpty()) {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra(KeyIntent.KEY_URL_PAYMENT, it)
//                intent.putExtra(KeyIntent.KEY_FROM_ORDER, true)
                startActivity(intent)
            }
        }
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KeyIntent.KEY_PAYMENT_METHOD_CODE) {
            val bank = data?.getStringExtra(KeyIntent.KEY_PAYMENT_METHOD)
            paymentMethod = bank.toString()
            when(bank) {
                "BCA" -> {
                    binding.textViewBank.text = "BCA"
                    binding.imageViewBank.setImageResource(R.drawable.ic_bca)
                }
                "Mandiri" -> {
                    binding.textViewBank.text = "Mandiri"
                    binding.imageViewBank.setImageResource(R.drawable.ic_mandiri)
                }
                "BNI" -> {
                    binding.textViewBank.text = "BNI"
                    binding.imageViewBank.setImageResource(R.drawable.ic_bni)
                }
                "BRI" -> {
                    binding.textViewBank.text = "BRI"
                    binding.imageViewBank.setImageResource(R.drawable.ic_bri)
                }
            }
        }
    }
}