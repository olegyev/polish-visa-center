//package app.entities;
//
//import app.entities.enums.AdminPosition;
//import app.entities.enums.City;
//
//import org.junit.Before;
//import org.junit.Ignore;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//@Ignore
//public class AdminTest extends UserTest {
//    private Admin adminOne;
//    private Admin adminOneClone;
//    private Admin adminTwo;
//
//    @Before
//    public void initializeClients() {
//        adminOne = new Admin("A", "B", AdminPosition.OPERATOR, City.MINSK,
//                "email@domain.com", "+375291001010", "password123");
//
//        adminOneClone = new Admin("A", "B", AdminPosition.OPERATOR, City.MINSK,
//                "email@domain.com", "+375291001010", "password123");
//
//        adminTwo = new Admin("C", "D", AdminPosition.DIRECTOR, City.BREST,
//                "another-email@domain.com", "+375292002020", "password456");
//    }
//
//    @Test
//    public void testToString() {
//        String actual = adminOne.toString();
//        String expected = "Admin(super=User(id=0, firstName=A, lastName=B, email=email@domain.com, " +
//                "phoneNumber=+375291001010, password=password123), position=OPERATOR, city=MINSK)";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void testEqualsPositive() {
//        boolean actual = adminOne.equals(adminOneClone);
//        assertTrue(actual);
//    }
//
//    @Test
//    public void testEqualsNegative() {
//        boolean actual = adminOne.equals(adminTwo);
//        assertFalse(actual);
//    }
//
//    @Test
//    public void testHashCodePositive() {
//        assertEquals(adminOne.hashCode(), adminOneClone.hashCode());
//    }
//
//    @Test
//    public void testHashCodeNegative() {
//        assertNotEquals(adminOne.hashCode(), adminTwo.hashCode());
//    }
//
//    @Override
//    @Test
//    public void getId() {
//        long actual = adminOne.getId();
//        long expected = 0;
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getFirstName() {
//        String actual = adminOne.getFirstName();
//        String expected = "A";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getLastName() {
//        String actual = adminOne.getLastName();
//        String expected = "B";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getEmail() {
//        String actual = adminOne.getEmail();
//        String expected = "email@domain.com";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getPhoneNumber() {
//        String actual = adminOne.getPhoneNumber();
//        String expected = "+375291001010";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void getPassword() {
//        String actual = adminOne.getPassword();
//        String expected = "password123";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void getPosition() {
//        AdminPosition actual = adminOne.getPosition();
//        AdminPosition expected = AdminPosition.OPERATOR;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void getCity() {
//        City actual = adminOne.getCity();
//        City expected = City.MINSK;
//        assertEquals(expected, actual);
//    }
//
//   /* @Override
//    @Test
//    public void setId() {
//        adminOne.setId(1);
//        long actual = adminOne.getId();
//        long expected = 1;
//        assertEquals(expected, actual);
//    }*/
//
//    @Override
//    @Test
//    public void setFirstName() {
//        adminOne.setFirstName("FOO");
//        String actual = adminOne.getFirstName();
//        String expected = "FOO";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void setLastName() {
//        adminOne.setLastName("BAR");
//        String actual = adminOne.getLastName();
//        String expected = "BAR";
//        assertEquals(expected, actual);
//    }
//
//    @Override
//    @Test
//    public void setEmail() {
//        adminOne.setEmail("foo-bar@domain.com");
//        String actual = adminOne.getEmail();
//        String expected = "foo-bar@domain.com";
//        assertEquals(actual, expected);
//    }
//
//    @Override
//    @Test
//    public void setPhoneNumber() {
//        adminOne.setPhoneNumber("+375293003030");
//        String actual = adminOne.getPhoneNumber();
//        String expected = "+375293003030";
//        assertEquals(actual, expected);
//    }
//
//    @Override
//    @Test
//    public void setPassword() {
//        adminOne.setPassword("foobarbaz123");
//        String actual = adminOne.getPassword();
//        String expected = "foobarbaz123";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void setPosition() {
//        adminOne.setPosition(AdminPosition.MANAGER);
//        AdminPosition actual = adminOne.getPosition();
//        AdminPosition expected = AdminPosition.MANAGER;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void setCity() {
//        adminOne.setCity(City.GRODNO);
//        City actual = adminOne.getCity();
//        City expected = City.GRODNO;
//        assertEquals(expected, actual);
//    }
//}