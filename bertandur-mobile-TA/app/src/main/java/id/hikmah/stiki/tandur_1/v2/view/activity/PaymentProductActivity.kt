package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityPaymentProductBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ProductConfirmAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ProductConfirmSubTotalAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ProductPaymentAdapter
import id.hikmah.stiki.tandur_1.v2.model.PassDataProductConfirmToPaymentModel
import id.hikmah.stiki.tandur_1.v2.model.RequestProductModel
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.PaymentProductViewModel

class PaymentProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentProductBinding
    private lateinit var viewModel: PaymentProductViewModel
    private lateinit var dialog : ProgressDialog
    private var productPaymentAdapter = ProductPaymentAdapter()
    private var productConfirmSubTotalAdapter = ProductConfirmSubTotalAdapter()
    private lateinit var passDataProductConfirmToPaymentModel: PassDataProductConfirmToPaymentModel
    private var paymentMethod = "BCA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        passDataProductConfirmToPaymentModel = intent.getParcelableExtra<PassDataProductConfirmToPaymentModel>(KeyIntent.KEY_PASS_DATA)!!

        viewModel = ViewModelProvider(this)[PaymentProductViewModel::class.java]
        dialog = ProgressDialog(this)

        binding.recyclerViewProduct.apply {
            adapter = productPaymentAdapter
            layoutManager = LinearLayoutManager(this@PaymentProductActivity)
        }

        binding.recyclerViewSubTotal.apply {
            adapter = productConfirmSubTotalAdapter
            layoutManager = LinearLayoutManager(this@PaymentProductActivity)
        }

        productPaymentAdapter.list = passDataProductConfirmToPaymentModel.data!!
        productPaymentAdapter.listRequest = passDataProductConfirmToPaymentModel.dataRequest!!
        productConfirmSubTotalAdapter.list = passDataProductConfirmToPaymentModel.data!!
        productConfirmSubTotalAdapter.listRequest = passDataProductConfirmToPaymentModel.dataRequest!!

        binding.textViewNamaUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).nameUser.toString()
        binding.textViewEmailUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).emailUser.toString()
        binding.textViewTeleponUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).telpUser.toString()
        binding.textViewAlamatUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).addressUser.toString()

        binding.textViewOngkosKirim.text = Utils.changePrice(passDataProductConfirmToPaymentModel.ongkir.toString())
        binding.textViewTotal.text = Utils.changePrice(passDataProductConfirmToPaymentModel.total.toString())

        binding.cardViewMetodePembayaran.setOnClickListener {
            val intent = Intent(this, PaymentMethodActivity::class.java)
            startActivityForResult(intent, KeyIntent.KEY_PAYMENT_METHOD_CODE)
        }

        binding.textViewPesanSekarang.setOnClickListener {
            val data = RequestProductModel(
                productConfirmSubTotalAdapter.listRequest,
                "JNE",
                paymentMethod,
                JWTUtils.decoded(Prefs(this).jwt.toString()).nameUser.toString(),
                JWTUtils.decoded(Prefs(this).jwt.toString()).emailUser.toString(),
                JWTUtils.decoded(Prefs(this).jwt.toString()).telpUser.toString(),
                JWTUtils.decoded(Prefs(this).jwt.toString()).addressUser.toString(),
                passDataProductConfirmToPaymentModel.ongkir,
                passDataProductConfirmToPaymentModel.total
            )

            viewModel.purchaseProduct(
                this,
                data
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

        viewModel.idPurchase.observe(this) {
            if (!it.toString().isNullOrEmpty()) {
                val intent = Intent(this, FeedbackOrderProductActivity::class.java)
                intent.putExtra(KeyIntent.KEY_ID_PURCHASE, it)
                intent.putExtra(KeyIntent.KEY_FROM_ORDER, true)
                startActivity(intent)
            }
        }

        viewModel.purchaseData.observe(this) {
            if (it != null) {
                val intent = Intent(this, WebViewActivity::class.java)
                intent.putExtra(KeyIntent.KEY_ID_PURCHASE, it.idPurchase)
                intent.putExtra(KeyIntent.KEY_URL_PAYMENT, it.paymentUrl)
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