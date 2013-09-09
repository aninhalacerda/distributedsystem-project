package jgroups.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;

import jgroups.SimpleChat;
import jgroups.SimpleChatReceiver;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ChatDialog extends javax.swing.JPanel {
	private JTextPane jTextPane1;
	private JButton jButton1;
	private JTextField jTextField1;
	
	private SimpleChat chat;
	private SimpleChatReceiver chatReceiver;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public void showDialog() {
		JFrame frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		frame.setName("Chat");
	}
	
	public ChatDialog(SimpleChat chat, SimpleChatReceiver receiver) {
		super();
		this.chat = chat;
		this.chatReceiver = receiver;
		initGUI();
	}
	
	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
			this.setLayout(null);
			{
				jTextPane1 = new JTextPane();
				this.add(jTextPane1);
				System.out.println(this.chatReceiver.getBody());
				jTextPane1.setText(this.chatReceiver.getBody());
				jTextPane1.setBounds(12, 12, 369, 198);
			}
			{
				jTextField1 = new JTextField();
				this.add(jTextField1);
				jTextField1.setText("");
				jTextField1.setBounds(12, 242, 215, 23);
			}
			{
				jButton1 = new JButton();
				this.add(jButton1);
				jButton1.setText("Send");
				jButton1.setBounds(322, 242, 40, 23);
				jButton1.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							chat.send(jTextField1.getText());
							updateText();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateText() {
		System.out.println("update");
		System.out.println(this.chatReceiver.getBody());
		jTextPane1.setText(chatReceiver.getBody());
	}

}
