package id.hikmah.stiki.tandur_1.v2.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.FragmentFavoriteBinding
import id.hikmah.stiki.tandur_1.databinding.FragmentTransactionBinding
import id.hikmah.stiki.tandur_1.v2.adapter.FavoriteGridAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ProductGridAdapter
import id.hikmah.stiki.tandur_1.v2.database.entity.FavoriteEntity
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.util.GridItemDecoration
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.PixelHelper
import id.hikmah.stiki.tandur_1.v2.view.activity.DetailProductActivity
import id.hikmah.stiki.tandur_1.v2.viewmodel.FavoriteViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.TransactionViewModel

class FavoriteFragment : Fragment() {
    private val listenerProduct = object : ItemClickListener<FavoriteEntity> {
        override fun onClickItem(item: FavoriteEntity) {
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_PRODUCT, item.idProduct.toString())
            startActivity(intent)
        }
    }

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var viewModel: FavoriteViewModel
    private var favoriteGridAdapter = FavoriteGridAdapter(listenerProduct)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        binding.recyclerViewFavorite.apply {
            adapter = favoriteGridAdapter
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
        }

        getFav()

        return binding.root
    }

    private fun getFav() {
        viewModel.repository.getAllFavorite().observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()) {
                binding.textViewTidakAdaData.visibility = View.VISIBLE
            } else {
                binding.textViewTidakAdaData.visibility = View.GONE
            }
            favoriteGridAdapter.list = it.toMutableList()
            favoriteGridAdapter.notifyDataSetChanged()
        }
    }
}