package com.example.gridtopagerkotlin.adapter

import android.graphics.drawable.Drawable
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gridtopagerkotlin.MainActivity
import com.example.gridtopagerkotlin.R
import com.example.gridtopagerkotlin.adapter.ImageData.Companion.IMAGE_DRAWABLES
import com.example.gridtopagerkotlin.fragment.ImagePagerFragment
import java.util.concurrent.atomic.AtomicBoolean

class GridAdapter(fragment: Fragment) : RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {

    private var requestManager: RequestManager = Glide.with(fragment)
    private var viewHolderListener: ViewHolderListener = ViewHolderListenerImpl(fragment)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_card, parent, false)

        return ImageViewHolder(view, requestManager, viewHolderListener)
    }

    override fun getItemCount(): Int = IMAGE_DRAWABLES.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }

    interface ViewHolderListener {
        fun onLoadCompleted(view: ImageView?, adapterPosition: Int)
        fun onItemClicked(view: View?, adapterPosition: Int)
    }

    private class ViewHolderListenerImpl(val fragment: Fragment) : ViewHolderListener {
        private var enterTransitionStarted : AtomicBoolean = AtomicBoolean()

        override fun onLoadCompleted(view: ImageView?, adapterPosition: Int) {
            if (MainActivity.currentPositon != adapterPosition){
                return;
            }
            if (enterTransitionStarted.getAndSet(true)){
                return;
            }
            fragment.startPostponedEnterTransition()
        }

        /**
         * Handles a view click by setting the current position to the given {@code position} and
         * starting a {@link  ImagePagerFragment} which displays the image at the position.
         *
         * @param view the clicked {@link ImageView} (the shared element view will be re-mapped at the
         * GridFragment's SharedElementCallback)
         * @param position the selected view position
         */
        override fun onItemClicked(view: View?, adapterPosition: Int) {
            // Update the position.
            MainActivity.currentPositon = adapterPosition

            // Exclude the clicked card from the exit transition (e.g. the card will disappear immediately
            // instead of fading out with the rest to prevent an overlapping animation of fade and move).: RequestManager
            val transitionSet : TransitionSet? = fragment.exitTransition as TransitionSet?
            if (transitionSet != null){
                transitionSet.excludeTarget(view, true)
                val fragmentManager = fragment.fragmentManager

                if (view != null && fragmentManager != null){
                    val transitioningView : ImageView = view.findViewById(R.id.card_image)
                    fragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .addSharedElement(transitioningView, transitioningView.transitionName)
                        .replace(R.id.fragment_container, ImagePagerFragment(), ImagePagerFragment::class.java.simpleName)
                        .addToBackStack(null)
                        .commit()
                }
            }

        }

    }

    class ImageViewHolder(
        itemView: View,
        val requestManager: RequestManager,
        val viewHolderListener: ViewHolderListener
    ) : RecyclerView.ViewHolder(itemView) {

        private var image : ImageView = itemView.findViewById(R.id.card_image)

        init {
            itemView.findViewById<CardView>(R.id.card_view).setOnClickListener {
                viewHolderListener.onItemClicked(it, adapterPosition)
            }
        }

        fun onBind(){
            setImage(adapterPosition)
            image.transitionName = IMAGE_DRAWABLES[adapterPosition].toString()
        }

        fun setImage(adapterPosition : Int ){
            requestManager
                .load(IMAGE_DRAWABLES[adapterPosition])
                .listener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(image, adapterPosition)
                        return false
                    }

                })
                .into(image)

        }
    }
}