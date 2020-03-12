package com.example.gridtopagerkotlin.fragment

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.gridtopagerkotlin.MainActivity
import com.example.gridtopagerkotlin.R
import com.example.gridtopagerkotlin.adapter.ImagePagerAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [ImagePagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImagePagerFragment : Fragment() {

    private lateinit var view_pager : ViewPager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        view_pager =  inflater.inflate(R.layout.fragment_image_pager, container, false) as ViewPager
        view_pager.adapter = ImagePagerAdapter(this)
        // Set the current position and add a listener that will update the selection coordinator when
        // paging the images.
        view_pager.currentItem = MainActivity.currentPositon
        view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
            override fun onPageSelected(position: Int) {
                MainActivity.currentPositon = position
            }
        })

        prepareSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
        if (savedInstanceState == null){
            postponeEnterTransition()
        }
        return view_pager
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private fun prepareSharedElementTransition(){
        val transition : Transition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)
        sharedElementEnterTransition = transition

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                // Locate the image view at the primary fragment (the ImageFragment that is currently
                // visible). To locate the fragment, call instantiateItem with the selection position.
                // At this stage, the method will simply return the fragment at the position and will
                // not create a new one.
                val currentFragment = view_pager.adapter?.instantiateItem(view_pager, MainActivity.currentPositon) as Fragment
                val view = currentFragment.view
                if (view == null){
                    return;
                }

                // Map the first shared element name to the child ImageView.
                if (sharedElements != null && names != null)
                    sharedElements.put(names.get(0), view.findViewById(R.id.image))
            }
        })
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
        fun newInstance() =
            ImagePagerFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
