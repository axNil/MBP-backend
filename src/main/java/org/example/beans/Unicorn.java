package org.example.beans;

import java.sql.Timestamp;
public class Unicorn {
    public int id = 0;
    public String name = "";
    public String description = "";
    public String reportedBy = "";
    public Location spottedWhere = new Location();
    public Timestamp spottedWhen = new Timestamp(0);
    public String image = "";

    public Unicorn() {}
}
