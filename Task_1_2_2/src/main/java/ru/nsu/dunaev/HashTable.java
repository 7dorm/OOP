package ru.nsu.dunaev;

import java.util.*;

public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {
    private final List<Entry<K, V>>[] table;
    private int size = 0;
    private final int capacity = 16;
    private int modCount = 0;

    public HashTable() {
        table = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public void put(K key, V value) {
        int index = hash(key);
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        table[index].add(new Entry<>(key, value));
        size++;
        modCount++;
    }

    public V get(Object key) {
        int index = hash((K) key);
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    public void remove(K key) {
        int index = hash(key);
        Iterator<Entry<K, V>> iterator = table[index].iterator();
        while (iterator.hasNext()) {
            Entry<K, V> entry = iterator.next();
            if (entry.key.equals(key)) {
                iterator.remove();
                size--;
                modCount++;
                return;
            }
        }
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public void update(K key, V value) {
        put(key, value);
    }

    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        HashTable<?, ?> other = (HashTable<?, ?>) obj;

        if (this.size != other.size) return false;

        // Use the iterator to traverse the current hash table
        for (Entry<K, V> entry : this) {
            Object key = entry.key;     // Generic key type to avoid type issues
            Object value = entry.value;  // Generic value type to avoid type issues

            // Check if `other` contains the key and the values match
            if (!other.containsKey(key) || !Objects.equals(value, other.get(key))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                builder.append(entry.key).append("=").append(entry.value).append(", ");
            }
        }
        if (builder.length() > 1) builder.setLength(builder.length() - 2);
        builder.append("}");
        return builder.toString();
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    private class HashTableIterator implements Iterator<Entry<K, V>> {
        private final int expectedModCount = modCount;
        private int currentIndex = 0;
        private Iterator<Entry<K, V>> bucketIterator = table[currentIndex].iterator();

        @Override
        public boolean hasNext() {
            checkForModification();
            while (currentIndex < table.length && !bucketIterator.hasNext()) {
                currentIndex++;
                if (currentIndex < table.length) {
                    bucketIterator = table[currentIndex].iterator();
                }
            }
            return currentIndex < table.length && bucketIterator.hasNext();
        }

        @Override
        public Entry<K, V> next() {
            checkForModification();
            if (!hasNext()) throw new NoSuchElementException();
            return bucketIterator.next();
        }

        private void checkForModification() {
            if (modCount != expectedModCount) throw new ConcurrentModificationException();
        }
    }

    public static class Entry<K, V> {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Entry<?, ?> other = (Entry<?, ?>) obj;
            return Objects.equals(key, other.key) && Objects.equals(value, other.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}