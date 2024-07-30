package cs2110;

import java.util.Iterator;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedSeqTest {

    // Helper functions for creating lists used by multiple tests.

    /**
     * Creates [].
     */
    static Seq<String> makeList0() {
        return new LinkedSeq<>();
    }

    /**
     * Creates ["A"].  Only uses prepend.
     */
    static Seq<String> makeList1() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B"].  Only uses prepend.
     */
    static Seq<String> makeList2() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates ["A", "B", "C"].  Only uses prepend.
     */
    static Seq<String> makeList3() {
        Seq<String> ans = new LinkedSeq<>();
        ans.prepend(new String("C"));
        ans.prepend(new String("B"));
        ans.prepend(new String("A"));
        return ans;
    }

    /**
     * Creates a list containing the same elements (in the same order) as array `elements`.  Only
     * uses prepend.
     */
    static <T> Seq<T> makeList(T[] elements) {
        Seq<T> ans = new LinkedSeq<>();
        for (int i = elements.length; i > 0; i--) {
            ans.prepend(elements[i - 1]);
        }
        return ans;
    }

    @Test
    void testConstructorSize() {
        Seq<String> list = new LinkedSeq<>();
        assertEquals(0, list.size());
    }

    @Test
    void testPrependSize() {
        Seq<String> list;

        list = makeList1();
        assertEquals(1, list.size());

        list = makeList2();
        assertEquals(2, list.size());

        list = makeList3();
        assertEquals(3, list.size());
    }

    @Test
    void testToString() {
        Seq<String> list;

        list = makeList0();
        assertEquals("[]", list.toString());

        list = makeList1();
        assertEquals("[A]", list.toString());

        list = makeList2();
        assertEquals("[A, B]", list.toString());

        list = makeList3();
        assertEquals("[A, B, C]", list.toString());
    }

    @Test
    void testContains() {
        Seq<String> list;
        list = makeList0();
        assertFalse(list.contains("h"));

        list = makeList3();
        assertTrue(list.contains("A"));

        list = makeList3();
        list.prepend("B");
        list.prepend("B");
        assertTrue(list.contains("B"));
    }

    @Test
    void testGet() {
        Seq<String> list;
        String[] string_array = {"A", "B", "C", "Help", "Test", "A3"};
        list = makeList(string_array);

        assertEquals("Test", list.get(4));
        assertEquals("A", list.get(0));
        assertEquals("A3", list.get(5));
    }

    @Test
    void testAppend() {
        Seq<String> list;
        String[] str_array = {"A", "B", "C", "D", "E"};
        list = makeList(str_array);
        list.append("F");
        assertEquals(list.get(5), "F");

        list = makeList0();
        list.append("A");
        assertEquals(list.get(0), "A");

        list = makeList1();
        list.append("B");
        assertEquals(list.get(1), "B");
    }

    @Test
    void testInsertBefore() {
        Seq<String> list;
        list = makeList1();
        list.insertBefore("B", "A");
        assertEquals(list.get(0), "B");

        list = makeList3();
        list.insertBefore("A", "C");
        assertEquals(list.get(2), "A");

        String[] str_array = {"A", "B", "C", "D", "E"};
        list = makeList(str_array);
        list.insertBefore("help", "D");
        assertEquals(list.get(3), "help");
    }

    @Test
    void testRemove() {
        Seq<String> list;
        list = makeList3();
        assertTrue(list.remove("A"));
        assertFalse(list.contains("A"));

        String[] str_array = {"A", "B", "C", "D", "E"};
        list = makeList(str_array);
        assertFalse(list.remove("Z"));

        assertTrue(list.remove("D"));
        assertEquals(list.get(3), "E");

        list = makeList0();
        assertFalse(list.remove("D"));

        list = makeList1();
        assertTrue(list.remove("A"));

        list = makeList2();
        assertTrue(list.remove("B"));
    }

    @Test
    void testEquals() {
        Seq<String> list;
        Seq<String> list2;
        list = makeList3();
        list2 = makeList3();
        assertTrue(list.equals(list2));
        assertTrue(list2.equals(list));

        list = makeList0();
        list2 = makeList2();
        assertFalse(list.equals(list2));
        assertFalse(list2.equals(list));

        String[] str_array = {"A", "B", "C", "D", "E"};
        list = makeList(str_array);
        list2 = makeList1();
        assertFalse(list.equals(list2));
        assertFalse(list2.equals(list));

        list2 = makeList(str_array);
        assertTrue(list.equals(list2));
        assertTrue(list2.equals(list));

        list = makeList1();
        list2 = makeList1();
        assertTrue(list.equals(list2));
        assertTrue(list2.equals(list));

        String[] str_array2 = {"he", "", "", ""};
        String[] str_array3 = {"he", "", "", ""};
        list = makeList(str_array2);
        list2 = makeList(str_array3);
        assertTrue(list.equals(list2));
        assertTrue(list2.equals(list));
    }

    @Test
    void testHashCode() {
        assertEquals(makeList0().hashCode(), makeList0().hashCode());

        assertEquals(makeList1().hashCode(), makeList1().hashCode());

        assertEquals(makeList2().hashCode(), makeList2().hashCode());

        assertEquals(makeList3().hashCode(), makeList3().hashCode());
    }

    @Test
    void testIterator() {
        Seq<String> list;
        Iterator<String> it;

        list = makeList0();
        it = list.iterator();
        assertFalse(it.hasNext());
        Iterator<String> itAlias = it;
        assertThrows(NoSuchElementException.class, () -> itAlias.next());

        list = makeList1();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertFalse(it.hasNext());

        list = makeList2();
        it = list.iterator();
        assertTrue(it.hasNext());
        assertEquals("A", it.next());
        assertTrue(it.hasNext());
        assertEquals("B", it.next());
        assertFalse(it.hasNext());
    }
}
