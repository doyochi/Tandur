package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.hikmah.stiki.tandur_1.databinding.ActivityPaymentMethodBinding
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent

class PaymentMethodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent()
        binding.linearLayoutBCA.setOnClickListener {
            intent.putExtra(KeyIntent.KEY_PAYMENT_METHOD, "BCA")
            setResult(KeyIntent.KEY_PAYMENT_METHOD_CODE, intent)
            finish()
        }
        binding.linearLayoutMandiri.setOnClickListener {
            intent.putExtra(KeyIntent.KEY_PAYMENT_METHOD, "Mandiri")
            setResult(KeyIntent.KEY_PAYMENT_METHOD_CODE, intent)
            finish()
        }
        binding.linearLayoutBNI.setOnClickListener {
            intent.putExtra(KeyIntent.KEY_PAYMENT_METHOD, "BNI")
            setResult(KeyIntent.KEY_PAYMENT_METHOD_CODE, intent)
            finish()
        }
        binding.linearLayoutBRI.setOnClickListener {
            intent.putExtra(KeyIntent.KEY_PAYMENT_METHOD, "BRI")
            setResult(KeyIntent.KEY_PAYMENT_METHOD_CODE, intent)
            finish()
        }
    }
}