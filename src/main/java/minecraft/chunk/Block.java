package minecraft.chunk;

import minecraft.Material;
import minecraft.chunk.location.Location;
import minecraft.math.IVector;
import minecraft.math.Vector;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class Block {

    public enum BlockFace {

        UP(0, 1, 0, 1, 0, 0, 0, 0, 1),
        DOWN(0, -1, 0, 1, 0, 0, 0, 0, 1),
        NORTH(0, 0, -1, 0, 1, 0, 1, 0, 0),
        SOUTH(0, 0, 1, 0, 1, 0, 1, 0, 0),
        EAST(1, 0, 0, 0, 1, 0, 1, 0, 0),
        WEST(-1, 0, 0, 0, 1, 0, 1, 0, 0);

        private final int offsetX, offsetY, offsetZ,
                basis1X, basis1Y, basis1Z,
                basis2X, basis2Y, basis2Z;
        BlockFace(int offsetX, int offsetY, int offsetZ, int basis1X, int basis1Y, int basis1Z, int basis2X, int basis2Y, int basis2Z) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.basis1X = basis1X;
            this.basis1Y = basis1Y;
            this.basis1Z = basis1Z;
            this.basis2X = basis2X;
            this.basis2Y = basis2Y;
            this.basis2Z = basis2Z;
        }

        public IVector getOffsetVector() {
            return Vector.createVector(offsetX, offsetY, offsetZ);
        }

        IVector getBasisVector1() {
            return Vector.createVector(basis1X, basis1Y, basis1Z);
        }

        IVector getBasisVector2() {
            return Vector.createVector(basis2X, basis2Y, basis2Z);
        }
    }

    private final Location location;
    private final ChunkBlock chunkBlock;
    private final IChunkLoader chunkLoader;

    public Block(Location location, ChunkBlock chunkBlock, IChunkLoader chunkLoader) {
        this.chunkBlock = chunkBlock;
        this.chunkLoader = chunkLoader;
        this.location = location.getBlockLocation();
    }

    public Material getMaterial() {
        return chunkBlock.getMaterial();
    }

    public Location getLocation() {
        return location.clone();
    }

    public void setType(Material type) {
        chunkLoader.setBlock(location, type);
    }

    public Location getRelativeLocation(BlockFace face) {
        return location.clone().add(face.getOffsetVector());
    }

    public BlockFace getBlockFaceLookingAt(Location eyeLocation) {
        IVector eye = eyeLocation.toVector();
        IVector direction = eyeLocation.getDirection();

        double bestDistance = Double.MAX_VALUE;
        BlockFace bestFace = null;
        for (BlockFace face : BlockFace.values()) {
            double distance = getDistanceToFace(face, eye, direction);
            if (distance < 0) continue; //behind head
            if (distance < bestDistance) {
                bestDistance = distance;
                bestFace = face;
            }
        }

        return bestFace;
    }

    //Solve (FaceCenter) + a(basis1) + b(basis2) = c(direction) + (EyeLocation)
    //Code adopted from one of my past projects: https://github.com/Unfaxed/dexterity/blob/main/src/me/c7dev/dexterity/interaction/DexClickDetector.java
    private float getDistanceToFace(BlockFace face, IVector eyeLocation, IVector direction) {
        float distanceFromCenterOfBlockToFace = 0.5f;
        IVector basis1 = face.getBasisVector1();
        IVector basis2 = face.getBasisVector2();
        IVector faceCenter = location.toVector()
                .add(distanceFromCenterOfBlockToFace, distanceFromCenterOfBlockToFace, distanceFromCenterOfBlockToFace)
                .add(face.getOffsetVector().multiply(distanceFromCenterOfBlockToFace));
        IVector eyeMinusFaceCenter = eyeLocation.clone().subtract(faceCenter);

        Matrix3f matrix = new Matrix3f(
                basis1.getX(), basis1.getY(), basis1.getZ(),
                basis2.getX(), basis2.getY(), basis2.getZ(),
                direction.getX(), direction.getY(), direction.getZ()
        );
        matrix.invert();
        Vector3f jomlSolution = matrix.transform(new Vector3f(eyeMinusFaceCenter.getX(), eyeMinusFaceCenter.getY(), eyeMinusFaceCenter.getZ()));

        double basis1Coefficient = jomlSolution.x; //a
        double basis2Coefficient = jomlSolution.y; //b

        if (isOutOfFace(basis1Coefficient) || isOutOfFace(basis2Coefficient)) return -1;
        return -jomlSolution.z; //c
    }

    private boolean isOutOfFace(double basisCoefficient) {
        float maximumCoefficient = 0.5f; //half of block side length, or distance from center of face to the edge of the block
        return Math.abs(basisCoefficient) > maximumCoefficient;
    }
}
