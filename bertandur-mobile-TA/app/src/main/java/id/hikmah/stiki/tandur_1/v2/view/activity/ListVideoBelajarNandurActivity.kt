package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import id.hikmah.stiki.tandur_1.databinding.ActivityListVideoBelajarNandurBinding
import id.hikmah.stiki.tandur_1.v2.adapter.ListVideoAdapter
import id.hikmah.stiki.tandur_1.v2.model.TutorialData
import id.hikmah.stiki.tandur_1.v2.model.TutorialDetailData
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.viewmodel.BelajarNandurViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.ListVideoBelajarNandurViewModel

class ListVideoBelajarNandurActivity : AppCompatActivity() {
    private val listener = object : ItemClickListener<TutorialDetailData> {
        override fun onClickItem(item: TutorialDetailData) {
            val intent = Intent(this@ListVideoBelajarNandurActivity, VideoPlayerActivity::class.java)
            intent.putExtra(KeyIntent.KEY_DETAIL_TUTORIAL_DATA, item)
            startActivity(intent)
        }
    }

    private val binding by lazy {
        ActivityListVideoBelajarNandurBinding.inflate(layoutInflater)
    }
    private var tutorialData: TutorialData? = null
    private lateinit var viewModel: ListVideoBelajarNandurViewModel
    private var listVideoAdapter = ListVideoAdapter(listener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tutorialData = intent.getParcelableExtra(KeyIntent.KEY_TUTORIAL_DATA)

        viewModel = ViewModelProvider(this)[ListVideoBelajarNandurViewModel::class.java]

        if (tutorialData != null) {
            initView()
            observeData()
        }
    }

    private fun initView() {
        binding.apply {
            recyclerViewVideo.apply {
                layoutManager = LinearLayoutManager(this@ListVideoBelajarNandurActivity)
                adapter = listVideoAdapter
            }

            Glide.with(this@ListVideoBelajarNandurActivity)
                .load(tutorialData?.URLIMG_TUTORIAL.toString())
                .into(imageViewBanner)
            textViewJudul.text = tutorialData?.TITLE_TUTORIAL.toString()
            textViewDeskripsi.text = tutorialData?.DESC_TUTORIAL.toString()
        }
    }

    private fun observeData() {
        viewModel.getTutorialVideos(this, tutorialData?.ID_TUTORIAL.toString())
        viewModel.tutorialDetails.observe(this) {
            binding.textViewJumlahVideo.text = "${it.size} Pelajaran"
            listVideoAdapter.list = it
            listVideoAdapter.notifyDataSetChanged()
        }
    }
}