package server.model;

import java.util.*;
import sharedClasses.*;

/**
 * This class stores a synchronized object collection for the connected clients.
 * It uses a hashMap and some of the important functions usually associated with it.
 */
public class UnsentMessages {
	private HashMap<User, ArrayList<Message>> unsentMap = new HashMap<>();

	public synchronized void put(User user, Message message) {
		if (!contains(user)) {
			unsentMap.put(user, new ArrayList<Message>());
		}
		unsentMap.get(user).add(message);
	}

	public synchronized ArrayList<Message> getUnsentMessageList(User user) {
		return unsentMap.get(user);
	}

	public synchronized void remove(User user) {
		unsentMap.remove(user);
	}

	public synchronized Boolean contains(User user) {
		return unsentMap.containsKey(user);
	}
}
