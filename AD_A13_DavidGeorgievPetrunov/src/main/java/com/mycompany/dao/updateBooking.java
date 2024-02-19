package com.mycompany.dao;

public class updateBooking {

    private int newLocationNumber;
    private int id_client;
    private String client;
    private String agency;
    private int id_agency;
    private String price;
    private String room;
    private int id_type;
    private String hotel;
    private int id_hotel;
    private String check_in;
    private int room_nights;
    private int location_number;

    public int getNewLocationNumber() {
        return newLocationNumber;
    }

    public void setNewLocationNumber(int newLocationNumber) {
        this.newLocationNumber = newLocationNumber;
    }

    public int getId_client() {
        return id_client;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public int getId_agency() {
        return id_agency;
    }

    public void setId_agency(int id_agency) {
        this.id_agency = id_agency;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getId_type() {
        return id_type;
    }

    public void setId_type(int id_type) {
        this.id_type = id_type;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public int getId_hotel() {
        return id_hotel;
    }

    public void setId_hotel(int id_hotel) {
        this.id_hotel = id_hotel;
    }

    public String getCheck_in() {
        return check_in;
    }

    public void setCheck_in(String check_in) {
        this.check_in = check_in;
    }

    public int getRoom_nights() {
        return room_nights;
    }

    public void setRoom_nights(int room_nights) {
        this.room_nights = room_nights;
    }

    public int getLocation_number() {
        return location_number;
    }

    public void setLocation_number(int location_number) {
        this.location_number = location_number;
    }

    public void setBooking(Booking booking, int newLoc) {
        this.setNewLocationNumber(newLoc);
        this.setId_client(booking.getId_client());
        this.setClient(booking.getClient());
        this.setAgency(booking.getAgency());
        this.setId_agency(booking.getId_agency());
        this.setPrice(booking.getPrice());
        this.setRoom(booking.getRoom());
        this.setId_type(booking.getId_type());
        this.setHotel(booking.getHotel());
        this.setId_hotel(booking.getId_hotel());
        this.setCheck_in(booking.getCheck_in());
        this.setRoom_nights(booking.getRoom_nights());
        this.setLocation_number(booking.getLocation_number());
    }
}
