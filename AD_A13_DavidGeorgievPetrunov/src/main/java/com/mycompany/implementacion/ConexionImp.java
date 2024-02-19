package com.mycompany.implementacion;

import com.mycompany.dao.Booking;
import com.mycompany.dao.Bookings;
import com.mycompany.dao.ConexionDAO;
import com.mycompany.dao.MySessionFactory;
import com.mycompany.dao.updateBooking;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ibatis.session.SqlSession;

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
    public void downloadAsCSV(File file) {
        SqlSession session = null;
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException ex) {
            Logger.getLogger(ConexionImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            session = instancia.getFactory().openSession();
            Bookings bookings = new Bookings();
            bookings.setBookingList(session.selectList("selectAllData"));
            for (int i = 0; i < bookings.getBookingList().size(); i++) {
                Booking booking = bookings.getBookingList().get(i);
                writer.write(booking.getLocation_number() + ","
                        + booking.getPrice() + ","
                        + booking.getRoom_nights() + ","
                        + booking.getRoom() + ","
                        + booking.getAgency() + ","
                        + booking.getClient() + ","
                        + booking.getHotel() + ","
                        + booking.getCheck_in() + "\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null) {
                session.close();
            }
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ConexionImp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String getVersionMysql() {
        String version = "";
        SqlSession session = null;
        try {
            session = instancia.getFactory().openSession();
            version = session.selectOne("mysqlVersion");
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return version;
    }

    @Override
    public int selectById(int location_number) {
        SqlSession session = null;
        try {
            session = instancia.getFactory().openSession();
            Integer result = session.selectOne("selectById", location_number);

            if (result == null) {
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e);
            return -1;
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

    @Override
    public void loadXMLINFILE(String ruta) {
        SqlSession session = null;
        try {
            session = instancia.getFactory().openSession();
            session.insert("loadXmlData", ruta);
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
    public void modificarReserva(updateBooking booking) {
        SqlSession session = null;
        try {
            session = instancia.getFactory().openSession();
            session.update("updateBooking", booking);
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
    public void saveToCSV(String ruta) {
        SqlSession session = null;
        try {
            session = instancia.getFactory().openSession();
            session.selectOne("saveDataToCsv", ruta);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
