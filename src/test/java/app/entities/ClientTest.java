//package app.entities;
//
//import app.entities.enums.ClientOccupation;
//
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
//public class ClientTest extends UserTest{
//    private Client clientOne;
//    private Client clientOneClone;
//    private Client clientTwo;
//
//    @Before
//    public void initializeClients() {
//        try {
//            clientOne = new Client("A", "B", new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"),
//                    ClientOccupation.EMPLOYED, "email@domain.com", "+375291001010", "password123", true);
//
//            clientOneClone = new Client("A", "B", new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01"),
//                    ClientOccupation.EMPLOYED, "email@domain.com", "+375291001010", "password123", true);
//
//            clientTwo = new Client("C", "D", new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"),
//                    ClientOccupation.EMPLOYED, "another-email@domain.com", "+375292002020", "password456", true);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void testToString() {
//        String actual = clientOne.toString();
//        String expected = "Client(super=User(id=0, firstName=A, lastName=B, email=email@domain.com, phoneNumber=+375291001010, password=password123), " +
//                "dateOfBirth=Mon Jan 01 00:00:00 MSK 1990, occupation=EMPLOYED, personalDataProcAgreement=true)";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testEqualsPositive() {
//        boolean actual = clientOne.equals(clientOneClone);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void testEqualsNegative() {
//        boolean actual = clientOne.equals(clientTwo);
//        assertFalse(actual);
//    }
//
//    @Test
//    public void testHashCodePositive() {
//        assertEquals(clientOne.hashCode(), clientOneClone.hashCode());
//    }
//
//    @Test
//    public void testHashCodeNegative() {
//        assertNotEquals(clientOne.hashCode(), clientTwo.hashCode());
//    }
//
//    @Override
//    @Test
//    public void getId() {
//        long actual = clientOne.getId();
//        long expected = 0;
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getFirstName() {
//        String actual = clientOne.getFirstName();
//        String expected = "A";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getLastName() {
//        String actual = clientOne.getLastName();
//        String expected = "B";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getEmail() {
//        String actual = clientOne.getEmail();
//        String expected = "email@domain.com";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getPhoneNumber() {
//        String actual = clientOne.getPhoneNumber();
//        String expected = "+375291001010";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getPassword() {
//        String actual = clientOne.getPassword();
//        String expected = "password123";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void getDateOfBirth() throws ParseException {
//        Date actual = clientOne.getDateOfBirth();
//        Date expected = new SimpleDateFormat("yyyy-MM-dd").parse("1990-01-01");
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void getOccupation() {
//        ClientOccupation actual = clientOne.getOccupation();
//        ClientOccupation expected = ClientOccupation.EMPLOYED;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void isPersonalDataProcAgreement() {
//        boolean actual = clientOneClone.isPersonalDataProcAgreement();
//        assertTrue(actual);
//    }
//
//    /*@Override
//    @Test
//    public void setId() {
//        clientOne.setId(1);
//        long actual = clientOne.getId();
//        long expected = 1;
//        assertEquals(expected, actual);
//    }*/
//
//    @Override
//    @Test
//    public void setFirstName() {
//        clientOne.setFirstName("FOO");
//        String actual = clientOne.getFirstName();
//        String expected = "FOO";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void setLastName() {
//        clientOne.setLastName("BAR");
//        String actual = clientOne.getLastName();
//        String expected = "BAR";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void setEmail() {
//        clientOne.setEmail("foo-bar@domain.com");
//        String actual = clientOne.getEmail();
//        String expected = "foo-bar@domain.com";
//        assertEquals(actual, expected);
//    }
//
//    @Override
//    @Test
//    public void setPhoneNumber() {
//        clientOne.setPhoneNumber("+375293003030");
//        String actual = clientOne.getPhoneNumber();
//        String expected = "+375293003030";
//        assertEquals(actual, expected);
//    }
//
//    @Override
//    @Test
//    public void setPassword() {
//        clientOne.setPassword("foobarbaz123");
//        String actual = clientOne.getPassword();
//        String expected = "foobarbaz123";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void setDateOfBirth() {
//        Date actual = null;
//        Date expected = null;
//
//        try {
//            clientOne.setDateOfBirth(new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01"));
//            actual = clientOne.getDateOfBirth();
//            expected = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");
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
//    public void setOccupation() {
//        clientOne.setOccupation(ClientOccupation.ENTREPRENEUR);
//        ClientOccupation actual = clientOne.getOccupation();
//        ClientOccupation expected = ClientOccupation.ENTREPRENEUR;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void setPersonalDataProcAgreement() {
//        clientOne.setPersonalDataProcAgreement(false);
//        boolean actual = clientOne.isPersonalDataProcAgreement();
//        assertFalse(actual);
//    }
//}