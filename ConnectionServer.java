import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

class ConnectionServer implements Runnable { 
    // some constants 
    public static final int WAIT_AUTH = 0; 
    public static final int AUTH_DONE = 1;
	public static final int NEXT_BID=2;
    public static int CLIENT_STATE=0;

    public static final String WAIT_AUTH_MSG = "Symbol of Security pls!";
	public static final String WAIT_AUTH_MSG2 = "Client Name pls!";
    public static final String AUTH_DONE_MSG = "You are authorised to bid. ";
    public static final String MSG_POSTED    = "Your bid is placed. ";

    // per connection variables
    private Socket mySocket; // connection socket per thread 
    private int currentState;
    private String clientName;
    private double symbolPrice;		//Price of the symbol
	private String symbol;
    private MainServer mainServer; 

    public ConnectionServer(MainServer mainServer) { 
	this.mySocket = null;
	this.currentState = WAIT_AUTH; 
	this.clientName = null;
	this.mainServer = mainServer;
    }

    public boolean handleConnection(Socket socket) { 
	this.mySocket = socket; 
	Thread newThread = new Thread(this); 
	newThread.start();
	return true; 
    }

    public void run() { // can not use "throws .." interface is different
	BufferedReader in=null; 
	PrintWriter out=null;
	CLIENT_STATE=0;
	try {
	    in = new 
		BufferedReader(new InputStreamReader(mySocket.getInputStream()));
	    out = new 
		PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));

		String line, outline=WAIT_AUTH_MSG2;


	    for(line = in.readLine(); line != null && !line.equals("quit") ; line = in.readLine()) {
			if (CLIENT_STATE==0){
				outline=WAIT_AUTH_MSG2;
				if(!line.equals("")){
					clientName=line;
					CLIENT_STATE=1;
					outline=WAIT_AUTH_MSG;
				}
			}
			else {
				switch (currentState) {
					case WAIT_AUTH:
						// we are waiting for symbols
						if (mainServer.isAuthorized(line)) {
							currentState = AUTH_DONE;
							symbol=line;
							symbolPrice = mainServer.getPrice(line);
							outline = Double.toString(symbolPrice);
						} else {
							outline = "-1\n"+WAIT_AUTH_MSG;
							//outline = WAIT_AUTH_MSG;
						}
						break;
					/*****************************/
					case AUTH_DONE:
						if(Double.parseDouble(line)>symbolPrice){
							outline =WAIT_AUTH_MSG;
							mainServer.postMSG(line);
							mainServer.changePrice(symbol,Double.parseDouble(line));
							currentState = WAIT_AUTH;
						}
						else{
							outline = WAIT_AUTH_MSG;
							currentState = WAIT_AUTH;
						}
						break;
					default:
						System.out.println("Undefined state");
						return;
				} // case
			}

			out.println(outline); // Send the said message
			out.flush(); // flush to network

	    } // for 

	    // close everything 
	    out.close(); 
	    in.close(); 
	    this.mySocket.close(); 
	} // try 	     
	catch (IOException e) { 
	    System.out.println(e); 
	}
	catch(NumberFormatException e){
		System.out.println(e);
	}
    }
}

    
    

