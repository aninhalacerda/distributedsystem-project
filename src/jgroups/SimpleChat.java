package jgroups;


import java.util.LinkedList;
import java.util.List;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.demos.Draw;
import org.jgroups.util.Util;

public class SimpleChat extends ReceiverAdapter {
	
	JChannel channel;
	JChannel channel2;
    String user_name;
    
    final List<String> state=new LinkedList<String>();
    
    public SimpleChat(String user_name) {
    	this.user_name = user_name;
	}
    
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
        String line=msg.getSrc() + ": " + msg.getObject();
        System.out.println(line);
    }

    public byte[] getState() {
        synchronized(state) {
            try {
                return Util.objectToByteBuffer(state);
            }
            catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
	public void setState(byte[] new_state) {
        try {
            List<String> list=(List<String>)Util.objectFromByteBuffer(new_state);
            synchronized(state) {
                state.clear();
                state.addAll(list);
            }
            System.out.println("received state (" + list.size() + " messages in chat history):");
            for(String str: list) {
                System.out.println(str);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void connect(String group, ReceiverAdapter receiver)  throws Exception {
    	channel=new JChannel();
    	channel.setReceiver(receiver);
        if (receiver == null) {
        	channel.setReceiver(this); 
    	}
        channel.connect(group);
        channel.getState(null, 10000);
        Draw draw = new Draw(channel);
        draw.go();
    }
    
	public void connectDraw() throws Exception {
		Draw draw = new Draw(null, false, false, false, 0, false, user_name+"@drawGroup");
        draw.go();
	}

	public void send(String text) throws Exception {
		String line="[" + user_name + "] " + text;
        Message msg=new Message(null, null, line);
        channel.send(msg);
	}
	
	public void quit() {
		channel.close();
	}

}
