import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import api.Admin;
import api.Patient;

public class Main {
  public static void main(String[] args) {
    System.setProperty("java.rmi.server.hostname", "localhost");

    Server server = new Server();
    try {
      Admin adminStub = (Admin) UnicastRemoteObject.exportObject(server.getAdmin(), 0);
//      Patient patientStub = (Patient) UnicastRemoteObject.exportObject(server.getPatient(), 1);
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind("Admin", adminStub);

      System.out.println("Server ready");

    } catch (RemoteException e) {
      e.printStackTrace();
    }

  }
}
