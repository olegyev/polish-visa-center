//package app.entities;
//
//import app.entities.enums.City;
//import app.entities.enums.ClientOccupation;
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import static org.junit.Assert.*;
//@Ignore
//public class AppointmentTest {
//    private Appointment appointmentOne;
//    private Appointment appointmentOneClone;
//    private Appointment appointmentTwo;
//    Client client;
//
//    @Before
//    public void initializeAppointments() {
//        try {
//            client = new Client("A", "B", new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"),
//                    ClientOccupation.EMPLOYED, "email@domain.com", "+375291001010", "password123", true);
//
//            appointmentOne = new Appointment(client, City.MINSK, new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"), "09:00");
//            appointmentOneClone = new Appointment(client, City.MINSK, new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01"), "09:00");
//            appointmentTwo  = new Appointment(client, City.GRODNO, new SimpleDateFormat("yyyy-MM-dd").parse("2025-01-01"), "15:00");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testToString() {
//        String actual = appointmentOne.toString();
//        String expected = "Appointment(id=0, client=Client(super=User(id=0, firstName=A, lastName=B, " +
//                "email=email@domain.com, phoneNumber=+375291001010, password=password123), " +
//                "dateOfBirth=Mon Jan 01 00:00:00 MSK 1990, occupation=EMPLOYED, personalDataProcAgreement=true), " +
//                "city=MINSK, appointmentDate=Wed Jan 01 00:00:00 MSK 2020, appointmentTime=09:00)";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testEqualsPositive() {
//        boolean actual = appointmentOne.equals(appointmentOneClone);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void testEqualsNegative() {
//        boolean actual = appointmentOne.equals(appointmentTwo);
//        assertFalse(actual);
//    }
//
//    @Test
//    public void testHashCodePositive() {
//        assertEquals(appointmentOne.hashCode(), appointmentOneClone.hashCode());
//    }
//
//    @Test
//    public void testHashCodeNegative() {
//        assertNotEquals(appointmentOne.hashCode(), appointmentTwo.hashCode());
//    }
//
//    @Test
//    public void getId() {
//        long actual = appointmentOne.getId();
//        long expected = 0;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void getClient() {
//        Client actual = appointmentOne.getClient();
//        Client expected = client;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void getCity() {
//        City actual = appointmentOne.getCity();
//        City expected = City.MINSK;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void getAppointmentDate() {
//        try {
//            Date actual = appointmentOne.getAppointmentDate();
//            Date expected = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
//            assertEquals(expected, actual);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void getAppointmentTime() {
//        String actual = appointmentOne.getAppointmentTime();
//        String expected = "09:00";
//        assertEquals(expected, actual);
//    }
//
//    /*@Test
//    public void setId() {
//        appointmentOne.setId(1);
//        long actual = appointmentOne.getId();
//        long expected = 1;
//        assertEquals(expected, actual);
//    }*/
//
//    @Test
//    public void setClient() {
//        Client anotherClient = new Client();
//        appointmentOne.setClient(anotherClient);
//        Client actual = appointmentOne.getClient();
//        assertEquals(anotherClient, actual);
//    }
//
//    @Test
//    public void setCity() {
//        appointmentOne.setCity(City.BREST);
//        City actual = appointmentOne.getCity();
//        City expected = City.BREST;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void setAppointmentDate() {
//        Date actual = null;
//        Date expected = null;
//
//        try {
//            appointmentOne.setAppointmentDate(new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-10"));
//            actual = appointmentOne.getAppointmentDate();
//            expected = new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-10");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        if (actual != null && expected != null) {
//            assertEquals(expected, actual);
//        }
//    }
//
//    @Test
//    public void setAppointmentTime() {
//        appointmentOne.setAppointmentTime("10:00");
//        String actual = appointmentOne.getAppointmentTime();
//        String expected = "10:00";
//        assertEquals(expected, actual);
//    }
//}