// Klasse zur Verwaltung der Datenbankverbindung und der DAOs
package at.ac.fhcampuswien.fhmdb.database;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:h2:file:./db/fhmdb"; // in memory: jdbc:h2:mem:fhmdb
    public static final String user = "admin";
    public static final String pass = "pass";

    private static ConnectionSource connectionSource;
    private static DatabaseManager instance;

    private final Dao<WatchlistMovieEntity, Long> watchlistMovieDao;

    // Konstruktor (privat, um das Singleton-Muster zu implementieren)
    private DatabaseManager() throws DataBaseException {
        try {
            createConnectionSource();
            watchlistMovieDao = DaoManager.createDao(connectionSource, WatchlistMovieEntity.class);
            createTables();
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    // Methode zur Rückgabe der Singleton-Instanz der Klasse
    public static DatabaseManager getInstance() throws DataBaseException {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    // Methode zur Rückgabe der ConnectionSource
    public static ConnectionSource getConnectionSource() throws DataBaseException {
        if (connectionSource == null) {
            createConnectionSource();
        }
        return connectionSource;
    }

    // Erstellt eine neue ConnectionSource zur Datenbank mit url dem username und passwort
    private static void createConnectionSource() throws DataBaseException {
        try {
            connectionSource = new JdbcConnectionSource(DB_URL, user, pass);
        } catch (SQLException e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    // Schließt die Datenbankverbindung
    public static void closeConnectionSource() throws DataBaseException {
        if(connectionSource != null){
            try {
                connectionSource.close();
            } catch (Exception e) {
                e.printStackTrace();
                throw new DataBaseException(e.getMessage());
            }
        }
    }

    // Erstellt die Tabellen in der Datenbank (falls sie nicht vorhanden sind)
    private static void createTables() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, WatchlistMovieEntity.class);
    }

    // Entfernt die Tabellen aus der Datenbank
    private static void dropTables() throws SQLException {
        TableUtils.dropTable(connectionSource, WatchlistMovieEntity.class, true);
    }

    // Gibt das DAO für die WatchlistMovieEntity zurück
    public Dao<WatchlistMovieEntity, Long> getWatchlistDao() {
        return watchlistMovieDao;
    }
}
