package id.hikmah.stiki.tandur_1.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.hikmah.stiki.tandur_1.R
import id.hikmah.stiki.tandur_1.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuFind()
        moveToDetail()
        moveToProfil()
    }

    private fun moveToDetail() {
        binding.icUrbanFarming.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_detailLahanFragment)
        }
    }

    private fun moveToProfil() {
        binding.icNotif.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
    }
    private fun aaa(){
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.page_profil -> {
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    }
    private fun menuFind(){
        when (binding.bottomNavigation.selectedItemId){
            R.id.page_profil -> {
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }

    }
}