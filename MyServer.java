import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

public class MyServer extends JFrame implements ActionListener, Runnable
{
	JLabel l;
	JTextArea sendtxt,recievetxt;
	JButton send;

	MyServer(String s)
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

	static int i=0;
	Socket[] s=new Socket[5];
	BufferedReader[] br=new BufferedReader[6];
	PrintStream[] ps=new PrintStream[6];

	public static void main(String... args)
	{
		MyServer obj=new MyServer("Server");

		while(true)			 
		{	
			try
			{	
					Thread t=new Thread(obj);
					ServerSocket ss=new ServerSocket(9786);
					System.out.println("waiting for request...");
					obj.connectedUsers();
					obj.s[i]=ss.accept();
					i++; 
					System.out.println(obj.s[i-1]);				
					t.start();
					System.out.println("YEAH");	
			}
			catch (Exception e)
			{
				//System.out.println("Not Found data for Socket: "+e);
			}	
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		String msg=sendtxt.getText();
		sendtxt.setText("");
		System.out.println(msg);
		for(int j=0;j<i;j++)	
			{
				ps[j].println("Server says :"+msg);
				ps[j].flush();
			}
			if(msg.equals("exit")==true)
					{
						System.out.print("Connection is terminated by Client");
						System.exit(1);
					}
	}

	public void run()
	{
	   final int count=i-1;
	   try {	
			System.out.println("Request accepted");
			br[i-1]=new BufferedReader(new InputStreamReader(s[i-1].getInputStream()));
			ps[i-1]=new PrintStream(s[i-1].getOutputStream());
			while(true)
			  {
				String str=br[count].readLine();
				for(int j=0;j<i;j++)	
					{		
				    		ps[j].println("Client "+(count+1)+" : "+str);
					 		ps[j].flush();					 	
					}

				if(str.equals("exit")==true)
					{
						System.out.print("Connection lost...");
						System.exit(1);
					}	
				recievetxt.setText(recievetxt.getText()+str+"\n");
			  }
			}
			catch(Exception e){e.printStackTrace();}
	}

	public void connectedUsers()
	{
		for(int j=0;j<i;j++)
			System.out.println("Client "+(j+1)+" : "+s[j].isConnected());
	}
}