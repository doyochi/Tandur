package id.hikmah.stiki.tandur_1.v2.view.fragment

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.FragmentNotificationBinding
import id.hikmah.stiki.tandur_1.databinding.FragmentReviewBinding
import id.hikmah.stiki.tandur_1.v2.adapter.NotificationAdapter
import id.hikmah.stiki.tandur_1.v2.model.NotificationData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.viewmodel.HomeViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.NotificationViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.ReviewViewModel

class ReviewFragment(private var isLandType: Boolean, private var id: String, private var idRentPurchase: String) : SuperBottomSheetFragment() {

    private var binding: FragmentReviewBinding? = null
    private lateinit var viewModel: ReviewViewModel
    private lateinit var dialog : ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ReviewViewModel::class.java]

        dialog = ProgressDialog(requireActivity())

        binding?.materialButtonKirim?.setOnClickListener {
            if (validate()) {
                when(isLandType) {
                    true -> {
                        viewModel.postReviewLand(
                            requireActivity(),
                            id,
                            idRentPurchase,
                            binding?.ratingBar?.rating.toString(),
                            binding?.editTextReviewTitle?.text.toString(),
                            binding?.editTextReviewContent?.text.toString(),
                        )
                    }
                    false -> {
                        viewModel.postReviewProduct(
                            requireActivity(),
                            id,
                            idRentPurchase,
                            binding?.ratingBar?.rating.toString(),
                            binding?.editTextReviewTitle?.text.toString(),
                            binding?.editTextReviewContent?.text.toString(),
                        )
                    }
                }
            }
        }
        
        viewModel.state.observe(this) {
            when(it) {
                State.LOADING -> {
                    showProgressDialog()
                }
                State.COMPLETE -> {
                    dialog.dismiss()
                    this.dismiss()
                    Toast.makeText(requireActivity(), "Ulasan berhasil dikirimkan", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    dialog.dismiss()
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return binding?.root
    }
    
    private fun validate(): Boolean {
        var check = true
        
        if (binding?.ratingBar?.rating == 0f) {
            check = false
            Toast.makeText(requireActivity(), "Mohon isi rating terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
        
        if (binding?.editTextReviewTitle?.text.toString().isNullOrEmpty()) {
            check = false
            binding?.editTextReviewTitle?.error = "Mohon isi judul ulasan anda"
        }
        
        if (binding?.editTextReviewContent?.text.toString().isNullOrEmpty()) {
            check = false
            binding?.editTextReviewContent?.error = "Mohon isi ulasan anda"
        }
        
        return check
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun getPeekHeight() = 1500

    override fun getCornerRadius() = requireActivity().resources.getDimension(R.dimen.demo_sheet_rounded_corner)

    override fun getStatusBarColor() = Color.RED
}