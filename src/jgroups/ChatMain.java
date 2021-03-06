package jgroups;

import java.util.HashMap;
import java.util.Map;

import jgroups.gui.ChatPanel;

public class ChatMain {
	
	Map<String, SimpleChat> chats;
	Map<String, SimpleChatReceiver> chatsReceiver;
	
	public ChatMain() {
		this.chats = new HashMap<String, SimpleChat>();
		this.chatsReceiver = new HashMap<String, SimpleChatReceiver>();
	}
	
	public void connect(String groupID, String user) throws Exception {
		if (!chats.containsKey(user)) {
			chats.put(user, new SimpleChat(user));
		}
		
		chats.get(user).connect(groupID, null);
	}
	
	public static void main(String[] args) {
		ChatPanel panel = new ChatPanel();
		panel.show();
	}

	public void connectDrawCluster(String user) throws Exception {
		if (!chats.containsKey(user)) {
			chats.put(user, new SimpleChat(user));
		}
		chats.get(user).connectDraw();
	}

}
