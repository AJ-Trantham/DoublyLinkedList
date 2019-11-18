/**
 * DLLNode represents a node in a double linked list.
 *
 * @author AJ Trantham
 *
 */
public class DLLNode<E> {
	private DLLNode<E> next;
	private DLLNode<E> previous;
	private E element;

	/**
  	 * Creates an empty node.
  	 */
	public DLLNode() {
		next = null;
		previous = null;
		element = null;
	}

	/**
  	 * Creates a node storing the specified element.
 	 *
  	 * @param elem
  	 *            the element to be stored within the new node
  	 */
	public DLLNode(E elem) {
		next = null;
		previous = null;
		element = elem;
	}

	/**
 	 * Returns the node that follows this one.
  	 *
  	 * @return the node that follows the current one
  	 */
	public DLLNode<E> getNext() {
		return next;
	}

	/**
 	 * Sets the node that follows this one.
 	 *
 	 * @param node
 	 *            the node to be set to follow the current one
 	 */
	public void setNext(DLLNode<E> node) {
		next = node;
	}
	
	/**
 	 * Returns the node that precedes this one.
  	 *
  	 * @return the node that precedes the current one
  	 */
	public DLLNode<E> getPrevious() {
		return previous;
	}

	/**
 	 * Sets the node that comes before this one.
 	 *
 	 * @param node
 	 *            the node to be set to follow the current one
 	 */
	public void setPrevious(DLLNode<E> node) {
		 previous = node;
	}

	/**
 	 * Returns the element stored in this node.
 	 *
 	 * @return the element stored in this node
 	 */
	public E getElement() {
		return element;
	}

	/**
 	 * Sets the element stored in this node.
  	 *
  	 * @param elem
  	 *            the element to be stored in this node
  	 */
	public void setElement(E elem) {
		element = elem;
	}

	@Override
	public String toString() {
		return "Element: " + element.toString() + " Has next: " + (next != null);
	}
}




