package jgroups;

import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

public class SimpleChatReceiver extends ReceiverAdapter {
	
	public String body = "";
	
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
        String line=msg.getSrc() + ": " + msg.getObject();
        body+= line+"\n";
        System.out.println(line);
    }

	public String getBody() {
		return body;
	}
}
