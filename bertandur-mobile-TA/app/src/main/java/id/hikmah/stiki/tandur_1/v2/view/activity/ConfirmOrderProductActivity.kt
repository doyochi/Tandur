package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import id.hikmah.stiki.tandur_1.databinding.ActivityConfirmOrderProductBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ProductConfirmAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ProductConfirmSubTotalAdapter
import id.hikmah.stiki.tandur_1.v2.database.entity.ProductEntity
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.model.PassDataProductConfirmToPaymentModel
import id.hikmah.stiki.tandur_1.v2.model.ProductData
import id.hikmah.stiki.tandur_1.v2.model.RequestProductData
import id.hikmah.stiki.tandur_1.v2.util.*

class ConfirmOrderProductActivity : AppCompatActivity() {
    private val listenerQty = object : ItemClickListener<Boolean> {
        override fun onClickItem(item: Boolean) {
            calculate()
        }
    }

    private lateinit var binding: ActivityConfirmOrderProductBinding
    private var productConfirmAdapter = ProductConfirmAdapter(listenerQty)
    private var productConfirmSubTotalAdapter = ProductConfirmSubTotalAdapter()
    private var productData: ProductData? = null
    private var listProductData: ArrayList<ProductData>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmOrderProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productData = intent.getParcelableExtra(KeyIntent.KEY_PRODUCT)
        listProductData = intent.getSerializableExtra(KeyIntent.KEY_LIST_PRODUCT) as ArrayList<ProductData>?

        binding.recyclerViewProduct.apply {
            adapter = productConfirmAdapter
            layoutManager = LinearLayoutManager(this@ConfirmOrderProductActivity)
        }

        binding.recyclerViewSubTotal.apply {
            adapter = productConfirmSubTotalAdapter
            layoutManager = LinearLayoutManager(this@ConfirmOrderProductActivity)
        }

        if (productData != null) {
            val arrayData = ArrayList<ProductData>()
            val arrayRequestData = ArrayList<RequestProductData>()
            arrayData.add(productData!!)
            arrayRequestData.add(RequestProductData(productData?.idProduct, 1, (productData?.priceProduct.toString().toInt() * 1)))
            productConfirmAdapter.list = arrayData.toList()
            productConfirmAdapter.listRequest = arrayRequestData.toList()
            productConfirmAdapter.notifyDataSetChanged()
            calculate()
        } else {
            val arrayRequestData = ArrayList<RequestProductData>()
            listProductData?.forEach {
                arrayRequestData.add(RequestProductData(it.idProduct, 1, (it.priceProduct.toString().toInt() * 1)))
            }
            productConfirmAdapter.list = listProductData!!
            productConfirmAdapter.listRequest = arrayRequestData.toList()
            productConfirmAdapter.notifyDataSetChanged()
            calculate()
        }

        binding.textViewNamaUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).nameUser.toString()
        binding.textViewEmailUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).emailUser.toString()
        binding.textViewTeleponUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).telpUser.toString()
        binding.textViewAlamatUser.text = JWTUtils.decoded(Prefs(this).jwt.toString()).addressUser.toString()

        binding.textViewLanjutkan.setOnClickListener {
            val data = PassDataProductConfirmToPaymentModel(
                productConfirmSubTotalAdapter.list,
                productConfirmSubTotalAdapter.listRequest,
                35000,
                (productConfirmSubTotalAdapter.sumTotalPrice() + 35000)
            )

            val intent = Intent(this, PaymentProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_PASS_DATA, data)
            startActivity(intent)
        }
    }

    fun calculate() {
        productConfirmSubTotalAdapter.list = productConfirmAdapter.list
        productConfirmSubTotalAdapter.listRequest = productConfirmAdapter.listRequest
        productConfirmSubTotalAdapter.notifyDataSetChanged()
        val ongkosKirim = 35000
        binding.textViewOngkosKirim.text = Utils.changePrice(ongkosKirim.toString())
        binding.textViewTotal.text = Utils.changePrice((productConfirmSubTotalAdapter.sumTotalPrice() + ongkosKirim).toString())
    }
}