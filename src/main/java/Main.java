import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import api.Admin;
import api.Patient;
import service.AdminImpl;
import service.PatientImpl;

public class Main {
  public static void main(String[] args) {
    System.setProperty("java.rmi.server.hostname", "localhost");

    try {
      Admin adminStub = (Admin) UnicastRemoteObject.exportObject(AdminImpl.getInstance(), 0);
      Patient patientStub = (Patient) UnicastRemoteObject.exportObject(PatientImpl.getInstance(), 0);
      Registry registry = LocateRegistry.getRegistry();
      registry.rebind("AdminMTL", adminStub);
      registry.rebind("PatientMTL", patientStub);

      System.out.println("Server ready");

    } catch (RemoteException e) {
      e.printStackTrace();
    }

  }
}
