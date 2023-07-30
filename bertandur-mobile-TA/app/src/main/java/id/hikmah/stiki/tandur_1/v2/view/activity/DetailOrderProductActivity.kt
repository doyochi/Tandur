package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityDetailOrderProductBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ProductConfirmSubTotalAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ProductDetailAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ProductDetailSubTotalAdapter
import id.hikmah.stiki.tandur_1.v2.model.NewPurchaseData
import id.hikmah.stiki.tandur_1.v2.model.NewPurchaseDetail
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.util.Utils
import id.hikmah.stiki.tandur_1.v2.view.fragment.ReviewFragment
import id.hikmah.stiki.tandur_1.v2.viewmodel.DetailPurchaseProductViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.DetailRentLandViewModel

class DetailOrderProductActivity : AppCompatActivity() {
    private val listener = object : ItemClickListener<NewPurchaseDetail> {
        override fun onClickItem(item: NewPurchaseDetail) {
            val sheet = ReviewFragment(isLandType = false, id = item.ID_PRODUCT.toString(), idPurchase)
            sheet.show(supportFragmentManager, "NotificationFragment")
        }
    }

    private lateinit var binding: ActivityDetailOrderProductBinding
    private lateinit var viewModel: DetailPurchaseProductViewModel
    private lateinit var dialog : ProgressDialog
    private var productDetailSubTotalAdapter = ProductDetailSubTotalAdapter()
    private var productDetailAdapter = ProductDetailAdapter(listener)
    private lateinit var idPurchase: String
    private lateinit var urlPayemnt: String
    private lateinit var idProduct: String
    private var fromOrder: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrderProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idPurchase = intent.getStringExtra(KeyIntent.KEY_ID_PURCHASE).toString()
        fromOrder = intent.getBooleanExtra(KeyIntent.KEY_FROM_ORDER, false)

        dialog = ProgressDialog(this)
        viewModel = ViewModelProvider(this).get(DetailPurchaseProductViewModel::class.java)

        binding.recyclerViewProduct.apply {
            adapter = productDetailAdapter
            layoutManager = LinearLayoutManager(this@DetailOrderProductActivity)
        }

        binding.recyclerViewSubTotal.apply {
            adapter = productDetailSubTotalAdapter
            layoutManager = LinearLayoutManager(this@DetailOrderProductActivity)
        }

        viewModel.getDetailPurchase(this, idPurchase)
        viewModel.purcase.observe(this) {
            val data = it

            if (!data.PAYMENT_DETAIL.isNullOrEmpty()) {
                binding.textViewStatusPembayaran.text = if (data.PAYMENT_DETAIL.first()?.status.equals("PENDING",true)) {
                    "Menunggu Pembayaran"
                } else if (data.PAYMENT_DETAIL.first()?.status.equals("FAILURE",true) || data.PAYMENT_DETAIL.first()?.status.equals("EXPIRE",true)) {
                    "Gagal Dibayar"
                } else {
                    "Sudah Dibayar"
                }

                if (data.PAYMENT_DETAIL.first()?.status.equals("PENDING",true)) {
                    binding.cardViewBayarSekarang.visibility = View.VISIBLE
                } else {
                    binding.cardViewBayarSekarang.visibility = View.GONE
                }

                productDetailAdapter.isPaid = data.PAYMENT_DETAIL.first()?.status.equals("SETTLEMENT",true)
                productDetailAdapter.isReview = data.STATUS_REVIEW_PRODUCT.equals("1",true)

                if (!data.PAYMENT_DETAIL.first()?.va_number.isNullOrEmpty()) {
                    binding.linearLayoutVirtualNumber.visibility = View.VISIBLE
                    binding.textViewVirtualNumber.text = data.PAYMENT_DETAIL.first()?.va_number.toString()
                    binding.textViewBank.text = "${data.PAYMENT_DETAIL.first()?.payment_type.toString().uppercase().replace("_"," ")} (${data.PAYMENT_DETAIL.first()?.bank_code.toString().uppercase()})"
                } else {
                    binding.textViewBank.text = data.PAYMENT_DETAIL.first()?.payment_type.toString().uppercase().replace("_"," ")
                }

                urlPayemnt = data.PAYMENT_URL.toString()
            }

            idProduct = data.PURCHASE_DETAIL?.first()?.ID_PRODUCT.toString()

//            binding.textViewBank.text = data.PAYMENT_DETAIL?.first()?.payment_type.toString().uppercase().replace("_"," ")

//            when(data.PAYMENT_DETAIL?.first()?.payment_type) {
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

            binding.textViewJumlahTagihan.text = Utils.changePrice(data.TOTPAYMENT_PURCHASE.toString())
            binding.textViewNomorTagihan.text = data.ID_PURCHASE.toString()
            binding.textViewOngkosKirim.text = Utils.changePrice((data.SHIPPING_COST).toString())

            productDetailSubTotalAdapter.list = data.PURCHASE_DETAIL!!
            productDetailSubTotalAdapter.notifyDataSetChanged()
            productDetailAdapter.list = data.PURCHASE_DETAIL
            productDetailAdapter.notifyDataSetChanged()
        }

        binding.materialButtonBayarSekarang.setOnClickListener {
//            val intent = Intent(this, UploadEvidenceProductActivity::class.java)
//            intent.putExtra(KeyIntent.KEY_ID_PURCHASE, idPurchase)
//            startActivityForResult(intent, KeyIntent.KEY_UPLOAD_EVIDENCE)
            if (!urlPayemnt.isNullOrEmpty()) {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra(KeyIntent.KEY_ID_PURCHASE, idPurchase)
                intent.putExtra(KeyIntent.KEY_URL_PAYMENT, urlPayemnt)
                startActivity(intent)
            }
        }

        binding.materialButtonReview.setOnClickListener {
            val sheet = ReviewFragment(isLandType = false, id = idProduct, idPurchase)
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
            val recIdPurchase = data?.getStringExtra(KeyIntent.KEY_ID_PURCHASE)
            viewModel.getDetailPurchase(this, recIdPurchase.toString())
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