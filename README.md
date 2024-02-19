# JDBC

    url = "jdbc:mysql://localhost:3306/alumnat";
    usuario = "root";
    contraseña = "";

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
    

# MYBATIS

    public class MySessionFactory {

        private static MySessionFactory instancia;
        private SqlSessionFactory factory;

        private MySessionFactory() {
            String recurso = "mybatis-config.xml";
            Reader lector = null;

            try {
                lector = Resources.getResourceAsReader(recurso);
                factory = new SqlSessionFactoryBuilder().build(lector);
                lector.close();
            } catch (IOException error) {
                System.out.println(error);
            }
        }

        public SqlSessionFactory getFactory() {
            return factory;
        }

        public static MySessionFactory getInstance() {
            if (instancia == null) {
                instancia = new MySessionFactory();
            }
            return instancia;
        }

    }

    public class ConexionImp implements ConexionDAO {
    
        private final MySessionFactory instancia = MySessionFactory.getInstance();

        @Override
        public void cargarArchivoXML(Bookings bookings) {
            SqlSession session = null;
            try {
                session = instancia.getFactory().openSession();
                for (int i = 0; i < bookings.getBookingList().size(); i++) {
                    System.out.println("Insertando reserva con location_number:" + bookings.getBookingList().get(i).getLocation_number());
                    session.insert("insertBooking", bookings.getBookingList().get(i));
                }
                session.commit();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
        
        @Override
        public void deleteAllData() {
            SqlSession session = null;
            try {
                session = instancia.getFactory().openSession();
                session.delete("deleteData");
                session.commit();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }

        @Override
        public void deleteById(int location_number) {
            SqlSession session = null;
            try {
                session = instancia.getFactory().openSession();
                session.update("deleteById", location_number);
                session.commit();
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }
        
        @Override
        public void insertarReserva(Booking booking) {
            SqlSession session = null;
            try {
                session = instancia.getFactory().openSession();
                session.selectOne("insertBooking", booking);
            } catch (Exception e) {
                System.out.println(e);
            } finally {
                if (session != null) {
                    session.close();
                }
            }
        }


