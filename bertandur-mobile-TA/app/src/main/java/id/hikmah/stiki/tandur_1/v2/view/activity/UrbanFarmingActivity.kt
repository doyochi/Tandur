package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.hikmah.stiki.tandur_1.databinding.ActivityUrbanFarmingBinding
import id.hikmah.stiki.tandur_1.v2.adapter.LandAdapter
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.viewmodel.UrbanFarmingViewModel

class UrbanFarmingActivity : AppCompatActivity() {
    private val listenerHubungi = object : ItemClickListener<LandData> {
        override fun onClickItem(item: LandData) {
            val phoneUser = if (item.telpUser.toString()[0] == '0') {
                "62" + item.telpUser.toString().substring(1)
            } else {
                item.telpUser.toString()
            }

            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/${phoneUser}")
            )
            startActivity(intent)
        }
    }

    private val listenerSewa = object : ItemClickListener<LandData> {
        override fun onClickItem(item: LandData) {
            val intent = Intent(this@UrbanFarmingActivity, ConfirmOrderLandActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_LAND, item.idLand)
            intent.putExtra(KeyIntent.KEY_DATA_DETAIL_LAND, item)
            startActivity(intent)
        }
    }

    private val listenerDetail = object : ItemClickListener<LandData> {
        override fun onClickItem(item: LandData) {
            val intent = Intent(this@UrbanFarmingActivity, DetailLandActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_LAND, item.idLand.toString())
            startActivity(intent)
        }
    }

    private lateinit var binding: ActivityUrbanFarmingBinding
    private lateinit var viewModel: UrbanFarmingViewModel
    private var landAdapter = LandAdapter(listenerHubungi, listenerSewa, listenerDetail)
    private var isSearch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUrbanFarmingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(UrbanFarmingViewModel::class.java)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@UrbanFarmingActivity)
            adapter = landAdapter
        }

        viewModel.getLand(this, null)
        viewModel.lands.observe(this) {
//            var data = ArrayList<LandData>()
//            for (i in 1..4) {
//                data.add(it[0])
//            }
//            landAdapter.list = data.toMutableList() ?: emptyList()
            landAdapter.list = it.toMutableList() ?: emptyList()
            landAdapter.notifyDataSetChanged()
        }

        binding.editTextSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                    if (!binding.editTextSearch.text.isNullOrEmpty()) {
                        isSearch = true
                        viewModel.getLand(this@UrbanFarmingActivity, binding.editTextSearch.text.toString())
                    }
                    return true
                }
                return false
            }
        })

        binding.editTextSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    binding.imageViewClear.visibility = View.GONE
                } else {
                    binding.imageViewClear.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.imageViewClear.setOnClickListener {
            binding.editTextSearch.text.clear()
            if (isSearch) {
                viewModel.getLand(this@UrbanFarmingActivity, null)
            }
        }

        binding.materialButtonSewakan.setOnClickListener {
            startActivity(Intent(this, RentLandActivity::class.java))
        }
    }
}