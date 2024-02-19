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

# HIBERNATE
        
    public class HibernateExercise {

        static SessionFactory factory;

        // Main
        public static void main(String[] args) {

            Configuration cfg = new Configuration();
            cfg.configure("/com/mycompany/config/hibernate.cfg.xml");
            factory = cfg.buildSessionFactory();
        }
        factory.close();
    }
    
    private static void insertStudent(String nombre, String lastname, int age, String email, String phone) {
        Session session = null;
        try {
            session = factory.openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                Student newStudent = new Student();
                newStudent.setName(nombre);
                //Set everything

                session.save(newStudent);

                transaction.commit();
            }
        }
    }


    private static void updateStudent(int id, String nombre, String lastname, int age, String email, String phone) {
    // Session null 
        try {
            // Open session set transaction null

            try {
                transaction = session.beginTransaction();

                Student existingStudent = (Student) session.get(Student.class, id);

                if (existingStudent != null) {

                    existingStudent.setName(nombre);
                    // Set everthing

                    session.update(existingStudent);

                    transaction.commit();
                }
            }
        }
    }

    private static void deleteStudent(int id) {
    // Session null 
        try {
            // Open session set transaction null

            try {
                transaction = session.beginTransaction();

                Student existingStudent = (Student) session.get(Student.class, id);

                if (existingStudent != null) {
                    session.delete(existingStudent);

                    transaction.commit();
                }
            }
        }
    }

    private static Student getStudent(int id) {
        // Session null
        try {
            session = factory.openSession();

            Student student = (Student) session.get(Student.class, id);
        }
    }
    
    private static List<Student> listStudents() {
        List<Student> students = new ArrayList<>();
        // Session null

        try {
            session = factory.openSession();

            // Basicamente un "select * from student;"

            String hql = "FROM Student";
            Query query = session.createQuery(hql);
            students = query.list();
        }
    }

# MONGODB
    public class MongoDBConnector {
    
        private MongoClient mongoClient;
        private MongoDatabase database;
    
        public MongoDBConnector() {
            this.mongoClient = MongoClients.create();
            this.database = mongoClient.getDatabase("reservesDB");
        }
    
        public MongoDatabase getDatabase() {
            return database;
        }
    
    }
    
    public class BookingDAO {
    
        private MongoCollection<Document> collection;
    
        public BookingDAO() {
            this.collection = new MongoDBConnector().getDatabase().getCollection("reservas");
        }
    
        public void insertarReserva(Booking bookingD) {
                Document booking = new Document("location_number", bookingD.getId())
                    .append("client", bookingD.getClient())
                   //Hacemos append de todos los parametros.
    
                collection.insertOne(booking);
                System.out.println("Reserva insertada correctamente.");
            }
    
        public void mostrarReserva() {
            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        }
    
        public void actualizarPreuReserva(int id, double newPrice) {
            Document update = new Document("$set", new Document("price", newPrice));
            collection.updateOne(existingDocument, update);
            System.out.println("Precio reserva actualizado.");
        }
    
        public void eliminarReserva(int id) {
            collection.deleteOne(existingDocument);
            System.out.println("Reserva con id: " + id + "eliminada correctamente.");    
        }
    
        void dropCollection() {
            collection.drop();
        }
    
    }    

# CRUD JAX-RS

    @ApplicationPath("/api")
    public class HelloApplication extends Application {
    
    }

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
            this.bookings.add(newBooking);
            System.out.println(newBooking.toString());
            writeBookingsToXML();
            updateXML();
            
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
    
                boolean found = false;
                for (Booking booking : bookings) {
                    if (String.valueOf(booking.getId()).equals(id)) {
    
                        booking.setIdClient(updatedBooking.getIdClient());
                        //Set all parameters
    
                        found = true;
                        break;
                    }
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
            } 
        }
    }

