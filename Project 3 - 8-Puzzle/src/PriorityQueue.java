package proj3;

/**
 * This is Priority Queue "wrapper" of a min binary heap.
 * 
 * @version 04/15/12
 * @author Rostislav Tsiomenko <tsiomr1@umbc.edu>
 * Project: CMSC 341 - Spring 2012 - Project 3
 * Section: 01
 */
public class PriorityQueue<T extends Comparable<T>> {

	private BinaryHeap<T> minHeap;

	/**
	 * Default constructor. Creates new PriorityQueue
	 * with default size as specified in BinaryHeap.
	 */
	public PriorityQueue () {
		minHeap = new BinaryHeap<T>();
	}

	/**
	 * Inserts an Object into the queue.
	 * @param x Comparable Object to insert.
	 */
	public void enqueue (T x) {
		minHeap.insert(x);
	}
	
	/**
	 * Returns Object with highest priority and deletes it
	 * from queue.
	 * @return Object with highest priority (root of min binary heap)
	 */
	public T dequeue () {
		return minHeap.deleteMin();
	}

	/**
	 * Returns number of elements in queue.
	 * @return number of elements
	 */
	public int size () {
		return minHeap.getSize();
	}

}
