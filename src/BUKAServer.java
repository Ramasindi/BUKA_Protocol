import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BUKAServer
{
	private ServerSocket ss;
	private boolean running;
	
	private void startServer() {
		System.out.println("Starting Server your honor");
		while(running) {
			try {
				Socket s = ss.accept();
				System.out.println("New Client connected your honor");
				BUKAHandler bh = new BUKAHandler(s);
				Thread t = new Thread(bh);
				t.start();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	public BUKAServer(int port) {
		try {
			ss = new ServerSocket(port);
			running = true;
			startServer();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
		
    public static void main(String[] argv)
    {
    	BUKAServer handler = new BUKAServer(2018);
    }
}
