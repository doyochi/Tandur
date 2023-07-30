package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityBelajarNandurBinding
import id.hikmah.stiki.tandur_1.v2.adapter.TutorialAdapter
import id.hikmah.stiki.tandur_1.v2.model.TutorialData
import id.hikmah.stiki.tandur_1.v2.util.GridItemDecoration
import id.hikmah.stiki.tandur_1.v2.util.ItemClickListener
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent
import id.hikmah.stiki.tandur_1.v2.util.PixelHelper
import id.hikmah.stiki.tandur_1.v2.viewmodel.BelajarNandurViewModel
import id.hikmah.stiki.tandur_1.v2.viewmodel.HomeViewModel

class BelajarNandurActivity : AppCompatActivity() {
    private val listener = object : ItemClickListener<TutorialData> {
        override fun onClickItem(item: TutorialData) {
            val intent = Intent(this@BelajarNandurActivity, ListVideoBelajarNandurActivity::class.java)
            intent.putExtra(KeyIntent.KEY_TUTORIAL_DATA, item)
            startActivity(intent)
        }
    }
    private val binding by lazy {
        ActivityBelajarNandurBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: BelajarNandurViewModel
    private var tutorialAdapter = TutorialAdapter(listener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[BelajarNandurViewModel::class.java]

        initView()
        observeData()
    }

    private fun initView() {
        binding.apply {
            recyclerViewTutorial.apply {
                adapter = tutorialAdapter
                layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
                itemAnimator = null
                val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
                val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
                addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
            }
        }
    }

    private fun observeData() {
        viewModel.getTutorials(this)
        viewModel.tutorials.observe(this) {
            tutorialAdapter.list = it
            tutorialAdapter.notifyDataSetChanged()
        }
    }
}