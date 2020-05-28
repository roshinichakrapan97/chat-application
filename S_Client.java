import java.io.*;
import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

class S_Client
   {

   public static void main(String args[])
       { 
        MainFrame frm = new MainFrame();
       frm.setResizable(false);
       frm.show();
       }
   }
class MainFrame extends Frame implements ActionListener
{
   TextField t1,t2;
   Label l1,l2,l3,St;
   Button b1;
   static Socket c_s;
   static String serv="localhost";
   int flag = 0;
   String nm,ps;
   public MainFrame()
       {
        super("Connection To...");
       setSize(250,150);
       setLayout(null);
       addWindowListener(new WindowAdapter ()
      {
        public void windowClosing (WindowEvent e)
        {
   System.exit(0);
   }
 } );
add(l1=new Label("\nServer IP:"));
 l1.setBounds(20,40,80,20);
 add(t1 = new TextField(10));
 t1.setBounds(110,40,80,20);
add(l2=new Label("\nPort:"));
 l2.setBounds(20,70,80,20);
add(t2 = new TextField("2000"));
t2.setBounds(110,70,80,20);
 add(b1 = new Button("\nConnect"));
b1.setBounds(130,100,60,20);
add(l3=new Label("\nStatus"));
 l3.setBounds(10,125,200,20);
 St=new Label("");
 St.setBounds(60,125,190,20);
 add(St);
 b1.addActionListener(this);
 }
 public void actionPerformed(ActionEvent ae)
 {
 if(ae.getSource()==b1)
 {
     if(t2.getText().equals(""))
    l3.setText("Port No. meust be entered.");
    else
 CONNECT_TO();
}}
 public void CONNECT_TO()
{
 int port=2000;
if(t1.getText()=="")serv="localhost";
 else
serv=t1.getText();
try
{
port = Integer.parseInt(t2.getText());
}
 catch (Exception e) { }

 try
 {
        c_s = new Socket(serv, port);
        l3.setText("Connected to server " +c_s.getInetAddress()+":" + c_s.getPort());
         form F=new form();
         F.show();
       F.setResizable(false);
        this.hide();
       }
      catch (UnknownHostException e)
  {
     System.out.println(e);
      System.out.println("Error in connecting Server");
    //System.exit(ERROR);
 }
catch (IOException e)
{
System.out.println(e);
System.out.println("Error in connecting Server");//System.exit(ERROR);
} } }

class form extends Frame implements ActionListener
   {
   static TextField t1,t2;
   Label l1,l2,l3,St;
   Button b1,b2;

   static BufferedReader C_input;
   static PrintWriter C_output;

   int flag = 0;
   static int po;
   String nm,ps;
   public form()
       {
 super("Login to MAIN_SERVER Chatting Server");
setSize(250,150);
 setLayout(null);
addWindowListener(new WindowAdapter ()
{
 public void windowClosing (WindowEvent e)
    {
     System.exit(0);
     }
 } );
   try
 {C_input = new BufferedReader(new InputStreamReader(MainFrame.c_s.getInputStream()));
   C_output = new PrintWriter(MainFrame.c_s.getOutputStream(),true);
      }
 catch(Exception E){}
      add(l1=new Label("Username:"));
       l1.setBounds(20,40,80,20);
       add(t1 = new TextField(10));
       t1.setBounds(110,40,80,20);
       add(l2=new Label("Password:"));
       l2.setBounds(20,70,80,20);
       add(t2 = new TextField(10));
       t2.setBounds(110,70,80,20);
       add(b1 = new Button("LOGIN"));
       b1.setBounds(140,100,50,20);
       add(b2 = new Button("Sign Up..."));
       b2.setBounds(70,100,60,20);
       add(l3=new Label("Report:"));
       l3.setBounds(10,125,200,20);
       St=new Label("");
       St.setBounds(60,125,190,20);
       add(St);

       t2.setEchoChar('*');
       b1.addActionListener(this);
       b2.addActionListener(this);

       }
public void actionPerformed(ActionEvent ae)
 {
po=3000;
String l="";
  if(ae.getSource()==b2)
  {
     Sign_UP Su = new Sign_UP();
     Su.show();
       t1.setText("");
       t2.setText("");
}
 if(ae.getSource() == b1)
{
  C_output.println("S_U");
 C_output.println(t1.getText());
C_output.println(t2.getText());
  try
   {
 l=C_input.readLine();
 System.out.println(l);
}
catch(IOException E){}
 if(!l.equals("No ACK"))
 {
 if(l.equals("U_A_E"))
 {
l3.setText("User Alreal Exist");
      System.out.println("User Alreal Exist");
 t1.setText("");
t2.setText("");
 }
else
   {
  try
   {
   po=Integer.parseInt(l);
      }
  catch(Exception E){}
 try
{
System.out.println("Waiting for other cmd");
l=C_input.readLine();
System.out.println(l);
System.out.println("CMD Recieved");
}
 catch(IOException E){}
 Frame3 F3=new Frame3(t1.getText());
 F3.show();
 F3.setResizable(false);
 this.hide();
}
 }
 else
 l3.setText("Invalid Username/Password.");
 }

}
 } 
