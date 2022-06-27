package net.top.repo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import net.top.repo.api.response.home_page.GitResponse
import net.top.repo.databinding.HomeItemBinding
import net.top.repo.utilities.OnItemClickListener


class HomeAdapter(

) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    private var repoItemList = mutableListOf<GitResponse.Item>()

    class MyViewHolder(binding: HomeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var homeItemBinding: HomeItemBinding? = binding
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = HomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = repoItemList[position]
        val binding = holder.homeItemBinding
        if (binding != null) {

            binding.username.text = data.full_name
            binding.itemDesc.text = data.description
            Picasso.get().load(data.owner?.avatar_url).into(binding.avatarIv)
            binding.itemForkCount.text = data.forks_count.toString()
            binding.itemStar.text = data.stargazers_count.toString()
            binding.repoName.text = data.name

            binding.homeItem.setOnClickListener {
                mClickListener.onItemClick(it,data,position)
            }

        }
    }

    override fun getItemCount() = repoItemList.size

    fun setData(listData: MutableList<GitResponse.Item>) {
        repoItemList = listData
        notifyDataSetChanged()
    }

    lateinit var mClickListener: OnItemClickListener

    fun setOnclickListener(aClickListener: OnItemClickListener) {
        mClickListener = aClickListener
    }

}