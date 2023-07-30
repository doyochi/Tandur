package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.hikmah.stiki.tandur_1.databinding.ActivityFeedbackInsertLahanBinding

class FeedbackInsertLahanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedbackInsertLahanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackInsertLahanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.materialButtonSelesai.setOnClickListener {
            toMain()
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