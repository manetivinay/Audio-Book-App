package com.vinaymaneti.audiobookapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vinaymaneti.audiobookapp.player.PlayerActivity
import com.vinaymaneti.audiobookapp.utils.AudioBookUtils


const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private var viewItems: MutableList<AudioBooksModel> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navigation = findViewById<BottomNavigationView>(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(
            AudioBookUtils.addItemsFromJSON(),
            callbackInterface = object : CustomAdapter.CallbackInterface {
                override fun passResultCallback(audioBooksModel: AudioBooksModel, position: Int) {
                    val intent = Intent(this@MainActivity, PlayerActivity::class.java)
                    intent.putExtra("audioBookModel", audioBooksModel)
                    intent.putExtra("position", position)
                    startActivity(intent)
                }

            })

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

//        adapter.setItemSelectionAction { // <- it is Item
//            val intent = Intent(this@MainActivity, PlayerActivity::class.java)
//            intent.putExtra("audioBookModel", it)
//            startActivity(intent)
//        }

    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var fragment: Fragment
            when (item.itemId) {
                R.id.navigation_home -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_library -> {
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


}