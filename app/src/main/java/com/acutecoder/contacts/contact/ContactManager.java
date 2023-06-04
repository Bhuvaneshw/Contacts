package com.acutecoder.contacts.contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.ArraySet;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

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
    public static ContactList getContactList(Context context) {
        ContactList list = new ContactList();
        Cursor cur = context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(getColumnIndex(cur, ContactsContract.Contacts._ID));
                String name = cur.getString(getColumnIndex(cur, ContactsContract.Contacts.DISPLAY_NAME));
                list.add(new Contact(id, name, Contact.Companion.getRandomColor()));
            }
        }
        if (cur != null) {
            cur.close();
        }
        return list;
    }

    public static ContactDetail getContactDetail(Context context, Contact contact) {
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cur = getCursorFor(contentResolver, contact.getId());
        if (cur.isClosed()) return null;

        String name = cur.getString(getColumnIndex(cur, ContactsContract.Contacts.DISPLAY_NAME));
        ArraySet<String> numbers = new ArraySet<>();
        int columnIndex = cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        if (columnIndex >= 0 && cur.getInt(columnIndex) > 0) {
            Cursor pCur = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[]{contact.getId()}, null);
            if (pCur != null) {
                while (pCur.moveToNext()) {
                    columnIndex = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    if (columnIndex >= 0) {
                        String phoneNo = pCur.getString(columnIndex);
                        numbers.add(phoneNo);
                    }
                }
                pCur.close();
            }
        }
        return new ContactDetail(contact.getId(), name, numbers, contact.getColor());
    }

    private static Cursor getCursorFor(ContentResolver contentResolver, String id) {
        String cContactIdString = ContactsContract.Contacts._ID;
        Uri cCONTACT_CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;

        String selection = cContactIdString + " = ? ";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        Cursor cursor = contentResolver.query(cCONTACT_CONTENT_URI, null, selection, selectionArgs, null);
        if ((cursor != null) && (cursor.getCount() > 0)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int columnIndex = cursor.getColumnIndex(cContactIdString);
                if (columnIndex >= 0) {
                    if (id.equals(cursor.getString(columnIndex))) {
                        break;
                    }
                }
                cursor.moveToNext();
            }
        }
        return cursor;
    }

    @IntRange(from = 0L)
    private static int getColumnIndex(Cursor cur, String columnName) {
        int index = cur.getColumnIndex(columnName);
        return Math.max(index, 0);
    }

    @NonNull
    public static CompleteContactList getCompleteContactList(@NotNull Activity activity) {
        CompleteContactList list = new CompleteContactList();
        Cursor cur = activity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(getColumnIndex(cur, ContactsContract.Contacts._ID));
                String name = cur.getString(getColumnIndex(cur, ContactsContract.Contacts.DISPLAY_NAME));
                ArrayList<String> numbers = new ArrayList<>();
                int columnIndex = cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                if (columnIndex >= 0 && cur.getInt(columnIndex) > 0) {
                    ContentResolver contentResolver = activity.getContentResolver();
                    Cursor pCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (pCur != null) {
                        while (pCur.moveToNext()) {
                            columnIndex = pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                            if (columnIndex >= 0) {
                                String phoneNo = pCur.getString(columnIndex);
                                if (!numbers.contains(phoneNo))
                                    numbers.add(phoneNo);
                            }
                        }
                        pCur.close();
                    }
                }
                list.add(new CompleteContactList.CompleteContact(name, numbers));
            }
        }
        if (cur != null) {
            cur.close();
        }
        list.sort();
        return list;
    }
}
