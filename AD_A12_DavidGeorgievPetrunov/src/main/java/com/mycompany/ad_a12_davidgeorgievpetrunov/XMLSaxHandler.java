package com.mycompany.ad_a12_davidgeorgievpetrunov;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Vector;

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
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("booking".equals(qName)) {
            booking = new Booking();
            booking.setId(attributes.getValue("location_number"));
            bookings.add(booking);
        } else {
            data = new StringBuilder();
            currentElement = qName;
            currentElementIdAttribute = attributes.getValue("id_" + currentElement);
            currentElementIdRoomType = attributes.getValue("id_type");
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("client".equals(qName)) {
            booking.setClient(data.toString().trim());
            booking.setIdClient(currentElementIdAttribute);
        } else if ("price".equals(qName)) {
            booking.setPrice(data.toString().trim());
        } else if ("agency".equals(qName)) {
            booking.setAgency(data.toString().trim());
            booking.setIdAgency(currentElementIdAttribute);
        } else if ("room".equals(qName)) {
            booking.setRoom(data.toString().trim());
            booking.setIdType(currentElementIdRoomType);
        } else if ("hotel".equals(qName)) {
            booking.setHotel(data.toString().trim());
            booking.setIdHotel(currentElementIdAttribute);
        } else if ("check_in".equals(qName)) {
            booking.setCheck_in(data.toString().trim());
        } else if ("room_nights".equals(qName)) {
            booking.setRoom_nights(data.toString().trim());
        }

        currentElement = null;
        currentElementIdAttribute = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (currentElement != null) {
            data.append(new String(ch, start, length));
        }
    }
}
