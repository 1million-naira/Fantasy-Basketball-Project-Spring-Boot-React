package org.example.newtopsquadproject.Model.DTO;

public class PlayerDTO {
    private int id;
    private String image;
    private String name;
    private String pos;

    private int points;

    private int value;

    private boolean bench;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isBench() {
        return bench;
    }

    public void setBench(boolean bench) {
        this.bench = bench;
    }


}
