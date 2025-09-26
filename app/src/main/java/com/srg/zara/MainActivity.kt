package com.srg.zara

import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.srg.zara.databinding.ActivityMainBinding
import com.srg.zara.util.LanguageManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            val slideDownOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_down)
            val slideUpIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_up)

            if (destination.id == R.id.charDetailFragment) {
                navView.isVisible = false
                navView.startAnimation(slideDownOut)
            } else {
                if(navView.isVisible == false) {
                    navView.isVisible = true
                    navView.startAnimation(slideUpIn)
                }
            }
        }

        navView.setupWithNavController(navController)


    }

    override fun attachBaseContext(newBase: Context?) {
        val language = LanguageManager.getLanguage(requireNotNull(newBase))
        println(language.toString())
        val context = LanguageManager.setLocale(newBase, language)
        super.attachBaseContext(context)
    }
}