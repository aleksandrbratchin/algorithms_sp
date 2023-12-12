package ru.bratchin.list.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.bratchin.list.exception.IndexIncorrectException;
import ru.bratchin.list.exception.IndexOutsideTheArrayException;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class IntegerListTest {

    private IntegerList integerList;

    private static Field fieldArr;

    private static Field fieldSize;


    @BeforeAll
    public static void setup() throws NoSuchFieldException {
        fieldArr = IntegerList.class.getDeclaredField("arr");
        fieldArr.setAccessible(true);
        fieldSize = IntegerList.class.getDeclaredField("size");
        fieldSize.setAccessible(true);
    }

    @Nested
    @Disabled
    class FastSort {
        @Test
        void sortTest() {
            int limit = 100_000;
            Integer[] arr1 = new Random()
                    .ints(0, limit)
                    .limit(limit)
                    .boxed()
                    .toArray(Integer[]::new);

            IntegerList integerList1 = new IntegerList(arr1);
            IntegerList integerList2 = new IntegerList(arr1);
            IntegerList integerList3 = new IntegerList(arr1);
            long start = System.currentTimeMillis();
            integerList1.sortBubble(integerList1.toArray());
            long time1 = System.currentTimeMillis() - start;
            start = System.currentTimeMillis();
            integerList2.sortInsertion(integerList2.toArray());
            long time2 = System.currentTimeMillis() - start;
            start = System.currentTimeMillis();
            integerList3.sortSelection(integerList3.toArray());
            long time3 = System.currentTimeMillis() - start;

            System.out.println("sortBubble = " + time1);
            System.out.println("sortInsertion = " + time2);
            System.out.println("sortSelection = " + time3);
        }
    }

    @Nested
    class TestSort {
        @Test
        void sort() throws IllegalAccessException {
            integerList = new IntegerList(
                    new Integer[]{
                            4, 3, 2, 1, 0
                    }
            );

            integerList.sort();

            var arr = (Integer[]) fieldArr.get(integerList);
            assertThat(arr).startsWith(0, 1, 2, 3, 4);
        }
    }

    @Nested
    class Error {
        @Test
        void create() {
            Throwable thrown = catchThrowable(() -> new IntegerList(-1));

            assertThat(thrown).isInstanceOf(IndexIncorrectException.class);
        }

        @Nested
        class IncorrectIndex {

            @ParameterizedTest
            @CsvSource(
                    value = {
                            "0",
                            "1"
                    }
            )
            void add(int index) {
                integerList = new IntegerList(0);

                Throwable thrown = catchThrowable(() -> integerList.add(index, 10));

                assertThat(thrown).isInstanceOf(IndexOutsideTheArrayException.class);
            }

            @ParameterizedTest
            @CsvSource(
                    value = {
                            "4",
                            "5"
                    }
            )
            void set(int index) {
                integerList = new IntegerList(
                        new Integer[]{
                                1, 2, 3, 4
                        }
                );

                Throwable thrown = catchThrowable(() -> integerList.set(index, 10));

                assertThat(thrown).isInstanceOf(IndexOutsideTheArrayException.class);
            }

            @Test
            void remove() {
                integerList = new IntegerList(
                        new Integer[]{
                                1, 2, 3, 4
                        }
                );

                Throwable thrown = catchThrowable(() -> integerList.remove(7));

                assertThat(thrown).isInstanceOf(IndexOutsideTheArrayException.class);
            }

            @Test
            void get() {
                integerList = new IntegerList(
                        new Integer[]{
                                1, 2, 3, 4
                        }
                );

                Throwable thrown = catchThrowable(() -> integerList.get(7));

                assertThat(thrown).isInstanceOf(IndexOutsideTheArrayException.class);
            }

        }

        @Nested
        class IncorrectItem {

            @BeforeEach
            public void initEach() {
                integerList = new IntegerList(
                        new Integer[]{
                                1, 2, 3, 4
                        }
                );
            }

            @Test
            void set() {
                Throwable thrown = catchThrowable(() -> integerList.set(0, null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void add() {
                Throwable thrown = catchThrowable(() -> integerList.add(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void lastIndexOf() {
                Throwable thrown = catchThrowable(() -> integerList.lastIndexOf(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void indexOf() {
                Throwable thrown = catchThrowable(() -> integerList.indexOf(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void contains() {
                Throwable thrown = catchThrowable(() -> integerList.contains(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void remove() {
                Throwable thrown = catchThrowable(() -> integerList.remove(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

        }

        @Test
        void remove() {
            integerList = new IntegerList(
                    new Integer[]{
                            1, 2, 3, 4
                    }
            );

            Throwable thrown = catchThrowable(() -> integerList.remove(Integer.valueOf(10)));

            assertThat(thrown).isInstanceOf(NoSuchElementException.class);
        }

    }

    @Nested
    class Success {

        @Nested
        class ListIsFull {

            @BeforeEach
            public void initEach() throws IllegalAccessException {
                integerList = new IntegerList(0);
                fieldArr.set(integerList, new Integer[]{
                        1, 2, 3, 4
                });
                fieldSize.setInt(integerList, 4);
            }

            @Test
            void add() throws IllegalAccessException {
                Integer result = integerList.add(10);

                assertThat(result).isEqualTo(10);
                var arr = (Integer[]) fieldArr.get(integerList);
                assertThat(arr).contains(10);
                assertThat(arr).startsWith(1, 2, 3, 4, 10);
                var tek = (Integer) fieldSize.get(integerList);
                assertThat(tek).isEqualTo(5);
            }
        }

        @Nested
        class ListIsNotEmpty {
            @BeforeEach
            public void initEach() {
                integerList = new IntegerList(
                        new Integer[]{
                                1, 2, 3, 4
                        }
                );
            }

            @Test
            void equalsTrue() {
                boolean contains = integerList.equals(
                        new IntegerList(
                                new Integer[]{
                                        1, 2, 3, 4
                                }
                        )
                );

                assertThat(contains).isTrue();
            }

            @Test
            void equalsFalse() {
                boolean contains = integerList.equals(
                        new IntegerList(
                                new Integer[]{
                                        1, 3, 3
                                }
                        )
                );

                assertThat(contains).isFalse();
            }

            @Test
            void clear() throws IllegalAccessException {
                integerList.clear();

                var tek = (Integer) fieldSize.get(integerList);
                assertThat(tek).isEqualTo(0);
            }

            @Test
            void add() throws IllegalAccessException {
                Integer result = integerList.add(10);

                assertThat(result).isEqualTo(10);
                var arr = (Integer[]) fieldArr.get(integerList);
                assertThat(arr).contains(10);
                assertThat(arr).startsWith(1, 2, 3, 4, 10);
                var tek = (Integer) fieldSize.get(integerList);
                assertThat(tek).isEqualTo(5);
            }

            @Test
            void addIndex() throws IllegalAccessException {
                Integer result = integerList.add(1, 10);

                assertThat(result).isEqualTo(10);
                var arr = (Integer[]) fieldArr.get(integerList);
                assertThat(arr).contains(10);
                assertThat(arr).startsWith(1, 10, 2, 3, 4);
                var tek = (Integer) fieldSize.get(integerList);
                assertThat(tek).isEqualTo(5);
            }

            @Test
            void set() throws IllegalAccessException {
                Integer str = integerList.set(1, 3);

                assertThat(str).isEqualTo(3);
                var arr = (Integer[]) fieldArr.get(integerList);
                assertThat(arr).doesNotContain(2);
                assertThat(arr).startsWith(1, 3, 3, 4);
                var tek = (Integer) fieldSize.get(integerList);
                assertThat(tek).isEqualTo(4);
            }

            @Test
            void removeByIndex() throws IllegalAccessException {
                Integer str = integerList.remove(0);

                assertThat(str).isEqualTo(1);
                var arr = (Integer[]) fieldArr.get(integerList);
                assertThat(arr).doesNotContain(1);
                assertThat(arr).startsWith(2, 3, 4);
                var tek = (Integer) fieldSize.get(integerList);
                assertThat(tek).isEqualTo(3);
            }

            @Test
            void remove() throws IllegalAccessException {
                Integer str = integerList.remove(Integer.valueOf(3));

                assertThat(str).isEqualTo(3);
                var arr = (Integer[]) fieldArr.get(integerList);
                assertThat(arr).startsWith(1, 2, 4);
                var tek = (Integer) fieldSize.get(integerList);
                assertThat(tek).isEqualTo(3);
            }

            @Test
            void toArray() {
                Integer[] array = integerList.toArray();

                assertThat(array).containsExactly(1, 2, 3, 4);
            }

            @Test
            void containsTrue() {
                boolean contains = integerList.contains(4);

                assertThat(contains).isTrue();
            }

            @Test
            void containsFalse() {
                boolean contains = integerList.contains(5);

                assertThat(contains).isFalse();
            }

            @Test
            void indexIsPresent() {
                int index = integerList.indexOf(3);

                assertThat(index).isEqualTo(2);
            }

            @Test
            void indexIsNotPresent() {
                int index = integerList.indexOf(5);

                assertThat(index).isEqualTo(-1);
            }

            @Test
            void lastIndexIsPresent() {
                int index = integerList.lastIndexOf(3);

                assertThat(index).isEqualTo(2);
            }

            @Test
            void lastIndexIsNotPresent() {
                int index = integerList.lastIndexOf(5);

                assertThat(index).isEqualTo(-1);
            }

            @Test
            void get() {
                Integer str = integerList.get(2);

                assertThat(str).isEqualTo(3);
            }

            @Test
            void size() {
                int size = integerList.size();

                assertThat(size).isEqualTo(4);
            }

            @Test
            void isEmpty() {
                boolean b = integerList.isEmpty();

                assertThat(b).isFalse();
            }

        }

        @Nested
        class ListIsEmpty {
            @BeforeEach
            public void initEach() {
                integerList = new IntegerList(0);
            }

            @Test
            void equalsTrue() {
                boolean contains = integerList.equals(
                        new IntegerList(0)
                );

                assertThat(contains).isTrue();
            }

            @Test
            void equalsFalse() {
                boolean contains = integerList.equals(
                        new IntegerList(
                                new Integer[]{
                                        1, 3, 3
                                }
                        )
                );

                assertThat(contains).isFalse();
            }

            @Test
            void add() throws IllegalAccessException {
                Integer result = integerList.add(10);

                assertThat(result).isEqualTo(10);
                var arr = (Integer[]) fieldArr.get(integerList);
                assertThat(arr).contains(10);
                assertThat(arr).startsWith(10);
                var tek = (Integer) fieldSize.get(integerList);
                assertThat(tek).isEqualTo(1);
            }

            @Test
            void isEmpty() {
                boolean b = integerList.isEmpty();

                assertThat(b).isTrue();
            }

            @Test
            void size() {
                int size = integerList.size();

                assertThat(size).isEqualTo(0);
            }


        }

        @Test
        void createEmpty() throws IllegalAccessException {
            IntegerList stringList = new IntegerList(0);

            assertThat(stringList.isEmpty()).isTrue();
            var arr = (Integer[]) fieldArr.get(stringList);
            assertThat(arr.length).isEqualTo(0);
            var tek = (Integer) fieldSize.get(stringList);
            assertThat(tek).isEqualTo(0);

        }


    }


}