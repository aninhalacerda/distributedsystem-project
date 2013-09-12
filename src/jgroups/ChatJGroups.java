package jgroups;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
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

	final String SEPARADOR = "\\|";
	final String MESSAGE_ALL = "-a";
	final String MESSAGE = "-m";
	final String NODES = "-n";
	final String QUIT = "quit";
	final String EXIT = "exit";
	
    JChannel channel;
    String user_name=System.getProperty("user.name", "n/a");
    String output;
    int count = 0;
    
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

    private void start(String user, String output, String inputfile) throws Exception {
    	this.output = output;
    	this.user_name = user;
    	channel=new JChannel(getClass().getClassLoader().getResourceAsStream("udp.xml"));
    	channel.setReceiver(this);
        channel.setName(user);
        channel.connect("ChatCluster");
        channel.getState(null, 10000);
        if (inputfile == null) {
        	eventLoop();
        	channel.close();
        } else {
        	executeTestFile(inputfile);
        }
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
    
    public void executeTestFile(String inputfile) throws Exception {
    	List<Message> msgs = readInputFile(inputfile);
    	for (Message m : msgs) {
    		logSend();
			channel.send(m);
    	}
    }
    private List<Message> readInputFile(String inputfile) {
		List<Message> params = new ArrayList<Message>();

		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File(
					inputfile)));

			while (bufferedReader.ready()) {
				String line = bufferedReader.readLine();
				String[] campos = line.split(SEPARADOR);

				String receiver = campos[1];
				String msg = campos[2];
				
				Address ad_receiver = null;

				for (Address a : channel.getView().getMembers()) {
					if (!receiver.equals("all") && a.toString().equals(receiver)) {
						ad_receiver = a;
					}
				}
				
				params.add(new Message(ad_receiver, null, msg));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
				}
			}
		}
		return params;
	}


	private void logSend() throws IOException {
		String log = "SEND " + count + " " + user_name + " : " + Calendar.getInstance().getTimeInMillis()+ "\n";
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
			System.out.println("Usage: java -Djava.net.preferIPv4Stack=true -Djgrous.bind_address=ip jgroups.ChatJGroups user_name output_filename [input_filename]");
		} else {
			String user = args[0];
			String output = args[1];
			String inputfile = args.length == 3? args[2] : null;
			new ChatJGroups().start(user, output, inputfile);
		}
    }
}
