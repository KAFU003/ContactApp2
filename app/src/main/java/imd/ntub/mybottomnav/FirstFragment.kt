package imd.ntub.mybottomnav

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import imd.ntub.mybottomnav.viewmodel.ContactViewModel

class FirstFragment : Fragment() {
    private val contactViewModel: ContactViewModel by activityViewModels()
    private lateinit var myAdapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager

        myAdapter = MyAdapter(mutableListOf())
        recyclerView.adapter = myAdapter

        val contactFileManager = ContactFileManager(requireContext())

        // 讀取聯絡人資料
        val contacts = contactFileManager.loadContacts()
        myAdapter.updateData(contacts)

        contactViewModel.contacts.observe(viewLifecycleOwner, Observer { contacts ->
            myAdapter.updateData(contacts)
            // 保存聯絡人資料
            contactFileManager.saveContacts(contacts)
        })

        return view
    }

    class MyAdapter(private var data: MutableList<Contact>) :
        RecyclerView.Adapter<MyAdapter.ViewHolder>() {

        inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
            val txtName: TextView = v.findViewById(R.id.txtName)
            val txtPhone: TextView = v.findViewById(R.id.txtPhone)
            val ivCall: ImageView = v.findViewById(R.id.imageView)
            val btnDelete: AppCompatImageButton = v.findViewById(R.id.btnDelete)

            init {
                btnDelete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        showDeleteConfirmationDialog(position)
                    }
                }
                txtName.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        showEditContactDialog(position)
                    }
                }
            }

            private fun showEditContactDialog(position: Int) {
                val builder = AlertDialog.Builder(itemView.context)
                val inflater = LayoutInflater.from(itemView.context)
                val dialogView = inflater.inflate(R.layout.edit_contact_layout, null)

                val edtName = dialogView.findViewById<EditText>(R.id.edtName)
                val edtPhone = dialogView.findViewById<EditText>(R.id.edtPhone)
                val btnAdd = dialogView.findViewById<Button>(R.id.btnAdd)

                // 初始化姓名和電話資料
                val contact = data[position]
                edtName.setText(contact.name)
                edtPhone.setText(contact.phone)

                builder.setView(dialogView)
                val dialog = builder.create()

                btnAdd.setOnClickListener {
                    // 在此處處理編輯聯絡人的操作，你可以從EditText中獲取新的姓名和電話資料並進行更新
                    val newName = edtName.text.toString()
                    val newPhone = edtPhone.text.toString()
                    if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                        updateContact(position, newName, newPhone)
                        dialog.dismiss()
                    } else {
                        Toast.makeText(itemView.context, "姓名和電話不能為空", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.show()
            }

            private fun updateContact(position: Int, newName: String, newPhone: String) {
                // 更新聯絡人資料
                data[position] = Contact(newName, newPhone)
                notifyItemChanged(position)
            }

            private fun showDeleteConfirmationDialog(position: Int) {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("確認刪除")
                builder.setMessage("你確定要刪除這個聯絡人嗎？")
                builder.setPositiveButton("對的:<") { _, _ ->
                    deleteContact(position)
                }
                builder.setNegativeButton("沒有我按錯惹q_q", null)
                val dialog = builder.create()
                dialog.show()
            }

            private fun deleteContact(position: Int) {
                data.removeAt(position)
                notifyItemRemoved(position)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.adapter_row, parent, false)
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val currentItem = data[position]
            holder.txtName.text = currentItem.name
            holder.txtPhone.text = currentItem.phone
            holder.ivCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:" + currentItem.phone)
                it.context.startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        fun updateData(newData: MutableList<Contact>) {
            data = newData
            notifyDataSetChanged()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FirstFragment()
    }
}