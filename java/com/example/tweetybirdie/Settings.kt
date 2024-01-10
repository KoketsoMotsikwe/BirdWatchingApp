//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT


package com.example.tweetybirdie

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class Settings : AppCompatActivity() {

    private lateinit var logout: TextView
    private lateinit var back: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        logout = findViewById(R.id.tvLogout)
        back = findViewById(R.id.tvBackSettings)

        val pagerAdapter = PagerAdapterSettings(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        logout.setOnClickListener() {
            logout()
        }

        back.setOnClickListener() {
            val intent = Intent(this, Hotpots::class.java)
            startActivity(intent)
        }

        val desiredFragmentIndex = intent.getIntExtra("desiredFragmentIndex", 0)
        viewPager.setCurrentItem(desiredFragmentIndex, false)
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val intent = Intent(this, Hotpots::class.java)
        startActivity(intent)
    }

    private fun logout()
    {
        ToolBox.users.clear()
        FirebaseAuth.getInstance().signOut();
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}