package id.hikmah.stiki.tandur_1.v2.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.hikmah.stiki.tandur_1.databinding.ActivityListReviewBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ReviewLandAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ReviewProductAdapter
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.viewmodel.DetailLandViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.ListReviewViewModel

class ListReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListReviewBinding
    private lateinit var viewModel: ListReviewViewModel
    private var reviewProductAdapter = ReviewProductAdapter()
    private var reviewLandAdapter = ReviewLandAdapter()

    private lateinit var idProduct: String
    private lateinit var idLand: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idProduct = intent.getStringExtra(KeyIntent.KEY_ID_PRODUCT).toString()
        idLand = intent.getStringExtra(KeyIntent.KEY_ID_LAND).toString()

        viewModel = ViewModelProvider(this).get(ListReviewViewModel::class.java)

        binding.recyclerViewReview.apply {
            layoutManager = LinearLayoutManager(this@ListReviewActivity)
            adapter = if (idProduct.isNullOrEmpty()) {
                reviewLandAdapter
            } else {
                reviewProductAdapter
            }
        }

        if (idProduct.isNullOrEmpty()) {
            viewModel.getReviewLand(this, idLand)
        } else {
            viewModel.getReviewProduct(this, idProduct)
        }

        viewModel.landsReview.observe(this) {
            reviewLandAdapter.listReview = it
            reviewLandAdapter.notifyDataSetChanged()
        }

        viewModel.reviewProduct.observe(this) {
            reviewProductAdapter.listReview = it
            reviewProductAdapter.notifyDataSetChanged()
        }
    }
}