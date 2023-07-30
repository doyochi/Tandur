package id.hikmah.stiki.tandur_1.v2.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.ActivityMainBinding
import id.hikmah.stiki.tandur_1.v2.view.fragment.FavoriteFragment
import id.hikmah.stiki.tandur_1.v2.view.fragment.HomeFragment
import id.hikmah.stiki.tandur_1.v2.view.fragment.ProfileFragment
import id.hikmah.stiki.tandur_1.v2.view.fragment.TransactionFragment

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var fragmentActive: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(HomeFragment())
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.page_home -> {
                loadFragment(HomeFragment())
                return true
            }
            R.id.page_fav -> {
                loadFragment(FavoriteFragment())
                return true
            }
            R.id.page_transaksi -> {
                loadFragment(TransactionFragment())
                return true
            }
            R.id.page_profil -> {
                loadFragment(ProfileFragment())
                return true
            }
        }
        return false
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        if (fragment != null) {
            fragmentActive = fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            return true
        }
        return false
    }
}