package client.model;

import java.util.LinkedList;

/**
 * This class acts as a synchronized object list for Message-objects.
 * @param <T>
 */

public class MessageBuffer<T> {
private LinkedList<T> buffer = new LinkedList<T>();
	
	public synchronized void put(T obj) {
		buffer.addLast(obj);
		notifyAll();
	}
	
	public synchronized T get() throws InterruptedException {
		while(buffer.isEmpty()) {
			wait();
		}
		return buffer.removeFirst();
	}
	
	public int size() {
		return buffer.size();
	}
}