class Sign_UP extends Frame implements ActionListener
   {
   TextField tt1,tt2,tt3;
   Label l1,l2,l3,l4;
   Button b1;

   public Sign_UP()
       {
       super("New User to MAIN_SERVER Chatting Server");
       setSize(250,180);
       setLayout(null);
 addWindowListener(new WindowAdapter ()
{
  public void windowClosing (WindowEvent e)
{
dispose();
}
  } );
add(l1=new Label("Username:"));
l1.setBounds(20,40,80,20);
 add(tt1 = new TextField(10));
 tt1.setBounds(110,40,80,20);
add(l2=new Label("Password:"));
l2.setBounds(20,70,80,20);
add(tt2 = new TextField(10));
 tt2.setBounds(110,70,80,20);
 add(l4=new Label("Confirm Pass."));
l4.setBounds(20,100,80,20);
add(tt3 = new TextField(10));
 tt3.setBounds(110,100,80,20);
 add(b1 = new Button("Sign In"));
b1.setBounds(130,130,60,20);
 b1.addActionListener(this);
 add(l3=new Label("Report:"));
 l3.setBounds(10,155,200,20);
tt2.setEchoChar('-');
 tt3.setEchoChar('-');
}
 public void actionPerformed(ActionEvent AE)
  {
if(tt1.getText().equals("")||tt2.getText().equals("")||tt3.getText().equals(""))
 l3.setText("Text Field(s) Empty.");
else
 {
 if(tt3.getText().equals(tt2.getText()))
{
 form.C_output.println("N_U");
 form.C_output.println(tt1.getText());
form.C_output.println(tt2.getText());
try{
String s = form.C_input.readLine();
l3.setText(s);
}
catch(IOException E){}
}
else
l3.setText("Password mis-match.");
}
}
}

