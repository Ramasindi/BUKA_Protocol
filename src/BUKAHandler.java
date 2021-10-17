import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BUKAHandler implements Runnable
{
	private Socket socket;
	private OutputStream os;
	private InputStream is;
	private PrintWriter pw;
	private BufferedReader br;
	private DataOutputStream dos;
	private DataInputStream dis;
	private String[] listData;
	
    public BUKAHandler(Socket newConnectionToClient)
    {	
    	try {
				os = newConnectionToClient.getOutputStream();
				is = newConnectionToClient.getInputStream();
				br = new BufferedReader(new InputStreamReader(is));
				pw = new PrintWriter(os);
				dis = new DataInputStream(is);
				dos = new DataOutputStream(os);

			}catch(IOException e1) {
				e1.printStackTrace();
			}
    }
    @Override
    public void run()
    {
	//Process commands from client
    	System.out.println("Ready to serve your honor");
		boolean processing = true;
		boolean isLogged = false;
		try {
			while(processing) {
				String message = br.readLine();
				System.out.println("Message: " + message);
				StringTokenizer st = new StringTokenizer(message);
				String command = st.nextToken().toUpperCase();
				switch (command) {
				case "AUTH":
					if(matchUser(st.nextToken(),st.nextToken())) {
						
						pw.println("Access Granted");
						pw.flush();
						isLogged = true;
					}else {
						
						pw.println("Access Denied");
						pw.flush();
						isLogged = false;
					}
					break;
				case "LIST":
					if(isLogged) {
						String ret = "";
						try {
							Scanner sc = new Scanner(new File("./data/server/PdfList.txt"));
							while(sc.hasNext()) {
								String pdf = sc.nextLine();
								ret += pdf + "#";
							}
							pw.println(ret);
							pw.flush();
							System.out.println("Pdf List loaded your honor");;
							sc.close();
						}catch(FileNotFoundException e) {
							e.printStackTrace();
						}
					}else {
						pw.println("login first your honor");
						pw.flush();
					}
					
					
					break;
				case "PDFRET":
					if(isLogged) {
							String fileID = st.nextToken();
							System.out.println("ID requested: " + fileID);
							String fileName = "";
							File fileList = new File("data/server/PdfList.txt");
							Scanner sc = new Scanner(fileList);
							String line = "";
							while(sc.hasNext()) {
								line = sc.nextLine();
								StringTokenizer tokenizer = new StringTokenizer(line);
								String id = tokenizer.nextToken();
								String fName = tokenizer.nextToken();
								if(id.equals(fileID))
								{
									fileName = fName;
								}
							}
							sc.close();
							
							System.out.println("Name of requested file: " + fileName);
							File fileToReturn = new File("./data/server/" + fileName);
							if(fileToReturn.exists()) {
								pw.println(fileToReturn.length());
								pw.flush();
								FileInputStream fis = new FileInputStream(fileToReturn);
								byte[] buffer = new byte[1024];
								int n = 0;
								while((n = fis.read(buffer))>0) {
									dos.write(buffer,0,n);
									dos.flush();
								}
								fis.close();
								System.out.println("File sent to client your honor");
								
							}else {
								pw.println("File do not exist");
								pw.flush();
							}
					}else {
						pw.println("Login first your honor");
						pw.flush();
					}
					break;
				case "LOGOUT":
					pw.println("logged out");
					pw.flush();
					processing =false;
					System.out.println("Logged out your honor");
					break;
				default:
					pw.println("Unable to process that");
					pw.flush();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
    }
    
    private boolean matchUser(String username,String password)
    {
	boolean found = false;
	File userFile = new File("data/server/users.txt");
	try
	{
	    Scanner scan = new Scanner(userFile);
	    while(scan.hasNextLine()&&!found)
	    {
	    	String line = scan.nextLine();
	    	String lineSec[] = line.split("\\s");
    		
	    	//Compare user 
	    	if(lineSec[0].equals(username) && lineSec[1].equals(password)) {
	    		System.out.println("User found your honor");
	    		found = true;
	    	}else {
	    		System.err.println("User not found your honor");
	    	}
		
	    }
	    scan.close();
	}
	catch(IOException ex)
	{
	    ex.printStackTrace();
	}
	
	return found;
    }
    
    private ArrayList<String> getFileList()
    {
		ArrayList<String> result = new ArrayList<String>();
		File lstFile = new File("data/server/PdfList.txt");
		try
		{
			Scanner scan = new Scanner(lstFile);

			//Read in each line of file
			
			scan.close();
		}	    
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
		
		return result;
    }
    
    private String idToFile(String ID)
    {
    	String result = "";
    	File lstFile = new File("data/server/PdfList.txt");
    	try
    	{
    		Scanner scan = new Scanner(lstFile);

    		//Read filename from file
    		
    		scan.close();
    	}
    	catch(IOException ex)
    	{
    		ex.printStackTrace();
    	}
    	return result;
    }
}
