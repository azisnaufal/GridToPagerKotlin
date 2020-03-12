package com.example.gridtopagerkotlin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gridtopagerkotlin.fragment.ImageFragment

class ImagePagerAdapter(fragment: Fragment) : FragmentPagerAdapter(fragment.childFragmentManager) {
    override fun getItem(position: Int): Fragment = ImageFragment.newInstance(ImageData.IMAGE_DRAWABLES[position])

    override fun getCount(): Int = ImageData.IMAGE_DRAWABLES.size
}