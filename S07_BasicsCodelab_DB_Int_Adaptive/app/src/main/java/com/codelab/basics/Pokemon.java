package com.codelab.basics;

public class Pokemon {

    private long id;
    private String name;
    private int pokedexNumber;
    private int powerLevel;
    private String description;
    private int accessCount;
    private String imageFileName; // New Field

    // Default constructor
    public Pokemon() {
        this.id = 0;
        this.name = "Unknown";
        this.pokedexNumber = 0;
        this.powerLevel = 0;
        this.description = "";
        this.accessCount = 0;
        this.imageFileName = "pikachu"; // Default image to prevent crashes
    }

    // Full constructor (Now with 7 arguments)
    public Pokemon(long id, String name, int pokedexNumber, int powerLevel, String description, int accessCount, String imageFileName) {
        this.id = id;
        this.name = name;
        this.pokedexNumber = pokedexNumber;
        this.powerLevel = powerLevel;
        this.description = description;
        this.accessCount = accessCount;
        this.imageFileName = imageFileName;
    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", #=" + pokedexNumber +
                ", power=" + powerLevel +
                ", views=" + accessCount +
                ", image='" + imageFileName + '\'' +
                '}';
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPokedexNumber() { return pokedexNumber; }
    public void setPokedexNumber(int pokedexNumber) { this.pokedexNumber = pokedexNumber; }

    public int getPowerLevel() { return powerLevel; }
    public void setPowerLevel(int powerLevel) { this.powerLevel = powerLevel; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getAccessCount() { return accessCount; }
    public void setAccessCount(int accessCount) { this.accessCount = accessCount; }

    public String getImageFileName() { return imageFileName; }
    public void setImageFileName(String imageFileName) { this.imageFileName = imageFileName; }
}