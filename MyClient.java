import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

public class MyClient extends JFrame implements ActionListener 
{
	JLabel l;
	JTextArea sendtxt,recievetxt;
	JButton send;

	MyClient(String s)
	{
		super(s);
		l=new JLabel("Enter the Message");
		sendtxt=new JTextArea();
		recievetxt=new JTextArea();
		send=new JButton("SEND");
		l.setBounds(10,10,120,20);
		sendtxt.setBounds(10,40,300,100);
		recievetxt.setBounds(150,200,300,100);
		send.setBounds(10,150,100,50);
		add(l); add(sendtxt); add(send); add(recievetxt);
		send.addActionListener(this);

		setLayout(null);
		setSize(500,400);
		setVisible(true);
	}

	BufferedReader br;
	PrintStream ps;

	public static void main(String... args)
	{
		MyClient obj=new MyClient("MyChatterBox");

		try
		{
			Socket s=new Socket("localhost",9786);
			System.out.println("Server is connected");
			obj.br=new BufferedReader(new InputStreamReader(s.getInputStream()));
			obj.ps=new PrintStream(s.getOutputStream());
			
			while(true)
			{
				String str=obj.br.readLine();
				
				if(str.equals("exit")==true)
					{
						System.out.print("Connection lost...");
						System.exit(1);
					}	
				obj.recievetxt.setText(obj.recievetxt.getText()+str+"\n");
			}
		}
		catch(UnknownHostException e)
		{
			System.out.println("Not find the IP-ADDRESS for: "+e);
		}
		catch(Exception e)
		{
			System.out.println("Not Found data for Socket: "+e);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		String msg=sendtxt.getText();
		sendtxt.setText("");
		ps.println(msg);
		ps.flush();
			if(msg.equals("exit")==true)
					{
						System.out.print("Connection is terminated by Clinet");
						System.exit(1);
					}
	}
}