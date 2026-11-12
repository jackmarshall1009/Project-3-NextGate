/**
 *  Tests for the queue class used by Next Gate.
 * 
 */


public class QueueTest extends student.TestCase
{
    private Queue<String> queue;
    private Queue<Passenger> line;
    private Passenger ana;
    private Passenger ben;
    private Passenger cal;
    
    
    /**
     * Runs before every test. Makes an empty String queue, an
     * empty Passenger line, and three passengers.
     */
    public void setUp()
    {
        queue = new Queue<String>();
        line = new Queue<Passenger>();
        ana = new Passenger("Ana", "AA100", 1, false);
        ben = new Passenger("Ben", "DL200", 2, true);
        cal = new Passenger("Cal", "UA300", 3, false);
    }
    
    /**
     * Tests that a new queue is empty with size 0.
     */
    public void testNewQueueIsEmpty()
    {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }
    
    /**
     * Tests enqueue on an empty queue and on a queue that
     * already has items.
     */
    public void testEnqueue()
    {
        queue.enqueue("A");
 
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.size());
        assertEquals("A", queue.peek());
 
        queue.enqueue("B");
        queue.enqueue("C");
 
        assertEquals(3, queue.size());
        assertEquals("A", queue.peek());
    }
    
    /**
     * Tests that peek returns null on an empty queue.
     */
    public void testPeekEmpty()
    {
        assertNull(queue.peek());
    }
    
    /**
     * Tests that peek shows the front item without removing it.
     */
    public void testPeekDoesNotRemove()
    {
        queue.enqueue("A");
        queue.enqueue("B");
 
        assertEquals("A", queue.peek());
        assertEquals("A", queue.peek());
        assertEquals(2, queue.size());
    }
    
    /**
     * Tests that dequeue returns null on an empty queue and
     * does not change the size.
     */
    public void testDequeueEmpty()
    {
        assertNull(queue.dequeue());
        assertEquals(0, queue.size());
    }
    
    /**
     * Tests that dequeue removes items in first in, first out
     * order.
     */
    public void testDequeueOrder()
    {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
 
        assertEquals("A", queue.dequeue());
        assertEquals("B", queue.dequeue());
        assertEquals("C", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertNull(queue.dequeue());
    }
    
    /**
     * Tests that the queue still works after it is emptied by
     * dequeue. This checks that lastNode was reset to null.
     */
    public void testEnqueueAfterEmptied()
    {
        queue.enqueue("A");
        queue.dequeue();
        queue.enqueue("B");
        queue.enqueue("C");
 
        assertEquals(2, queue.size());
        assertEquals("B", queue.dequeue());
        assertEquals("C", queue.dequeue());
    }
    
    /**
     * Tests positionOf for the front, middle, and back of the
     * line.
     */
    public void testPositionOfFound()
    {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
 
        assertEquals(1, queue.positionOf("A"));
        assertEquals(2, queue.positionOf("B"));
        assertEquals(3, queue.positionOf("C"));
    }
    
    /**
     * Tests that positionOf returns -1 when the item is not in
     * the queue, including when the queue is empty.
     */
    public void testPositionOfNotFound()
    {
        assertEquals(-1, queue.positionOf("A"));
 
        queue.enqueue("A");
 
        assertEquals(-1, queue.positionOf("Z"));
    }
    
    /**
     * Tests that remove returns false on an empty queue and
     * when the item is not there.
     */
    public void testRemoveNotFound()
    {
        assertFalse(queue.remove("A"));
 
        queue.enqueue("A");
 
        assertFalse(queue.remove("Z"));
        assertEquals(1, queue.size());
    }
    
    /**
     * Tests removing the item at the front of the queue.
     */
    public void testRemoveFront()
    {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
 
        assertTrue(queue.remove("A"));
        assertEquals(2, queue.size());
        assertEquals("B", queue.peek());
    }
    
    /**
     * Tests removing an item from the middle of the queue.
     */
    public void testRemoveMiddle()
    {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
 
        assertTrue(queue.remove("B"));
        assertEquals(2, queue.size());
        assertEquals("A", queue.dequeue());
        assertEquals("C", queue.dequeue());
    }
    
    /**
     * Tests removing the last item, then adding a new one. This
     * checks that lastNode was moved back correctly.
     */
    public void testRemoveLast()
    {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
 
        assertTrue(queue.remove("C"));
 
        queue.enqueue("D");
 
        assertEquals(3, queue.size());
        assertEquals(3, queue.positionOf("D"));
        assertEquals(-1, queue.positionOf("C"));
    }
    
    /**
     * Tests removing the only item, then adding a new one.
     */
    public void testRemoveOnlyItem()
    {
        queue.enqueue("A");
 
        assertTrue(queue.remove("A"));
        assertTrue(queue.isEmpty());
        assertNull(queue.peek());
 
        queue.enqueue("B");
 
        assertEquals(1, queue.size());
        assertEquals("B", queue.peek());
    }
    
    /**
     * Tests that toArray returns an empty array for an empty
     * queue.
     */
    public void testToArrayEmpty()
    {
        Object[] array = queue.toArray();
 
        assertEquals(0, array.length);
    }
    
    /**
     * Tests that toArray lists items from front to back.
     */
    public void testToArray()
    {
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
 
        Object[] array = queue.toArray();
 
        assertEquals(3, array.length);
        assertEquals("A", array[0]);
        assertEquals("B", array[1]);
        assertEquals("C", array[2]);
    }
    
    /**
     * Tests the airport line with real Passenger objects.
     * Passengers should be screened in the order they joined.
     */
    public void testPassengerLineOrder()
    {
        line.enqueue(ana);
        line.enqueue(ben);
        line.enqueue(cal);
 
        assertEquals(3, line.size());
        assertEquals(ana, line.dequeue());
        assertEquals(ben, line.dequeue());
        assertEquals(cal, line.dequeue());
    }
    
    /**
     * Tests that positionOf finds a passenger using a different
     * object with the same name and flight. This works because
     * Passenger.equals compares name and flight number.
     */
    public void testPassengerPositionOf()
    {
        line.enqueue(ana);
        line.enqueue(ben);
 
        Passenger benCopy = new Passenger("Ben", "DL200", 9, false);
 
        assertEquals(2, line.positionOf(benCopy));
        assertEquals(-1, line.positionOf(cal));
    }
    
    /**
     * Tests that a passenger can leave the line from the middle.
     */
    public void testPassengerRemove()
    {
        line.enqueue(ana);
        line.enqueue(ben);
        line.enqueue(cal);
 
        assertTrue(line.remove(ben));
        assertEquals(2, line.size());
        assertEquals(2, line.positionOf(cal));
        assertFalse(line.remove(ben));
    }
}
