package com.example.gridtopagerkotlin.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

import com.example.gridtopagerkotlin.R

/**
 * A simple [Fragment] subclass.
 * Use the [ImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        // Just like we do when binding views at the grid, we set the transition name to be the string
        // value of the image res.
        val arguments : Bundle? = arguments
        if (arguments != null){
            @DrawableRes val imageRes : Int = arguments.getInt(KEY_IMAGE_RES)

            // Load the image with Glide to prevent OOM error when the image drawables are very large.
            Glide.with(this)
                .load(imageRes)
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        parentFragment?.startPostponedEnterTransition()
                        return false
                    }

                }).into(view.findViewById(R.id.image))
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment GridFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(@DrawableRes drawableRes: Int) =
            ImageFragment().apply {
                arguments = Bundle().apply {
                    putInt(KEY_IMAGE_RES, drawableRes)
                }
            }

        val KEY_IMAGE_RES = "com.google.samples.gridtopager.key.imageRes"
    }
}
