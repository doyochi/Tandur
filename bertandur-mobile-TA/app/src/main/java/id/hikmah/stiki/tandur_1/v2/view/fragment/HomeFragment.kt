package id.hikmah.stiki.tandur_1.v2.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.models.SlideModel
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.FragmentHome2Binding
import id.hikmah.stiki.tandur_1.v2.adapter.CustomDotIndicatorAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.LandGridAdapter
import id.hikmah.stiki.tandur_1.v2.adapter.ProductGridAdapter
import id.hikmah.stiki.tandur_1.v2.model.LandData
import id.hikmah.stiki.tandur_1.v2.model.ListProductData
import id.hikmah.stiki.tandur_1.v2.model.ProductData
import id.hikmah.stiki.tandur_1.v2.util.*
import id.hikmah.stiki.tandur_1.v2.view.activity.*
import id.hikmah.stiki.tandur_1.v2.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
    private val listenerLand = object : ItemClickListener<LandData> {
        override fun onClickItem(item: LandData) {
            val intent = Intent(requireContext(), DetailLandActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_LAND, item.idLand.toString())
            startActivity(intent)
        }
    }

    private val listenerProduct = object : ItemClickListener<ListProductData> {
        override fun onClickItem(item: ListProductData) {
            val intent = Intent(requireContext(), DetailProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_PRODUCT, item.idProduct.toString())
            startActivity(intent)
        }
    }

    private lateinit var binding: FragmentHome2Binding
    private lateinit var viewModel: HomeViewModel
    private var landAdapter = LandGridAdapter(listenerLand)
    private var productGridAdapter = ProductGridAdapter(listenerProduct)
    private var productGroundGridAdapter = ProductGridAdapter(listenerProduct)
    private var customDotIndicatorAdapter = CustomDotIndicatorAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHome2Binding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        binding.textViewNamaUser.text = "Halo, ${JWTUtils.decoded(Prefs(requireContext()).jwt.toString()).nameUser}"

        binding.rvData.apply {
            adapter = landAdapter
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
        }

//        binding.dotsIndicator.attachTo(binding.imageSlider)

        binding.rvDataBarang.apply {
            adapter = productGridAdapter
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
        }

        binding.rvDataBarangGround.apply {
            adapter = productGroundGridAdapter
            layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.grid_count))
            itemAnimator = null
            val marginDecoration = resources.getDimension(R.dimen.space_half).toInt()
            val marginDp = PixelHelper.convertDpToPx(marginDecoration, resources)
            addItemDecoration(GridItemDecoration(resources.getInteger(R.integer.grid_count), marginDp, true))
        }

        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.ic_carousel_1, ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel(R.drawable.ic_carousel_2, ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel(R.drawable.ic_carousel_3, ScaleTypes.CENTER_INSIDE))
        imageList.add(SlideModel(R.drawable.ic_carousel_4, ScaleTypes.CENTER_INSIDE))
        binding.imageSlider.setImageList(imageList)
        customDotIndicatorAdapter.list = imageList
        customDotIndicatorAdapter.notifyDataSetChanged()

        binding.imageSlider.apply {
            setImageList(imageList, ScaleTypes.CENTER_CROP)
            setItemChangeListener(object :ItemChangeListener{
                override fun onItemChanged(position: Int) {
                    customDotIndicatorAdapter.activePosition = position
                    customDotIndicatorAdapter.notifyDataSetChanged()
                }

            })
        }

        binding.recyclerViewDots.apply {
            adapter = customDotIndicatorAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        viewModel.getLand(requireContext())
        viewModel.lands.observe(viewLifecycleOwner) {
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

        viewModel.getProduct(requireContext(), 1)
        viewModel.products.observe(viewLifecycleOwner) {
            if (it.size > 4) {
                val data = ArrayList<ListProductData>()
                for (i in 0..3) {
                    data.add(it[i])
                }
                productGridAdapter.list = data.toMutableList() ?: emptyList()
            } else {
                productGridAdapter.list = it.toMutableList() ?: emptyList()
            }
            productGridAdapter.notifyDataSetChanged()
        }

        viewModel.getProduct(requireContext(), 2)
        viewModel.productsGround.observe(viewLifecycleOwner) {
            if (it.size > 4) {
                val data = ArrayList<ListProductData>()
                for (i in 0..3) {
                    data.add(it[i])
                }
                productGroundGridAdapter.list = data.toMutableList() ?: emptyList()
            } else {
                productGroundGridAdapter.list = it.toMutableList() ?: emptyList()
            }
            productGroundGridAdapter.notifyDataSetChanged()
        }

        binding.linearLayoutUrbanFarming.setOnClickListener {
            startActivity(Intent(requireContext(), UrbanFarmingActivity::class.java))
        }

        binding.linearLayouPasarTandur.setOnClickListener {
            val intent = Intent(requireContext(), TandurMarketActivity::class.java)
            intent.putExtra(KeyIntent.KEY_PRODUCT_TYPE, 1)
            startActivity(intent)
        }

        binding.linearLayouGroundGarden.setOnClickListener {
            val intent = Intent(requireContext(), TandurMarketActivity::class.java)
            intent.putExtra(KeyIntent.KEY_PRODUCT_TYPE, 2)
            startActivity(intent)
        }

        binding.materialButtonUrbanFarming.setOnClickListener {
            startActivity(Intent(requireContext(), UrbanFarmingActivity::class.java))
        }

        binding.materialButtonTandurMarket.setOnClickListener {
            val intent = Intent(requireContext(), TandurMarketActivity::class.java)
            intent.putExtra(KeyIntent.KEY_PRODUCT_TYPE, 1)
            startActivity(intent)
        }

        binding.materialButtonGroundGarden.setOnClickListener {
            val intent = Intent(requireContext(), TandurMarketActivity::class.java)
            intent.putExtra(KeyIntent.KEY_PRODUCT_TYPE, 2)
            startActivity(intent)
        }

        binding.linearLayouBelajarNandur.setOnClickListener {
            startActivity(Intent(requireActivity(), BelajarNandurActivity::class.java))
        }

        binding.imageViewManajemenLahan.setOnClickListener {
            startActivity(Intent(requireContext(), ManajemenLahanActivity::class.java))
        }

        binding.imageViewCart.setOnClickListener {
            startActivity(Intent(requireContext(), CartActivity::class.java))
        }

        binding.imageViewNotification.setOnClickListener {
            val sheet = NotificationFragment()
            sheet.show(requireActivity().supportFragmentManager, "NotificationFragment")
        }

        return binding.root
    }
}