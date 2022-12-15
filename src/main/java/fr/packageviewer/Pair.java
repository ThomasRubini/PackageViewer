package fr.packageviewer;

/**
 * The Pair class stores two objects of distinct type
 * 
 * @author R.Thomas
 * @version 1.0
 */
public class Pair<K, V> {
	private K first;
	private V second;

	/**
	 * Empty Constructor for the Pair class
	 */
	public Pair() {

	}

	/**
	 * Main Constructor for the Pair class
	 * 
	 * @param first, the first object to be stored
	 * @param second the second object to be stored
	 */
	public Pair(K first, V second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Getter for the attribute first
	 * 
	 * @return the object stored in the attribute first
	 */
	public K getFirst() {
		return first;
	}

	/**
	 * Setter for the attribute first
	 * 
	 * @param first Store the given object in the attribute first
	 */
	public void setFirst(K first) {
		this.first = first;
	}

	/**
	 * Getter for the attribute second
	 * 
	 * @return the object stored in the attribute second
	 */
	public V getSecond() {
		return second;
	}

	/**
	 * Setter for the attribute second
	 * 
	 * @param second Store the given object in the attribute second
	 */
	public void setSecond(V second) {
		this.second = second;
	}

	/**
     * Returns a string representation of the pair
     * 
     * @return String, string representation of the pair
     */
	@Override
	public String toString() {
		return "Pair{key=%s,value=%s}".formatted(first, second);
	}
}
