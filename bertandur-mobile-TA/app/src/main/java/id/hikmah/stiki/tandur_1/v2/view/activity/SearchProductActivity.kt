package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivitySearchProductBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ProductGridAdapter
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.model.ProductData
import id.hikmah.stiki.tandur_1.v2.util.GridItemDecoration
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.PixelHelper
import id.hikmah.stiki.tandur_1.v2.viewmodel.SearchProductViewModel

class SearchProductActivity : AppCompatActivity() {
    private val listenerProduct = object : ItemClickListener<ListProductData> {
        override fun onClickItem(item: ListProductData) {
            val intent = Intent(this@SearchProductActivity, DetailProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_PRODUCT, item.idProduct.toString())
            startActivity(intent)
        }
    }

    private lateinit var binding: ActivitySearchProductBinding
    private lateinit var viewModel: SearchProductViewModel
    private var search: String? = null
    private var category: Int? = null
    private var productType: Int? = null
    private var productGridAdapter = ProductGridAdapter(listenerProduct)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        search = intent.getStringExtra(KeyIntent.KEY_SEARCH)
        category = intent.getIntExtra(KeyIntent.KEY_CATEGORY, 0)
        productType = intent.getIntExtra(KeyIntent.KEY_PRODUCT_TYPE, 0)

        viewModel = ViewModelProvider(this).get(SearchProductViewModel::class.java)

        binding.recyclerViewProduk.apply {
            adapter = productGridAdapter
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
        }

        viewModel.getProduct(this, search, 1, productType, category)
        viewModel.products.observe(this) {
//            var data = ArrayList<ProductData>()
//            for (i in 1..4) {
//                data.add(it[0])
//            }
//            productGridAdapter.list = data.toMutableList() ?: emptyList()
            productGridAdapter.list = it.toMutableList() ?: emptyList()
            productGridAdapter.notifyDataSetChanged()
        }

        binding.cardViewRelevansi.setOnClickListener {
            binding.cardViewRelevansi.setCardBackgroundColor(resources.getColor(R.color.green_theme1))
            binding.cardViewTerbaru.setCardBackgroundColor(resources.getColor(R.color.bg_gray))
            binding.cardViewTerlaris.setCardBackgroundColor(resources.getColor(R.color.bg_gray))
            binding.textViewRelevansi.setTextColor(resources.getColor(R.color.white))
            binding.textViewTerbaru.setTextColor(resources.getColor(R.color.text2))
            binding.textViewTerlaris.setTextColor(resources.getColor(R.color.text2))

            viewModel.getProduct(this, search, 1, productType, category)
        }

        binding.cardViewTerbaru.setOnClickListener {
            binding.cardViewRelevansi.setCardBackgroundColor(resources.getColor(R.color.bg_gray))
            binding.cardViewTerbaru.setCardBackgroundColor(resources.getColor(R.color.green_theme1))
            binding.cardViewTerlaris.setCardBackgroundColor(resources.getColor(R.color.bg_gray))
            binding.textViewRelevansi.setTextColor(resources.getColor(R.color.text2))
            binding.textViewTerbaru.setTextColor(resources.getColor(R.color.white))
            binding.textViewTerlaris.setTextColor(resources.getColor(R.color.text2))

            viewModel.getProduct(this, search, 2, productType, category)
        }

        binding.cardViewTerlaris.setOnClickListener {
            binding.cardViewRelevansi.setCardBackgroundColor(resources.getColor(R.color.bg_gray))
            binding.cardViewTerbaru.setCardBackgroundColor(resources.getColor(R.color.bg_gray))
            binding.cardViewTerlaris.setCardBackgroundColor(resources.getColor(R.color.green_theme1))
            binding.textViewRelevansi.setTextColor(resources.getColor(R.color.text2))
            binding.textViewTerbaru.setTextColor(resources.getColor(R.color.text2))
            binding.textViewTerlaris.setTextColor(resources.getColor(R.color.white))

            viewModel.getProduct(this, search, 3, productType, category)
        }

        binding.imageViewCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }
}