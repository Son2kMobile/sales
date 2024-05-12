package com.ngoctuan.sales.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.ngoctuan.sales.R
import com.ngoctuan.sales.databinding.ItemListBinding
import com.ngoctuan.sales.db.Person

class PeopleAdapter(
    private val context: Context
) : RecyclerView.Adapter<PeopleAdapter.PersonViewHolder>() {
    private var people: List<Person> = listOf()


    inner class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName: TextView = itemView.findViewById(R.id.idName)
        private val txtNumber: TextView = itemView.findViewById(R.id.idNumber)
        private val check: CheckBox = itemView.findViewById(R.id.idCheck)
        private val call: Button = itemView.findViewById(R.id.idCall)
        fun onBind(person: Person) {
            txtName.text = person.name
            txtNumber.text = person.number
            check.isChecked = person.called
            call.setOnClickListener {
                val dialIntent =
                    Intent(Intent.ACTION_CALL) // ACTION_DIAL để hiển thị số điện thoại trong giao diện điện thoại, ACTION_CALL để thực hiện cuộc gọi tự động.
                dialIntent.data =
                    Uri.parse("tel:${txtNumber.text}") // "tel:" là schema cho số điện thoại
                context.startActivity(dialIntent)
                check.isChecked = true
                //   call.setBackgroundColor(R.color.green)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false)
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