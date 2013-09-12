package jgroups;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;

public class ChatJGroups extends ReceiverAdapter {

	final String MESSAGE_ALL = "-a";
	final String MESSAGE = "-m";
	final String NODES = "-n";
	final String QUIT = "quit";
	final String EXIT = "exit";
	
    JChannel channel;
    String user_name=System.getProperty("user.name", "n/a");
    String output;
    
    final List<String> state=new LinkedList<String>();

    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    public void receive(Message msg) {
    	try {
			logReceive();
		} catch (IOException e) {
			e.printStackTrace();
		}
        String line= msg.getSrc() + ": " + msg.getObject();
        System.out.println(line);
        synchronized(state) {
            state.add(line);
        }
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

    public void setState(byte[] new_state) {
        try {
            @SuppressWarnings("unchecked")
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

    private void start(String user, String output) throws Exception {
    	this.output = output;
    	this.user_name = user;
    	channel=new JChannel(getClass().getClassLoader().getResourceAsStream("udp.xml"));
    	channel.setReceiver(this);
        channel.setName(user);
        channel.connect("ChatCluster");
        channel.getState(null, 10000);
        eventLoop();
        channel.close();
    }

    private void eventLoop() {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.print("> "); System.out.flush();
                String line=in.readLine().toLowerCase();
                if(line.startsWith(QUIT) || line.startsWith(EXIT)) {
                    break;
                } else if(line.startsWith(MESSAGE_ALL)) {
	                line=line.substring(MESSAGE_ALL.length()).trim();
	                Message msg=new Message(null, null, line);
	                logSend();
	                channel.send(msg);
                } else if (line.startsWith(NODES)) {
                	String txt = "[" + channel.getView().getMembers().size() + "] ";
					for (Address a : channel.getView().getMembers()) {
						txt += a.toString()+", ";
					}
					System.out.println(txt);
				} else if (line.startsWith(MESSAGE)) {
					line = line.substring(MESSAGE.length()).trim();
					String dst = line.substring(0, line.indexOf(" "));
					String txt = line.substring(dst.length());
					for (Address a : channel.getView().getMembers()) {
						if (a.toString().equals(dst)) {
							Message msg=new Message(a, null, txt);
							logSend();
							channel.send(msg);
						}
					}
				} else {
					printUsage();
				}
            }
            catch(Exception e) {
            }
        }
    }


	private void logSend() throws IOException {
		String log = "SEND " + user_name + " : " + Calendar.getInstance().getTimeInMillis()+ "\n";
		Path path = Paths.get(output);
	    Files.write(path, log.getBytes(), StandardOpenOption.APPEND);
	}
	
	private void logReceive() throws IOException {
		String log = "RECE " + user_name + " : " + Calendar.getInstance().getTimeInMillis()+ "\n";
		Path path = Paths.get(output);
	    Files.write(path, log.getBytes(), StandardOpenOption.APPEND);
	}

	private static void printUsage() {
		System.out.println("Usage:\n" + 
				"Message to a specific node\n" +
				"-m dest_name message\n" +
				"Message to all:\n" +
				"-a message\n" +
				"See all nodes:\n" +
				"-n");
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Usage: java -Djava.net.preferIPv4Stack=true -Djgrous.bind_address=ip jgroups.ChatJGroups user_name output_filename");
		} else {
			String user = args[0];
			String output = args[1];
			new ChatJGroups().start(user, output);
		}
    }
}
