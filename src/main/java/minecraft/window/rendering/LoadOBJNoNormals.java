package minecraft.window.rendering;


import minecraft.Minecraft;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

// Load an OBJ file without normals
public class LoadOBJNoNormals implements  IOBJLoader {
    public IMesh loadFile(String file_path) {
        List<Vertex> vertices = new ArrayList<>();
        List<Float> positions = new ArrayList<>();
        List<Float> textureCoords = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();

        Map<String, Integer> faceMap = new HashMap<>();

        File myObj = new File(file_path);
        // try-with-resources: Scanner will be closed automatically
        try (Scanner myReader = new Scanner(myObj)) {
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine().trim(); // moved inside the loop

                if (data.startsWith("v ")) {
                    // Vertex position: "v x y z"
                    String[] parts = data.split("\\s+");
                    positions.add(Float.parseFloat(parts[1]));
                    positions.add(Float.parseFloat(parts[2]));
                    positions.add(Float.parseFloat(parts[3]));

                } else if (data.startsWith("vt ")) {
                    // Texture coordinate: "vt u v"
                    String[] parts = data.split("\\s+");
                    textureCoords.add(Float.parseFloat(parts[1]));
                    textureCoords.add(Float.parseFloat(parts[2]));

                } else if (data.startsWith("f ")) {
                    // Face: "f v1/vt1 v2/vt2 v3/vt3" (triangulated, so always 3 pairs)
                    String[] parts = data.split("\\s+");
                    for (int i = 1; i <= 3; i++) {
                        String[] indices_pair = parts[i].split("/");
                        // .obj indices are 1-based
                        int posIndex = Integer.parseInt(indices_pair[0]) - 1;
                        int texIndex = Integer.parseInt(indices_pair[1]) - 1;
                        String key = posIndex + "/" + texIndex;
                        // Check if the vertex already exists. If it does, add that index to the indices
                        if (faceMap.containsKey(key)) {
                            indices.add(faceMap.get(key));
                        } else { // Vertex doesn't exist in the map, so create a new index (current size of the vertex list) and add the new index
                            int newIndex = vertices.size();
                            faceMap.put(key, newIndex);
                            vertices.add(new Vertex(positions.get(posIndex * 3),
                                    positions.get(posIndex * 3 + 1),
                                    positions.get(posIndex * 3 + 2),
                                    textureCoords.get(texIndex * 2),
                                    textureCoords.get(texIndex * 2 + 1)));
                            indices.add(newIndex);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            Minecraft.getLogger().error("File not found");
            return null;
        }
        return new MeshNoNormals(vertices, indices);
    }
}
