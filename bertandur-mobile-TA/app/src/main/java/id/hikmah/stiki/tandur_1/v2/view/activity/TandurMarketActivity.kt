package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityTandurMarketBinding
import id.hikmah.stiki.tandur_1.v2.adapter.CategoryAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ProductGridAdapter
import id.hikmah.stiki.tandur_1.v2.model.CategoryData
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.model.ProductData
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.TandurMarketViewModel

class TandurMarketActivity : AppCompatActivity() {
    private val listenerCategory = object : ItemClickListener<CategoryData> {
        override fun onClickItem(item: CategoryData) {
            val intent = Intent(this@TandurMarketActivity, SearchProductActivity::class.java)
//            intent.putExtra(KeyIntent.KEY_SEARCH, binding.editTextSearch.text.toString())
            intent.putExtra(KeyIntent.KEY_PRODUCT_TYPE, productType)
            intent.putExtra(KeyIntent.KEY_CATEGORY, item.idPcat?.toInt())
            startActivity(intent)
        }
    }

    private val listenerProduct = object : ItemClickListener<ListProductData> {
        override fun onClickItem(item: ListProductData) {
            val intent = Intent(this@TandurMarketActivity, DetailProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_PRODUCT, item.idProduct.toString())
            startActivity(intent)
        }
    }

    private lateinit var binding: ActivityTandurMarketBinding
    private lateinit var viewModel: TandurMarketViewModel
    private var categoryAdapter = CategoryAdapter(listenerCategory)
    private var productGridAdapter = ProductGridAdapter(listenerProduct)
    private var productType: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTandurMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productType = intent.getIntExtra(KeyIntent.KEY_PRODUCT_TYPE,0)

        viewModel = ViewModelProvider(this).get(TandurMarketViewModel::class.java)

        binding.recyclerViewKategori.apply {
            adapter = categoryAdapter
            layoutManager = LinearLayoutManager(this@TandurMarketActivity, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.recyclerViewProduk.apply {
            adapter = productGridAdapter
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
        }

        binding.editTextSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                    if (!binding.editTextSearch.text.isNullOrEmpty()) {
                        val intent = Intent(this@TandurMarketActivity, SearchProductActivity::class.java)
                        intent.putExtra(KeyIntent.KEY_SEARCH, binding.editTextSearch.text.toString())
                        intent.putExtra(KeyIntent.KEY_PRODUCT_TYPE, productType)
                        startActivity(intent)
                    }
                    return true
                }
                return false
            }
        })

        viewModel.getCategory(this, productType)
        viewModel.categories.observe(this) {
            categoryAdapter.list = it.toMutableList()
            categoryAdapter.notifyDataSetChanged()
        }

        viewModel.getProduct(this, productType)
        viewModel.products.observe(this) {
//            var data = ArrayList<ProductData>()
//            for (i in 1..4) {
//                data.add(it[0])
//            }
//            productGridAdapter.list = data.toMutableList() ?: emptyList()
            productGridAdapter.list = it.toMutableList() ?: emptyList()
            productGridAdapter.notifyDataSetChanged()
        }

        binding.imageViewCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.materialButtonJual.setOnClickListener {
            val intent = Intent(this@TandurMarketActivity, SellProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_PRODUCT_TYPE, productType)
            startActivity(intent)
        }
    }
}