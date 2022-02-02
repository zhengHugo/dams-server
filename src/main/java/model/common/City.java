package model.common;

public enum City {
  Montreal("MTL"),
  Quebec("QUE"),
  Sherbrooke("SHE"),
  ;

  public final String code;

  City(String code) {
    this.code = code;
  }
}
