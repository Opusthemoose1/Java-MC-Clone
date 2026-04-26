package minecraft;

import java.util.HashMap;
import java.util.TreeMap;

public enum Material {

    AIR(0, null, false),
    DIRT(1, "dirt", true),
    COBBLESTONE(2, "cobblestone", true);

    private final byte id;
    private final String textureName;
    private final boolean solid;

    private static final TreeMap<Byte, Material> idMap = new TreeMap<>();

    static {
        for (Material material : Material.values()) {
            idMap.put(material.getId(), material);
        }
    }

    Material(int id, String textureName, boolean solid) {
        this.id = (byte) id;
        this.textureName = textureName;
        this.solid = solid;
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

    public static Material fromId(byte id) {
        return idMap.get(id);
    }

    public static TreeMap<Byte, Material> getIdMap() {
        return idMap;
    }

}
