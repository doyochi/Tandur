package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.models.SlideModel
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityDetailProductBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ProductGridAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ReviewProductAdapter
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.model.ProductData
import id.hikmah.stiki.tandur_1.v2.model.ReviewProductData
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.DetailLandViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.DetailProductViewModel

class DetailProductActivity : AppCompatActivity() {
    private val listenerProduct = object : ItemClickListener<ListProductData> {
        override fun onClickItem(item: ListProductData) {
            val intent = Intent(this@DetailProductActivity, DetailProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_PRODUCT, item.idProduct.toString())
            startActivity(intent)
        }
    }

    private lateinit var binding: ActivityDetailProductBinding
    private lateinit var viewModel: DetailProductViewModel
    private lateinit var dialog : ProgressDialog
    private lateinit var productData: ProductData
    private lateinit var idProduct: String
    private lateinit var phoneUser: String
    private var isInCart = false
    private var productGridAdapter = ProductGridAdapter(listenerProduct)
    private var reviewProductAdapter = ReviewProductAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idProduct = intent.getStringExtra(KeyIntent.KEY_ID_PRODUCT).toString()
        dialog = ProgressDialog(this)

        binding.recyclerViewProduk.apply {
            adapter = productGridAdapter
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
        }

        binding.recyclerViewReview.apply {
            layoutManager = LinearLayoutManager(this@DetailProductActivity)
            adapter = reviewProductAdapter
        }

        binding.materialButtonLihatLainnya.setOnClickListener {
            val intent = Intent(this, ListReviewActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_PRODUCT, idProduct)
            startActivity(intent)
        }

        viewModel = ViewModelProvider(this).get(DetailProductViewModel::class.java)

        getCart()
        getFav()

        viewModel.getDetailProduct(this, idProduct)
        viewModel.getReviewProduct(this, idProduct)
        viewModel.product.observe(this) {
            val data = it.first()
            productData = data

            var imageList = ArrayList<SlideModel>()
            data.photoProduct?.forEach {
                if (it != "-") {
                    imageList.add(SlideModel(it, ""))
                }
            }

            binding.imageSlider.apply {
                setImageList(imageList)
                stopSliding()
                setItemChangeListener(object : ItemChangeListener {
                    override fun onItemChanged(position: Int) {
                        binding.textViewGallery.text = "${position+1}/${imageList.size}"
                    }
                })
            }

            binding.textViewGallery.text = "1/" + imageList.size
            binding.textViewNamaProduk.text = data.nameProduct
            binding.ratingBar.rating = if (data.ratingProduct.toString().isEmpty()) {
                0f
            } else {
                data.ratingProduct!!
            }
            binding.textViewRating.text = data.ratingProduct.toString()
            binding.textViewLokasi.text = data.addressUser.toString()

            Glide.with(this)
                .load(data.imgUser)
                .placeholder(R.drawable.ic_profil)
                .error(R.drawable.ic_profil)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.shapeableImageViewUser)
            binding.textViewNamaUser.text = data.nameUser
            phoneUser = if (data.telpUser.toString()[0] == '0') {
                "62" + data.telpUser.toString().substring(1)
            } else {
                data.telpUser.toString()
            }

            binding.textViewStokTersedia.text = data.stockProduct.toString()
            binding.textViewTerjual.text = data.jmlTerjual.toString()
            binding.textViewKondisi.text = data.conditionProduct.toString()
            binding.textViewKategori.text = data.namePcat.toString()
            binding.textViewDeskripsi.text = data.descProduct.toString()
            binding.textViewCatatan.text = data.noteProduct.toString()

            binding.textViewHarga.text = Utils.changePrice(data.priceProduct.toString())
        }

        viewModel.reviewProduct.observe(this) {
            if (!it.isNullOrEmpty()) {
                binding.linearLayoutReview.visibility = View.GONE
                if (it.size > 2) {
                    binding.materialButtonLihatLainnya.visibility = View.VISIBLE
                    val data = ArrayList<ReviewProductData>()
                    for (i in 0..1) {
                        data.add(it[i])
                    }
                    reviewProductAdapter.listReview = data.toMutableList() ?: emptyList()
                } else {
                    reviewProductAdapter.listReview = it.toMutableList() ?: emptyList()
                }
                reviewProductAdapter.notifyDataSetChanged()
            }
        }

        viewModel.getProduct(this,1)
        viewModel.products.observe(this) {
            if (it.size > 4) {
                val data = ArrayList<ListProductData>()
                for (i in 0..3) {
                    data.add(it[i])
                }
                productGridAdapter.list = data.toMutableList() ?: emptyList()
            } else {
                productGridAdapter.list = it.toMutableList() ?: emptyList()
            }
            productGridAdapter.notifyDataSetChanged()
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

        binding.materialButtonKeranjang.setOnClickListener {
            if (isInCart) {
                showAlertDialog("Produk sudah berada dalam keranjang")
            } else {
                addToCart()
            }
        }

        binding.materialButtonBeliSekarang.setOnClickListener {
            val intent = Intent(this, ConfirmOrderProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_PRODUCT, productData)
            startActivity(intent)
        }

        binding.cardViewFav.setOnClickListener {
            addToFav()
        }

        binding.cardViewUnFav.setOnClickListener {
            removeFromFav()
        }
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun getCart() {
        viewModel.repository.getProduct().observe(this) {
            isInCart = it.find { it.idProduct == idProduct } != null
        }
    }

    private fun getFav() {
        viewModel.repository.getFavorite(idProduct).observe(this) {
            if (it == null) {
                binding.cardViewFav.visibility = View.VISIBLE
                binding.cardViewUnFav.visibility = View.GONE
            } else {
                binding.cardViewFav.visibility = View.GONE
                binding.cardViewUnFav.visibility = View.VISIBLE
            }
        }
    }

    private fun addToCart() {
        viewModel.addToCart(productData)
        showAlertDialog("Produk berhasil ditambahkan ke keranjang")
    }

    private fun addToFav() {
        viewModel.addToFav(productData)
    }

    private fun removeFromFav() {
        viewModel.removeFromFav(idProduct)
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

}