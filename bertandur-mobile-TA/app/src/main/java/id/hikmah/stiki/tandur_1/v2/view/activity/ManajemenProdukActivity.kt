package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.hikmah.stiki.tandur_1.databinding.ActivityManajemenProdukBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ProductUserAdapter
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.viewmodel.TandurMarketViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.UrbanFarmingViewModel

class ManajemenProdukActivity : AppCompatActivity() {
    private val listener = object : ItemClickListener<ListProductData> {
        override fun onClickItem(item: ListProductData) {
            val intent = Intent(this@ManajemenProdukActivity, UpdateProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_PRODUCT, item.idProduct)
            intent.putExtra(KeyIntent.KEY_CATEGORY, item.category)
            startActivity(intent)
        }
    }
    private val binding by lazy {
        ActivityManajemenProdukBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(TandurMarketViewModel::class.java)
    }
    private val productUserAdapter = ProductUserAdapter(listener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        observeData()
    }

    private fun initView() {
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ManajemenProdukActivity)
                adapter = productUserAdapter
            }
        }
    }

    private fun observeData() {
        viewModel.getProductUser(this)
        viewModel.products.observe(this) {
            productUserAdapter.list = it.toMutableList()
            productUserAdapter.notifyDataSetChanged()
        }
    }
}