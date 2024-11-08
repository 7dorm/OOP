package ru.nsu.dunaev;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MainTest {
    private HashTable<String, Number> hashTable;

    @BeforeEach
    public void setup() {
        hashTable = new HashTable<>();
    }

    @Test
    public void mainTest(){
        Main.main(new String[]{"test"});
        assertTrue(true);
    }

    @Test
    public void testCreateHashTable() {
        assertNotNull(hashTable);
    }

    @Test
    public void testPutAndGet() {
        hashTable.put("one", 1);
        assertEquals(1, hashTable.get("one"));

        hashTable.put("two", 2.0);
        assertEquals(2.0, hashTable.get("two"));
    }

    @Test
    public void testUpdate() {
        hashTable.put("one", 1);
        hashTable.put("one", 1.0);
        assertEquals(1.0, hashTable.get("one"));
    }

    @Test
    public void testRemove() {
        hashTable.put("one", 1);
        assertEquals(1, hashTable.get("one"));

        hashTable.remove("one");
        assertThrows(NoSuchElementException.class, () -> {hashTable.get("one");});
        hashTable.remove("one");
    }

    @Test
    public void testContainsKey() {
        hashTable.put("one", 1);
        assertTrue(hashTable.containsKey("one"));

        hashTable.remove("one");
        assertThrows(NoSuchElementException.class, () -> {hashTable.containsKey("one");});
    }

    @Test
    public void testSize() {
        assertEquals(0, hashTable.size(), "Размер пустой хеш-таблицы должен быть равен 0.");
        hashTable.put("one", 1);
        assertEquals(1, hashTable.size(), "Размер хеш-таблицы должен быть равен 1 после добавления одного элемента.");

        hashTable.put("two", 2.0);
        assertEquals(2, hashTable.size(), "Размер хеш-таблицы должен быть равен 2 после добавления второго элемента.");

        hashTable.remove("one");
        assertEquals(1, hashTable.size(), "Размер хеш-таблицы должен быть равен 1 после удаления одного элемента.");
    }

    @Test
    public void testIterator() {
        hashTable.put("one", 1);
        hashTable.put("two", 2.0);

        Iterator<HashTable.Entry<String, Number>> iterator = hashTable.iterator();
        assertNotNull(iterator, "Итератор не должен быть null.");

        int count = 0;
        while (iterator.hasNext()) {
            HashTable.Entry<String, Number> entry = iterator.next();
            count++;
        }
        assertEquals(2, count, "Итератор должен пройти по двум элементам.");
    }

    @Test
    public void testConcurrentModificationException() {
        hashTable.put("one", 1);
        hashTable.put("two", 2.0);

        Iterator<HashTable.Entry<String, Number>> iterator = hashTable.iterator();
        hashTable.put("three", 3.0);

        assertThrows(ConcurrentModificationException.class, iterator::next, "Ожидается ConcurrentModificationException при модификации таблицы во время итерации.");
    }

    @Test
    public void testEquals() {
        HashTable<String, Number> anotherHashTable = new HashTable<>();
        hashTable.put("one", 1);
        anotherHashTable.put("one", 1);

        assertTrue(anotherHashTable.equals(hashTable), "Хеш-таблицы должны быть равны.");

        anotherHashTable.put("two", 2.0);
        assertNotEquals(hashTable, anotherHashTable, "Хеш-таблицы не должны быть равны после добавления нового элемента.");
    }

    @Test
    public void testToString() {
        hashTable.put("one", 1);
        hashTable.put("two", 2.0);

        String tableString = hashTable.toString();
        assertTrue(tableString.contains("one=1"), "Строковое представление должно содержать 'one=1'.");
        assertTrue(tableString.contains("two=2.0"), "Строковое представление должно содержать 'two=2.0'.");
    }
}