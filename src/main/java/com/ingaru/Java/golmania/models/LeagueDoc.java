package com.ingaru.Java.golmania.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "leagues")
public class LeagueDoc {
    @Id
    private Long id;
    private String name;
    private String type;
    private String logo;
    private String country;
    private Integer season;
    private String start;
    private String end;
    private boolean current;

    public LeagueDoc() {}

    public LeagueDoc(Long id, String name, String type, String logo,
                     String country, Integer season, String start, String end, boolean current) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.logo = logo;
        this.country = country;
        this.season = season;
        this.start = start;
        this.end = end;
        this.current = current;
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLogo() { return logo; }
    public void setLogo(String logo) { this.logo = logo; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Integer getSeason() { return season; }
    public void setSeason(Integer season) { this.season = season; }

    public String getStart() { return start; }
    public void setStart(String start) { this.start = start; }

    public String getEnd() { return end; }
    public void setEnd(String end) { this.end = end; }

    public boolean isCurrent() { return current; }
    public void setCurrent(boolean current) { this.current = current; }
}

