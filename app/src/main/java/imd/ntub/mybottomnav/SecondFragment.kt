package imd.ntub.mybottomnav


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import imd.ntub.mybottomnav.viewmodel.ContactViewModel

class SecondFragment : Fragment() {
    private val contactViewModel: ContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        val edtName = view.findViewById<EditText>(R.id.edtName)
        val edtPhone = view.findViewById<EditText>(R.id.edtPhone)
        val btnAdd = view.findViewById<Button>(R.id.btnAdd)

        btnAdd.setOnClickListener {
            val name = edtName.text.toString()
            val phone = edtPhone.text.toString()
            if (name.isNotEmpty() && phone.isNotEmpty()) {
                contactViewModel.addContact(Contact(name, phone))
                edtName.text.clear()
                edtPhone.text.clear()
            }
        }
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = SecondFragment()
    }
}
