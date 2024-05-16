package serversocketsproj1;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSide {
	Label l1;
	TextField t1;
	Button b1;
	String name;
	TextArea ta;
	TextField t2;
	Button b2;

	public ClientSide() {
		Frame f1 = new Frame("chat Application");
		l1 = new Label();
		t1 = new TextField();
		b1 = new Button("Submit");
		l1.setText("Enter your name:");
		l1.setBounds(100, 100, 100, 30);
		t1.setBounds(220, 100, 100, 30);
		b1.setBounds(170, 150, 50, 20);
		b1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				name = t1.getText();
				try {
					socketopeneing(name);
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				f1.dispose();

			}
		});

		f1.add(l1);
		f1.add(t1);
		f1.add(b1);
		f1.setSize(500, 500);
		f1.setLayout(null);
		f1.setVisible(true);
		f1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0); // Close the window
			}
		});

	}

	public static void main(String[] args) {
		ClientSide c = new ClientSide();
	}

	private void socketopeneing(String name) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 6666);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		out.println(name);

		Frame f = new Frame("chat application");
		ta = new TextArea();
		ta.setBounds(20, 50, 300, 300);

		l1 = new Label("Enter the msg:");
		l1.setBounds(20, 370, 100, 20);
		t2 = new TextField();
		t2.setBounds(140, 370, 100, 20);
		b2 = new Button("Send");
		b2.setBounds(100, 400, 50, 30);
		f.add(ta);
		f.add(l1);
		f.add(t2);
		f.add(b2);
		f.setSize(400, 500);
		f.setLayout(null);
		f.setVisible(true);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0); // Close the window
			}
		});

		b2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = t2.getText();
				System.out.println(msg);
				out.println(msg);
				ta.append("you:" + msg + "\n");
				t2.setText("");

			}

		});

		new Thread(() -> {
			String serverResponse;
			try {
				while ((serverResponse = in.readLine()) != null) {
					ta.append(serverResponse + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

	}
}
