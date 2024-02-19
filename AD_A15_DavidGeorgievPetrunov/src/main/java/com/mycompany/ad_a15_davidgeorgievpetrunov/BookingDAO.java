package com.mycompany.ad_a15_davidgeorgievpetrunov;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

public class BookingDAO {

    private MongoCollection<Document> collection;

    public BookingDAO() {
        this.collection = new MongoDBConnector().getDatabase().getCollection("reservas");
    }

    public void insertarReserva(Booking bookingD) {
        Document existingDocument = collection.find(Filters.eq("location_number", bookingD.getId())).first();

        if (existingDocument == null) {
            Document booking = new Document("location_number", bookingD.getId())
                    .append("client", bookingD.getClient())
                    .append("idClient", bookingD.getIdClient())
                    .append("agency", bookingD.getAgency())
                    .append("idAgency", bookingD.getIdAgency())
                    .append("room", bookingD.getRoom())
                    .append("idType", bookingD.getIdType())
                    .append("hotel", bookingD.getHotel())
                    .append("idHotel", bookingD.getIdHotel())
                    .append("check_in", bookingD.getCheck_in())
                    .append("room_nights", bookingD.getRoom_nights())
                    .append("price", bookingD.getPrice());

            collection.insertOne(booking);
            System.out.println("Reserva insertada correctamente.");

        } else {

            System.out.println("Documento con location_number " + bookingD.getId() + " ya existe.");
        }

    }

    public void mostrarReserva() {
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            System.out.println(cursor.next().toJson());
        }
        System.out.println("Fin reservas.");
    }

    public void actualizarPreuReserva(int id, double newPrice) {
        Document existingDocument = collection.find(Filters.eq("location_number", id)).first();

        if (existingDocument == null) {
            System.out.println("Reserva con id: "+ id + " no existe.");
        } else {
            Document update = new Document("$set", new Document("price", newPrice));
            collection.updateOne(existingDocument, update);
            System.out.println("Precio reserva actualizado.");
        }
    }

    public void eliminarReserva(int id) {
        Document existingDocument = collection.find(Filters.eq("location_number", id)).first();

        if (existingDocument == null) {
            System.out.println("Reserva con id: " + id + " no existe");
        } else {
            collection.deleteOne(existingDocument);
            System.out.println("Reserva con id: " + id + "eliminada correctamente.");
        }
    }

    void dropCollection() {
        collection.drop();
    }

}
