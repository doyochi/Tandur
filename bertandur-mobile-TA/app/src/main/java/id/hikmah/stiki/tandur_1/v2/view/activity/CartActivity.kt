package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.hikmah.stiki.tandur_1.databinding.ActivityCartBinding
import id.hikmah.stiki.tandur_1.v2.adapter.CartAdapter
import id.hikmah.stiki.tandur_1.v2.database.entity.ProductEntity
import id.hikmah.stiki.tandur_1.v2.model.HistoryRentData
import id.hikmah.stiki.tandur_1.v2.model.ProductData
import id.hikmah.stiki.tandur_1.v2.model.ProductModel
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.viewmodel.CartViewModel
import java.io.Serializable

class CartActivity : AppCompatActivity() {
    private val listenerHapus = object : ItemClickListener<ProductEntity> {
        override fun onClickItem(item: ProductEntity) {
            removeFromCart(item.idProduct)
        }
    }

    private val listenerBeli = object : ItemClickListener<ProductEntity> {
        override fun onClickItem(item: ProductEntity) {
            val list = mutableListOf<String>()
            list.add(item.photoProduct.toString())
            val data = ProductData(
                item.idProduct,
                "0",
                list,
                item.nameProduct,
                item.priceProduct,
                item.ratingProduct,
                item.addressUser,
                item.nameUser,
                item.imgUser,
                item.telpUser,
                item.stockProduct,
                item.jmlTerjual,
                item.conditionProduct,
                item.namePcat,
                item.descProduct,
                item.noteProduct,
                "0"
            )

            val intent = Intent(this@CartActivity, ConfirmOrderProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_PRODUCT, data)
            startActivity(intent)
        }
    }

    private lateinit var binding: ActivityCartBinding
    private lateinit var viewModel: CartViewModel
    private var cartAdapter = CartAdapter(listenerHapus, listenerBeli)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CartViewModel::class.java]

        binding.recyclerViewKeranjang.apply {
            layoutManager = LinearLayoutManager(this@CartActivity)
            adapter = cartAdapter
        }

        binding.materialButtonBeliSemua.setOnClickListener {
            val intent = Intent(this@CartActivity, ConfirmOrderProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_LIST_PRODUCT, cartAdapter.convertToProductDataList() as Serializable)
            startActivity(intent)
        }

        getCart()
    }

    private fun getCart() {
        viewModel.repository.getProduct().observe(this) {
            cartAdapter.list = it.toMutableList()
            cartAdapter.notifyDataSetChanged()

            if (it.isNullOrEmpty()) {
                binding.textViewTidakAdaData.visibility = View.VISIBLE
                binding.materialButtonBeliSemua.visibility = View.GONE
            } else {
                binding.textViewTidakAdaData.visibility = View.GONE
                binding.materialButtonBeliSemua.visibility = View.VISIBLE
            }
        }
    }

    private fun removeFromCart(idProduct: String) {
        viewModel.removeProductFromCart(idProduct)
    }
}