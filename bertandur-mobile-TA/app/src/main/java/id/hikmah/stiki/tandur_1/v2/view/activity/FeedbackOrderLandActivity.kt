package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.hikmah.stiki.tandur_1.databinding.ActivityFeedbackOrderLandBinding
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent

class FeedbackOrderLandActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackOrderLandBinding
    private lateinit var idRent: String
    private var fromOrder: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackOrderLandBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idRent = intent.getStringExtra(KeyIntent.KEY_ID_RENT).toString()
        fromOrder = intent.getBooleanExtra(KeyIntent.KEY_FROM_ORDER, false)

        binding.materialButtonSelesai.setOnClickListener {
            val intent = Intent(this, DetailOrderLandActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_RENT, idRent)
            intent.putExtra(KeyIntent.KEY_FROM_ORDER, fromOrder)
            startActivity(intent)
        }
    }

    private fun toMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        toMain()
    }
}