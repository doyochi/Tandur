package id.hikmah.stiki.tandur_1.v2.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.FragmentProfileBinding
import id.hikmah.stiki.tandur_1.v2.util.JWTUtils
import id.hikmah.stiki.tandur_1.v2.util.Prefs
import id.hikmah.stiki.tandur_1.v2.view.activity.*

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.linearLayoutLogout.setOnClickListener {
            Prefs(requireContext()).jwt = null

            val intent = Intent(requireContext(), SplashScreenActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        binding.linearLayoutDetailProfile.setOnClickListener {
            startActivity(Intent(requireActivity(), DetailProfileActivity::class.java))
        }
        binding.linearLayoutManajemenLahan.setOnClickListener {
            startActivity(Intent(requireActivity(), ManajemenLahanActivity::class.java))
        }
        binding.linearLayoutKatalogBarangAnda.setOnClickListener {
            startActivity(Intent(requireActivity(), ManajemenProdukActivity::class.java))
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val user = JWTUtils.decoded(Prefs(requireContext()).jwt.toString())

        Glide.with(this)
            .load(user.imgUser)
            .placeholder(R.drawable.ic_no_profile)
            .error(R.drawable.ic_no_profile)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.shapeableImageView)

//        binding.textViewNama.text = user.nameUser
        binding.textViewNamaLengkap.text = user.nameUser
//        binding.textViewKTP.text = user.ktpUser
//        binding.textViewAlamat.text = user.addressUser
        binding.textViewEmail.text = user.emailUser
//        binding.textViewHP.text = user.telpUser
    }
}