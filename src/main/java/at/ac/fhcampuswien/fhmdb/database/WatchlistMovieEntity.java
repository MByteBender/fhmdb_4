// Klasse zur Repräsentation eines Films in der Watchlist-Datenbank
package at.ac.fhcampuswien.fhmdb.database;

import at.ac.fhcampuswien.fhmdb.models.Genre;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Arrays;
import java.util.List;

@DatabaseTable(tableName = "watchlist") // Angabe der Datenbanktabelle
public class WatchlistMovieEntity {
    @DatabaseField(generatedId = true) // Automatisch generierte ID
    private long id;

    @DatabaseField(canBeNull = false) // Nicht null-Wert erforderlich
    private String apiId;

    @DatabaseField(canBeNull = false) // Nicht null-Wert erforderlich
    private String title;

    @DatabaseField() // Optional
    private String description;

    @DatabaseField() // Optional
    private String genres;

    @DatabaseField() // Optional
    private int releaseYear;

    @DatabaseField() // Optional
    private String imgUrl;

    @DatabaseField() // Optional
    private int lengthInMinutes;

    @DatabaseField() // Optional
    private double rating;

    public WatchlistMovieEntity(){} // Standardkonstruktor

    // Konstruktor mit Parametern
    public WatchlistMovieEntity(String apiId, String title, String description, int releaseYear, List<Genre> genres, String imgUrl, int lengthInMinutes, double rating) {
        this.apiId = apiId;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.genres = genresToString(genres);
        this.imgUrl = imgUrl;
        this.lengthInMinutes = lengthInMinutes;
        this.rating = rating;
    }

    // Hilfsmethode zur Konvertierung der Genres-Liste in einen String
    private String genresToString(List<Genre> genres) {
        StringBuilder sb = new StringBuilder();
        for (Genre genre : genres) {
            sb.append(genre.name());
            sb.append(",");
        }
        return sb.toString();
    }

    // Getter-Methoden
    public long getId() {
        return id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    // Überschreiben der toString() Methode für die einfache Darstellung des Objekts
    @Override
    public String toString() {
        return "Movie [id=" + id + ", apiId=" + apiId + ", title=" + title + ", description=" + description + ", releaseYear=" + releaseYear + "]";
    }

    // Konvertiert den genres-String in eine Liste von Genre-Objekten
    public List<Genre> getGenres() {
        return Arrays.stream(genres.split(",")).map(Genre::valueOf).toList();
    }

    // Setzt die genres-Eigenschaft auf Basis einer Liste von Genre-Objekten
    public void setGenres(List<Genre> genres) {
        this.genres = genresToString(genres);
    }

    // Gibt die Länge des Films formatiert als Zeichenkette zurück
    public String getLengthInMinutes() {
        return lengthInMinutes + " min";
    }

    // Gibt die Bewertung des Films formatiert als Zeichenkette zurück
    public String getRating() {
        return rating + "/10";
    }
}
