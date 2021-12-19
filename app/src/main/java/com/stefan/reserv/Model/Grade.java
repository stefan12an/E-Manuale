package com.stefan.reserv.Model;

public class Grade {
    String id;
    String cinema_name;
    String cinema_location;
    byte[] cinema_photo;

    public Grade(String id, String cinema_name, String cinema_location, byte[] cinema_photo) {
        this.id = id;
        this.cinema_name = cinema_name;
        this.cinema_location = cinema_location;
        this.cinema_photo = cinema_photo;
    }

    public String getId() {
        return id;
    }

    public String getCinema_name() {
        return cinema_name;
    }

    public String getCinema_location() {
        return cinema_location;
    }

    public byte[] getCinema_photo() {
        return cinema_photo;
    }

}
