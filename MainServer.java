import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MainServer { 

    /* Some constants */     
    public static final int BASE_PORT = 2000;  // do not change


    /* local data for the server 
     * Every main server is defined in terms of the port it 
     * listens and the database of allowed users 
     */ 
    private ServerSocket serverSocket=null;  // server Socket for main server 
    private StocksDB allowedUsers=null;     // who are allowed to chat

    public MainServer(int socket, StocksDB users) {
	this.allowedUsers = users; 

	try { 
	    this.serverSocket = new ServerSocket(socket); 
	} catch (IOException e) { 
	    System.out.println(e); 
	}
    }

    /* each server will provide the following functions to 
     * the public. Note that these are non-static 
     */ 
    public boolean isAuthorized(String regNo) { 
	return this.allowedUsers.findPrice(regNo) != null;
    }    

    public Double getPrice(String Symbol) {
	// should these be synchronized? 
	return this.allowedUsers.findPrice(Symbol);
    }	

    /* server will define how the messages should be posted 
     * this will be used by the connection servers
     */ 

    public void postMSG(String msg) { 
	// all threads print to same screen 
	System.out.println(msg); 
    }

    public void changePrice(String key,Double price){
    	allowedUsers.changePrice(key,price);
	}

    public String authorizedOnce(String a) { 
	// need to implement this. 
	return null; 
    }



    public void server_loop() { 
	try { 
	    while(true) { 
		Socket socket = this.serverSocket.accept(); 
		ConnectionServer worker = new ConnectionServer(this); 
		worker.handleConnection(socket); 
	    }
	} catch(IOException e) { 
	    System.out.println(e);
	}
    }// end server_loop 
}


	




