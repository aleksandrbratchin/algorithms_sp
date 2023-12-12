package ru.bratchin.list.impl;

import ru.bratchin.list.api.MyList;
import ru.bratchin.list.exception.IndexIncorrectException;
import ru.bratchin.list.exception.IndexOutsideTheArrayException;
import ru.bratchin.list.exception.MyIllegalArgumentException;
import ru.bratchin.list.exception.MyNoSuchElementException;

import java.util.Arrays;
import java.util.Optional;

public class StringList implements MyList<String> {

    private String[] arr;

    private int size = 0;

    public StringList(int length) {
        if (length < 0) {
            throw new IndexIncorrectException();
        }
        arr = new String[length];
    }

    public StringList(String[] arr) {
        this.size = arr.length;
        this.arr = new String[arr.length * 2];
        System.arraycopy(arr, 0, this.arr, 0, arr.length);
    }

    public String add(String item) {
        checkItem(item);
        if (arr.length == 0) {
            arr = new String[1];
            arr[0] = item;
            size++;
            return arr[0];
        }
        return addToArray(size, item);
    }

    public String add(int index, String item) {
        checkIndex(index);
        checkItem(item);
        return addToArray(index, item);
    }

    private String addToArray(int index, String item) {
        String[] newArr = new String[arr.length];
        if (size + 1 > arr.length) {
            newArr = new String[arr.length * 2];
        }
        System.arraycopy(arr, 0, newArr, 0, index);
        newArr[index] = item;
        System.arraycopy(arr, index, newArr, index + 1, size - index);
        arr = newArr;
        size++;
        return arr[index];
    }

    public String set(int index, String item) {
        checkIndex(index);
        checkItem(item);

        arr[index] = item;
        return arr[index];
    }

    public String remove(String item) {
        checkItem(item);
        int index = indexOf(item);
        if (index == -1) {
            throw new MyNoSuchElementException();
        }
        return remove(index);
    }

    public String remove(int index) {
        index++;
        checkIndex(index);
        String tmp = arr[index - 1];
        System.arraycopy(arr, index, arr, index - 1, arr.length - size);
        size--;
        return tmp;
    }

    public boolean contains(String item) {
        checkItem(item);
        for (int i = 0; i < size; i++) {
            if (item.equals(arr[i])) {
                return true;
            }
        }
        return false;
    }

    public int indexOf(String item) {
        checkItem(item);
        for (int i = 0; i < size; i++) {
            if (item.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(String item) {
        checkItem(item);
        for (int i = size - 1; i >= 0; i--) {
            if (item.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

    public String get(int index) {
        checkIndex(index);
        return arr[index];
    }

    @Override
    public boolean equals(MyList otherList) {
        return Arrays.equals(otherList.toArray(), this.toArray());
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        arr = new String[1];
        size = 0;
    }

    public String[] toArray() {
        String[] newArr = new String[size];
        System.arraycopy(arr, 0, newArr, 0, size);
        return newArr;
    }

    private void checkIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutsideTheArrayException();
        }
    }

    private void checkItem(String item) {
        Optional.ofNullable(item).orElseThrow(MyIllegalArgumentException::new);
    }

}
