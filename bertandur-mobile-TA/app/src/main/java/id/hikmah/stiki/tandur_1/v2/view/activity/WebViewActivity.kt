package id.hikmah.stiki.tandur_1.v2.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import id.hikmah.stiki.tandur_1.databinding.ActivityWebViewBinding
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent


class WebViewActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityWebViewBinding.inflate(layoutInflater)
    }

    private lateinit var idPurchase: String
    private lateinit var urlPayment: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        idPurchase = intent.getStringExtra(KeyIntent.KEY_ID_PURCHASE).toString()
        urlPayment = intent.getStringExtra(KeyIntent.KEY_URL_PAYMENT).toString()


        openUrlFromWebView(urlPayment)
    }

    private fun openUrlFromWebView(url: String) {
        binding.apply {
            webView.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val requestUrl = request?.url.toString()
                    return if (requestUrl.contains("gojek://")
                        || requestUrl.contains("shopeeid://")
                        || requestUrl.contains("//wsa.wallet.airpay.co.id/") // This is handle for sandbox Simulator
                        || requestUrl.contains("/gopay/partner/")
                        || requestUrl.contains("/shopeepay/")
                    ) {
                        val intent = Intent(Intent.ACTION_VIEW, request!!.url)
                        startActivity(intent)
                        // `true` means for the specified url, will be handled by OS by starting Intent
                        true
                    } else {
                        // `false` means any other url will be loaded normally by the WebView
//                        if (requestUrl.contains("/finish")) {
//                            val intent = Intent(this@WebViewActivity, MainActivity::class.java)
//                            intent.putExtra(KeyIntent.KEY_ID_PURCHASE, idPurchase)
//                            intent.putExtra(KeyIntent.KEY_FROM_ORDER, true)
//                            startActivity(intent)
//                        } else if (requestUrl.contains("/unfinish ")) {
//                            val intent = Intent(this@WebViewActivity, MainActivity::class.java)
//                            intent.putExtra(KeyIntent.KEY_ID_PURCHASE, idPurchase)
//                            intent.putExtra(KeyIntent.KEY_FROM_ORDER, true)
//                            startActivity(intent)
//                        } else {
//                            val intent = Intent(this@WebViewActivity, MainActivity::class.java)
//                            intent.putExtra(KeyIntent.KEY_ID_PURCHASE, idPurchase)
//                            intent.putExtra(KeyIntent.KEY_FROM_ORDER, true)
//                            startActivity(intent)
//                        }
                        val intent = Intent(this@WebViewActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        false
                    }
                }
            }

            webView.settings.loadsImagesAutomatically = true
            webView.settings.javaScriptEnabled =true
            webView.scrollBarSize = View.SCROLLBARS_INSIDE_OVERLAY
            webView.loadUrl(url);
        }
    }
}