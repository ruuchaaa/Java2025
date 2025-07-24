package com.ap.GUITAR.model;

public class GuitarSpec {
    private Builder builder;
    private String model;
    private Type type;
    private Wood backWood;
    private Wood topWood;
    private int numStrings;

    public GuitarSpec(Builder builder, String model, Type type, Wood backWood, Wood topWood, int numStrings) {
        this.builder = builder;
        this.model = model;
        this.type = type;
        this.backWood = backWood;
        this.topWood = topWood;
        this.numStrings = numStrings;
    }

    public Builder getBuilder() { return builder; }
    public String getModel() { return model; }
    public Type getType() { return type; }
    public Wood getBackWood() { return backWood; }
    public Wood getTopWood() { return topWood; }
    public int getNumStrings() { return numStrings; }
}
