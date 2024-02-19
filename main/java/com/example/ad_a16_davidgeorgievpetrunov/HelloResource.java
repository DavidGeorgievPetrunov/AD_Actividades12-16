package com.example.ad_a16_davidgeorgievpetrunov;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.PropertyException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

@Path("/bookings")
public class HelloResource {
    Vector<Booking> bookings;
    public String xmlFile = "C:\\Users\\david\\OneDrive\\Documentos\\NetBeansProjects\\AD_A16_DavidGeorgievPetrunov\\src\\main\\java\\com\\mycompany\\ad_a16_davidgeorgievpetrunov\\bookings.xml";

    @PostConstruct
    public void init() {
        this.bookings = cargarDatos();
        if (bookings == null) {
            System.out.println("Ha habido un error cargando los datos");
        }
    }

    @GET
    @Path("/get")
    @Produces("application/json")
    public String getBookingsJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this.bookings);
    }

    @POST
    @Path("/post")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createBooking(Booking newBooking) {
        if (newBooking == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("La solicitud de reserva está vacía").build();
        } else if (!checkDuplicated(newBooking)) {
            this.bookings.add(newBooking);
            System.out.println(newBooking.toString());
            writeBookingsToXML();
            updateXML();
        }
        return Response.status(Response.Status.NOT_FOUND).entity("Reserva con ID proporcionado: " + newBooking.id + " ya existe.").build();
    }

    private void updateXML() {
        try {
            JAXBContext context = JAXBContext.newInstance(this.bookings.getClass());

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(this.bookings, new File(xmlFile));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @PUT
    @Path("/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateBooking(@PathParam("id") String id, Booking updatedBooking) {
        try {
            if (updatedBooking == null) {
                return Response.status(Response.Status.BAD_REQUEST).entity("La reserva actualizada es nula").build();
            }

            boolean found = false;
            for (Booking booking : bookings) {
                if (String.valueOf(booking.getId()).equals(id)) {

                    booking.setIdClient(updatedBooking.getIdClient());
                    booking.setClient(updatedBooking.getClient());
                    booking.setIdAgency(updatedBooking.getIdAgency());
                    booking.setAgency(updatedBooking.getAgency());
                    booking.setPrice(updatedBooking.getPrice());
                    booking.setIdType(updatedBooking.getIdType());
                    booking.setRoom(updatedBooking.getRoom());
                    booking.setIdHotel(updatedBooking.getIdHotel());
                    booking.setHotel(updatedBooking.getHotel());
                    booking.setCheck_in(updatedBooking.getCheck_in());
                    booking.setRoom_nights(updatedBooking.getRoom_nights());

                    found = true;
                    break;
                }
            }

            if (!found) {
                return Response.status(Response.Status.NOT_FOUND).entity("Reserva no encontrada para el ID proporcionado: " + id).build();
            }

            writeBookingsToXML();
            init();

            return Response.ok(bookings).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Error al actualizar la reserva: " + e.getMessage()).build();
        }
    }


    @DELETE
    @Path("/delete/{id}")
    public Response deleteBooking(@PathParam("id") String id) {

        boolean removed = bookings.removeIf(booking -> String.valueOf(booking.getId()).equals(id));

        if (removed) {
            writeBookingsToXML();
            init();
            return Response.status(Response.Status.NOT_FOUND).entity("Reserva con id: "+ id +" borrada con exito.").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Reserva no encontrada para el ID proporcionado: " + id).build();
        }
    }

    private boolean checkDuplicated(Booking newBooking) {
        for (int i = 0; i < this.bookings.size(); i++) {
            if (newBooking.id == this.bookings.get(i).id) {
                return true;
            }
        }
        return false;
    }


    private Vector<Booking> cargarDatos() {
        File inputFile = new File(xmlFile);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLSaxHandler userhandler = new XMLSaxHandler();
            saxParser.parse(inputFile, userhandler);
            return userhandler.getBookings();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }


    private void writeBookingsToXML() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("bookings");
            doc.appendChild(rootElement);

            for (Booking booking : bookings) {
                Element bookingElement = doc.createElement("booking");
                rootElement.appendChild(bookingElement);

                bookingElement.setAttribute("location_number", String.valueOf(booking.getId()));

                Element clientElement = doc.createElement("client");
                clientElement.setAttribute("id_client", String.valueOf(booking.getIdClient()));
                clientElement.appendChild(doc.createTextNode(booking.getClient()));
                bookingElement.appendChild(clientElement);

                Element agencyElement = doc.createElement("agency");
                agencyElement.setAttribute("id_agency", String.valueOf(booking.getIdAgency()));
                agencyElement.appendChild(doc.createTextNode(booking.getAgency()));
                bookingElement.appendChild(agencyElement);

                Element priceElement = doc.createElement("price");
                priceElement.appendChild(doc.createTextNode(String.valueOf(booking.getPrice())));
                bookingElement.appendChild(priceElement);

                Element roomElement = doc.createElement("room");
                roomElement.setAttribute("id_type", String.valueOf(booking.getIdType()));
                roomElement.appendChild(doc.createTextNode(booking.getRoom()));
                bookingElement.appendChild(roomElement);

                Element hotelElement = doc.createElement("hotel");
                hotelElement.setAttribute("id_hotel", String.valueOf(booking.getIdHotel()));
                hotelElement.appendChild(doc.createTextNode(booking.getHotel()));
                bookingElement.appendChild(hotelElement);

                Element checkInElement = doc.createElement("check_in");
                checkInElement.appendChild(doc.createTextNode(booking.getCheck_in()));
                bookingElement.appendChild(checkInElement);

                Element roomNightsElement = doc.createElement("room_nights");
                roomNightsElement.appendChild(doc.createTextNode(String.valueOf(booking.getRoom_nights())));
                bookingElement.appendChild(roomNightsElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(xmlFile);

            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

}
