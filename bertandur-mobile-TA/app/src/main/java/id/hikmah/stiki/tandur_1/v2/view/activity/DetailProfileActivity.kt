package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityDetailProfileBinding
import id.hikmah.stiki.tandur_1.v2.util.JWTUtils
import id.hikmah.stiki.tandur_1.v2.util.Prefs

class DetailProfileActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityDetailProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.materialButtonEditProfile.setOnClickListener {
            startActivity(Intent(this, UpdateProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        val user = JWTUtils.decoded(Prefs(this).jwt.toString())

        Glide.with(this)
            .load(user.imgUser)
            .placeholder(R.drawable.ic_no_profile)
            .error(R.drawable.ic_no_profile)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.shapeableImageView)

        binding.textViewNama.text = user.nameUser
        binding.textViewNamaLengkap.text = user.nameUser
        binding.textViewKTP.text = user.ktpUser
        binding.textViewAlamat.text = user.addressUser
        binding.textViewEmail.text = user.emailUser
        binding.textViewHP.text = user.telpUser
    }
}