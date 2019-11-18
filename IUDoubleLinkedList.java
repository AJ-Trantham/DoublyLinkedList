import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/**
 * A doubly linked list implementation of the IndexedUnsortedList interface. 
 * @author AJ Trantham
 *
 * @param <T>
 */

//-----------------------------------------------------------------
//-------------------------------------------------------------------
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T>{
	private DLLNode<T> end, head, tail;
	private int size;
	private int modCount;
	private final static int  NOT_FOUND = -1;

	public IUDoubleLinkedList() {
		head = null;
		tail = null;
		end = new DLLNode<T>(null);
		modCount = 0;
		size = 0;
	}
	
	
	@Override
	public void addToFront(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);
		
		// check for an empty list
		if(size == 0)
		{
			newNode.setNext(end);
			newNode.setPrevious(null);
			head = tail = newNode;
		}
				
		else {
			newNode.setNext(head);
			head.setPrevious(newNode);
			head = newNode;
		}
				
		size++;
		modCount++;
	}

	@Override
	public void addToRear(T element) {
		DLLNode<T> newNode = new DLLNode<T>(element);
		DLLNode<T> next = end;
		DLLNode<T> previous = tail;
		
		//If empty list update tail
		if(size == 0)
		{
			newNode.setNext(next);
			end.setPrevious(newNode);
			head = tail = newNode;
		}
		
		//General Case
		else
		{
			next.setPrevious(newNode);
			newNode.setNext(next);
			newNode.setPrevious(previous);
			previous.setNext(newNode);
			tail = newNode;
		} 
		
		size++;
		modCount++;
		
	}

	@Override
	public void add(T element) {
		addToRear(element);
		
	}

	@Override
	public void addAfter(T element, T target) {
		boolean found = false;
		DLLNode<T> current = head;
		DLLNode<T> newNode = new DLLNode<T>(element);
		DLLNode<T> next;
		DLLNode<T> previous;
		
		while(!found && current != null && current != end)
		{
			if(current.getElement() == target)
				found = true;
			
			else
			{
				current = current.getNext();
			}
		}
		
		if(!found)
			throw new NoSuchElementException("The target: " + target + " was not found in the list.");
	
		// adding to rear
		else if(current == tail){
			addToRear(element);
		}
		
		//add to front not needed since we are always adding after something else.
		
		else
		{
			previous = current;
			next = current.getNext();
			next.setPrevious(newNode);
			newNode.setNext(next);
			newNode.setPrevious(previous);
			previous.setNext(newNode);
			size++;
			modCount++;
		}
	
	
	}
	
	

	@Override
	public void add(int index, T element) {
		if(index < 0 || index > size)
			throw new IndexOutOfBoundsException("Invalid index");
		
		// create a new node to be added
		//LinearNode<T> newNode = new LinearNode<T>(element);
		DLLNode<T> current = head;
		
		//set current at the index before the index we wish to add at
		for(int i = 0; i < index -1; i++)
		{
			current = current.getNext();
		}
		
		if(index == 0)
			addToFront(element);
		else
		{
			addAfter(element, current.getElement());
		}
		
		
	}

	@Override
	public T removeFirst() {
		
		if(size == 0)
		throw new NoSuchElementException("Cannot remove on an empty list");
	
		DLLNode<T> current = head;
		
		// scenario: 1 element in the list
		if(current.getNext() == end)
		{
			head = tail = null;
			current.setNext(null);
			end.setPrevious(null);
		}
		
		else
		{
			head = current.getNext();
			current.setNext(null);
			head.setPrevious(null);
		}
		
		
		size--;
		modCount++;
		return current.getElement();
	}

	@Override
	public T removeLast() {
		
	if(size == 0)
		throw new NoSuchElementException("Cannot remove on an empty list");
	
	DLLNode<T> previous = tail.getPrevious();
	T result = null;
	
	// scenario: 1 element in the list
	if(size == 1)
	{
		result = tail.getElement();
		tail.setNext(null);
		end.setPrevious(null);
		head = tail = null;
	}
	
	// general case
	else
	{
		tail.setNext(null);
		tail.setPrevious(null);
		end.setPrevious(previous);
		previous.setNext(end);
		result = tail.getElement();
		tail = previous;//updates tail
	}
	
	size--;
	modCount++;
	return result;
	}

	@Override
	public T remove(T element) {
		// check for an empty list
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		
		//find the element to remove
		boolean found = false;
		DLLNode<T> previous = null;
		DLLNode<T> current = head;
		DLLNode<T> next = null;
		T result = null;
		
		while(!found && current != null && current != end)
		{
			if(element.equals(current.getElement()))
				found = true;
			
			else
			{
				current = current.getNext();
			}
		}
		
		if(!found)
			throw new NoSuchElementException(element + " is not in the list");
		
		//remove from 4 different cases
		
		//Case 1 Element is at the front of the list
		if(current.equals(head))
			result = removeFirst();
		
		//Case 2 Element is the last element in the list
		else if(current.equals(tail))
			result = removeLast();
		
		// Case 3 Element is the only element in the list
		else if(size == 1)
		{
			result = head.getElement();
			head = tail = null;
			head.setNext(null);
			end.setPrevious(null);
			size--;
			modCount++;
		}
			
		// Case 4 Element is in the middle of the list
		else
		{
			previous = current.getPrevious();
			next = current.getNext();
			current.setNext(null);
			current.setPrevious(null);
			previous.setNext(next);
			next.setPrevious(previous);
			result = current.getElement();
			size--;
			modCount++;
		}
		
		return result;
	}

	@Override
	public T remove(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Invalid Index");
		
		DLLNode<T> current = head;
		DLLNode<T> previous = null;
		DLLNode<T> next = null;
		T result = null;
		
		//if index calls for the first element in the list 
		if(index == 0)
		{
			result = removeFirst();
		}
		
		//if index calls for the last element in the list
		else if(index == size - 1)
			result = removeLast();
		
		
		else {
			for(int i = 0; i < index; i++)
			{
				current = current.getNext();
			}
			
			previous = current.getPrevious();
			next = current.getNext();
			previous.setNext(next);
			current.setPrevious(null);
			current.setNext(null);
			next.setPrevious(previous);
			result = current.getElement();
			size--;
			modCount++;
		}
		
		return result;
	}

	@Override
	public void set(int index, T element) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Invalid index");
		
		DLLNode<T> current = head;
		
		for(int i = 0; i < index; i++)
		{
			current = current.getNext();
		}
		
		current.setElement(element);
		modCount++;
		
	}

	@Override
	public T get(int index) {
		if(index < 0 || index >= size)
			throw new IndexOutOfBoundsException("Invalid index");
		
		DLLNode<T> current = head;
		for(int i = 0; i < index; i++)
		{
			current = current.getNext();
		}
		
		return current.getElement();
	}

	@Override
	public int indexOf(T element) {
		boolean found = false;
		DLLNode<T> current = head;
		int index = 0;
		
		while(!found && current != null && current != end)
		{
			if(element.equals(current.getElement()))
				found = true;
			
			else
			{
				current = current.getNext();
				index++;
			}
		}
		
		if(!found)
			index = -1;
		
		return index;
	}

	@Override
	public T first() {
	if(size == 0)
		throw new NoSuchElementException("Cannot remove on an empty list");
		
	return head.getElement();
	}

	@Override
	public T last() {
		if(size == 0)
			throw new NoSuchElementException("Cannot remove on an empty list");
			
		return tail.getElement();
	}

	@Override
	public boolean contains(T target) {
		return indexOf(target) != NOT_FOUND;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}
	
	public String toString()
	{
		String resultString = "[ ";
		DLLNode<T> current = head;
		for(int i = 0; i < size; i++)
		{
			
			resultString += current.getElement() + " ";
			current = current.getNext();
			//resultString += "element" + i + ",";		
		}
		
		resultString += "]";
		return resultString;
	}

	@Override
	public Iterator<T> iterator() {
		Iterator<T> itr = new IUDoubleLinkedListListIterator();
		return itr;
	}

	@Override
	public ListIterator<T> listIterator() {
		ListIterator<T> itr = new IUDoubleLinkedListListIterator();
		return itr;
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		if(startingIndex < 0 || startingIndex > size)
			throw new IndexOutOfBoundsException("Index: " + startingIndex + "is not in the range of 0 to " + size);
		
		ListIterator<T> itr = new IUDoubleLinkedListListIterator(startingIndex);
		return itr;
	}
	
	private class IUDoubleLinkedListListIterator implements ListIterator<T>
	{
		private int nextCount;
		private int previousCount;
		private int itrModCount;
		private boolean  didNext, didPrevious;
		private final static int DEFAULT_START = 0; 
		
		//Constructor for ListIterator starting at position 0 (Default)
		public IUDoubleLinkedListListIterator()
		{
			this(DEFAULT_START);
		}
		
		//Constructor for ListIterator starting at position index (Overloaded)
		public IUDoubleLinkedListListIterator(int index)
		{
			nextCount = 0;
			previousCount = 0;
			itrModCount = modCount;
			
			for(int i = 0; i < index; i++)
			{
				this.next();	
			}
			
			//Set these to false since next will be set to true when next is called moving the cursor to the specified index
			didNext = didPrevious = false;
		}
		
		
		@Override
		public void add(T e) {
			
			checkModCount();
			IUDoubleLinkedList.this.add(previousCount, e);
			previousCount++;
			nextCount++;
			didNext = false;
			didPrevious = false;
			itrModCount++;
		}

		@Override
		public boolean hasNext() {
			checkModCount();
			return nextCount < size;
		}

		@Override
		public boolean hasPrevious() {
			checkModCount();
			return previousCount > 0;
		}

		@Override
		public T next() {
			if(!hasNext())  
				throw new NoSuchElementException();
			
			nextCount++;
			previousCount++;
			didNext = true;
			didPrevious = false;
			
			return get(nextCount - 1);
		}

		@Override
		public int nextIndex() {
			checkModCount();
			return nextCount;
		}

		@Override
		public T previous() {
			if(!hasPrevious())
				throw new NoSuchElementException();
			
			previousCount--;
			nextCount--;	
			didPrevious = true;
			didNext = false;
			
			return get(previousCount);
		}

		@Override
		public int previousIndex() {
			checkModCount();
			return previousCount - 1;
		}

		@Override
		public void remove() {
			
			checkModCount();
			
			if(!didNext && !didPrevious)
				throw new IllegalStateException("Preconditions not met to perform this action.");			
			
			// Do not need a condition for if Next and Previous have been called. Since the most recent
			// call is all that matters, the one boolean is turned to false whenever its twin is called
			
			// Remove the item
			
			if(didNext)
			{
				// remove item after next
				IUDoubleLinkedList.this.remove(nextCount - 1);
				//reset
				nextCount--;
				previousCount--;
				didNext = false;
			}
			
			//Previous
			else
			{
				//remove previous item
				IUDoubleLinkedList.this.remove(previousCount);
				didPrevious = false;
			}
			
			itrModCount++;
			
		}

		@Override
		public void set(T e) {
			checkModCount();
			if(!didNext && !didPrevious)
				throw new IllegalStateException("Preconditions not met to perform this action.");
			
			else if(didNext)
			{
				// set item after next
				IUDoubleLinkedList.this.set(nextCount - 1, e);
				//reset
				nextCount--;
				didNext = false;
			}
			
			//Previous
			else
			{
				//set previous item
				IUDoubleLinkedList.this.set(previousCount, e);
				
				didPrevious = false;
			}
			
			itrModCount++;
			
		}
		/**
		 * A helper method designed to ensure the list is modified properly from the iterator
		 */
		private void checkModCount()
		{
			if(itrModCount != modCount)
				throw new ConcurrentModificationException();
		}		
	}

}


