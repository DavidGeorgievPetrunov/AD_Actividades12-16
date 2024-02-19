package com.mycompany.ad_a15_davidgeorgievpetrunov;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class AD_A15_DavidGeorgievPetrunov {

    Vector<Booking> bookings;

    public static void main(String[] args) {
        AD_A15_DavidGeorgievPetrunov ad15 = new AD_A15_DavidGeorgievPetrunov();
    }

    public AD_A15_DavidGeorgievPetrunov() {
        bookings = cargarDatos();
        if (bookings == null) {
            System.out.println("Ha habido un error cargando los datos");
        } else {
            BookingDAO bookingDAO = new BookingDAO();
            for (Booking booking : bookings) {
                bookingDAO.insertarReserva(booking);
            }
            bookingDAO.mostrarReserva();
            bookingDAO.actualizarPreuReserva(2, 0);
            bookingDAO.actualizarPreuReserva(-1, 0);
            bookingDAO.mostrarReserva();

//            bookingDAO.dropCollection();
        }
    }

    private Vector<Booking> cargarDatos() {
        try {

            File inputFile = new File("src/main/java/com/mycompany/ad_a15_davidgeorgievpetrunov/bookings.xml");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLSaxHandler userhandler = new XMLSaxHandler();
            saxParser.parse(inputFile, userhandler);
            return userhandler.getBookings();

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Booking.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (SAXException ex) {
            Logger.getLogger(Booking.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(Booking.class
                    .getName()).log(Level.SEVERE, null, ex);

        }

        return null;
    }
}
