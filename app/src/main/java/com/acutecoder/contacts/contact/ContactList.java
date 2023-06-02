package com.acutecoder.contacts.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Bhuvaneshwaran
 * on 11:53 PM, 6/2/2023
 *
 * @author AcuteCoder
 */

public class ContactList extends ArrayList<Contact> {

    private int contactSize = 0;

    public void sort() {
        Collections.sort(this, (contact1, contact2) -> contact1.getName().compareTo(contact2.getName()));
    }

    public void updateContactSize() {
        contactSize = size();
    }

    public int getContactSize() {
        return contactSize;
    }

    public void insertHeaders() {
        char letter = '\0';
        ArrayList<HashMap<String, String>> inserts = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            Contact contact = get(i);
            if (contact.getName().charAt(0) != letter) {
                letter = contact.getName().charAt(0);
                HashMap<String, String> map = new HashMap<>();
                map.put("index", String.valueOf(i));
                map.put("value", String.valueOf(letter));
                inserts.add(map);
            }
        }
        for (int j = 0; j < inserts.size(); j++) {
            HashMap<String, String> map = inserts.get(j);
            int i = Integer.parseInt(Objects.requireNonNull(map.get("index")));
            String title = map.get("value");
            add(i + j, new Title(title));
        }
    }
}
