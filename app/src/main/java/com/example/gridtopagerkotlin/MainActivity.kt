package com.example.gridtopagerkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gridtopagerkotlin.fragment.GridFragment

class MainActivity : AppCompatActivity() {

    companion object {
        var currentPositon : Int = 0
        private const val KEY_CURRENT_POSITION = "com.example.gridtopagerkotlin.currentPosition"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null){
            currentPositon = savedInstanceState.getInt(KEY_CURRENT_POSITION,0)
            return
        }

        val fragmentManager = supportFragmentManager
        fragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, GridFragment.newInstance(), GridFragment::class.java.simpleName)
                .commit()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_POSITION, currentPositon)
    }
}
