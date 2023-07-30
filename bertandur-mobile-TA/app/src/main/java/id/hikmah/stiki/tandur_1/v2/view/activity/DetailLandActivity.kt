package id.hikmah.stiki.tandur_1.v2.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.models.SlideModel
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityDetailLandBinding
import id.hikmah.stiki.tandur_1.v2.adapter.FacilityGridAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.LandGridAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ReviewLandAdapter
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.model.ReviewLandData
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.DetailLandViewModel

class DetailLandActivity : AppCompatActivity() {
    private val listenerLand = object : ItemClickListener<LandData> {
        override fun onClickItem(item: LandData) {
            val intent = Intent(this@DetailLandActivity, DetailLandActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_LAND, item.idLand.toString())
            startActivity(intent)
        }
    }

    private lateinit var binding: ActivityDetailLandBinding
    private lateinit var viewModel: DetailLandViewModel
    private lateinit var dialog : ProgressDialog
    private lateinit var idLand: String
    private lateinit var land: LandData
    private lateinit var phoneUser: String
    private lateinit var latitude: String
    private lateinit var longitude: String

    private var facilityGridAdapter = FacilityGridAdapter()
    private var landAdapter = LandGridAdapter(listenerLand)
    private var reviewLandAdapter = ReviewLandAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idLand = intent.getStringExtra(KeyIntent.KEY_ID_LAND).toString()

        viewModel = ViewModelProvider(this).get(DetailLandViewModel::class.java)
        dialog = ProgressDialog(this)

        binding.recyclerViewFasilitas.apply {
            adapter = facilityGridAdapter
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
        }

        binding.recyclerViewLahan.apply {
            adapter = landAdapter
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
        }

        binding.recyclerViewReview.apply {
            layoutManager = LinearLayoutManager(this@DetailLandActivity)
            adapter = reviewLandAdapter
        }

        binding.materialButtonLihatLainnya.setOnClickListener {
            val intent = Intent(this, ListReviewActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_LAND, idLand)
            startActivity(intent)
        }

        viewModel.getDetailLand(this, idLand)
        viewModel.getReviewLand(this, idLand)
        viewModel.lands.observe(this) {
            val data = it.first()
            land = data

            var imageList = ArrayList<SlideModel>()
//            data.galleryLand?.forEach {
//                if (it != "-") {
//                    imageList.add(SlideModel(it, ""))
//                }
//            }

            data.urlGalleryLand?.forEach {
                if (it != "-") {
                    imageList.add(SlideModel(it, ""))
                }
            }
            data.urlDocLand?.forEach {
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
//            binding.textViewNamaLahan.text = "Lahan " + data.lengthLand + "x" + data.widthLand + " m " + data.nameLand
            binding.textViewNamaLahan.text = data.nameLand
            binding.ratingBar.rating = if (data.ratingLand.toString().isEmpty()) {
                0f
            } else {
                data.ratingLand!!
            }
            binding.textViewRating.text = data.ratingLand.toString()
            binding.textViewLokasi.text = data.addressLand.toString()

            Glide.with(this)
                .load(data.imgUser)
                .placeholder(R.drawable.ic_profil)
                .error(R.drawable.ic_profil)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.shapeableImageViewUser)
            binding.textViewNamaUser.text = data.ownNameLand

            phoneUser = if (data.telpUser.toString()[0] == '0') {
                "62" + data.telpUser.toString().substring(1)
            } else {
                data.telpUser.toString()
            }
            latitude = data.latLand.toString()
            longitude = data.longLand.toString()

            facilityGridAdapter.list = data.facilityLand.toString().split(";")
            facilityGridAdapter.notifyDataSetChanged()

            binding.textViewUkuranLahan.text = data.lengthLand.toString() + " x " + data.widthLand + " meter"
            binding.textViewIdLahan.text = data.idLand.toString()
            binding.textViewTerdaftarPada.text = data.registerAtLand.toString()
            binding.textViewPeraturanLahan.text = data.ruleLand.toString()
            binding.textViewHarga.text = Utils.changePrice(data.priceLand.toString()) + "/Bln"
        }

        viewModel.landsReview.observe(this) {
            if (!it.isNullOrEmpty()) {
                binding.linearLayoutReview.visibility = View.GONE
                if (it.size > 2) {
                    binding.materialButtonLihatLainnya.visibility = View.VISIBLE
                    val data = ArrayList<ReviewLandData>()
                    for (i in 0..1) {
                        data.add(it[i])
                    }
                    reviewLandAdapter.listReview = data.toMutableList() ?: emptyList()
                } else {
                    reviewLandAdapter.listReview = it.toMutableList() ?: emptyList()
                }
                reviewLandAdapter.notifyDataSetChanged()
            }
        }

        viewModel.getLand(this)
        viewModel.landsRecommendation.observe(this) {
            if (it.size > 4) {
                val data = ArrayList<LandData>()
                for (i in 0..3) {
                    data.add(it[i])
                }
                landAdapter.list = data.toMutableList() ?: emptyList()
            } else {
                landAdapter.list = it.toMutableList() ?: emptyList()
            }
            landAdapter.notifyDataSetChanged()
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

        viewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                showAlertDialog(it)
            }
        }

        binding.imageViewLokasiLahan.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?q=${latitude},${longitude}")
            )
            startActivity(intent)
        }

        binding.materialButtonHubungi.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://wa.me/${phoneUser}")
            )
            startActivity(intent)
        }

        binding.materialButtonSewaSekarang.setOnClickListener {
            val intent = Intent(this, ConfirmOrderLandActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_LAND, idLand)
            intent.putExtra(KeyIntent.KEY_DATA_DETAIL_LAND, land)
            startActivity(intent)
        }
    }

    private fun showProgressDialog() {
        //show dialog
        dialog.setMessage("Mohon tunggu...")
        dialog.setCancelable(false)
        dialog.show()
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pesan")
        builder.setMessage(message)
        builder.setCancelable(false)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
            finish()
        }
        builder.show()
    }
}