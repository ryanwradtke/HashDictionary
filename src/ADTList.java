
import java.util.NoSuchElementException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Ryan W Radtke <RyanWRadtke@gmail.com>
 * @param <T>
 */
public class ADTList<T> implements ADTListInterface<T> {

    private Node firstNode;
    private Node lastNode;
    private int numberOfEntries;

    public ADTList() {
        this.numberOfEntries = 0;
    }

    @Override
    public void add(T newEntry) {
        if (numberOfEntries != 0) {
            Node newNode = new Node(newEntry, lastNode, null);
            lastNode.next = newNode;
            lastNode = newNode;
        } else {
            firstNode = lastNode = new Node(newEntry);
        }
        numberOfEntries++;
    }

    @Override
    public boolean add(int newPosition, T newEntry) {
        boolean success = false;

        if (numberOfEntries != 0 && numberOfEntries >= newPosition) {
            Node tempNode = firstNode;
            Node newNode;

            while (newPosition > 1 && tempNode.next != null) {
                tempNode = tempNode.next;
                newPosition--;
            }

            newNode = new Node(newEntry, tempNode.previous, tempNode);
            tempNode.previous.next = newNode;
            success = true;
            numberOfEntries++;

        } else {
            add(newEntry);
        }

        return success;
    }

    @Override
    public T remove(int givenPosition) {
        Node tempNode = firstNode;

        while (givenPosition > 1) {
            tempNode = tempNode.next;
            givenPosition--;
        }

        if (numberOfEntries == 1) {
            firstNode = lastNode = null;
        } else if (tempNode.equals(firstNode)) {
            tempNode.next.previous = null;
            firstNode = tempNode.next;
        } else if (tempNode.equals(lastNode)) {
            tempNode.previous.next = null;
            lastNode = tempNode.previous;
        } else {
            tempNode.previous.next = tempNode.next;
            tempNode.next.previous = tempNode.previous;
        }

        numberOfEntries--;

        return (T) tempNode.data;
    }

    @Override
    public void clear() {
        firstNode = lastNode = null;
        numberOfEntries = 0;
    }

    @Override
    public boolean replace(int givenPosition, T newEntry
    ) {
        boolean success = false;

        if (numberOfEntries != 0 && numberOfEntries >= givenPosition) {
            Node tempNode = firstNode;
            Node newNode;

            while (givenPosition > 1 && tempNode.next != null) {
                tempNode = tempNode.next;
                givenPosition--;
            }

            newNode = new Node(newEntry, tempNode.previous, tempNode.next);

            if (numberOfEntries == 1) {
                firstNode = lastNode = newNode;
            } else if (tempNode.equals(firstNode)) {
                tempNode.next.previous = newNode;
                firstNode = newNode;
            } else if (tempNode.equals(lastNode)) {
                tempNode.previous.next = newNode;
                lastNode = newNode;
            } else {
                tempNode.previous.next = newNode;
                tempNode.next.previous = newNode;
            }

            success = true;

        }

        return success;
    }

    @Override
    public T getEntry(int givenPosition
    ) {
        Node entry = firstNode;

        while (givenPosition > 0) {
            entry = entry.next;
            givenPosition--;
        }

        return (T) entry.getData();
    }

    @Override
    public boolean contains(T anEntry) {
        boolean containsEntry = false;

        for (T data : this) {
            if (data.equals(anEntry)) {
                containsEntry = true;
            }
        }

        return containsEntry;
    }

    @Override
    public int getLength() {
        return numberOfEntries;
    }

    @Override
    public boolean isEmpty() {
        return numberOfEntries == 0;
    }

    @Override
    public T[] toArray() {
        T[] newArray = (T[]) new Object[numberOfEntries];
        Iterator itr = this.getIterator();
        int i = 0;

        while (itr.hasNext()) {
            newArray[i] = (T) itr.next();
            i++;
        }

        return newArray;
    }

    @Override
    public Iterator<T> iterator() {
        return getIterator();
    }

    @Override
    public Iterator<T> getIterator() {
        return new Iterator();
    }

    private class Node<T> {

        private T data;
        private Node next, previous;

        private Node(T data) {
            this(data, null, null);
        }

        private Node(T data, Node previous, Node next) {
            this.data = data;
            this.previous = previous;
            this.next = next;
        }

        public T getData() {
            return data;
        }

        private Node getNext() {
            return next;
        }

        private Node getPrevious() {
            return previous;
        }

        private void setData(T data) {
            this.data = data;
        }

        private void setNext(Node next) {
            this.next = next;
        }

        private void setPrevious(Node previous) {
            this.previous = previous;
        }

    }

    private class Iterator<T> implements IteratorInterface<T> {

        private Node nextNode, previousNode;
        private boolean removeLegal;

        private Iterator() {
            nextNode = firstNode;
            removeLegal = false;
        }

        @Override
        public boolean hasNext() {
            return nextNode != null;
        }

        @Override
        public T next() {

            if (hasNext()) {
                Node returnNode = nextNode;
                previousNode = nextNode;
                nextNode = nextNode.next;
                removeLegal = true;
                return (T) returnNode.data;
            } else {
                throw new NoSuchElementException("Illegal call to next(); "
                        + "iterator is after end of list.");
            }
        }

        @Override
        public void remove() {

            if (removeLegal) {
                if (numberOfEntries == 1) {
                    firstNode = lastNode = null;
                } else if (previousNode.equals(firstNode)) {
                    firstNode = nextNode;
                    nextNode.previous = null;
                } else if (previousNode.equals(lastNode)) {
                    lastNode = previousNode.previous;
                    previousNode.previous.next = null;
                    nextNode = null;
                } else {
                    previousNode.previous.next = nextNode;
                    nextNode.previous = previousNode.previous;
                    previousNode = null;
                }

                numberOfEntries--;
                removeLegal = false;
            } else {
                throw new IllegalStateException("Illegal call to remove(): "
                        + "next() was not called");
            }
        }

    }
}
