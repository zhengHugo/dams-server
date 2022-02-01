package model;

public class PatientId {
    private String id;

    PatientId(City city, int number) {
        this.id = city.code + "P" + number;
    }

    public String getId() {
        return id;
    }
}
