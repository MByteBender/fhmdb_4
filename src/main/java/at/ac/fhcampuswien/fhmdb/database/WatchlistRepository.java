package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.observer.Observable;
import at.ac.fhcampuswien.fhmdb.observer.ObservableMessages;
import at.ac.fhcampuswien.fhmdb.observer.Observer;
import com.j256.ormlite.dao.Dao;

import java.util.List;

public class WatchlistRepository implements Observable {
    private static WatchlistRepository instance;


    Dao<WatchlistMovieEntity, Long> dao;

    private WatchlistRepository() throws DataBaseException {
        try {
            this.dao = DatabaseManager.getInstance().getWatchlistDao();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }



    // TODO Singelton patern
    public static WatchlistRepository getInstance() throws DataBaseException {
        if(instance == null)
        {
            instance = new WatchlistRepository();
        }

        return instance;
    }

    public List<WatchlistMovieEntity> readWatchlist() throws DataBaseException {
        try {
            return dao.queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while reading watchlist");
        }
    }
    public void addToWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            // only add movie if it does not exist yet
            long count = dao.queryBuilder().where().eq("apiId", movie.getApiId()).countOf();
            if (count == 0) {
                dao.create(movie);
                //TODO update observer and setting observable messages
                updateObserver(ObservableMessages.ADDED);
            } else {
                updateObserver(ObservableMessages.ALREADY_EXISTS);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataBaseException("Error while adding to watchlist");
        }
    }

    public void removeFromWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            dao.delete(movie);
        } catch (Exception e) {
            throw new DataBaseException("Error while removing from watchlist");
        }
    }

    public boolean isOnWatchlist(WatchlistMovieEntity movie) throws DataBaseException {
        try {
            return dao.queryForMatching(movie).size() > 0;
        } catch (Exception e) {
            throw new DataBaseException("Error while checking if movie is on watchlist");
        }
    }

    // TODO add observer pattern add method
    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }



    @Override
    public void updateObserver(ObservableMessages message) {
        for(Observer observer : this.observers){
            observer.update(message);
        }
    }
}
