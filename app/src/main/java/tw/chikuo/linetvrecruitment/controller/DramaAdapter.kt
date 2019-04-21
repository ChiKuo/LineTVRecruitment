package tw.chikuo.linetvrecruitment.controller

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_drama_item.view.*
import kotlinx.android.synthetic.main.recycler_drama_search.view.*
import tw.chikuo.linetvrecruitment.R
import tw.chikuo.linetvrecruitment.model.Drama


class DramaAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dramaList : MutableList<Drama> = ArrayList<Drama>()

    private val spacing = convertDpToPixel(15F)
    var isEmpty = false
    var searchKeyword = ""

    companion object {
        const val TYPE_SEARCH = 0
        const val TYPE_ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_SEARCH) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_drama_search, parent, false)
            return SearchViewHolder(itemView)
        } else if (viewType == TYPE_ITEM) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_drama_item, parent, false)
            return ItemViewHolder(itemView)
        }
        throw RuntimeException("there is no type that matches the type $viewType + make sure your using types correctly")
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ItemViewHolder) {

            // Spacing
            if (position % 2 == 1) {
                // Left
                viewHolder.itemView.setPadding(spacing , spacing / 2, spacing / 2, spacing / 2)
            } else {
                // Right
                viewHolder.itemView.setPadding(spacing / 2 , spacing / 2, spacing, spacing / 2)
            }
            val drama = dramaList[position - 1]
            viewHolder.bind(drama)

        } else if (viewHolder is SearchViewHolder) {
            viewHolder.bind()
        }
    }

    override fun getItemCount(): Int {
        return dramaList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_SEARCH
            else -> TYPE_ITEM
        }
    }

    inner class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        init {
            // SearchView
            itemView.searchView.setOnClickListener {
                itemView.searchView.isIconified = false
            }
            itemView.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(str: String?): Boolean {
                    onKeywordInputListener?.textChange(str!!)
                    return true
                }
            })
        }

        fun bind() {

            if (isEmpty){
                itemView.emptyLayout.visibility = View.VISIBLE
            } else {
                itemView.emptyLayout.visibility = View.GONE
            }

            if (searchKeyword != ""){
                itemView.searchView.isIconified = false
                itemView.searchView.setQuery(searchKeyword, false)
                searchKeyword = ""
            }
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(drama: Drama) {
            Picasso.get()
                    .load(drama.thumb)
                    .into(itemView.imageView)
            itemView.nameTextView.text = drama.name
            itemView.ratingTextView.text = drama.getRating()
            itemView.createdAtTextView.text = drama.getCreateTime()
        }

        override fun onClick(v: View?) {
            val drama = dramaList[adapterPosition - 1]
            val intent = Intent(context, DramaActivity::class.java)
            intent.putExtra("drama", drama)
            context.startActivity(intent)
        }
    }

    var onKeywordInputListener : OnKeywordInputListener? = null

    interface OnKeywordInputListener{
        fun textChange(string : String)
    }

    private fun convertDpToPixel(dp: Float): Int {
        return (dp * (Application.DEVICE_DENSITY_DPI / 160f)).toInt()
    }


}