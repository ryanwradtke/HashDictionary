
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Implement the ADT dictionary by using hashing and separate chaining. Use an
 * ADT List for each bucket. The dictionary’s entries should have distinct
 * search keys.
 */
/**
 *
 * @author Ryan W Radtke <RyanWRadtke@gmail.com>
 * @param <K> Key
 * @param <V> Value
 */
public class ADTHashDict<K, V> implements DictInterface<K, V> {

    private ADTListInterface<DictEntry>[] hashTable;
    private static final int DEFAULT_SIZE = 53;
    private static final double DEFAULT_MAX_LOAD_FACTOR = 2;
    private double maxLoadFactor;
    private int tableLength, numberOfIndices, numberOfEntries;

    public ADTHashDict() {
        this(DEFAULT_SIZE, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ADTHashDict(int size) {
        this(size, DEFAULT_MAX_LOAD_FACTOR);
    }

    public ADTHashDict(double maxLoadFactor) {
        this(DEFAULT_SIZE, maxLoadFactor);
    }

    public ADTHashDict(int size, double maxLoadFactor) {
        size = (isPrime(size)) ? size : getNextPrime(size);
        this.maxLoadFactor = maxLoadFactor;
        hashTable = new ADTList[size];
        tableLength = size;
        numberOfIndices = 0;
        numberOfEntries = 0;
    }

    @Override
    public V add(K key, V value) {
        DictEntry newEntry = new DictEntry(key, value);
        V returnValue = value;
        int index = getHashIndex(key);

        if (hashTable == null) {
            hashTable = new ADTList[DEFAULT_SIZE];
        }

        if (hashTable[index] == null) {
            ADTList<DictEntry> newList = new ADTList<>();
            newList.add(newEntry);
            hashTable[index] = newList;
            numberOfIndices++;
            numberOfEntries++;

        } else if (hashTable[index].getLength() == DEFAULT_MAX_LOAD_FACTOR) {
            int keyCount = 0;

            for (DictEntry d : hashTable[index]) {
                if (d.key.equals(key)) {
                    keyCount++;
                }
            }

            if (keyCount < maxLoadFactor) {
                numberOfEntries++;
                rehash();
                add(key, value);
            } else {
                returnValue = null;
            }

        } else {
            hashTable[index].add(newEntry);
            numberOfEntries++;
        }

        return returnValue;
    }

    @Override
    public V remove(K key) {
        int index = getHashIndex(key);
        boolean firstRemoved = false;

        DictEntry d;
        Iterator itr = hashTable[index].getIterator();
        V value = null;

        while (itr.hasNext() && !firstRemoved) {
            d = (DictEntry) itr.next();
            if (d.key.equals(key)) {
                value = (V) d.value;
                itr.remove();
                firstRemoved = true;
                numberOfEntries--;
            }
        }

        if (hashTable[index].isEmpty()) {
            hashTable[index] = null;
            numberOfIndices--;
        }

        return value;

    }

    public V remove(K key, int givenPosition) {
        int index = getHashIndex(key);
        boolean firstRemoved = false;

        DictEntry d;
        Iterator itr = hashTable[index].getIterator();
        V value = null;

        while (itr.hasNext() && !firstRemoved) {
            d = (DictEntry) itr.next();
            if (d.key.equals(key)) {
                givenPosition--;
                if (givenPosition == 0) {
                    value = (V) d.value;
                    itr.remove();
                    firstRemoved = true;
                    numberOfEntries--;
                }
            }
        }

        if (hashTable[index].isEmpty()) {
            hashTable[index] = null;
            numberOfIndices--;
        }

        return value;

    }

    @Override
    /**
     * Returns first value found that matches key or null if key not found.
     */
    public V getValue(K key) {
        int index = getHashIndex(key);
        for (DictEntry d : hashTable[index]) {
            if (d.getKey().equals(key)) {
                return (V) d.getValue();
            }
        }
        return null;
    }

    /**
     * returns all values found.
     *
     * @param key
     * @return ADTList: contains a list of all values found for particular key.
     */
    public ADTListInterface getAllValues(K key) {
        ADTListInterface<V> returnList = new ADTList<>();

        int index = getHashIndex(key);

        for (DictEntry d : hashTable[index]) {
            if (d.getKey().equals(key)) {
                returnList.add((V) d.getValue());
            }
        }

        return returnList;
    }

    @Override
    public boolean contains(K key) {
        Iterator itr = new KeyIterator();
        boolean contains = false;

        while (itr.hasNext()) {
            if (itr.next().equals(key)) {
                contains = true;
            }
        }

        return contains;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public int getSize() {
        return numberOfEntries;
    }

    @Override
    public void clear() {
        hashTable = null;
        numberOfEntries = 0;
        numberOfIndices = 0;
    }

    private int getHashIndex(K key) {
        return key.hashCode() % tableLength;
    }

    private void rehash() {
        ADTListInterface[] oldTable = hashTable;
        int oldLength = tableLength;
        numberOfEntries = numberOfIndices = 0;
        hashTable = new ADTList[getNextPrime(tableLength *= 2)];
        DictEntry tempEntry;

        Iterator itr;

        for (int i = 0; i < oldLength; i++) {
            if (oldTable[i] != null) {
                itr = oldTable[i].getIterator();

                while (itr.hasNext()) {
                    tempEntry = (DictEntry) itr.next();
                    add((K) tempEntry.key, (V) tempEntry.value);
                }
            }
        }
    }

    private boolean isPrime(int n) {
        if (n == 2 || n == 1) {
            return true;
        }

        for (int i = 2; i < Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    public int getNextPrime(int n) {
        return isPrime(n) ? n : getNextPrime(n + 1);
    }

    @Override
    public Iterator<K> getKeyIterator() {
        return new KeyIterator();
    }

    @Override
    public Iterator<V> getValueIterator() {
        return new ValueIterator();
    }

    @Override
    public Iterator iterator() {
        return new DictEntryIterator();
    }

    class DictEntry<K, V> {

        private final K key;
        private final V value;

        private DictEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

    }

    private class KeyIterator<K> implements IteratorInterface<K> {

        Iterator listItr;
        private int nextIndex, numberIndicesLeft, numberEntriesLeft;
        boolean newIndex = true;

        private KeyIterator() {
            nextIndex = 0;
            numberIndicesLeft = numberOfIndices;
            numberEntriesLeft = numberOfEntries;
        }

        @Override
        public boolean hasNext() {
            return numberIndicesLeft > 0;
        }

        @Override
        public K next() {
            DictEntry temp;
            K returnKey;

            if (hasNext()) {

                while (hashTable[nextIndex] == null) {
                    nextIndex++;
                }

                if (newIndex) {
                    listItr = hashTable[nextIndex].getIterator();
                    numberIndicesLeft--;
                    newIndex = false;
                }

                temp = (DictEntry) listItr.next();
                returnKey = (K) temp.getKey();

                numberEntriesLeft--;

                if (!(listItr.hasNext())) { //wHY is this trigered every time?
                    nextIndex++;
                    newIndex = true;
                }
                return returnKey;

            } else {
                throw new NoSuchElementException("Illegal call to next(); "
                        + "iterator is after end of list.");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    private class ValueIterator<V> implements IteratorInterface<V> {

        Iterator listItr;
        private int nextIndex, numberIndicesLeft, numberEntriesLeft;
        boolean newIndex = true;

        private ValueIterator() {
            nextIndex = 0;
            numberIndicesLeft = numberOfIndices;
            numberEntriesLeft = numberOfEntries;
        }

        @Override
        public boolean hasNext() {
            return numberIndicesLeft > 0;
        }

        @Override
        public V next() {
            DictEntry temp;
            V returnValue;

            if (hasNext()) {

                while (hashTable[nextIndex] == null) {
                    nextIndex++;
                }

                if (newIndex) {
                    listItr = hashTable[nextIndex].getIterator();
                    numberIndicesLeft--;
                    newIndex = false;
                }

                temp = (DictEntry) listItr.next();
                returnValue = (V) temp.getValue();

                numberEntriesLeft--;

                if (!(listItr.hasNext())) { //wHY is this trigered every time?
                    nextIndex++;
                    newIndex = true;
                }
                return returnValue;

            } else {
                throw new NoSuchElementException("Illegal call to next(); "
                        + "iterator is after end of list.");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported.");
        }
    }

    private class DictEntryIterator implements IteratorInterface {

        Iterator listItr;
        private int nextIndex, numberIndicesLeft, numberEntriesLeft;
        boolean newIndex = true;

        private DictEntryIterator() {
            nextIndex = 0;
            numberIndicesLeft = numberOfIndices;
            numberEntriesLeft = numberOfEntries;
        }

        @Override
        public boolean hasNext() {
            return numberIndicesLeft > 0;
        }

        @Override
        public DictEntry next() {
            DictEntry returnEntry;

            if (hasNext()) {

                while (hashTable[nextIndex] == null) {
                    nextIndex++;
                }

                if (newIndex) {
                    listItr = hashTable[nextIndex].getIterator();
                    numberIndicesLeft--;
                    newIndex = false;
                }

                returnEntry = (DictEntry) listItr.next();

                numberEntriesLeft--;

                if (!(listItr.hasNext())) { //wHY is this trigered every time?
                    nextIndex++;
                    newIndex = true;
                }
                return returnEntry;

            } else {
                throw new NoSuchElementException("Illegal call to next(); "
                        + "iterator is after end of list.");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported."); 
        }
    }
}
