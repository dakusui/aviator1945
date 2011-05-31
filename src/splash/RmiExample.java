package splash;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RmiExample extends UnicastRemoteObject {
	static Registry registry; 
	/**
	 * 
	 */
	private static final long serialVersionUID = -172926446606667709L;

	protected RmiExample() throws RemoteException {
		super();
	}

	public void print() {
		System.out.println("hello");
	}
	
	public static void main(String[] args) throws Exception {
		registry = LocateRegistry.createRegistry(9999);
		registry.bind("aaa", new RmiExample());
		((RmiExample) registry.lookup("aaa")).print();
	}
}
