package service.interserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import model.appointment.AppointmentAvailability;
import model.appointment.AppointmentType;
import model.common.City;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ListAppointmentClientThread implements Runnable {
  private final Logger logger = LogManager.getLogger();
  private final City targetCity;
  private final AppointmentType appointmentType;

  private volatile List<AppointmentAvailability> availabilities;

  public ListAppointmentClientThread(City targetCity, AppointmentType appointmentType) {
    this.targetCity = targetCity;
    this.appointmentType = appointmentType;
  }

  @Override
  public void run() {
    try (DatagramSocket socket = new DatagramSocket()) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(appointmentType);
      objectOutputStream.flush();
      objectOutputStream.close();
      byte[] data = byteArrayOutputStream.toByteArray();

      InetAddress host = InetAddress.getByName("localhost");

      DatagramPacket request = new DatagramPacket(data, data.length, host, getPortByCity(targetCity));
      socket.send(request);
      logger.info("Requesting availabilities from " + targetCity);
      byte[] buffer = new byte[256];
      DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
      socket.receive(reply);
      var responseBytes = reply.getData();
      ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(responseBytes));

      availabilities = (List<AppointmentAvailability>)objectInputStream.readObject();
      logger.info("Received availabilities from " + targetCity + " : " + availabilities);

    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public List<AppointmentAvailability> getAvailabilities() {
    return availabilities;
  }

  private int getPortByCity(City city) {
    return switch (city) {
      case Montreal -> 7777;
      case Quebec -> 7778;
      case Sherbrooke -> 7779;
    };
  }
}
