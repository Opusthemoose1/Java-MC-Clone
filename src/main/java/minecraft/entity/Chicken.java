package minecraft.entity;

import minecraft.WorldContext;
import minecraft.chunk.location.Location;

public class Chicken extends Entity {

    static final float INITIAL_HEALTH = 3.5f, WEIGHT = 0.2f;

    public Chicken(Location location, WorldContext context) {
        super(location, INITIAL_HEALTH, context);
    }

    @Override
    public float getWeight() {
        return WEIGHT;
    }

    @Override
    public void takeFallDamage(float y) {
        //nothing; chicken lands with wings
    }

}
