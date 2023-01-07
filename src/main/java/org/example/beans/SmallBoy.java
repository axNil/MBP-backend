package org.example.beans;

public class SmallBoy {
    public int id = 0;
    public String name = "";
    public String details = "";
    SmallBoy() {}
    @Override
    public String toString() {
        return String.format("%s, %s, %d", name, details, id);
    }
}
