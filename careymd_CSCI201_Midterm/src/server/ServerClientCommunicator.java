package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;

import resource.Factory;
import utilities.Util;

public class ServerClientCommunicator extends Thread {

	private Socket socket;
	private ObjectOutputStream oos;
	private BufferedReader br;
	private ServerListener serverListener;
	
	public ServerClientCommunicator(Socket socket, ServerListener serverListener) throws IOException {
		this.socket = socket;
		this.serverListener = serverListener;
		this.oos = new ObjectOutputStream(socket.getOutputStream());
		this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}
	
	public void sendFactory(Factory factory) {
		try {
			oos.writeObject(factory);
			oos.flush();
		} catch (IOException ioe) {
			Util.printExceptionToCommand(ioe);
		}
	}
	
	public void run() {
		try {
			String line = br.readLine();
			while (line != null) {
				FactoryServerGUI.addMessage(socket.getInetAddress() + ":" + socket.getPort() + " - " + line);
				line = br.readLine();
			}
		} catch (IOException ioe) {
			serverListener.removeServerClientCommunicator(this);
			FactoryServerGUI.addMessage(socket.getInetAddress() + ":" + socket.getPort() + " - " + Constants.clientDisconnected);
			// this means that the socket is closed since no more lines are being received
			try {
				socket.close();
			} catch (IOException ioe1) {
				Util.printExceptionToCommand(ioe1);
			}
		}
	}
}
