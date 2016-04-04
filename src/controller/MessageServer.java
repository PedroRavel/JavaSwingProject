package controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.Message;
//This is a simulated message server

public class MessageServer implements Iterable<Message>{
	private Map<Integer,List<Message>> messages;
	
	private List<Message> selected;
	
	public MessageServer() {
		selected = new ArrayList<Message>();
		messages = new TreeMap<Integer,List<Message>>();
		
		List<Message> list = new ArrayList<Message>();
		list.add(new Message("Sent required instructions", "Waiting for reply"));
		list.add(new Message("See you later", "Shutting down"));
		list.add(new Message("Database is up and running", "Database check complete"));
		list.add(new Message("Welcome back", "Booting up"));
		messages.put(0, list);
		
		list = new ArrayList<Message>();
		list.add(new Message("Program is 48% complete", "Alot of work left"));
		messages.put(1, list);
	}
	
	public void setSelectedServer(Set<Integer> servers){
		selected.clear();
		for(Integer id: servers){
			if(messages.containsKey(id)) {
				selected.addAll(messages.get(id));
			}
		}
	}
	
	public int getMessageCount() {
		return selected.size();
	}

	@Override
	public Iterator<Message> iterator() {
		// TODO Auto-generated method stub
		return new MessageIterator(selected);
	}
	
}
class MessageIterator implements Iterator {
	private Iterator<Message> iterator;
	
	public MessageIterator(List<Message> messages){
		iterator = messages.iterator();
		
	}
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return iterator.hasNext();
	}

	@Override
	public Object next() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {

		}
		return iterator.next();
	}

	@Override
	public void remove() {
		iterator.remove();
		
	}
	
}
