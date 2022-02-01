import api.Admin;
import api.Patient;
import service.AdminImpl;
import service.PatientImpl;

public class Server {
  private Patient patient;
  private Admin admin;

  public Server() {
    this.patient = new PatientImpl();
    this.admin = new AdminImpl();
  }

  public Patient getPatient() {
    return patient;
  }

  public Admin getAdmin() {
    return admin;
  }
}
