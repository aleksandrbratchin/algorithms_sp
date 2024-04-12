package ru.bratchin.list.api;

public abstract class NumberList<T extends Number> implements MyList<T> {
    public abstract T[] sort();

    public void swapElements(T[] arr, int indexA, int indexB) {
        T tmp = arr[indexA];
        arr[indexA] = arr[indexB];
        arr[indexB] = tmp;
    }

}
