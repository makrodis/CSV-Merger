package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * A list of elements of type `T` implemented as a singly linked list.  Null elements are not
 * allowed.
 */
public class LinkedSeq<T> implements Seq<T> {

    /**
     * Number of elements in the list.  Equal to the number of linked nodes reachable from `head`.
     */
    private int size;

    /**
     * First node of the linked list (null if list is empty).
     */
    private Node<T> head;

    /**
     * Last node of the linked list starting at `head` (null if list is empty).  Next node must be
     * null.
     */
    private Node<T> tail;

    /**
     * Assert that this object satisfies its class invariants.
     */
    private void assertInv() {
        assert size >= 0;
        if (size == 0) {
            assert head == null;
            assert tail == null;
        } else {
            assert head != null;
            assert tail != null;

            int count = 0;
            Node<T> current_node = head;
            Node<T> actual_tail = null;
            while (current_node != null) {
                count += 1;
                if (current_node.next() == null) {
                    actual_tail = current_node;
                }
                current_node = current_node.next();
            }
            assert count == size;
            assert tail == actual_tail;

        }
    }

    /**
     * Create an empty list.
     */
    public LinkedSeq() {
        size = 0;
        head = null;
        tail = null;

        assertInv();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void prepend(T elem) {
        assertInv();
        assert elem != null;

        head = new Node<>(elem, head);
        // If list was empty, assign tail as well
        if (tail == null) {
            tail = head;
        }
        size += 1;

        assertInv();
    }

    /**
     * Return a text representation of this list with the following format: the string starts with
     * '[' and ends with ']'.
     */
    @Override
    public String toString() {
        assertInv();
        String str = "[";
        Node<T> current_node = head;
        if (current_node == null) {
            str += "]";
            assertInv();
            return str;
        }
        while (current_node != null) {
            if (current_node.next() != null) {
                str += current_node.data() + ", ";
                current_node = current_node.next();
            } else {
                str += current_node.data() + "]";
                current_node = current_node.next();
            }
        }
        assertInv();
        return str;
    }

    @Override
    public boolean contains(T elem) {
        assertInv();
        assert elem != null;
        Node<T> current_node = head;
        if (current_node == null) {
            assertInv();
            return false;
        }
        while (current_node != null) {
            if (current_node.data().equals(elem)) {
                assertInv();
                return true;
            }
            current_node = current_node.next();
        }
        assertInv();
        return false;
    }

    @Override
    public T get(int index) {
        assertInv();
        assert 0 <= index;
        assert index <= size();
        Node<T> current_node = head;
        for (int i = 0; i < index; i++) {
            current_node = current_node.next();
        }
        assertInv();
        return current_node.data();
    }

    @Override
    public void append(T elem) {
        assertInv();
        assert elem != null;
        if (head == null) {
            tail = new Node<>(elem, null);
            head = tail;
        } else {
            tail.setNext(new Node<>(elem, null));
            tail = tail.next();
        }
        size += 1;
        assertInv();
    }

    @Override
    public void insertBefore(T elem, T successor) {
        assertInv();
        assert elem != null && successor != null;
        assert contains(successor);
        Node<T> current_node = head;
        if (current_node.data().equals(successor)) {
            prepend(elem);
        } else {
            while (!current_node.next().data().equals(successor)) {
                current_node = current_node.next(); //current node is node before successor
            }
            Node<T> append_node = new Node<>(elem, current_node.next());
            current_node.setNext(append_node);
            size += 1;
        }
        assertInv();
    }

    @Override
    public boolean remove(T elem) {
        assertInv();
        assert elem != null;
        if (contains(elem)) {
            Node<T> current_node = head;
            if (current_node.data().equals(elem)) {
                if (size == 1) {
                    head = null;
                    tail = null;
                    size = size - 1;
                    assertInv();
                    return true;
                } else {
                    head = current_node.next();
                    current_node.setNext(null);
                    size = size - 1;
                    assertInv();
                    return true;
                }
            }
            while (!current_node.next().data().equals(elem)) {
                current_node = current_node.next();
            }
            Node<T> remove_node = current_node.next();
            if (current_node.next().equals(tail)) {
                tail = current_node;
            }
            current_node.setNext(remove_node.next());
            remove_node.setNext(null);
            size = size - 1;
            assertInv();
            return true;
        } else {
            assertInv();
            return false;
        }
    }

    /**
     * Return whether this and `other` are `LinkedSeq`s containing the same elements in the same
     * order.
     */
    @Override
    public boolean equals(Object other) {
        assertInv();
        if (!(other instanceof LinkedSeq)) {
            assertInv();
            return false;
        }
        LinkedSeq otherSeq = (LinkedSeq) other;
        Node<T> currNodeThis = head;
        Node currNodeOther = otherSeq.head;
        if (currNodeThis == null && currNodeOther != null
                || currNodeOther == null && currNodeThis != null) {
            assertInv();
            return false;
        }

        while (currNodeThis != null) {
            if (!currNodeThis.data().equals(currNodeOther.data())) {
                assertInv();
                return false;
            }
            currNodeOther = currNodeOther.next();
            currNodeThis = currNodeThis.next();
            if ((currNodeThis == null && currNodeOther != null) || (currNodeThis != null
                    && currNodeOther == null)) {
                assertInv();
                return false;
            }
        }
        assertInv();
        return true;
    }

    /**
     * Returns a hash code value for the object.  See `Object.hashCode()` for additional
     * guarantees.
     */
    @Override
    public int hashCode() {
        int hash = 1;
        for (T e : this) {
            hash = 31 * hash + e.hashCode();
        }
        return hash;
    }

    /**
     * Return an iterator over the elements of this list (in sequence order).
     */
    @Override
    public Iterator<T> iterator() {
        assertInv();
        return new Iterator<>() {
            private Node<T> next = head;

            public T next() throws NoSuchElementException {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T result = next.data();
                next = next.next();
                return result;
            }

            public boolean hasNext() {
                return next != null;
            }
        };
    }
}
