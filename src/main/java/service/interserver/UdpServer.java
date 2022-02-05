package service.interserver;

public class UdpServer {
  public static void startListening() {
    Thread serverThread = new Thread(new ListAppointmentServerThread());
    serverThread.start();
  }
}
