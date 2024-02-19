package com.mycompany.ad_a13_davidgeorgievpetrunov;

import com.mycompany.dao.Booking;
import com.mycompany.dao.Bookings;
import com.mycompany.dao.ConexionDAO;
import com.mycompany.dao.updateBooking;
import com.mycompany.implementacion.ConexionImp;
import java.util.Scanner;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AD_A13_DavidGeorgievPetrunov {

    ConexionDAO conexion;
    Scanner scanner;

    public static void main(String[] args) {
        AD_A13_DavidGeorgievPetrunov ad = new AD_A13_DavidGeorgievPetrunov();
    }

    public AD_A13_DavidGeorgievPetrunov() {
        menu();
    }

    private Booking askBooking(boolean allowSame) {
        Booking booking = new Booking();

        System.out.println("Porfavor, inserta un location_number adecuado");
        if (allowSame == false) {
            booking.setLocation_number(askLocationNumber());
        } else {
            System.out.println("Inserta un location_number valido");
            int num = askNumber();

            while (this.conexion.selectById(num) != -1) {
                System.out.println("Inserta un location_number validom el location number insertado no existe");
                num = askNumber();
            }
            booking.setLocation_number(num);
        }
        System.out.println("Porfavor, inserta una id de cliente");
        booking.setId_client(askNumber());

        System.out.println("Porfavor, inserta un cliente");
        booking.setClient(askString());

        System.out.println("Porfavor, inserta una agencia");
        booking.setAgency(askString());

        System.out.println("Porfavor, inserta una id de agencia");
        booking.setId_agency(askNumber());

        System.out.println("Porfavor, inserta el precio");
        booking.setPrice(askPrice());
        System.out.println("El precio es: " + booking.getPrice());

        System.out.println("Porfavor, inserta la habitacion");
        booking.setRoom(askString());

        System.out.println("Porfavor, inserta el id de tipo de habitacion");
        booking.setId_type(askNumber());

        System.out.println("Porfavor, inserta un hotel");
        booking.setHotel(askString());

        System.out.println("Porfavor, inserta una id de hotel");
        booking.setId_hotel(askNumber());

        System.out.println("Porfavor, inserta un check_in");
        booking.setCheck_in(askDate());

        System.out.println("Porfavor, inserta la cantidad de noches");
        booking.setRoom_nights(askNumber());

        return booking;
    }

    private String askDate() {
        while (true) {
            try {
                System.out.print("Ingresa una fecha (yyyy/MM/dd): ");
                String input = scanner.nextLine();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                dateFormat.setLenient(false);
                Date date = dateFormat.parse(input);
                return dateFormat.format(date);
            } catch (ParseException e) {
                System.out.println("Por favor, ingresa una fecha válida (formato: yyyy/MM/dd).");
            }
        }
    }

    private int askLocationNumber() {
        while (true) {
            int location_number = askNumber();
            System.out.println(this.conexion.selectById(location_number));
            if (this.conexion.selectById(location_number) == 1) {
                return location_number;
            }
        }
    }

    private int askNumber() {
        while (true) {
            try {
                System.out.print("Ingresa un número: ");
                int num = Integer.parseInt(scanner.nextLine());
                if (num >= 0) {
                    return num;
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingresa un número válido.");
            }
        }
    }

    private String askPrice() {
        while (true) {
            System.out.print("Ingresa un precio: ");
            String input = scanner.nextLine();
            try {
                double price = Double.parseDouble(input);
                if (price >= 0) {
                    return String.format("%.2f", price);
                } else {
                    System.out.println("Por favor, ingresa un precio válido (mayor o igual a cero).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingresa un precio válido.");
            }
        }
    }

    private String askString() {
        System.out.print("Ingresa un texto: ");
        return scanner.nextLine();
    }

    private int elegir() {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;
        boolean entradaValida = false;

        while (!entradaValida) {
            System.out.print("Ingrese su opción: ");
            try {
                opcion = scanner.nextInt();
                entradaValida = true;
            } catch (java.util.InputMismatchException e) {
                System.out.println("Por favor, ingrese un valor entero válido.");
                scanner.nextLine();
            }
        }

        return opcion;
    }

    private void borrarAllData() {
        this.conexion.deleteAllData();
    }

    private void descargarCSV() {
        Scanner x = new Scanner(System.in);
        System.out.println("Porfavor, inserta una ruta valida.");
        String ruta = x.nextLine();
        while (!this.checkRuta(ruta)) {
            System.out.println("Ruta invalida, inserta una ruta valida:");
            ruta = x.nextLine();
        }
        File csv = new File(ruta);
        this.conexion.downloadAsCSV(csv);
    }

    private boolean checkRuta(String ruta) {
        File file = new File(ruta);
        if (file.exists() && file.isFile()) {
            return true;
        } else try {
            if (file.createNewFile()) {
                return true;
            }
        } catch (IOException ex) {
            Logger.getLogger(AD_A13_DavidGeorgievPetrunov.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private void eliminarReserva() {
        boolean continuar = true;
        int location_number = -1;
        while (continuar) {
            try {
                System.out.println("Porfavor, inserta el location_number de la reserva a borrar");
                location_number = scanner.nextInt();
            } catch (Exception e) {
                System.out.println(e);
            }
            if (this.conexion.selectById(location_number) == -1 && location_number >= 0) {
                continuar = false;
            }
        }
        this.conexion.deleteById(location_number);
    }

    private void insertarReserva() {
        Booking booking = askBooking(false);
        this.conexion.insertarReserva(booking);
    }

    private void insertarXML() {
        Scanner x = new Scanner(System.in);
        System.out.println("Porfavor, inserta una ruta a un xml valido.");
        String ruta = x.nextLine();
        while (!this.checkRuta(ruta)) {
            System.out.println("Ruta invalida, inserta una ruta valida:");
            ruta = x.nextLine();
        }
        try {
            File xmlFile = new File(ruta);
            JAXBContext jaxbContext = JAXBContext.newInstance(Bookings.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Bookings bookings = (Bookings) jaxbUnmarshaller.unmarshal(xmlFile);
            System.out.println("XML is valid and parsed successfully.");
            this.conexion.cargarArchivoXML(bookings);
        } catch (JAXBException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void loadXMLLocal() {
        Scanner x = new Scanner(System.in);
        System.out.println("Porfavor, inserta una ruta a un xml valido.");
        String ruta = x.nextLine();
        while (!this.checkRuta(ruta)) {
            System.out.println("Ruta invalida, inserta una ruta valida:");
            ruta = x.nextLine();
        }
        this.conexion.loadXMLINFILE(ruta);
    }

    public void menu() {
        this.scanner = new Scanner(System.in);
        this.conexion = new ConexionImp();
        int opcion;
        boolean continuar = true;
        while (continuar) {
            menuPrint();
            opcion = elegir();
            switch (opcion) {
                case 1:
                    insertarXML();
                    break;
                case 2:
                    descargarCSV();
                    break;
                case 3:
                    borrarAllData();
                    break;
                case 4:
                    insertarReserva();
                    break;
                case 5:
                    eliminarReserva();
                    break;
                case 6:
                    modificarReserva();
                    break;
                case 7:
                    loadXMLLocal();
                    break;
//                case 9:
//                    saveToCSV();
//                    break;
                case 0:
                    continuar = false;
                    break;
            }
        }
    }

    public void menuPrint() {
        System.out.println("1 - Insertar XML");
        System.out.println("2 - Descargar como csv");
        System.out.println("3 - Borrar datos llamando a procedimiento");
        System.out.println("4 - Añadir reserva");
        System.out.println("5 - Eliminar reserva");
        System.out.println("6 - Modificar reserva");
//        System.out.println("9 - Descargar como csv 2");
        System.out.println("0 - SALIR");
    }

    private void modificarReserva() {
        Booking booking = askBooking(true);
        updateBooking uBooking = new updateBooking();
        boolean continuar = true;
        while (continuar) {
            System.out.println("Nueva location_number");
            int newLoc = askNumber();
            if (this.conexion.selectById(newLoc) == 1 || booking.getLocation_number() == newLoc) {
                uBooking.setBooking(booking, newLoc);
                continuar = false;
            }
        }
        this.conexion.modificarReserva(uBooking);
    }

//    private void saveToCSV() {
//        Scanner x = new Scanner(System.in);
//        System.out.println("Porfavor, inserta una ruta a un xml valido.");
//        String ruta = x.nextLine();
//        this.conexion.saveToCSV(ruta);
//    }
}
