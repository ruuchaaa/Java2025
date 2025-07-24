package com.ap.GUITAR.model;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Guitar> guitars;

    public Inventory() {
        guitars = new ArrayList<>();
    }

    public void addGuitar(String serialNumber, double price,
                          Builder builder, String model, Type type,
                          Wood backWood, Wood topWood, int numStrings) {
        GuitarSpec spec = new GuitarSpec(builder, model, type, backWood, topWood, numStrings);
        Guitar guitar = new Guitar(serialNumber, price, spec);
        guitars.add(guitar);
    }

    public List<Guitar> search(GuitarSpec searchSpec, Double minPrice, Double maxPrice) {
        List<Guitar> matchingGuitars = new ArrayList<>();
        for (Guitar guitar : guitars) {
            GuitarSpec spec = guitar.getSpec();

            if (searchSpec.getBuilder() != null && !searchSpec.getBuilder().equals(spec.getBuilder()))
                continue;
            if (searchSpec.getModel() != null && !searchSpec.getModel().equalsIgnoreCase(spec.getModel()))
                continue;
            if (searchSpec.getType() != null && !searchSpec.getType().equals(spec.getType()))
                continue;
            if (searchSpec.getBackWood() != null && !searchSpec.getBackWood().equals(spec.getBackWood()))
                continue;
            if (searchSpec.getTopWood() != null && !searchSpec.getTopWood().equals(spec.getTopWood()))
                continue;
            if (searchSpec.getNumStrings() != 0 && searchSpec.getNumStrings() != spec.getNumStrings())
                continue;
            if (minPrice != null && guitar.getPrice() < minPrice)
                continue;
            if (maxPrice != null && guitar.getPrice() > maxPrice)
                continue;

            matchingGuitars.add(guitar);
        }
        return matchingGuitars;
    }
}

