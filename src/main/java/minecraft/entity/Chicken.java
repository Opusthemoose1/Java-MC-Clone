package minecraft.entity;

import minecraft.chunk.Location;

public class Chicken extends Entity {

    static final float INITIAL_HEALTH = 3.5f, WEIGHT = 0.2f;

    public Chicken(Location location) {
        super(location, INITIAL_HEALTH);
    }

    @Override
    public float getWeight() {
        return WEIGHT;
    }

}
