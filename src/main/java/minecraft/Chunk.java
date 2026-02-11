package minecraft;

/*
Chunks are specified by their offset from world origin.
Each chunk is made from subchunks, where each subchunk is 16x16x16
Each subchunk contains its own VBO (mesh data). Meshes are constructed based on which block faces are visible
Each block is specified as an offset from the subchunk origin. It's then multiplied in the shader to map blocks to
world space
 */
public class Chunk {
}
