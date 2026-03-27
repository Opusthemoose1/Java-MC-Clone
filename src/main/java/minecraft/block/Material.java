package minecraft.block;

public enum Material {

    AIR(0, null),
    DIRT(1, "dirt"),
    COBBLESTONE(2, "cobblestone");

    private final int id;
    private final String textureName;

    Material(int id, String textureName) {
        this.id = id;
        this.textureName = textureName;
    }

    public int getId() {
        return id;
    }

    public String getTextureFileName() {
        return textureName;
    }

}
