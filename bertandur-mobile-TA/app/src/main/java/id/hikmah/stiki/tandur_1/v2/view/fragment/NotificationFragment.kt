package id.hikmah.stiki.tandur_1.v2.view.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.FragmentNotificationBinding
import id.hikmah.stiki.tandur_1.v2.adapter.NotificationAdapter
import id.hikmah.stiki.tandur_1.v2.model.NotificationData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.State
import id.hikmah.stiki.tandur_1.v2.viewmodel.HomeViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.NotificationViewModel

class NotificationFragment : SuperBottomSheetFragment() {
    private val listener = object : ItemClickListener<NotificationData> {
        override fun onClickItem(item: NotificationData) {
            viewModel.idLog = item.ID_LOG.toString().toInt()
            viewModel.putNotification(requireActivity(), item.ID_LOG.toString().toInt())
        }
    }

    private var binding: FragmentNotificationBinding? = null
    private var notificationAdapter = NotificationAdapter(listener)
    private lateinit var viewModel: NotificationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[NotificationViewModel::class.java]

        binding?.recyclerViewNotification?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = notificationAdapter
        }

        viewModel.getLogNotification(requireActivity())
        viewModel.notifications.observe(requireActivity()) {
            notificationAdapter.list.addAll(it)
            notificationAdapter.notifyDataSetChanged()
        }

        viewModel.stateUpdateNotification.observe(requireActivity()) {
            when(it) {
                State.LOADING -> {

                }
                State.COMPLETE -> {
                    notificationAdapter.deleteByIdLog(viewModel.idLog)
                }
                else -> {

                }
            }
        }

        return binding?.root
    }

    override fun getCornerRadius() = requireActivity().resources.getDimension(R.dimen.demo_sheet_rounded_corner)

    override fun getStatusBarColor() = Color.RED
}