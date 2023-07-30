package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.hikmah.stiki.tandur_1.databinding.ActivityFeedbackOrderProductBinding
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent

class FeedbackOrderProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackOrderProductBinding
    private lateinit var idPurchase: String
    private var fromOrder: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackOrderProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        idPurchase = intent.getStringExtra(KeyIntent.KEY_ID_PURCHASE).toString()
        fromOrder = intent.getBooleanExtra(KeyIntent.KEY_FROM_ORDER, false)

        binding.materialButtonSelesai.setOnClickListener {
            val intent = Intent(this, DetailOrderProductActivity::class.java)
            intent.putExtra(KeyIntent.KEY_ID_PURCHASE, idPurchase)
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