package com.ngoctuan.sales.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ngoctuan.sales.R
import com.ngoctuan.sales.databinding.ItemListBinding
import com.ngoctuan.sales.db.Person

class PeopleAdapter(
    private val listener: ((Int) -> Unit)? = null
) : RecyclerView.Adapter<PeopleAdapter.PersonViewHolder>() {
    private var people: List<Person> = listOf()

    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.idName)
        private val txtNumber: TextView = itemView.findViewById(R.id.idNumber)
        private val llItemView: LinearLayout = itemView.findViewById(R.id.llItem)
        fun onBind(person: Person) {
            txtName.text = person.name
            txtNumber.text = person.number
            llItemView.setOnClickListener {
                listener?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return PersonViewHolder(itemView)
    }


    override fun getItemCount(): Int = people.size


    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.onBind(people[position])
    }

    fun setAcc(cate: List<Person>) {
        this.people = cate
        notifyDataSetChanged()
    }
}