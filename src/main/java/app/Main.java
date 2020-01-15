package app;

import app.dao.AdminDaoImpl;
import app.dao.AppointmentDaoImpl;
import app.dao.ClientDaoImpl;
import app.entities.Admin;
import app.entities.Appointment;
import app.entities.Client;
import app.entities.enums.AdminPosition;
import app.entities.enums.City;
import app.entities.enums.ClientOccupation;
import app.services.AdminService;
import app.services.ClientService;
import org.hibernate.Hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
                                                    /* AdminDaoImpl TEST */
        // AdminDaoImpl dao = new AdminDaoImpl();

        /*Admin admin = new Admin("JJJ", "JJJ", AdminPosition.OPERATOR, City.MINSK,
                "email-1@domain.com", "+375291001010", "password123");
        long id = dao.create(admin);
        System.out.println(id);*/

        /*List<Admin> admins = dao.readAll();
        for (Admin admin : admins) {
            System.out.println(admin);
        }*/

        /*Admin admin = dao.readById(85);
        System.out.println(admin);
        admin.setFirstName("Ror");
        admin.setLastName("Tor");
        Admin updatedAdmin = dao.update(admin);
        System.out.println(updatedAdmin);*/

        /*Admin admin = dao.readById(85);
        dao.delete(admin);
        boolean isDeleted = dao.readById(85) == null;
        System.out.println(isDeleted);*/

        /*Admin admin = dao.authenticate("email-1@domain.com", "password123");
        System.out.println(admin);*/

        /*Admin admin = dao.readByEmail("email-3@domain.com");
        System.out.println(admin);*/

        /*List<Admin> admins = dao.readByLastName("D");
        for (Admin admin : admins) {
            System.out.println(admin);
        }*/

        /*long id = dao.getIdByEmail("email-3@domain.com");
        System.out.println(id);*/

        /*List<Admin> admins = dao.readByCity(City.MINSK);
        for (Admin admin : admins) {
            System.out.println(admin);
        }*/

        /*List<Admin> admins = dao.readByPositionAndCity(AdminPosition.MANAGER, City.MINSK);
        for (Admin admin : admins) {
            System.out.println(admin);
        }*/

        // System.out.println(dao.countAdminsByCityAndPosition(City.BREST, AdminPosition.OPERATOR));

        /*try {
            Client clientOne = new Client("C", "D", new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"),
                    ClientOccupation.EMPLOYED, "email@domain.com", "+375291001010", "password123", true);
            clientDao.create(clientOne);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/








                                                        /* ClientService TEST */
        // ClientService service = new ClientService();

        /*try {
            Client client = new Client("ZZZ", "ZZZ", new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"),
                    ClientOccupation.UNEMPLOYED, "email-7@domain.com", "+375291001010", "password123", true);
            long id = service.addClient(client);
            System.out.println(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        /*List<Client> clients = service.findAllClients();
        for (Client client : clients) {
            System.out.println(client);
        }*/

        // System.out.println(service.findClientById(192).getAppointment());









                                                    /* AppointmentDaoImpl TEST*/
        /*ClientDaoImpl clientDao = new ClientDaoImpl();
        AppointmentDaoImpl appDao = new AppointmentDaoImpl();*/

        /*try {
            Client client = clientDao.readById(204);
            Appointment appointment = new Appointment(client, City.GOMEL,
                    new SimpleDateFormat("yyyy-MM-dd").parse("1995-01-01"), "09:00");
            appDao.create(appointment);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        /*List<Appointment> appointments = appDao.readAll();
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }*/

        // System.out.println(appDao.readById(99));

        /*Appointment appointment = appDao.readById(99);
        appointment.setAppointmentTime("12:00");
        appointment.setCity(City.GOMEL);
        Appointment updatedAppointment = appDao.update(appointment);
        System.out.println(updatedAppointment);*/

        /*Appointment appointment = appDao.readById(98);
        appDao.delete(appointment);*/

        /*Client client = clientDao.readById(192);
        System.out.println(client.getAppointment());*/
        //clientDao.delete(client);

        /*List<Appointment> appointments = appDao.readAllByCity(City.MINSK);
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }*/

        /*List<Appointment> appointments = null;
        try {
            appointments = appDao.readAllByCityAndDate(City.MINSK, new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert appointments != null;
        for (Appointment appointment : appointments) {
            System.out.println(appointment);
        }*/

        /*Set<Date> dates = appDao.readDatesByCity(City.MINSK);
        for (Date date : dates) {
            System.out.println(date);
        }*/

        /*try {
            String[] time = appDao.readTimeByCityAndDate(City.MINSK, new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"));
            for (String t : time) {
                System.out.println(t);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/






                                                            /* AdminService TEST */
        // AdminService service = new AdminService();

        /*Admin admin = new Admin("KKK", "KKK", AdminPosition.OPERATOR, City.MINSK,
                "email-4@domain.com", "+375291001010", "password123");
        long addedAdminId = service.addAdmin(admin);
        System.out.println(addedAdminId);*/

        /*List<Admin> admins = service.findAllAdmins();
        for (Admin admin : admins) {
            System.out.println(admin);
        }*/

        /*Admin admin = service.findAdminById(87);
        System.out.println(admin);*/

        /*Admin admin = service.findAdminById(87);
        System.out.println(admin);
        admin.setFirstName("Ror");
        admin.setLastName("Tor");
        Admin updatedAdmin = service.updateAdmin(admin);
        System.out.println(updatedAdmin);*/

        /*Admin admin = service.findAdminById(87);
        service.deleteAdmin(admin);*/

        /*Admin admin = service.login("email-3@domain.com", "password123");
        System.out.println(admin);*/

        /*Admin admin = service.findAdminByEmail("email-3@domain.com");
        System.out.println(admin);*/

        /*List<Admin> admins = service.findAdminByLastName("D");
        for (Admin admin : admins) {
            System.out.println(admin);
        }*/

        /*long id = service.findIdByEmail("email-3@domain.com");
        System.out.println(id);*/


        /*List<Admin> admins = service.findAdminsByCity(City.MINSK);
        for (Admin admin : admins) {
            System.out.println(admin);
        }*/

        /*List<Admin> admins = service.findAdminsByPositionAndCity(AdminPosition.OPERATOR, City.MINSK);
        for (Admin admin : admins) {
            System.out.println(admin);
        }*/

        // System.out.println(service.countAdminsByCityAndPosition(City.MINSK, AdminPosition.OPERATOR));
    }
}