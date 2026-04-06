package minecraft.block;

public enum Material {

    AIR(0, null),
    DIRT(1, "dirt"),
    COBBLESTONE(2, "cobblestone");

    private final byte id;
    private final String textureName;

    Material(int id, String textureName) {
        this.id = (byte) id;
        this.textureName = textureName;
    }

    public byte getId() {
        return id;
    }

    public String getTextureFileName() {
        return textureName;
    }

}
