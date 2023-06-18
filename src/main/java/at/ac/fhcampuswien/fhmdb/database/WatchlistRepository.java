// Klasse zur Verwaltung einer Watchlist
package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.observer.Observable;
import at.ac.fhcampuswien.fhmdb.observer.ObservableMessages;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class WatchlistRepository implements Observable {
    private static WatchlistRepository instance;

    Dao<WatchlistMovieEntity, Long> dao;

    // Konstruktor (privat, um das Singleton-Muster zu implementieren)
    private WatchlistRepository() throws DataBaseException {
        try {
            // Initialisiere das DAO für die WatchlistMovieEntity
            this.dao = DatabaseManager.getInstance().getWatchlistDao();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    // Methode zur Rückgabe der Singleton-Instanz der Klasse
    public static WatchlistRepository getInstance() throws DataBaseException {
        if(instance == null) {
            instance = new WatchlistRepository();
        }
        return instance;
    }

    // Liest alle Einträge aus der Watchlist
    public List<WatchlistMovieEntity> readWatchlist() throws DataBaseException {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while reading watchlist");
        }
    }

    // Fügt einen Film zur Watchlist hinzu
    public void addToWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            // Füge den Film nur hinzu, wenn er noch nicht existiert
            long count = dao.queryBuilder().where().eq("apiId", movie.getApiId()).countOf();
            if (count == 0) {
                dao.create(movie);
                // Aktualisiere den Observer und sende eine hinzugefügt-Nachricht
                updateObserver(ObservableMessages.ADDED);
            } else {
                updateObserver(ObservableMessages.ALREADY_EXISTS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while adding to watchlist");
        }
    }

    // Entfernt einen Film von der Watchlist
    public void removeFromWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            dao.delete(movie);
        } catch (Exception e) {
            throw new DataBaseException("Error while removing from watchlist");
        }
    }

    // Überprüft, ob ein Film in der Watchlist enthalten ist
    public boolean isOnWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            return dao.queryForMatching(movie).size() > 0;
        } catch (Exception e) {
            throw new DataBaseException("Error while checking if movie is on watchlist");
        }
    }

    // Implementierung des Observer-Patterns (Hinzufügen eines Observers)
    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    // Aktualisiert alle Observer mit einer bestimmten Nachricht
    @Override
    public void updateObserver(ObservableMessages message) {
        for(Observer observer : this.observers){
            observer.update(message);
        }
    }
}
