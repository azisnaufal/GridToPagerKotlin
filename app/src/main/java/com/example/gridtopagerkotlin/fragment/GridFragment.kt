package com.example.gridtopagerkotlin.fragment

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.recyclerview.widget.RecyclerView
import com.example.gridtopagerkotlin.MainActivity

import com.example.gridtopagerkotlin.R
import com.example.gridtopagerkotlin.adapter.GridAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [GridFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GridFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        recyclerView = inflater.inflate(R.layout.fragment_grid, container, false) as RecyclerView

        recyclerView.adapter = GridAdapter(this)
        prepareTransition()
        postponeEnterTransition()

        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollToPosition()
    }
    /**
     * Scrolls the recycler view to show the last viewed item in the grid. This is important when
     * navigating back from the grid.
     */
    private fun scrollToPosition(){
        recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener{
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = recyclerView.layoutManager
                if (layoutManager != null){
                    val viewAtPosition = layoutManager.findViewByPosition(MainActivity.currentPositon)
                    // Scroll to position if the view for the current position is null (not currently part of
                    // layout manager children), or it's not completely visible.
                    if (viewAtPosition == null || layoutManager.isViewPartiallyVisible(viewAtPosition, false, true)){
                        recyclerView.post {
                            layoutManager.scrollToPosition(MainActivity.currentPositon)
                        }
                    }
                }
            }

        })
    }

    /**
     * Prepares the shared element transition to the pager fragment, as well as the other transitions
     * that affect the flow.
     */
    private fun prepareTransition(){
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_exit_transition)

        // A similar mapping is set at the ImagePagerFragment with a setEnterSharedElementCallback.
        setExitSharedElementCallback(object : SharedElementCallback(){
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                // Locate the ViewHolder for the clicked position.
                val selectedViewHolder = recyclerView.findViewHolderForAdapterPosition(MainActivity.currentPositon)
                        as RecyclerView.ViewHolder
                if (selectedViewHolder == null || selectedViewHolder.itemView == null){
                    return
                }

                // Map the first shared element name to the child ImageView.
                if (sharedElements != null && names != null)
                    sharedElements
                        .put(names.get(0), selectedViewHolder.itemView.findViewById(R.id.card_image))

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
            GridFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}
