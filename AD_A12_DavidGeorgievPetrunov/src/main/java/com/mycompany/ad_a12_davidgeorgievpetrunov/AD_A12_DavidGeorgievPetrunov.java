package com.mycompany.ad_a12_davidgeorgievpetrunov;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AD_A12_DavidGeorgievPetrunov {

    Vector<Booking> bookings;
    String url;
    String usuario;
    String contraseña;

    public static void main(String[] args) {
        AD_A12_DavidGeorgievPetrunov ad12 = new AD_A12_DavidGeorgievPetrunov();
    }

    public AD_A12_DavidGeorgievPetrunov() {

        bookings = cargarDatos();
        if (bookings == null) {
            System.out.println("Ha habido un error cargando los datos");
        } else {
            try {
                url = "jdbc:mysql://localhost:3306/alumnat";
                usuario = "root";
                contraseña = "";
                menu();
            } catch (IOException ex) {
                Logger.getLogger(AD_A12_DavidGeorgievPetrunov.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void actualizarReserva(String idReserva, String cliente, String agencia, String precio, String room, String hotel, String check_in, String room_nights) {
        String sql = "UPDATE reserva SET idCliente = ?, idAgencia = ?, price = ?, idRoom = ?, idHotel = ?, check_in = ?, room_nights = ? WHERE idReserva = ?";

        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int count = 0;
                int idReservaInt = Integer.parseInt(idReserva);

                if (obtenerIdReserva(idReserva) == -1) {
                    preparedStatement.setInt(1, consultarSinID("clientes", "NombreCliente", cliente, "idCliente"));
                    preparedStatement.setInt(2, consultarSinID("agencias", "NombreAgencia", agencia, "idAgencia"));
                    preparedStatement.setString(3, precio);
                    preparedStatement.setInt(4, consultarSinID("roomtype", "TypeRoom", room, "idRoomType"));
                    preparedStatement.setInt(5, consultarSinID("hoteles", "NombreHotel", hotel, "idHotel"));
                    java.sql.Date fechaSql = convertirAFechaSql(check_in);
                    preparedStatement.setDate(6, fechaSql);
                    preparedStatement.setInt(7, Integer.parseInt(room_nights));
                    preparedStatement.setInt(8, idReservaInt);

                    count = preparedStatement.executeUpdate();
                } else {
                    System.out.println("La reserva con ID " + idReserva + " no existe.");
                }
                System.out.println("Filas afectadas totales al actualizar:" + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Vector<Booking> cargarDatos() {
        try {
            File inputFile = new File("src/main/java/com/mycompany/ad_a12_davidgeorgievpetrunov/bookings.xml");
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

    private int consultarConID(String tabla, String columna, String valor, String resultado, String idConsultar) {
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "SELECT " + resultado + " FROM " + tabla + " WHERE " + columna + " = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, valor);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    int count = 0;
                    int id = -1;

                    while (resultSet.next()) {
                        count++;
                        id = resultSet.getInt(resultado);
                    }

                    if (count > 1) {
                        //                    System.out.println("Tabla " + tabla + " tiene id's repetidas");
                    } else if (count == 0) {
                        //                      System.out.println("Insertando nuevo cliente en la tabla: " + tabla);
                        insertar(tabla, columna, resultado, valor, idConsultar);
                        return consultarConID(tabla, columna, valor, resultado, idConsultar);
                    } else {
                        //                  System.out.println("Obteniendo id de " + tabla + " donde columna " + columna + " = " + valor + "");
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private int consultarSinID(String tabla, String columna, String valor, String resultado) {
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "SELECT " + resultado + " FROM " + tabla + " WHERE " + columna + " = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, valor);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    int count = 0;
                    int id = -1;

                    while (resultSet.next()) {
                        count++;
                        id = resultSet.getInt(resultado);
                    }

                    if (count > 1) {
                        //                    System.out.println("Tabla " + tabla + " tiene id's repetidas");
                    } else if (count == 0) {
                        //                      System.out.println("Insertando nuevo cliente en la tabla: " + tabla);
                        insertarSinID(tabla, columna, valor);
                        return consultarSinID(tabla, columna, valor, resultado);
                    } else {
                        //                  System.out.println("Obteniendo id de " + tabla + " donde columna " + columna + " = " + valor + "");
                        return id;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private String consultarValorUnico(String tabla, String columna, String valor, String resultado) {
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "SELECT " + resultado + " FROM " + tabla + " WHERE " + columna + " = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, valor);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    int count = 0;
                    String dato = "a";

                    while (resultSet.next()) {
                        count++;
                        dato = resultSet.getString(resultado);
                    }

                    if (count > 1) {
                        //                    System.out.println("Tabla " + tabla + " tiene id's repetidas");
                    } else {
                        //                  System.out.println("Obteniendo id de " + tabla + " donde columna " + columna + " = " + valor + "");
                        return dato;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "-1";
    }

    private java.sql.Date convertirAFechaSql(String fecha) {
        try {
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date fechaUtil = formatoEntrada.parse(fecha);
            return new java.sql.Date(fechaUtil.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void eliminarContenidoTabla(String tabla) {
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "DELETE FROM " + tabla + "";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int filasAfectadas = preparedStatement.executeUpdate();
                System.out.println("Filas afectadas al eliminar datos de " + tabla + ": " + filasAfectadas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarReserva(int localizador) {
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "DELETE FROM reserva WHERE idReserva = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, localizador);
                int filasAfectadas = preparedStatement.executeUpdate();
                System.out.println("Filas afectadas al eliminar datos de reserva : " + filasAfectadas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void eliminarTodasReservas() {
        eliminarContenidoTabla("reserva");
        eliminarContenidoTabla("clientes");
        eliminarContenidoTabla("agencias");
        eliminarContenidoTabla("hoteles");
        eliminarContenidoTabla("roomtype");
    }

    private boolean esNumeroEntero(String localizador) {
        try {
            Integer.parseInt(localizador);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void insertar(String tabla, String columna, String columnaId, String valor, String valorId) {
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "INSERT INTO " + tabla + " (" + columnaId + "," + columna + ") VALUES (?,?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, Integer.parseInt(valorId));
                preparedStatement.setString(2, valor);

                // Comprobacion
                int filasAfectadas = preparedStatement.executeUpdate();
//                System.out.println("Filas afectadas insertar en " + tabla + ": " + filasAfectadas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarReserva(String idReserva, String cliente, String agencia, String precio, String room, String hotel, String check_in, String room_nights) {
        String sql = "INSERT INTO reserva (idReserva,idCliente, idAgencia, price, idRoom, idHotel, check_in, room_nights) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int count = 1;
                if (obtenerIdReserva(idReserva) != -1) {
                    preparedStatement.setInt(1, Integer.parseInt(idReserva));
                    preparedStatement.setInt(2, consultarSinID("clientes", "NombreCliente", cliente, "idCliente"));
                    preparedStatement.setInt(3, consultarSinID("agencias", "NombreAgencia", agencia, "idAgencia"));
                    preparedStatement.setString(4, precio);
                    preparedStatement.setInt(5, consultarSinID("roomtype", "TypeRoom", room, "idRoomType"));
                    preparedStatement.setInt(6, consultarSinID("hoteles", "NombreHotel", hotel, "idHotel"));
                    java.sql.Date fechaSql = convertirAFechaSql(check_in);
                    preparedStatement.setDate(7, fechaSql);
                    preparedStatement.setInt(8, Integer.parseInt(room_nights));

                    preparedStatement.executeUpdate();
                } else {
                    System.out.println("Id duplicada");
                }

                System.out.println("Filas afectadas totales al añadir:" + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertarSinID(String tabla, String columna, String valor) {
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "INSERT INTO " + tabla + " (" + columna + ") VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, valor);

                // Comprobacion
                int filasAfectadas = preparedStatement.executeUpdate();
//                System.out.println("Filas afectadas insertar en " + tabla + ": " + filasAfectadas);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int solicitarLocalizador() {
        Scanner x = new Scanner(System.in);
        boolean continuar = true;
        System.out.println("\nInserta el numero de reserva\n");
        String localizador = x.nextLine();
        while (continuar == true) {
            if (esNumeroEntero(localizador)) {
                if (Integer.parseInt(localizador) < 0) {
                    System.out.println("\nEl valor es inferior a 0\n");
                    localizador = x.nextLine();
                } else {
                    continuar = false;
                }
            } else {
                System.out.println("\nEl valor insertado no es un numero entero, inserta un valor valido:\n");
                localizador = x.nextLine();
            }
        }
        return Integer.parseInt(localizador);
    }

    private void menu() throws IOException {
        String tabla;
        String resultado;
        String columna;
        String reserva;
        String cliente;
        String agencia;
        String price;
        String room;
        String hotel;
        String check_in;
        String room_nights;
        Scanner x = new Scanner(System.in);
        boolean continuar = true;
        while (continuar == true) {
            menuPrint();
            int opciones = x.nextInt();
            switch (opciones) {
                case 1:
                    insertarTodasReservas();
                    break;
                case 2:
                    eliminarTodasReservas();
                    break;
                case 3:
                    tabla = "reserva";
                    resultado = "idReserva, idCliente, idAgencia, price, idRoom, idHotel, check_in, room_nights";
                    columna = "idReserva";
                    obtenerDatosReservaCondicional(tabla, columna, String.valueOf(solicitarLocalizador()), resultado, false);
                    break;
                case 4:
                    tabla = "reserva";
                    resultado = "idReserva, idCliente, idAgencia, price, idRoom, idHotel, check_in, room_nights";
                    columna = "idAgencia";
                    obtenerAgencias();
                    obtenerDatosReservaCondicional(tabla, columna, String.valueOf(solicitarLocalizador()), resultado, true);
                    break;
                case 5:
                    System.out.println("Numero reserva:");
                    x.nextLine();
                    reserva = x.nextLine();
                    System.out.println("Cliente:");
                    cliente = x.nextLine();
                    System.out.println("Agencia:");
                    agencia = x.nextLine();
                    System.out.println("Precio:");
                    price = x.nextLine();
                    System.out.println("Tipo de habitacion:");
                    room = x.nextLine();
                    System.out.println("Hotel:");
                    hotel = x.nextLine();
                    System.out.println("Fecha:");
                    check_in = x.nextLine();
                    System.out.println("Noches:");
                    room_nights = x.nextLine();
                    insertarReserva(reserva, cliente, agencia, price, room, hotel, check_in, room_nights);
                    break;
                case 6:
                    eliminarReserva(solicitarLocalizador());
                    break;
                case 7:
                    System.out.println("Numero reserva:");
                    x.nextLine();
                    reserva = x.nextLine();
                    System.out.println("Cliente:");
                    cliente = x.nextLine();
                    System.out.println("Agencia:");
                    agencia = x.nextLine();
                    System.out.println("Precio:");
                    price = x.nextLine();
                    System.out.println("Tipo de habitacion:");
                    room = x.nextLine();
                    System.out.println("Hotel:");
                    hotel = x.nextLine();
                    System.out.println("Fecha:");
                    check_in = x.nextLine();
                    System.out.println("Noches:");
                    room_nights = x.nextLine();
                    actualizarReserva(reserva, cliente, agencia, price, room, hotel, check_in, room_nights);
                    break;
                case 8:
                    continuar = false;
                    break;
                default:
                    System.out.println("Porfavor, inserta un valor valido");
                    break;
            }

        }
    }

    private void menuPrint() {
        System.out.println("1 - Insertar todas las reservas");
        System.out.println("2 - Eliminar todas las reservas");
        System.out.println("3 - Consultar una reserva");
        System.out.println("4 - Consultar todas las reservas de una agencia");
        System.out.println("5 - Insertar una reserva");
        System.out.println("6 - Eliminar una reserva");
        System.out.println("7 - Modificar reserva");
        System.out.println("8 - Salir");
    }

    private void obtenerAgencias() {
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "SELECT idAgencia, NombreAgencia FROM agencias";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("idAgencia");
                        String nombre = resultSet.getString("NombreAgencia");

                        System.out.println("Agencia con id:" + id + " y nombre: " + nombre + "");
                    }
                }
                System.out.println("Elige las reservas de que agencia consultar");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void obtenerDatosReservaCondicional(String tabla, String columna, String valor, String resultado, boolean numero) {
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "SELECT " + resultado + " FROM " + tabla + " WHERE " + columna + " = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                if (numero == true) {
                    preparedStatement.setInt(1, Integer.parseInt(valor));
                } else {
                    preparedStatement.setString(1, valor);
                }
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    boolean continuar = true;
                    int count = 0;
                    while (continuar) {
                        if (resultSet.next()) {
                            count++;
                            int idReserva = resultSet.getInt("idReserva");
                            int idCliente = resultSet.getInt("idCliente");
                            int idAgencia = resultSet.getInt("idAgencia");
                            String price = resultSet.getString("price");
                            int idRoom = resultSet.getInt("idRoom");
                            int idHotel = resultSet.getInt("idHotel");
                            java.sql.Date check_in = resultSet.getDate("check_in");
                            int room_nights = resultSet.getInt("room_nights");

                            System.out.println("Localizador:" + idReserva);
                            System.out.println("idCliente:" + idCliente + consultarValorUnico("clientes", "idCliente", String.valueOf(idCliente), "NombreCliente"));
                            System.out.println("idAgencia:" + idAgencia + consultarValorUnico("agencias", "idAgencia", String.valueOf(idAgencia), "NombreAgencia"));
                            System.out.println("Price:" + price);
                            System.out.println("idRoom:" + idRoom + consultarValorUnico("roomtype", "idRoomType", String.valueOf(idRoom), "TypeRoom"));
                            System.out.println("idHotel:" + idHotel + consultarValorUnico("hoteles", "idHotel", String.valueOf(idHotel), "NombreHotel"));
                            System.out.println("check_in:" + check_in);
                            System.out.println("room_nights:" + room_nights);
                            System.out.println("");
                        } else if (count == 0) {
                            System.out.println("El valor insertado no existe");
                            continuar = false;
                        } else {
                            continuar = false;
                        }
                        if (numero == true) {
                            System.out.println("Cantidad de reservas de esta agencia: " + count + "");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int obtenerIdReserva(String valor) {
        String resultado = "idReserva";
        String tabla = "reserva";
        String columna = "idReserva";
        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            String sql = "SELECT " + resultado + " FROM " + tabla + " WHERE " + columna + " = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, valor);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    int count = 0;

                    while (resultSet.next()) {
                        count++;
                    }

                    if (count == 0) {
//                        System.out.println("Creando id de " + tabla + " donde columna " + columna + " = " + valor + "");
                        return Integer.parseInt(valor);
                    } else {
//                        System.out.println("Obteniendo id de " + tabla + " donde columna " + columna + " = " + valor + "");
                        return -1;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -2;
    }

    private void insertarTodasReservas() {
        String sql = "INSERT INTO reserva (idReserva,idCliente, idAgencia, price, idRoom, idHotel, check_in, room_nights) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, usuario, contraseña)) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int count = 0;
                for (Booking booking : bookings) {
                    if (obtenerIdReserva(booking.getId()) != -1) {
                        preparedStatement.setInt(1, Integer.parseInt(booking.getId()));
                        preparedStatement.setInt(2, consultarConID("clientes", "NombreCliente", booking.getClient(), "idCliente", booking.getIdClient()));
                        preparedStatement.setInt(3, consultarConID("agencias", "NombreAgencia", booking.getAgency(), "idAgencia", booking.getIdAgency()));
                        preparedStatement.setString(4, booking.getPrice());
                        preparedStatement.setInt(5, consultarConID("roomtype", "TypeRoom", booking.getRoom(), "idRoomType", booking.getIdType()));
                        preparedStatement.setInt(6, consultarConID("hoteles", "NombreHotel", booking.getHotel(), "idHotel", booking.getIdHotel()));
                        java.sql.Date fechaSql = convertirAFechaSql(booking.getCheck_in());
                        preparedStatement.setDate(7, fechaSql);
                        preparedStatement.setInt(8, Integer.parseInt(booking.getRoom_nights()));

                        count++;
                        int filasAfectadas = preparedStatement.executeUpdate();
                        //                    System.out.println("Filas afectadas: " + filasAfectadas);
                    } else {
//                        System.out.println("Id duplicada");
                    }
                }
                System.out.println("Filas afectadas totales al añadir:" + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
