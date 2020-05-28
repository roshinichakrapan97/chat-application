import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
class S_CHAT
{
	public static int CPort, S_Client_No,DPort;
	public static void main(String[] arg)
        {
        	System.out.println("Initializing Chatting Server...");
                System.out.println("Default binding port no : 2000.");
                System.out.println("Maximum 15 users can log at a time.");
                MainFrame MF= new MainFrame();
                MF.show();
        }
}
class CLIENT_INFO
{
        String Cli_name;
        Socket Cli_Cmd;
	CLIENT_INFO(String s, Socket C)
        {
        	Cli_name=s;
               	Cli_Cmd=C;
        }
}
class MainFrame extends Frame implements ActionListener
{
	JLabel l,ul;
        JTextField P,B_M;
        public static JButton S,CW,Broad;
        public static CLIENT_INFO C_INFO[]=new CLIENT_INFO[15];
        public static List UL;
        public static TextArea T;
	MainFrame()
        {
        super("MAIN_SERVER Chatting Server");
        setSize(500,550);
        setLayout(null);
        addWindowListener(new WindowAdapter ()
        {
	public void windowClosing (WindowEvent e)
	{
        System.exit(0);
	}
	} );
	l=new JLabel("Listen at Port:");
        add(l);
        l.setBounds(10,30,100,20);
  	P=new JTextField("2000");
        add(P);
        P.setBounds(120,30,60,20);
	S=new JButton("Start");
        add(S);
        S.setBounds(200,28,80,25);
        S.addActionListener(this);
	T=new TextArea("MAIN_SERVER Chatting Server...STARTED",25,50,1);
	add(T);
        T.setBounds(10,60,350,440);
        T.setEditable(false);
	ul = new JLabel("On Line Users");
        add(ul);
        ul.setBounds(370,60,120,20);
	UL=new List(25);
        add(UL);
        UL.setBounds(370,80,120,390);
	B_M=new JTextField("Server Broadcast Message.");
        add(B_M);
        B_M.setBounds(10,515,350,20);
	Broad=new JButton("Broadcast Msg.");
        add(Broad);
        Broad.setBounds(370,512,120,25);
        Broad.addActionListener(this);
	CW=new JButton("Message To...");
        add(CW);
        CW.setBounds(370,475,120,25);
        CW.setVisible(false);
        CW.addActionListener(this);
	}
	public void actionPerformed(ActionEvent AE)
        {
        if(AE.getSource()==S)
        {
        UL.addItem("[MAIN_SERVER]");
        C_INFO[0]=new CLIENT_INFO("[MAIN_SERVER]",null);
        try
        {
        S_CHAT.CPort=Integer.parseInt(P.getText());
        }
	catch(Exception E){}
        S_CHAT.DPort=S_CHAT.CPort+1000;
	new THBind(S_CHAT.CPort);
	if(UL.getItemCount()>0)
                CW.setVisible(true);
        }
	if(AE.getSource()==CW)
        {
        String msg=JOptionPane.showInputDialog("Enter the Message:");
        if(!(msg==null))
        if(!(UL.getSelectedItem()==null))
        {
           System.out.println("Message : "+msg+" to : "+UL.getSelectedIndex());
           send_msg(msg,UL.getSelectedIndex());
        }
        else
            JOptionPane.showMessageDialog(this,"No Client Selected","Alert",JOptionPane.INFORMATION_MESSAGE);
        }
	if(AE.getSource()==Broad)
        {
           for(int t=0;t<UL.getItemCount();t++)
              send_msg("BROADCAST : "+B_M.getText(),t);
	}
        }
	public void send_msg(String m,int TO)
	{
                 PrintWriter o;
    		 if(TO>0)
   		 {
                 try
                 {
                      o = new PrintWriter(MainFrame.C_INFO[TO].Cli_Cmd.getOutputStream(),true);
                      o.println("S_MSG");
                      o.println(m);
		 }
                 catch(Exception E){}
                 }
                 else
                 	JOptionPane.showMessageDialog(this,m,"Server Message", JOptionPane.INFORMATION_MESSAGE);

	}

}
class THBind extends Frame implements Runnable
{
Thread BT;
int dport,cport;
static int i=1;
static ServerSocket server_CSocket,server_DSocket;
public THBind(int cp)
{
 BT=new Thread(this);
 cport=cp;
 dport=S_CHAT.DPort;
 BT.start();
}
public void run()
{
  MainFrame.S.setVisible(false);
  if (cport<1024)
  {
     System.out.println("Server Binded to port = 2000 (default)");
     cport = 2000;
     dport=3000;
     S_CHAT.DPort=3000;
  }
  else
  {
  try 
  {
    server_CSocket = new ServerSocket(cport);
    server_DSocket = new ServerSocket(dport);
    MainFrame.T.append("Server waiting for client on port " +
    server_CSocket.getLocalPort());
    while(i<15)
   {
      Socket CSocket = server_CSocket.accept();
      MainFrame.T.append("New connection accepted " +CSocket.getInetAddress() + ":" + CSocket.getPort());
      new Client_P(CSocket,i);
      i++;
    } 
  } 
  catch (IOException e) 
  {
     System.out.println(e);
  }
  }
}
} 
class Client_P extends Frame implements Runnable
{
Thread t;
int ci;
private Socket c_s;
String uname="Anonymous",pass;
BufferedReader C_input;
PrintWriter C_output;
PrintWriter r=null,a=null;
BufferedReader r1=null,a1=null;
public Client_P(Socket C,int i)
{
t=new Thread(this);
c_s = C;
ci=i;
t.start();
}
public void run()
{
int check=login();
while(check!=1)
check=login();
if (check==1)
{
	System.out.println("OK...");
        C_output.println("U_E");
	MainFrame.C_INFO[ci]=new CLIENT_INFO(uname,c_s);
        MainFrame.UL.addItem(uname);
        MainFrame.T.append(""+MainFrame.C_INFO[ci].Cli_name);
        MainFrame.T.append(""+MainFrame.C_INFO[ci].Cli_Cmd);
	System.out.println("Broadcasting List...");
        BROADCAST_LIST();
        System.out.println(" List BroadCasted");
        System.out.println("Reading Clients");
        System.out.println(THBind.i);
        int i=1;
        try
        {
        while(i==1)
        {
        String l=C_input.readLine();
        i=Manipulate(l);
        if(i==2)
        {
         System.out.println("Enterting MSG_READER thread");
         new MSG_RDR(r1,a);
         new MSG_RDR(a1,r);
         System.out.println("Enterting MSG_READER thread done sucessfully");
         i=1;
        }
	}
	}
        catch(IOException E)
        {
	 System.out.println("Error in Reading Client Request");
	}
	MainFrame.T.append("User Logged Out : "+uname);
       	CLIENT_INFO tmp[]=new CLIENT_INFO[15];
        int j=0;
        for(int x=0;x<THBind.i;x++)
        {
           if(!uname.equals(MainFrame.C_INFO[x].Cli_name))
           {
             tmp[j]=MainFrame.C_INFO[x];
             System.out.println(tmp[j].Cli_name);
             j++;
           }
	}
        THBind.i--;
        System.out.println(THBind.i);
        MainFrame.C_INFO=new CLIENT_INFO[15];
        MainFrame.UL.removeAll();
        System.out.println("Modifying Client Info Array");
        for(int x=0;x<j;x++)
        {
           MainFrame.C_INFO[x]=tmp[x];
           MainFrame.UL.addItem(tmp[x].Cli_name);
        }
	BROADCAST_LIST();
}
else
{
System.out.println("User Already Exist");
}
}
void BROADCAST_LIST()
{
PrintWriter o;
for(int j=1;j<THBind.i;j++)
{
try
{
  o = new PrintWriter(MainFrame.C_INFO[j].Cli_Cmd.getOutputStream(),true);
  o.println("ULIST");
  for(int x=0;x<MainFrame.UL.getItemCount();x++)
  o.println(MainFrame.UL.getItem(x));
  o.println("END");
}
catch(Exception E){}
}
}
int Manipulate(String CMD)
{
if(CMD.equals("LOGOUT"))
   return 0;
if(CMD.equals("RQT_CHAT"))
{
try
{
   String u=C_input.readLine();
   String f=C_input.readLine();
   System.out.println("Waiting for Requestor");
   Socket Friend_Cmd=null;
   Socket d_s1=THBind.server_DSocket.accept();
   for(int j=0;j<MainFrame.UL.getItemCount();j++)
   if(f.equals(MainFrame.C_INFO[j].Cli_name))
   {
    Friend_Cmd=MainFrame.C_INFO[j].Cli_Cmd;
    break;
   }
   try
   {
     PrintWriter Fo=new PrintWriter(Friend_Cmd.getOutputStream(),true);
     Fo.println("CALL_CHAT");
     Fo.println(f);
     Fo.println(u);
     System.out.println("Information Transfered to Acceptor");
    }
    catch(Exception E){}
    System.out.println("Waiting for Acceptor");
    Socket d_s2=THBind.server_DSocket.accept();
    System.out.println("Connection for Acceptor Done");
    System.out.println(d_s2);
    COM_PROCESS(d_s1,d_s2);
    System.out.println("COM_PROCESS Done here.");
}
catch(IOException E)
{
 System.out.println("Reading Chatting Request");
}
 return 2;
}
else
  return 1;
}
public void COM_PROCESS(Socket rqt, Socket accp)
{
try
{
r=new PrintWriter(rqt.getOutputStream(),true);
a=new PrintWriter(accp.getOutputStream(),true);
r1=new BufferedReader(new InputStreamReader(rqt.getInputStream()));
a1=new BufferedReader(new InputStreamReader(accp.getInputStream()));
System.out.println("Streams Created");
}
catch(Exception E){}
}
boolean already_exist()
{
  System.out.println("index :"+ ci);
  if(ci!=0)
    for(int j=0;j<ci;j++)
    {
      if(MainFrame.C_INFO[j].Cli_name.equals(uname))
         return true;
      else
         continue;
    }
  return false;
}
public int login()
{
       int FOUND=0,flag=0;
       String cmd="S_U";
       try 
      {
	C_input = new BufferedReader(new InputStreamReader(c_s.getInputStream()));
	C_output = new PrintWriter(c_s.getOutputStream(),true);
        cmd=C_input.readLine();
        uname=C_input.readLine();
        pass=C_input.readLine();
        MainFrame.T.append("Authenticating user -"+uname);
	DataInputStream fin = new DataInputStream(new FileInputStream("data.txt"));
        while(true)
        {
        if( uname.equals(fin.readUTF()) )
        {
         flag = 1;
         if( pass.equals(fin.readUTF()) )
         {
         FOUND=1;
         break;
         } 
         else
         {
         FOUND=0;
         break;
         }
        }
       }
     }
     catch(Exception e1) { }
     System.out.println("CMD: "+cmd+"  flag="+flag);
     if(cmd.equals("N_U") && flag==0)
     {
    try
    {
      DataOutputStream fout = new DataOutputStream(new FileOutputStream("data.txt",true));
      fout.writeUTF(uname);
      fout.writeUTF(pass);
      fout.close();
      System.out.println("User Wrote in File");
      C_output.println("User Signed Sucessful");
    }
    catch(Exception E){};
   }
else
{
       if(flag==1&&FOUND==0&&cmd.equals("N_U"))
          C_output.println("User Exist Sign-in other.");
       else
        {
         if (FOUND==0&&cmd.equals("S_U"))
                 C_output.println("NACK");
         else
         {
            if(FOUND==1&&already_exist()==false)
            {
              MainFrame.T.append("User"+uname+" Authenticated");
              C_output.println(S_CHAT.DPort);
            }
            else
            {
              FOUND=0;
              C_output.println("U_A_E");
            }
         }
       }
}
return (FOUND);
}
}
class MSG_RDR extends Frame implements Runnable
{
   Thread t;
   BufferedReader BR;
   PrintWriter PW;
   public MSG_RDR(BufferedReader br, PrintWriter pw)
   {
       t=new Thread(this);
       System.out.println("I am Inside MSG_RDR");
       BR=br;
       PW=pw;
       PW.println("Stream established For Communication...");
       MainFrame.T.append("Indise The MESSAGE READER Thread");
       t.start();
   }
   public void run()
   {
    while(true)
    {
      try
      {
       String s= BR.readLine();
       PW.println(s);
      }
      catch(IOException E){}
    }
   }
}