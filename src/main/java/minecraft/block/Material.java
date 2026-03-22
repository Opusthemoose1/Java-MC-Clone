package minecraft.block;

public enum Material {

    AIR(0, null),
    COBBLESTONE(1, "cobblestone"),
    DIRT(2, "dirt");

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
