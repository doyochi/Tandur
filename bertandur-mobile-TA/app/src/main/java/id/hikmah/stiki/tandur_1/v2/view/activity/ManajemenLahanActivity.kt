package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.hikmah.stiki.tandur_1.databinding.ActivityManajemenLahanBinding
import id.hikmah.stiki.tandur_1.v2.adapter.LandAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ManajemenLandAdapter
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.JWTUtils
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import id.hikmah.stiki.tandur_1.v2.viewmodel.UrbanFarmingViewModel

class ManajemenLahanActivity : AppCompatActivity() {
    private val listenerDetail = object : ItemClickListener<LandData> {
        override fun onClickItem(item: LandData) {
            val intent = Intent(this@ManajemenLahanActivity, UpdateRentLandActivity::class.java)
            intent.putExtra(KeyIntent.KEY_LAND, item)
            startActivity(intent)
        }
    }

    private val binding by lazy {
        ActivityManajemenLahanBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(UrbanFarmingViewModel::class.java)
    }
    private val user by lazy {
        JWTUtils.decoded(Prefs(this).jwt.toString())
    }
    private var landAdapter = ManajemenLandAdapter(listenerDetail)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        observeData()
    }

    private fun initView() {
        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(this@ManajemenLahanActivity)
                adapter = landAdapter
            }
        }
    }

    private fun observeData() {
        viewModel.getLandUser(this, null)
        viewModel.lands.observe(this) {
            landAdapter.list = it.toMutableList()
            landAdapter.notifyDataSetChanged()
        }
    }
}