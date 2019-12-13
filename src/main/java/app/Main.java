package app;

import app.dao.AdminDao;
import app.dao.ClientDao;
import app.dao.EntityTransaction;
import app.entities.Admin;
import app.entities.Client;
import app.entities.enums.AdminPosition;
import app.entities.enums.ClientOccupation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ParseException {
        /*ClientDao dao = new ClientDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);
        Client client = new Client("LALITA", "PETROVA", new SimpleDateFormat("yyyy-MM-dd").parse("2018-12-12"), ClientOccupation.UNEMPLOYED, "lola@gmail.com",
                "+375296667788", "9e1370e430a1bba9bf5aa11fe6ffe443", true);

        Client client1 = dao.authenticate("ss", "f9139040e8c2fefcede1d4e35aa71c08");

        Client client1 = dao.readByEmail("lola@gmail.com");
        client1.setLastName("ISHINA");
        client1 = dao.update(client1);

        List<Client> clients = dao.readAll();
        for (Client client : clients) {
            System.out.println(client);
        }

        System.out.println(client1);

        transaction.commit();
        transaction.end();*/




        /*Admin admin = new Admin("ISIDOR", "PAULIUSHCHYK", AdminPosition.OPERATOR,
                "a.zharko@visacenter.by", "+375299876543", "qwertyuiop");

        Admin adminTwo = new Admin("ANATOL", "SIERDZIUKOU", AdminPosition.MANAGER,
                "v.zaycava@visacenter.by", "+375296001122", "qazwsxedc");

        AdminDao dao = new AdminDao();
        EntityTransaction transaction = new EntityTransaction();
        transaction.begin(dao);

        boolean flag = dao.create(adminTwo);

        Admin authent = dao.authenticate("a.zharko@visacenter.by", "qwertyuiop");
        System.out.println(flag);
        System.out.println(adminTwo);
        System.out.println(authent);

        transaction.commit();
        transaction.end();*/
    }
}