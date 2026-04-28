package minecraft;

import java.util.HashMap;
import java.util.TreeMap;

public enum Material {

    AIR(0, null, false, false),
    DIRT(1, "dirt", true, true),
    COBBLESTONE(2, "cobblestone", true, true),
    GRASS(3, "grass", true, true),
    STONE(4, "stone", true, true),
    BEDROCK(5, "bedrock", true, false);

    private final byte id;
    private final String textureName;
    private final boolean solid, breakable;

    private static final TreeMap<Byte, Material> idMap = new TreeMap<>();

    static {
        for (Material material : Material.values()) {
            idMap.put(material.getId(), material);
        }
    }

    Material(int id, String textureName, boolean solid, boolean breakable) {
        this.id = (byte) id;
        this.textureName = textureName;
        this.solid = solid;
        this.breakable = breakable;
    }

    public byte getId() {
        return id;
    }

    public String getTextureFileName() {
        return textureName;
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isBreakable() {
        return breakable;
    }

    public static Material fromId(byte id) {
        return idMap.get(id);
    }

    public static TreeMap<Byte, Material> getIdMap() {
        return idMap;
    }

}
