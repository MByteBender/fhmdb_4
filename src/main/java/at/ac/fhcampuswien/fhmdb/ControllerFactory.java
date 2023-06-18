package at.ac.fhcampuswien.fhmdb;

import javafx.util.Callback;

public class ControllerFactory implements Callback<Class<?>, Object> {



    private static ControllerFactory instance;

    private ControllerFactory() {

    }

    public static ControllerFactory getInstance() {
        if (instance == null) {
            instance = new ControllerFactory();
        }
        return instance;
    }

    @Override
    //returns singleton instance of controller, because it is not possible to implement singleton in javafx
    public Object call(Class<?> aClass) {
        try{
            return aClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}