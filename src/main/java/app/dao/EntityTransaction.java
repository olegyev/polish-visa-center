package app.dao;

import app.util.ConnectionPool;
import app.util.DbConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class EntityTransaction {
    private Connection connection;

    public void begin(AbstractDao dao, AbstractDao ... daos) {
        if (connection == null) {
            // connection = DbConnection.getConnection();
            connection = ConnectionPool.getInstance().getConnection();
        }

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dao.setConnection(connection);

        for (AbstractDao daoElement : daos) {
            daoElement.setConnection(connection);
        }
    }

    public void end() {
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        connection = null;
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}