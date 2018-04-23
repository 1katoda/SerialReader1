import java.awt.EventQueue;
import java.awt.Font;
import java.io.*;
import gnu.io.*;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.ComponentOrientation;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import javax.swing.JTextField;


public class SR1gui  {

	private JFrame frame;
	static JPanel panel;
	static DefaultComboBoxModel<String> portBoxModel;
	private static JComboBox<String> PortBox1;
	private JComboBox<String> BaudBox1;
	private static boolean isPortAvailable;
	static boolean firstRun = true;
	
	/**
	 * Launch the application.
	 */

	static String[] availablePorts = new String[10];
	static String selectedPort;
	//selectedBaudRate, selectedDataBits, selectedStopBits, selectedParity;
	static int[] SR1params = new int[4];	

	
    public static void updatePorts(boolean firstRun)
    {
        //Ports Check
        @SuppressWarnings("unchecked")
		java.util.Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        
        int p = 0;
        CommPortIdentifier portId = null; // set if port is found
        while (portIdentifiers.hasMoreElements()) {
        	CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers.nextElement();
        	if(pid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        		portId = pid;
        		availablePorts[p] = pid.getName();
        		selectedPort = pid.getName();
        		if(!firstRun) {
        			if(!(((String) pid.getName()) == ((String) PortBox1.getSelectedItem() )))
        				portBoxModel.addElement((String) pid.getName());
        				panel.revalidate();
        				panel.repaint();
        		}
        		p++;
        		isPortAvailable = true;
        	}
        }
        if(portId == null) {
        	if(firstRun) {
        		System.err.println("Could not find serial ports ");
        		isPortAvailable = false;
        	}
        }
    }



	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {	
					SR1gui window = new SR1gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SR1gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */

	private void initialize() {

		updatePorts(true);
		frame = new JFrame();
			//Update on windows focus change
		frame.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent arg0) {
				updatePorts(firstRun);
			}
			public void windowLostFocus(WindowEvent arg0) {
				updatePorts(firstRun);
			}
		});
		frame.setResizable(false);
		frame.setSize(500, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Ports options
		portBoxModel = new DefaultComboBoxModel<String>();
		
		panel = new JPanel();
		panel.setBounds(15, 5, 100, 25);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		PortBox1 = new JComboBox<String>(portBoxModel);
		PortBox1.setBounds(0, 0, 100, 25);
		panel.add(PortBox1);
		PortBox1.setName("Port");
		PortBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updatePorts(true);
				if(!(PortBox1.getSelectedItem() == null)){
					selectedPort = availablePorts[PortBox1.getSelectedIndex()];
					System.out.println(selectedPort + " port set");
				}
			}
		});
		PortBox1.setToolTipText("Port select");
		
		//Baud rate options
		String[] baudRate = new String[] {"1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "921600"};
		BaudBox1 = new JComboBox<String>();
		BaudBox1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SR1params[0] = Integer.parseInt((String) BaudBox1.getSelectedItem());
				System.out.println(SR1params[0] + " Baud rate set");
			}
		});
		BaudBox1.setBackground(new Color(255, 255, 255));
		BaudBox1.setSize(100, 25);
		BaudBox1.setLocation(235, 5);
		for(int i = 0; i<(baudRate.length-1); i++) {
			BaudBox1.addItem(baudRate[i]);
		};		
		BaudBox1.setSelectedIndex(3);
		BaudBox1.setToolTipText("Baud rate select");
		frame.getContentPane().add(BaudBox1, "cell 0 0,alignx right");
		
		//Data bits options
		String[] dataBitsOptions = new String[] {"5", "6", "7", "8"};
		JComboBox<String> dataBitBox = new JComboBox<String>();
		dataBitBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SR1params[1] = Integer.parseInt((String) dataBitBox.getSelectedItem());
				System.out.println(SR1params[1] + " data bits set");
			}
		});
		dataBitBox.setToolTipText("Data Bits");
		dataBitBox.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		dataBitBox.setBounds(15, 41, 100, 25);
		for(int i = 0; i<(dataBitsOptions.length); i++) {
			dataBitBox.addItem(dataBitsOptions[i]);
		};	
		dataBitBox.setSelectedIndex(3);
		frame.getContentPane().add(dataBitBox);
			
		//Stop bit options
		JComboBox<String> stopBitBox = new JComboBox<String>();
		stopBitBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SR1params[2] = Integer.parseInt((String) stopBitBox.getSelectedItem());
				System.out.println(SR1params[2] + " stop bits set");
			}
		});
		
		stopBitBox.setToolTipText("Stop Bits");
		stopBitBox.setBounds(125, 41, 100, 25);
		stopBitBox.addItem("1");
		stopBitBox.addItem("2");
		stopBitBox.setSelectedIndex(0);
		frame.getContentPane().add(stopBitBox);
		
		//Parity options
		JComboBox<String> parityBox = new JComboBox<String>();
		parityBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SR1params[3] = parityBox.getSelectedIndex();
				System.out.println("-"+SR1params[3]+"- "+((String) parityBox.getSelectedItem())+" parity set");
			}
		});
		parityBox.setToolTipText("Set Parity");
		parityBox.setBounds(235, 41, 100, 25);
		parityBox.addItem("None");
		parityBox.addItem("Odd");
		parityBox.addItem("Even");
		frame.getContentPane().add(parityBox);
		
		// Text area scroll pane
		JScrollPane scrollPane = new JScrollPane();			//Scroll Pane
		scrollPane.setBounds(10, 81, 474, 169);
		frame.getContentPane().add(scrollPane);
		
		JTextArea textArea = new JTextArea();				//Text Area
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		PrintStream txtStream = new PrintStream (new TextAreaOutputStream(textArea));
			textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
			
		//Extra update ports 
			JButton btnRefreshPorts = new JButton("");
			btnRefreshPorts.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					updatePorts(false);
				}
			});
			btnRefreshPorts.setToolTipText("Refresh ports");
			btnRefreshPorts.setIcon(new ImageIcon(SR1gui.class.getResource("/com/sun/javafx/scene/web/skin/Redo_16x16_JFX.png")));
			btnRefreshPorts.setBounds(125, 6, 27, 23);
			frame.getContentPane().add(btnRefreshPorts);
			
			//	Connect/Disconnect button
			JButton btnConnect = new JButton("Connect");
			btnConnect.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
			        if (!(SR1.setConnected) && isPortAvailable) {
			       		System.out.println("Starting serial communication on port "+ selectedPort + " at " + SR1params[0]);
			       		btnConnect.setText("Disconnect");
			       		SR1.launchSR1(selectedPort, SR1params);
			       		PortBox1.setEnabled(false);
			       		BaudBox1.setEnabled(false);
			       		dataBitBox.setEnabled(false);
			       		stopBitBox.setEnabled(false);
			        	parityBox.setEnabled(false);
			        	System.out.println(SR1params[0]+" "+SR1params[1]+" "+SR1params[2]+" "+SR1params[3]);
				    } 
			        else if(SR1.setConnected){
			        	SR1.disconnect();
			        	System.out.println("Stopped serial communication on port "+ selectedPort + " at " + SR1params[0]);
			        	btnConnect.setText("Connect");
			        	PortBox1.setEnabled(true);
			        	BaudBox1.setEnabled(true);
			        	dataBitBox.setEnabled(true);
			        	stopBitBox.setEnabled(true);
			        	parityBox.setEnabled(true);
			        }
			        else {System.out.println("No port to connect to");}
				}
			});
			btnConnect.setToolTipText("Connect to port and start serial read");
			btnConnect.setBounds(384, 41, 100, 25);
			frame.getContentPane().add(btnConnect);
			
			JPanel panelText = new JPanel();
			panelText.setBounds(15, 261, 359, 25);
			frame.getContentPane().add(panelText);
			panelText.setLayout(null);
			
			textSend = new JTextField();
			textSend.setBounds(0, 0, 359, 25);
			panelText.add(textSend);
			textSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(SR1.setConnected) {
						SR1.writetoport((String) textSend.getText());
						System.out.println((String) textSend.getText());
						textSend.setText("");
						panelText.revalidate();
						panelText.repaint();
					}
					else System.out.println("Connect to a port");
				}
			});
			

			textSend.setToolTipText("Send data");
			textSend.setColumns(10);
			
			JButton btnSend = new JButton("Send");
			btnSend.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if(SR1.setConnected) {
					SR1.writetoport((String) textSend.getText());
					System.out.println((String) textSend.getText());
					textSend.setText("");
					panelText.revalidate();
					panelText.repaint();
					}
					else System.out.println("Connect to a port");
				}
			});
			btnSend.setToolTipText("Send");
			btnSend.setBounds(384, 261, 100, 25);
			frame.getContentPane().add(btnSend);
			firstRun = false;
		//Text area output stream	
		System.setOut(txtStream);
		System.setErr(txtStream);
	}
	
	String readstate;
	private JTextField textSend;
	
	//Text area output stream
	public class TextAreaOutputStream extends OutputStream 
	{
		private javax.swing.JTextArea textArea;
		public TextAreaOutputStream(JTextArea textArea0) {
			this.textArea = textArea0;
		}
		public void write(int b) throws IOException {
			textArea.append(String.valueOf((char) b));
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
		public void write(char[] cbuf, int off, int len) throws IOException{
			textArea.append(new String (cbuf, off, len));
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}
}

