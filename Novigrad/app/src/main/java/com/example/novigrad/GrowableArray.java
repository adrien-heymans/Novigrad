package com.example.novigrad;


    public class GrowableArray {

        private String[] data;
        private int size = 0;

        // construct with initial capacity
        public GrowableArray(int capacity) {

            data = new String[capacity];
        }

        // double the capacity of the array
        private void grow() {

            String[] tmp= new String[2*data.length];

            for (int i=0; i< size; i++)
                tmp[i]= data[i];

            data= tmp;
        }

        // add a new integer (most efficient)
        public boolean add(String value) {

            if (size == data.length)
                grow();

            data[size++]= value;

            return true;
        }


        // search if a given integer is in the array
        public int search(String value) {

            int i=0;
            while (i<size && data[i].equals(value) == false)
                i++;

            if (i==size) {
                return -1;
            } else {
                return i;
            }
        }

        // get the element at a given index
        public String elementAt(int index) {

            return data[index];
        }

    }

