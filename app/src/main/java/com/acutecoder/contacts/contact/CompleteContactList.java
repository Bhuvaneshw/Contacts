package com.acutecoder.contacts.contact;

import android.util.ArraySet;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Bhuvaneshwaran
 * on 5:27 PM, 6/4/2023
 *
 * @author AcuteCoder
 */

public class CompleteContactList extends ArrayList<CompleteContactList.CompleteContact> {

    public void sort() {
        Collections.sort(this, (contact1, contact2) -> contact1.getName().compareTo(contact2.getName()));
    }

    public static class CompleteContact {
        private String name;
        private ArrayList<String> numbers;

        public CompleteContact(String name, ArrayList<String> numbers) {
            this.name = name;
            this.numbers = numbers;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<String> getNumbers() {
            return numbers;
        }

        public void setNumbers(ArrayList<String> numbers) {
            this.numbers = numbers;
        }
    }
}
