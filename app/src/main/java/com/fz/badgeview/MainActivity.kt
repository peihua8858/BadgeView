package com.fz.badgeview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ivIcon: BadgeImageView = findViewById(R.id.iv_station_letter_icon)
        ivIcon.showCirclePointBadge()
    }
}