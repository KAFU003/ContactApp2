package imd.ntub.mybottomnav.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import imd.ntub.mybottomnav.Contact

class ContactViewModel : ViewModel() {
    private val _contacts = MutableLiveData<MutableList<Contact>>(mutableListOf())
    val contacts: LiveData<MutableList<Contact>> get() = _contacts

    fun addContact(contact: Contact) {
        val currentContacts = _contacts.value ?: mutableListOf()
        currentContacts.add(contact)
        _contacts.postValue(currentContacts)
    }
    fun setContacts(contacts: MutableList<Contact>) {
        _contacts.value = contacts
    }
}