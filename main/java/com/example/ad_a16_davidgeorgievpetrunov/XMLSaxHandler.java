package com.example.ad_a16_davidgeorgievpetrunov;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.*;

class XMLSaxHandler extends DefaultHandler {

    private Booking booking;
    private Vector<Booking> bookings;
    private StringBuilder data;
    private String currentElement;
    private String currentElementIdAttribute;
    private String currentElementIdRoomType;

    @Override
    public void startDocument() {
        this.bookings = new Vector<>();
    }

    @Override
    public void endDocument() {
    }

    public Vector<Booking> getBookings() {
        return bookings;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if ("booking".equals(qName)) {
            booking = new Booking();
            booking.setId(Integer.parseInt(attributes.getValue("location_number")));
            bookings.add(booking);
        } else {
            data = new StringBuilder();
            currentElement = qName;
            currentElementIdAttribute = attributes.getValue("id_" + currentElement);
            currentElementIdRoomType = attributes.getValue("id_type");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if ("client".equals(qName)) {
            booking.setClient(data.toString().trim());
            booking.setIdClient(Integer.parseInt(currentElementIdAttribute));
        } else if ("price".equals(qName)) {
            String priceStr = data.toString().trim().replace(',', '.');
            booking.setPrice(Double.parseDouble(priceStr));
        } else if ("agency".equals(qName)) {
            booking.setAgency(data.toString().trim());
            booking.setIdAgency(Integer.parseInt(currentElementIdAttribute));
        } else if ("room".equals(qName)) {
            booking.setRoom(data.toString().trim());
            booking.setIdType(Integer.parseInt(currentElementIdRoomType));
        } else if ("hotel".equals(qName)) {
            booking.setHotel(data.toString().trim());
            booking.setIdHotel(Integer.parseInt(currentElementIdAttribute));
        } else if ("check_in".equals(qName)) {
            booking.setCheck_in(data.toString().trim());
        } else if ("room_nights".equals(qName)) {
            booking.setRoom_nights(Integer.parseInt(data.toString().trim()));
        }

        currentElement = null;
        currentElementIdAttribute = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (currentElement != null) {
            data.append(new String(ch, start, length));
        }
    }
}
