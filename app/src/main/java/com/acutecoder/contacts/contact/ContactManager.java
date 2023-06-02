package com.acutecoder.contacts.contact;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.annotation.IntRange;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by Bhuvaneshwaran
 * on 11:48 PM, 6/2/2023
 *
 * @author AcuteCoder
 */
public final class ContactManager {

    @NotNull
    public static ContactList getContactList(@NotNull ContentResolver contentResolver) {
        ContactList list = new ContactList();
        Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(getColumnIndex(cur, ContactsContract.Contacts._ID));
                String name = cur.getString(getColumnIndex(cur, ContactsContract.Contacts.DISPLAY_NAME));

                ArrayList<String> numbers = new ArrayList<>();
                if (cur.getInt(getColumnIndex(cur, ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            String phoneNo = pCur.getString(getColumnIndex(cur, ContactsContract.CommonDataKinds.Phone.NUMBER));
                            numbers.add(phoneNo);
                        }
                        pCur.close();
                    }
                }
                list.add(new Contact(name, numbers));
            }
        }
        if (cur != null) {
            cur.close();
        }
        return list;
    }

    @IntRange(from = 0L)
    private static int getColumnIndex(Cursor cur, String columnName) {
        int index = cur.getColumnIndex(columnName);
        return Math.max(index, 0);
    }
}
