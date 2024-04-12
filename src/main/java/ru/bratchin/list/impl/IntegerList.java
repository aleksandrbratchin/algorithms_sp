package ru.bratchin.list.impl;

import ru.bratchin.list.api.MyList;
import ru.bratchin.list.api.NumberList;
import ru.bratchin.list.exception.IndexIncorrectException;
import ru.bratchin.list.exception.IndexOutsideTheArrayException;
import ru.bratchin.list.exception.MyIllegalArgumentException;
import ru.bratchin.list.exception.MyNoSuchElementException;

import java.util.Arrays;
import java.util.Optional;

public class IntegerList extends NumberList<Integer> {

    private Integer[] arr;

    private int size = 0;

    public IntegerList(int length) {
        if (length < 0) {
            throw new IndexIncorrectException();
        }
        arr = new Integer[length];
    }

    public IntegerList(Integer[] arr) {
        this.size = arr.length;
        this.arr = new Integer[arr.length * 2];
        System.arraycopy(arr, 0, this.arr, 0, arr.length);
    }

    public Integer add(Integer item) {
        checkItem(item);
        if (arr.length == 0) {
            arr = new Integer[1];
            arr[0] = item;
            size++;
            return arr[0];
        }
        return addToArray(size, item);
    }

    public Integer add(int index, Integer item) {
        checkIndex(index);
        checkItem(item);
        return addToArray(index, item);
    }

    private Integer addToArray(int index, Integer item) {
        Integer[] newArr = new Integer[arr.length];
        if (size + 1 > arr.length) {
            newArr = new Integer[arr.length * 2];
        }
        System.arraycopy(arr, 0, newArr, 0, index);
        newArr[index] = item;
        System.arraycopy(arr, index, newArr, index + 1, size - index);
        arr = newArr;
        size++;
        return arr[index];
    }

    public Integer set(int index, Integer item) {
        checkIndex(index);
        checkItem(item);

        arr[index] = item;
        return arr[index];
    }

    public Integer remove(Integer item) {
        checkItem(item);
        int index = indexOf(item);
        if (index == -1) {
            throw new MyNoSuchElementException();
        }
        return remove(index);
    }

    public Integer remove(int index) {
        index++;
        checkIndex(index);
        Integer tmp = arr[index - 1];
        System.arraycopy(arr, index, arr, index - 1, arr.length - size);
        size--;
        return tmp;
    }

    public boolean contains(Integer element) {
        int min = 0;
        int max = size - 1;


        while (min <= max) {
            int mid = (min + max) / 2;


            if (element.equals(arr[mid])) {
                return true;
            }


            if (element < arr[mid]) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }
        }
        return false;
    }


    public int indexOf(Integer item) {
        checkItem(item);
        for (int i = 0; i < size; i++) {
            if (item.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(Integer item) {
        checkItem(item);
        for (int i = size - 1; i >= 0; i--) {
            if (item.equals(arr[i])) {
                return i;
            }
        }
        return -1;
    }

    public Integer get(int index) {
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
        arr = new Integer[1];
        size = 0;
    }

    public Integer[] toArray() {
        Integer[] newArr = new Integer[size];
        System.arraycopy(arr, 0, newArr, 0, size);
        return newArr;
    }

    private void checkIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutsideTheArrayException();
        }
    }

    private void checkItem(Integer item) {
        Optional.ofNullable(item).orElseThrow(MyIllegalArgumentException::new);
    }

    @Override
    public Integer[] sort() {
        sortSelection(arr);
        return arr;
    }

    public void sortBubble(Integer[] arr) {
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    swapElements(arr, j, j + 1);
                }
            }
        }
    }

    public void sortInsertion(Integer[] arr) {
        for (int i = 1; i < size; i++) {
            int temp = arr[i];
            int j = i;
            while (j > 0 && arr[j - 1] >= temp) {
                arr[j] = arr[j - 1];
                j--;
            }
            arr[j] = temp;
        }
    }

    public void sortSelection(Integer[] arr) {
        for (int i = 0; i < size - 1; i++) {
            int minElementIndex = i;
            for (int j = i + 1; j < size; j++) {
                if (arr[j] < arr[minElementIndex]) {
                    minElementIndex = j;
                }
            }
            swapElements(arr, i, minElementIndex);
        }
    }

}
