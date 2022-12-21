package org.example.beans;

public class UnicornInfo {
    public String color = "";
    public String horn = "";
    public String reportedBy = "";
    public Location spottedWhere = new Location();
    public String spottedWhen = "";
    public String behaviour = "";

    public UnicornInfo() {}
    public UnicornInfo(String color, String horn, double lon, double lat, String locname, String behaviour) {
        this.color = color;
        this.horn = horn;
        spottedWhere = new Location();
        spottedWhere.lon = lon;
        spottedWhere.lat = lat;
        spottedWhere.name = locname;
        this.behaviour = behaviour;
    }
}
