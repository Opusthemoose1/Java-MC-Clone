package minecraft.block;

public enum Material {

    COBBLESTONE("cobblestone.png");

    private final String textureName;

    Material(String textureName) {
        this.textureName = textureName;
    }

    public String getTextureFileName() {
        return textureName;
    }

}
