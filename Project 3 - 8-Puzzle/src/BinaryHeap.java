package proj3;

// BinaryHeap class
//
// CONSTRUCTION: with optional capacity (that defaults to 100)
//               or an array containing initial items
//
// ******************PUBLIC OPERATIONS*********************
// void insert( x )       --> Insert x
// Comparable deleteMin( )--> Return and remove smallest item
// Comparable findMin( )  --> Return smallest item
// boolean isEmpty( )     --> Return true if empty; else false
// int getSize( )		  --> Returns number of elements

/**
 * Implements a min binary heap. All code and comments adapted from Mark
 * Allen Weiss, found @ http://users.cis.fiu.edu/~weiss/dsaajava3/code/
 * @author Mark Allen Weiss
 */
public class BinaryHeap<AnyType extends Comparable<AnyType>>
{

	private static final int DEFAULT_CAPACITY = 10;

	private int currentSize;      // Number of elements in heap
	private AnyType [ ] array;    // The heap array

	/**
	 * Construct the binary heap.
	 */
	public BinaryHeap( )
	{
		this( DEFAULT_CAPACITY );
	}

	/**
	 * Construct the binary heap.
	 * @param capacity the capacity of the binary heap.
	 */
	@SuppressWarnings("unchecked")
	public BinaryHeap( int capacity )
	{
		currentSize = 0;
		array = (AnyType[]) new Comparable[ capacity + 1 ];
	}

	/**
	 * Insert into the priority queue, maintaining heap order.
	 * Duplicates are allowed.
	 * @param x the item to insert.
	 */
	public void insert( AnyType x )
	{
		if( currentSize == array.length - 1 )
			enlargeArray( array.length * 2 + 1 );

		// Percolate up
		int hole = ++currentSize;
		for( ; hole > 1 && x.compareTo( array[ hole / 2 ] ) < 0; hole /= 2 )
			array[ hole ] = array[ hole / 2 ];
		array[ hole ] = x;
	}

	@SuppressWarnings("unchecked")
	private void enlargeArray( int newSize )
	{
		AnyType [] old = array;
		array = (AnyType []) new Comparable[ newSize ];
		for( int i = 0; i < old.length; i++ )
			array[ i ] = old[ i ];        
	}

	/**
	 * Find the smallest item in the priority queue.
	 * @return the smallest item, or throw an UnderflowException if empty.
	 */
	public AnyType findMin( )
	{
		return array[ 1 ];
	}

	/**
	 * Remove the smallest item from the priority queue.
	 * @return the smallest item, or throw an UnderflowException if empty.
	 */
	public AnyType deleteMin( )
	{
		AnyType minItem = findMin( );
		array[ 1 ] = array[ currentSize-- ];
		percolateDown( 1 );
		return minItem;
	}

	public int getSize() {
		return currentSize;
	}

	/**
	 * Test if the priority queue is logically empty.
	 * @return true if empty, false otherwise.
	 */
	public boolean isEmpty( )
	{
		return currentSize == 0;
	}

	/**
	 * Internal method to percolate down in the heap.
	 * @param hole the index at which the percolate begins.
	 */
	private void percolateDown( int hole )
	{
		int child;
		AnyType tmp = array[ hole ];

		for( ; hole * 2 <= currentSize; hole = child )
		{
			child = hole * 2;
			if( child != currentSize &&
					array[ child + 1 ].compareTo( array[ child ] ) < 0 )
				child++;
			if( array[ child ].compareTo( tmp ) < 0 )
				array[ hole ] = array[ child ];
			else
				break;
		}
		array[ hole ] = tmp;
	}
}
