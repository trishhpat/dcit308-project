package model;

public class User {
    private String name;
    private String pin;

    // Constructors, getters, and setters

    public User(String name, String pin) {
        this.name = name;
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public boolean isDefaultPassword(String username, String pin){
        if(username.equals(this.name) && pin.equals(this.pin)){
            return true;
        }
        else return false;
    }
}
