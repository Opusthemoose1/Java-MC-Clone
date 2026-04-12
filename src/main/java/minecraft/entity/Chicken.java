package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.location.Location;

public class Chicken extends Entity {

    static final float INITIAL_HEALTH = 3.5f, WEIGHT = 0.2f, HEIGHT = 0.8f;

    public Chicken(Location location, WorldContext context) {
        super(location, INITIAL_HEALTH, context);
    }

    @Override
    public float getWeight() {
        return WEIGHT;
    }

    @Override
    protected void takeFallDamage(float y) {
        //nothing; chicken lands with wings
    }

    @Override
    public float getHeight() {
        return HEIGHT;
    }

}
