package com.mycompany.dao;

import java.io.File;

public interface ConexionDAO {

    public String getVersionMysql();

    public void cargarArchivoXML(Bookings bookings);

    public void downloadAsCSV(File file);

    public void deleteAllData();

    public void insertarReserva(Booking booking);

    public int selectById(int location_number);

    public void loadXMLINFILE(String ruta);

    public void saveToCSV(String ruta);

    public void deleteById(int location_number);

    public void modificarReserva(updateBooking bookings);
}
