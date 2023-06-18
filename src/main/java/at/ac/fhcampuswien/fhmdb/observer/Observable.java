package at.ac.fhcampuswien.fhmdb.observer;

import java.util.ArrayList;
import java.util.List;

public interface Observable {
    //statische liste alle die Observable implementieren haben dieselbe Liste
    List<Observer> observers = new ArrayList<>();
    void addObserver(Observer observer);
    void updateObserver(ObservableMessages message);
}
