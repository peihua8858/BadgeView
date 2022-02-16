package com.fz.badgeview.demo

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.fz.badgeview.BadgeFloatingActionButton
import com.fz.badgeview.BadgeImageView
import com.fz.badgeview.DragDismissDelegate
import com.fz.badgeview.IBadgeFeature
import com.fz.badgeview.annotation.BadgeView
import com.fz.imageloader.widget.RatioImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton

@BadgeView(values=[
    View::class,  // 对应 com.fz.badgeview.BadgeView，不想用这个类的话就删了这一行
    ImageView::class,  // 对应 com.fz.badgeview.BadgeImageView，不想用这个类的话就删了这一行
    RatioImageView::class,// 对应 com.fz.badgeview.BadgeRatioImageView，不想用这个类的话就删了这一行
    TextView::class,  // 对应 com.fz.badgeview.BadgeFloatingTextView，不想用这个类的话就删了这一行
    RadioButton::class,  // 对应 com.fz.badgeview.BadgeRadioButton，不想用这个类的话就删了这一行
    LinearLayout::class,  // 对应 com.fz.badgeview.BadgeLinearLayout，不想用这个类的话就删了这一行
    FrameLayout::class,  // 对应 com.fz.badgeview.BadgeFrameLayout，不想用这个类的话就删了这一行
    RelativeLayout::class,  // 对应 com.fz.badgeview.BadgeRelativeLayout，不想用这个类的话就删了这一行
    FloatingActionButton::class]
)
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ivIcon: BadgeImageView = findViewById(R.id.iv_station_letter_icon)
        ivIcon.showTextBadge("22")
        val bfdActionButton: BadgeFloatingActionButton = findViewById(R.id.bfab_main_chat)
        bfdActionButton.showTextBadge("22")
        bfdActionButton.setDragDismissDelegate(object : DragDismissDelegate {
            override fun onDismiss(feature: IBadgeFeature) {
               Toast.makeText(this@MainActivity,"清空聊天消息",Toast.LENGTH_SHORT).show()
            }
        })
    }
}