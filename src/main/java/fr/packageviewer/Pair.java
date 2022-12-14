package fr.packageviewer;

/**
 * The Pair class stores two objects of distinct type
 * 
 * @author C.Marla, R.Thomas, S.Djalim
 * @version 1.0
 */
public class Pair<K,V> {
	private K first;
	private V second;
	/**
	 * 
	 */
	public Pair() {

	}

	/**
	 * 
	 * @param first
	 * @param second
	 */
	public Pair(K first, V second) {
		this.first = first;
		this.second = second;
	}
	/**
	 * 
	 * @return
	 */
	public K getFirst() {
		return first;
	}

	public void setFirst(K first) {
		this.first = first;
	}
	
	/**
	 * 
	 * @return
	 */
	public V getSecond() {
		return second;
	}

	public void setSecond(V second) {
		this.second = second;
	}

	@Override
	public String toString() {
		return "Pair{key=%s,value=%s}".formatted(first, second);
	}
}