class Frame3 extends Frame implements ActionListener
{
JLabel L,M;
static List OUL;
JButton B;
String U;

public Frame3(String u)
 {
super("MAIN_SERVER Client-"+u);
setSize(200,410);
setLayout(null);
U=u;
L=new JLabel("Hello "+U);
add(L);
L.setBounds(10,25,180,20);

L=new JLabel("Online user list...");
add(L);
L.setBounds(10,45,180,20);

OUL=new List(15);
add(OUL);
OUL.setBounds(10,70,180,320);
OUL.addActionListener(this);

addWindowListener(new WindowAdapter ()
{
public void windowClosing (WindowEvent e)
{
try
{
form.C_output.println("LOGOUT");
MainFrame.c_s.close();
System.exit(0);
}
catch(IOException IE)
{}


}
} );
 new CMD_L();
}
public void actionPerformed(ActionEvent AE)
{
if(OUL.getSelectedItem().equals(U))
{
JOptionPane.showMessageDialog(this,"Self-chatting is denied","Warning",JOptionPane.WARNING_MESSAGE);
}
else
{
if(AE.getSource()==OUL||AE.getSource()==B)
{

if(!OUL.getSelectedItem().equals("[MAIN_SERVER]"))
{
form.C_output.println("RQT_CHAT");
form.C_output.println(U);
form.C_output.println(OUL.getSelectedItem());
CHAT_WIN CW=new CHAT_WIN(U,OUL.getSelectedItem());
CW.show();
}
else
JOptionPane.showMessageDialog(this,"Default Server User : Access Denied","Warning",JOptionPane.WARNING_MESSAGE);
}

}
}

}
class CHAT_WIN extends Frame implements ActionListener,TextListener
{static TextArea T;
TextField Tx;
String uname;
Socket d_s;
String s;
static BufferedReader D_input;
static PrintWriter D_output;

public CHAT_WIN(String u,String f)
{
super(u+"->>> * <<<-"+f);
   setSize(400,300);
   setLayout(null);
   setResizable(false);
   uname=u;

   Tx=new TextField("");
   add(Tx);
   Tx.setBounds(10,260,380,35);
   Tx.addActionListener(this);

   T=new TextArea("CHAT WINDOW",10,50,1);
   add(T);
   T.setBounds(10,25,380,230);
   T.setEditable(false);
   T.addTextListener(this);

try
   {
System.out.println("DS connection with");
d_s = new Socket(MainFrame.serv, form.po);
//T.append("Connected to Data server " +d_s.getInetAddress()+":"+d_s.getPort());
T.append("Establishing stream for communication...");
   }
catch (UnknownHostException e)
{
System.out.println(e);
System.out.println("Error in connecting Data Server");
//System.exit(ERROR);
}
catch (IOException e)
{
System.out.println(e);
System.out.println("Error in connecting Server");
//System.exit(ERROR);
                                                                  
 }
addWindowListener(new WindowAdapter ()
{
public void windowClosing (WindowEvent e)
{
hide();
}
} );
try
{
D_output=new PrintWriter(d_s.getOutputStream(),true);
D_input=new BufferedReader(new InputStreamReader(d_s.getInputStream()));
}
catch(IOException E){}
new MSG_READER();
}
public void textValueChanged(TextEvent TE)
{
this.show();
}

public void actionPerformed(ActionEvent TE)
{
s=uname+" :: "+Tx.getText();
Tx.setText("");
T.append(" "+s);

D_output.println(s);
System.out.println("Data Sent");
}
}

class MSG_READER extends Thread
{
Thread t;
public MSG_READER()
{
t=new Thread(this);                         
t.start();
}
public void run()
{
while(true)
try
{
CHAT_WIN.T.append(" "+CHAT_WIN.D_input.readLine());
}
catch(IOException E){}
}
}

class CMD_L extends Frame implements Runnable
{
Thread t;
String cmd="";
CMD_L()
{
t=new Thread(this);
t.start();
}

public void run()
{
while(true)
{
try
{
cmd=form.C_input.readLine();
compute(cmd);
}
catch(IOException E)
{}
//System.out.println("Error Reading Command At Client");}

}
}
public void compute(String cmd)
{
if(cmd.equals("ULIST"))
{
Frame3.OUL.removeAll();
try
{
String s=form.C_input.readLine();

while(!s.equals("END"))
{
Frame3.OUL.addItem(s);
s=form.C_input.readLine();
}

}
catch(IOException E){System.out.println("Error in Reading List by client");}
}

if(cmd.equals("CALL_CHAT"))
{
try
{
String s1=form.C_input.readLine();
String s2=form.C_input.readLine();
CHAT_WIN CW1=new CHAT_WIN(s1,s2);
CW1.show();
}
catch(IOException E){System.out.println("Error in Reading List by client");}
}
if(cmd.equals("S_MSG"))
{
try
{
String msgs=form.C_input.readLine();
JOptionPane.showMessageDialog(this,msgs,"Server Message",JOptionPane.PLAIN_MESSAGE);
}
catch(IOException E){System.out.println("Error in Reading List by client");}
}
}
}