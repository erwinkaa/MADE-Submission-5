package id.erwinka.madesubmission4.main.tvshow

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import id.erwinka.madesubmission4.R
import id.erwinka.madesubmission4.api.ApiRepository
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.inflater_film.view.*

class TVShowAdapter(
    private val context: Context,
    private val data: MutableList<TVShowModel>,
    private val onClickListener: (TVShowModel) -> Unit
) :
    RecyclerView.Adapter<TVShowAdapter.ViewHolder>() {

    fun setData(tvshow: List<TVShowModel>) {
        data.clear()
        data.addAll(tvshow)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.inflater_film, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(context, data[position], onClickListener)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        fun bindItem(
            context: Context,
            data: TVShowModel,
            onClickListener: (TVShowModel) -> Unit
        ) {
            itemView.tv_film_title.text = data.name
            Glide.with(context).load(ApiRepository.BASE_IMAGE_URL + data.poster_path).into(itemView.iv_film_poster)
            containerView.setOnClickListener { onClickListener(data) }
        }
    }

}