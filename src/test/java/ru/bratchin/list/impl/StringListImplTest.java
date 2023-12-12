package ru.bratchin.list.impl;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.bratchin.list.exception.IndexIncorrectException;
import ru.bratchin.list.exception.IndexOutsideTheArrayException;

import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class StringListImplTest {

    private StringListImpl stringList;

    private static Field fieldArr;

    private static Field fieldSize;


    @BeforeAll
    public static void setup() throws NoSuchFieldException {
        fieldArr = StringListImpl.class.getDeclaredField("arr");
        fieldArr.setAccessible(true);
        fieldSize = StringListImpl.class.getDeclaredField("size");
        fieldSize.setAccessible(true);
    }

    @Nested
    class Error {
        @Test
        void create() {
            Throwable thrown = catchThrowable(() -> new StringListImpl(-1));

            assertThat(thrown).isInstanceOf(IndexIncorrectException.class);
        }

        @Nested
        class IncorrectIndex{

            @ParameterizedTest
            @CsvSource(
                    value = {
                            "0",
                            "1"
                    }
            )
            void add(int index){
                stringList = new StringListImpl(0);

                Throwable thrown = catchThrowable(() -> stringList.add(index, "test"));

                assertThat(thrown).isInstanceOf(IndexOutsideTheArrayException.class);
            }

            @ParameterizedTest
            @CsvSource(
                value = {
                        "4",
                        "5"
                }
            )
            void set(int index){
                stringList = new StringListImpl(
                        new String[]{
                                "1", "3", "3", "4"
                        }
                );

                Throwable thrown = catchThrowable(() -> stringList.set(index, "test"));

                assertThat(thrown).isInstanceOf(IndexOutsideTheArrayException.class);
            }

            @Test
            void remove(){
                stringList = new StringListImpl(
                        new String[]{
                                "1", "3", "3", "4"
                        }
                );

                Throwable thrown = catchThrowable(() -> stringList.remove(7));

                assertThat(thrown).isInstanceOf(IndexOutsideTheArrayException.class);
            }

            @Test
            void get(){
                stringList = new StringListImpl(
                        new String[]{
                                "1", "3", "3", "4"
                        }
                );

                Throwable thrown = catchThrowable(() -> stringList.get(7));

                assertThat(thrown).isInstanceOf(IndexOutsideTheArrayException.class);
            }

        }

        @Nested
        class IncorrectItem{

            @BeforeEach
            public void initEach() {
                stringList = new StringListImpl(
                        new String[]{
                                "1", "3", "3", "4"
                        }
                );
            }
            @Test
            void set(){
                Throwable thrown = catchThrowable(() -> stringList.set(0, null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void add(){
                Throwable thrown = catchThrowable(() -> stringList.add(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void lastIndexOf(){
                Throwable thrown = catchThrowable(() -> stringList.lastIndexOf(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void indexOf(){
                Throwable thrown = catchThrowable(() -> stringList.indexOf(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void contains(){
                Throwable thrown = catchThrowable(() -> stringList.contains(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

            @Test
            void remove(){
                Throwable thrown = catchThrowable(() -> stringList.remove(null));

                assertThat(thrown).isInstanceOf(IllegalArgumentException.class);
            }

        }

        @Test
        void remove(){
            stringList = new StringListImpl(
                    new String[]{
                            "1", "3", "3", "4"
                    }
            );

            Throwable thrown = catchThrowable(() -> stringList.remove("test"));

            assertThat(thrown).isInstanceOf(NoSuchElementException.class);
        }

    }

    @Nested
    class Success {

        @Nested
        class ListIsFull {

            @BeforeEach
            public void initEach() throws IllegalAccessException {
                stringList = new StringListImpl(0);
                fieldArr.set(stringList, new String[]{
                        "1", "3", "3", "4"
                });
                fieldSize.setInt(stringList, 4);
            }
            @Test
            void add() throws IllegalAccessException {
                String result = stringList.add("test");

                assertThat(result).isEqualTo("test");
                var arr = (String[]) fieldArr.get(stringList);
                assertThat(arr).contains("test");
                assertThat(arr).startsWith("1", "3", "3", "4", "test");
                var tek = (Integer) fieldSize.get(stringList);
                assertThat(tek).isEqualTo(5);
            }
        }
        @Nested
        class ListIsNotEmpty {
            @BeforeEach
            public void initEach() {
                stringList = new StringListImpl(
                        new String[]{
                                "1", "3", "3", "4"
                        }
                );
            }

            @Test
            void equalsTrue() {
                boolean contains = stringList.equals(
                        new StringListImpl(
                                new String[]{
                                        "1", "3", "3", "4"
                                }
                        )
                );

                assertThat(contains).isTrue();
            }

            @Test
            void equalsFalse() {
                boolean contains = stringList.equals(
                        new StringListImpl(
                                new String[]{
                                        "1", "3", "3"
                                }
                        )
                );

                assertThat(contains).isFalse();
            }

            @Test
            void clear() throws IllegalAccessException {
                stringList.clear();

                var tek = (Integer) fieldSize.get(stringList);
                assertThat(tek).isEqualTo(0);
            }

            @Test
            void add() throws IllegalAccessException {
                String result = stringList.add("test");

                assertThat(result).isEqualTo("test");
                var arr = (String[]) fieldArr.get(stringList);
                assertThat(arr).contains("test");
                assertThat(arr).startsWith("1", "3", "3", "4", "test");
                var tek = (Integer) fieldSize.get(stringList);
                assertThat(tek).isEqualTo(5);
            }

            @Test
            void addIndex() throws IllegalAccessException {
                String result = stringList.add(1, "test");

                assertThat(result).isEqualTo("test");
                var arr = (String[]) fieldArr.get(stringList);
                assertThat(arr).contains("test");
                assertThat(arr).startsWith("1", "test", "3", "3", "4" );
                var tek = (Integer) fieldSize.get(stringList);
                assertThat(tek).isEqualTo(5);
            }

            @Test
            void set() throws IllegalAccessException {
                String str = stringList.set(0, "3");

                assertThat(str).isEqualTo("3");
                var arr = (String[]) fieldArr.get(stringList);
                assertThat(arr).doesNotContain("2");
                assertThat(arr).startsWith("3", "3", "3", "4" );
                var tek = (Integer) fieldSize.get(stringList);
                assertThat(tek).isEqualTo(4);
            }

            @Test
            void removeByIndex() throws IllegalAccessException {
                String str = stringList.remove(0);

                assertThat(str).isEqualTo("1");
                var arr = (String[]) fieldArr.get(stringList);
                assertThat(arr).doesNotContain("1");
                assertThat(arr).startsWith("3", "3", "4" );
                var tek = (Integer) fieldSize.get(stringList);
                assertThat(tek).isEqualTo(3);
            }

            @Test
            void remove() throws IllegalAccessException {
                String str = stringList.remove("3");

                assertThat(str).isEqualTo("3");
                var arr = (String[]) fieldArr.get(stringList);
                assertThat(arr).startsWith("1", "3", "4" );
                var tek = (Integer) fieldSize.get(stringList);
                assertThat(tek).isEqualTo(3);
            }

            @Test
            void toArray() {
                String[] array = stringList.toArray();

                assertThat(array).containsExactly("1", "3", "3", "4");
            }

            @Test
            void containsTrue() {
                boolean contains = stringList.contains("4");

                assertThat(contains).isTrue();
            }

            @Test
            void containsFalse() {
                boolean contains = stringList.contains("5");

                assertThat(contains).isFalse();
            }

            @Test
            void indexIsPresent() {
                int index = stringList.indexOf("3");

                assertThat(index).isEqualTo(1);
            }

            @Test
            void indexIsNotPresent() {
                int index = stringList.indexOf("5");

                assertThat(index).isEqualTo(-1);
            }

            @Test
            void lastIndexIsPresent() {
                int index = stringList.lastIndexOf("3");

                assertThat(index).isEqualTo(2);
            }

            @Test
            void lastIndexIsNotPresent() {
                int index = stringList.lastIndexOf("5");

                assertThat(index).isEqualTo(-1);
            }

            @Test
            void get() {
                String str = stringList.get(2);

                assertThat(str).isEqualTo("3");
            }

            @Test
            void size() {
                int size = stringList.size();

                assertThat(size).isEqualTo(4);
            }

            @Test
            void isEmpty() {
                boolean b = stringList.isEmpty();

                assertThat(b).isFalse();
            }

        }

        @Nested
        class ListIsEmpty {
            @BeforeEach
            public void initEach() {
                stringList = new StringListImpl(0);
            }

            @Test
            void equalsTrue() {
                boolean contains = stringList.equals(
                        new StringListImpl(0)
                );

                assertThat(contains).isTrue();
            }

            @Test
            void equalsFalse() {
                boolean contains = stringList.equals(
                        new StringListImpl(
                                new String[]{
                                        "1", "3", "3"
                                }
                        )
                );

                assertThat(contains).isFalse();
            }

            @Test
            void add() throws IllegalAccessException {
                String result = stringList.add("test");

                assertThat(result).isEqualTo("test");
                var arr = (String[]) fieldArr.get(stringList);
                assertThat(arr).contains("test");
                assertThat(arr).startsWith("test");
                var tek = (Integer) fieldSize.get(stringList);
                assertThat(tek).isEqualTo(1);
            }

            @Test
            void isEmpty() {
                boolean b = stringList.isEmpty();

                assertThat(b).isTrue();
            }

            @Test
            void size() {
                int size = stringList.size();

                assertThat(size).isEqualTo(0);
            }


        }

        @Test
        void createEmpty() throws IllegalAccessException {
            StringListImpl stringList = new StringListImpl(0);

            assertThat(stringList.isEmpty()).isTrue();
            var arr = (String[]) fieldArr.get(stringList);
            assertThat(arr.length).isEqualTo(0);
            var tek = (Integer) fieldSize.get(stringList);
            assertThat(tek).isEqualTo(0);

        }


    }


    @Test
    void testRemove() {
    }

    @Test
    void testEquals() {
    }



    @Test
    void toArray() {
    }
}