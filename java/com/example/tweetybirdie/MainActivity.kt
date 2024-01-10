//Authors:
//Lonwabo Gade
//Koketso Motsikwe
//OPSC7312 POE(Final Part)
//Sources: Git(Paths to git repos can be provided upon request) and ChatGPT


package com.example.tweetybirdie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        FirebaseApp.initializeApp(this)
        FirebaseApp.getInstance()

        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager = findViewById(R.id.viewPager)

        val pagerAdapter = PagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }
}