package jgroups.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import jgroups.ChatMain;

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
public class ChatPanel extends javax.swing.JPanel {
	private JButton jButton1;
	private JButton jButton3;
	private JLabel jLabel1;
	private JTextField name;
	private JButton jButton2;
	
	ChatMain chat;

	/**
	* Auto-generated main method to display this 
	* JPanel inside a new JFrame.
	*/
	public void show() {
		JFrame frame = new JFrame();
		frame.getContentPane().add(new ChatPanel());
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
	
	public ChatPanel() {
		super();
		chat = new ChatMain();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setPreferredSize(new Dimension(400, 300));
			this.setLayout(null);
			{
				jButton1 = new JButton();
				this.add(jButton1);
				jButton1.setText("Join grupo-1");
				jButton1.setBounds(24, 92, 186, 23);
			}
			{
				jButton2 = new JButton();
				this.add(jButton2);
				jButton2.setText("Join grupo-2");
				jButton2.setBounds(24, 135, 186, 23);
			}
			{
				name = new JTextField();
				this.add(name);
				name.setBounds(24, 34, 342, 23);
				name.setText("name");
			}
			{
				jLabel1 = new JLabel();
				this.add(jLabel1);
				jLabel1.setText("Your name:");
				jLabel1.setBounds(24, 18, 186, 16);
			}
			{
				jButton3 = new JButton();
				this.add(jButton3);
				jButton3.setText("Connect at Draw-cluster");
				jButton3.setBounds(24, 197, 186, 23);
			}

			jButton1.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						chat.connect("Group1", name.getText());
						System.out.println("joined group1");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			jButton2.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						chat.connect("Group2", name.getText());
						System.out.println("joined group2");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			jButton3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						chat.connectDrawCluster(name.getText());
						System.out.println("joined draw-group");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
