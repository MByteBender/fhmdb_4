package at.ac.fhcampuswien.fhmdb.observer;

import java.util.ArrayList;
import java.util.List;

public interface Observable {
    List<Observer> observers = new ArrayList<>();
    void addObserver(Observer observer);
    void updateObserver(ObservableMessages message);
}
