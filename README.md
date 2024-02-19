JDBC

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
    

