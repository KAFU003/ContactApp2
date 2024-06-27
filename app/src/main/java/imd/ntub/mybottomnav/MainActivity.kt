// MainActivity.kt
//11056003鄧寓憲
//11056034楊文豪
package imd.ntub.mybottomnav

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.activity.viewModels
import imd.ntub.mybottomnav.viewmodel.ContactViewModel

class ViewPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FirstFragment.newInstance()
            1 -> SecondFragment.newInstance()
            else -> ThirdFragment.newInstance()
        }
    }
}

class MainActivity : AppCompatActivity() {
    private val contactViewModel: ContactViewModel by viewModels()
    private lateinit var contactFileManager: ContactFileManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactFileManager = ContactFileManager(this)

        // 載入已保存的聯絡人資料
        val savedContacts = contactFileManager.loadContacts()
        contactViewModel.setContacts(savedContacts)

        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager2)
        viewPager2.adapter = ViewPagerAdapter(this)

        val btnNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                btnNav.selectedItemId = when (position) {
                    0 -> R.id.btnLeft
                    1 -> R.id.btnMiddle
                    else -> R.id.btnRight
                }
            }
        })

        btnNav.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.btnLeft -> {
                    viewPager2.currentItem = 0
                    true
                }
                R.id.btnMiddle -> {
                    viewPager2.currentItem = 1
                    true
                }
                else -> {
                    viewPager2.currentItem = 2
                    true
                }
            }
        }
    }

    // 在應用程式被停止時保存聯絡人資料
    override fun onStop() {
        super.onStop()
        contactFileManager.saveContacts(contactViewModel.contacts.value ?: mutableListOf())
    }
}