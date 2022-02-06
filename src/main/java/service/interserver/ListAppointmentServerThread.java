package service.interserver;

import common.GlobalConstants;
import database.Database;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import model.appointment.Appointment;
import model.appointment.AppointmentAvailability;
import model.appointment.AppointmentType;
import model.common.City;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ListAppointmentServerThread implements Runnable {

  private final Database database = Database.getInstance();
  private final Logger logger = LogManager.getLogger();

  @Override
  public void run() {
    try (DatagramSocket socket = new DatagramSocket(getPortByCity(GlobalConstants.thisCity))) {
      byte[] buffer = new byte[32768];

      while (true) {
        DatagramPacket request = new DatagramPacket(buffer, buffer.length);
        socket.receive(request);
        logger.info("Received request for availability.");

        var requestData = request.getData();

        ObjectInputStream objectInputStream =
            new ObjectInputStream(new ByteArrayInputStream(requestData));
        var requestDataObject = objectInputStream.readObject();
        if (requestDataObject.getClass() == AppointmentType.class) {
          var appointmentType = (AppointmentType) requestDataObject;
          var availabilities = getAppointmentAvailabilities(appointmentType);

          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
          objectOutputStream.writeObject(availabilities);
          objectOutputStream.flush();
          objectOutputStream.close();

          var replyData = byteArrayOutputStream.toByteArray();
          logger.info("replyData length is: " + replyData.length);

          DatagramPacket reply =
              new DatagramPacket(
                  replyData, replyData.length, request.getAddress(), request.getPort());
          socket.send(reply);
          logger.info("Replied request for availability with data: " + availabilities);
        }
      }
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private List<AppointmentAvailability> getAppointmentAvailabilities(AppointmentType type) {
    Collection<Appointment> appointments = database.findAllByType(type);
    return appointments.stream()
        .map(
            appointment ->
                new AppointmentAvailability(
                    appointment.getAppointmentId().getId(), appointment.getRemainingCapacity()))
        .collect(Collectors.toList());
  }

  private int getPortByCity(City city) {
    return switch (city) {
      case Montreal -> 7777;
      case Quebec -> 7778;
      case Sherbrooke -> 7779;
    };
  }
}
