package com.srg.zara.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.srg.domain.zara.entity.Character
import com.srg.zara.R
import com.srg.zara.databinding.ItemCharacterBinding


class CharacterAdapter(
    private val listener: CharacterListListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: ArrayList<Character> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CharacterViewHolder(ItemCharacterBinding.inflate(layoutInflater, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CharacterViewHolder).bind(item = items[position])
    }


    override fun getItemCount(): Int {
        return items.size
    }


    inner class CharacterViewHolder(val binding: ItemCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Character) {
            with(binding) {
                ivCharacter.load(item.image) {
                    placeholder(R.drawable.avatar_placeholder)
                    error(R.drawable.avatar_placeholder)
                }
                tvTitle.text = item.name
                tvSpecies.text = item.species
                root.setOnClickListener {
                    listener.onCharacterClicked(item)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Character>) {
        items.clear()
        list.let {
            items.addAll(list)
        }
        notifyDataSetChanged()
    }

    fun addData(list: List<Character>) {
        list.let {
            items.addAll(list)
        }
    }

    interface CharacterListListener {
        fun onCharacterClicked(user: Character)
    }
}
