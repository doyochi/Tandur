package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityDetailOrderLandBinding
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.util.Utils
import id.hikmah.stiki.tandur_1.v2.view.fragment.NotificationFragment
import id.hikmah.stiki.tandur_1.v2.view.fragment.ReviewFragment
import id.hikmah.stiki.tandur_1.v2.viewmodel.DetailRentLandViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.PaymentLandViewModel

class DetailOrderLandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailOrderLandBinding
    private lateinit var viewModel: DetailRentLandViewModel
    private lateinit var dialog : ProgressDialog
    private lateinit var idRent: String
    private lateinit var idLand: String
    private lateinit var urlPayment: String
    private var fromOrder: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderLandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idRent = intent.getStringExtra(KeyIntent.KEY_ID_RENT).toString()
        fromOrder = intent.getBooleanExtra(KeyIntent.KEY_FROM_ORDER, false)

        dialog = ProgressDialog(this)
        viewModel = ViewModelProvider(this).get(DetailRentLandViewModel::class.java)

        viewModel.getDetailRent(this, idRent)
        viewModel.rents.observe(this) {
            val data = it

            if (!data.PAYMENT_DETAIL.isNullOrEmpty()) {
                binding.textViewStatusPembayaran.text =  if (data.PAYMENT_DETAIL?.first()?.status.equals("PENDING",true)) {
                    "Menunggu Pembayaran"
                } else if (data.PAYMENT_DETAIL?.first()?.status.equals("FAILURE",true) || data.PAYMENT_DETAIL?.first()?.status.equals("EXPIRE",true)) {
                    "Gagal Dibayar"
                } else {
                    "Sudah Dibayar"
                }

                if (data.PAYMENT_DETAIL?.first()?.status.equals("PENDING",true)) {
                    binding.cardViewBayarSekarang.visibility = View.VISIBLE
                } else {
                    binding.cardViewBayarSekarang.visibility = View.GONE
                }

                if (data.PAYMENT_DETAIL?.first()?.status.equals("SETTLEMENT",true)) {
                    if (it.STATUS_REVIEW_LAND.equals("1")) {
                        binding.cardViewReview.visibility = View.GONE
                    } else {
                        binding.cardViewReview.visibility = View.VISIBLE
                    }
                } else {
                    binding.cardViewReview.visibility = View.GONE
                }

                if (!data.PAYMENT_DETAIL?.first()?.va_number.isNullOrEmpty()) {
                    binding.linearLayoutVirtualNumber.visibility = View.VISIBLE
                    binding.textViewVirtualNumber.text = data.PAYMENT_DETAIL?.first()?.va_number.toString()
                    binding.textViewBank.text = "${data.PAYMENT_DETAIL?.first()?.payment_type.toString().uppercase().replace("_"," ")} (${data.PAYMENT_DETAIL?.first()?.bank_code.toString().uppercase()})"
                } else {
                    binding.textViewBank.text = data.PAYMENT_DETAIL?.first()?.payment_type.toString().uppercase().replace("_"," ")
                }

                urlPayment = data.PAYMENT_URL.toString()
            }

            idLand = data.ID_LAND.toString()

//            when(data.methodPayRent) {
//                "BCA" -> {
//                    binding.textViewBank.text = "BCA"
//                    binding.imageViewBank.setImageResource(R.drawable.ic_bca)
//                }
//                "Mandiri" -> {
//                    binding.textViewBank.text = "Mandiri"
//                    binding.imageViewBank.setImageResource(R.drawable.ic_mandiri)
//                }
//                "BNI" -> {
//                    binding.textViewBank.text = "BNI"
//                    binding.imageViewBank.setImageResource(R.drawable.ic_bni)
//                }
//                "BRI" -> {
//                    binding.textViewBank.text = "BRI"
//                    binding.imageViewBank.setImageResource(R.drawable.ic_bri)
//                }
//            }

            binding.textViewJumlahTagihan.text = Utils.changePrice(data.TOTPAYMENT_RENT.toString())
            binding.textViewNomorTagihan.text = data.ID_RENT.toString()

            Glide.with(this)
                .load(data.URLGALLERY_LAND?.first())
                .placeholder(R.drawable.item_default)
                .error(R.drawable.item_default)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.imageView)

            binding.textViewJudul.text = data.NAME_LAND
            binding.textViewDeskripsi.text = "Luas ${data.LENGTH_LAND}x${data.WIDTH_LAND} meter\nFasilitas: ${data.FACILITY_LAND.toString().replace(";",", ")}"
            binding.textViewRating.text = data.RATING_LAND.toString()
            binding.textViewLokasi.text = data.NAME_DISTRICT.toString()

            val startSplitDate = data.STARTDATE_RENT?.split("-")
            val endSplitDate = data.ENDDATE_RENT?.split("-")
            binding.textViewMulaiPada.text = "${startSplitDate?.get(2)} ${Utils.getMonthName(startSplitDate?.get(1).toString().toInt())} ${startSplitDate?.get(0)}"
            binding.textViewSelesaiPada.text = "${endSplitDate?.get(2)} ${Utils.getMonthName(endSplitDate?.get(1).toString().toInt())} ${endSplitDate?.get(0)}"
        }

        binding.materialButtonBayarSekarang.setOnClickListener {
//            val intent = Intent(this, UploadEvidenceRentActivity::class.java)
//            intent.putExtra(KeyIntent.KEY_ID_RENT, idRent)
//            startActivityForResult(intent, KeyIntent.KEY_UPLOAD_EVIDENCE)
            if (!urlPayment.isNullOrEmpty()) {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra(KeyIntent.KEY_URL_PAYMENT, urlPayment)
//                intent.putExtra(KeyIntent.KEY_FROM_ORDER, true)
                startActivity(intent)
            }
        }

        binding.materialButtonReview.setOnClickListener {
            val sheet = ReviewFragment(isLandType = true, id = idLand, idRentPurchase = idRent)
            sheet.show(supportFragmentManager, "NotificationFragment")
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KeyIntent.KEY_UPLOAD_EVIDENCE) {
            val recIdRent = data?.getStringExtra(KeyIntent.KEY_ID_RENT)
            viewModel.getDetailRent(this, recIdRent.toString())
        }
    }

    override fun onBackPressed() {
        if (fromOrder) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        } else {
            super.onBackPressed()
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
            if (fromOrder) {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            } else {
                finish()
            }
        }
        builder.show()
    }
}