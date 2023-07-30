package id.hikmah.stiki.tandur_1.v2.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.FragmentHome2Binding
import id.hikmah.stiki.tandur_1.databinding.FragmentTransactionBinding
import id.hikmah.stiki.tandur_1.v2.adapter.HistoryProductAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.HistoryRentAdapter
import id.hikmah.stiki.tandur_1.v2.model.HistoryProductData
import id.hikmah.stiki.tandur_1.v2.model.HistoryRentData
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.view.activity.DetailLandActivity
import id.hikmah.stiki.tandur_1.v2.view.activity.DetailOrderLandActivity
import id.hikmah.stiki.tandur_1.v2.view.activity.DetailOrderProductActivity
import id.hikmah.stiki.tandur_1.v2.viewmodel.TransactionViewModel

class TransactionFragment : Fragment() {
    private val listener = object : ItemClickListener<HistoryRentData> {
        override fun onClickItem(item: HistoryRentData) {
            val intent = Intent(requireContext(), DetailOrderLandActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_RENT, item.ID_RENT.toString())
            intent.putExtra(KeyIntent.KEY_FROM_ORDER, false)
            startActivity(intent)
        }
    }

    private val listenerProduct = object : ItemClickListener<HistoryProductData> {
        override fun onClickItem(item: HistoryProductData) {
            val intent = Intent(requireContext(), DetailOrderProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_PURCHASE, item.ID_PURCHASE.toString())
            intent.putExtra(KeyIntent.KEY_FROM_ORDER, false)
            startActivity(intent)
        }
    }

    private lateinit var binding: FragmentTransactionBinding
    private lateinit var viewModel: TransactionViewModel
    private var historyAdapter = HistoryRentAdapter(listener)
    private var historyProductAdapter = HistoryProductAdapter(listenerProduct)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        binding.cardViewLahan.setOnClickListener {
            binding.cardViewLahan.setCardBackgroundColor(requireActivity().resources.getColor(R.color.green_theme1))
            binding.cardViewProduk.setCardBackgroundColor(requireActivity().resources.getColor(R.color.bg_gray))

            binding.textViewLahan.setTextColor(requireActivity().resources.getColor(R.color.white))
            binding.textViewProduk.setTextColor(requireActivity().resources.getColor(R.color.black))

            binding.recyclerViewHistory.visibility = View.VISIBLE
            binding.recyclerViewHistoryProduct.visibility = View.GONE
        }

        binding.cardViewProduk.setOnClickListener {
            binding.cardViewLahan.setCardBackgroundColor(requireActivity().resources.getColor(R.color.bg_gray))
            binding.cardViewProduk.setCardBackgroundColor(requireActivity().resources.getColor(R.color.green_theme1))

            binding.textViewLahan.setTextColor(requireActivity().resources.getColor(R.color.black))
            binding.textViewProduk.setTextColor(requireActivity().resources.getColor(R.color.white))

            binding.recyclerViewHistory.visibility = View.GONE
            binding.recyclerViewHistoryProduct.visibility = View.VISIBLE
        }

        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }

        binding.recyclerViewHistoryProduct.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyProductAdapter
        }

        viewModel.getHistoryRent(requireContext())
        viewModel.getHistoryProduct(requireContext())

        viewModel.history.observe(viewLifecycleOwner) {
            historyAdapter.listHistory = it
            historyAdapter.notifyDataSetChanged()
        }

        viewModel.historyProduct.observe(viewLifecycleOwner) {
            historyProductAdapter.listHistory = it
            historyProductAdapter.notifyDataSetChanged()
        }

        return binding.root
    }

}