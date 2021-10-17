import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BUKAClientPane extends GridPane
{
	private Socket socket;
	private OutputStream os;
	private InputStream is;
	private PrintWriter pw;
	private BufferedReader br;
	private DataOutputStream dos;
	private DataInputStream dis;
	private String[] listData;
	private String fileTogetName = "";
	
	private Button connect;
	private Button login;
	private Button list;
	private Button pdfret;
	private Button logout;
	private TextField txtlogin;
	private TextArea txtlist;
	private TextField txtid;
	
    public BUKAClientPane(Stage stage)
    {
    	setupUI();
	//Create client connection
    	connect.setOnAction((e) ->{
    		try {
 				socket = new Socket("localhost",2018);
 				os = socket.getOutputStream();
 				is = socket.getInputStream();
 				br = new BufferedReader(new InputStreamReader(is));
 				pw = new PrintWriter(os);
 				dis = new DataInputStream(is);
 				dos = new DataOutputStream(os);

 			}catch(IOException e1) {
 				e1.printStackTrace();
 			}
    	});
    	
	//Create buttons for each command
	//Use buttons to send commands
    	login.setOnAction((e)->{
    		
    		if(!txtlogin.getText().isEmpty()) {
	    		pw.println(txtlogin.getText());
	    		pw.flush();
	    		String response ="";
	    		try {
	    			response = br.readLine();
	    			txtlogin.clear();
	    			txtlogin.setText(response);
	    			System.out.println(response);
	    		}catch (IOException e2) {
	    			e2.printStackTrace();
	    		}
    		}else {
    			txtlogin.clear();
    			txtlogin.setText("Please enter valid data");
    		}
    	});
    	
    	list.setOnAction((e) ->{
    		pw.println("LIST");
    		pw.flush();
    		String response ="";
    		try {
    			response = br.readLine();
     			System.out.println(response);
     			if(response.equalsIgnoreCase("Login First your honor")) {
     				txtlist.appendText(response);
     			}else {
     				txtlist.clear();
     				listData = response.split("#");
         			for(int i = 0; i<listData.length;i++) {
         				txtlist.appendText(listData[i]+"\n");
         			}
     			}
    		}catch (IOException e2) {
    			e2.printStackTrace();
    		}
    	});
    	pdfret.setOnAction((e)->{
    		String regex = "\\d+";
    		if(!txtid.getText().isEmpty() && txtid.getText().matches(regex)) {
    			pw.println("PDFRET " + Integer.parseInt(txtid.getText()));
        		pw.flush();
        		String response ="";
        		try {
        			response = br.readLine();
        			if(response.equalsIgnoreCase("login first your honor")) {
        				txtlist.clear();
            			txtlist.setText(response);
        			}else if(response.equalsIgnoreCase("File do not exist")) {
        				txtlist.clear();
            			txtlist.setText(response);
        			}else{
        				int fileSize = Integer.parseInt(response);
        				//txtlist.clear();
            			//txtlist.appendText("\n\n"  + response);
        				//System.out.println(response);
         				for(String s: listData) {
         					StringTokenizer tok = new StringTokenizer(s);
         					String id = tok.nextToken();
         					String name = tok.nextToken();
         					if(id.equals(txtid.getText())) {
         						fileTogetName = name;
         					}
         							
         				}
         				File fileDownloaded = new File("./data/client/"+fileTogetName);
         				FileOutputStream fos = null;
         				
         				fos = new FileOutputStream(fileDownloaded);
         				byte[] buffer = new byte[1024];
         				int n = 0;
         				int totalBytes = 0;
         				while(totalBytes !=fileSize) {
         					n = dis.read(buffer,0,buffer.length);
         					fos.write(buffer,0,n);
         					fos.flush();
         				}
         				
        			}
        			
        			//System.out.println(response);
        		}catch (IOException e2) {
        			e2.printStackTrace();
        		}
    		}else {
    			txtid.setText("Enter id");
    		}
    		
    	});
    	logout.setOnAction((e)->{
    		pw.println("LOGOUT");
    		pw.flush();
    		String response ="";
    		try {
    			response = br.readLine();
    			txtlogin.clear();
    			txtlist.clear();
    			txtid.clear();
    			txtlogin.setText(response);
    			
    			System.out.println(response);
    		}catch (IOException e2) {
    			e2.printStackTrace();
    		}
    	});
    }
    private void setupUI() {
    	setHgap(10);
 		setVgap(10);
 		setAlignment(Pos.TOP_CENTER);
 		
 		connect = new Button("CONNECT");
 		login = new Button("LOGIN");
 		list = new Button("LIST");
 		pdfret = new Button("PDFRET");
 		logout = new Button("LOGOUT");
 		txtlogin = new TextField();
 		txtlist = new TextArea();
 		txtlist.setPrefRowCount(10);
 		txtid = new TextField();
 		
 		add(connect,0,0);
 		add(login,1,1);
 		add(txtlogin,0,1);
 		add(list,0,2);
 		add(txtlist,0,3);
 		add(pdfret,1,6);
 		add(txtid,0,6);
 		add(logout,0,7);
 		
    }
}
