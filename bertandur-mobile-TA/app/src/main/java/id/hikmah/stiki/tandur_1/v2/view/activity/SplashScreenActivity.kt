package id.hikmah.stiki.tandur_1.v2.view.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import id.hikmah.stiki.tandur_1.databinding.ActivitySplashScreenBinding
import id.hikmah.stiki.tandur_1.v2.util.Prefs

class SplashScreenActivity : Activity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Dexter.withContext(this)
                .withPermissions(
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ).withListener(object : com.karumi.dexter.listener.multi.MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        p0?.let {
                            if(p0.areAllPermissionsGranted()){
                                moveActivity()
                            } else {
//                                intentToMain()
                                //create toast
                                Toast.makeText(this@SplashScreenActivity, "Tolong berikan semua izin untuk aplikasi", Toast.LENGTH_LONG).show()
                                finish()
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        p1?.continuePermissionRequest()
                    }

                })
                .check()
        } else {
            moveActivity()
        }
    }

    private fun moveActivity() {
        Handler().postDelayed({
            if (Prefs(this).jwt.isNullOrEmpty()) {
                startActivity(Intent(this, SignInActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        },3000)
    }
}