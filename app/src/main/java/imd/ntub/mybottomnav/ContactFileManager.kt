package imd.ntub.mybottomnav

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class ContactFileManager(context: Context) {
    private val dbHelper = ContactDatabaseHelper(context)

    fun saveContacts(contacts: List<Contact>) {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM ${ContactDatabaseHelper.TABLE_NAME}") // 清空表格

        for (contact in contacts) {
            val values = ContentValues().apply {
                put(ContactDatabaseHelper.COLUMN_NAME, contact.name)
                put(ContactDatabaseHelper.COLUMN_PHONE, contact.phone)
            }
            db.insert(ContactDatabaseHelper.TABLE_NAME, null, values)
        }
        db.close()
    }

    fun loadContacts(): MutableList<Contact> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            ContactDatabaseHelper.TABLE_NAME,
            null, null, null, null, null, null
        )

        val contacts = mutableListOf<Contact>()
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(ContactDatabaseHelper.COLUMN_NAME))
                val phone = getString(getColumnIndexOrThrow(ContactDatabaseHelper.COLUMN_PHONE))
                contacts.add(Contact(name, phone))
            }
        }
        cursor.close()
        db.close()
        return contacts
    }
}