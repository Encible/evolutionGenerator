package EvoGen;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public Vector2d toVector() {
        switch (this) {
            case NORTH:     return new Vector2d(0,1);
            case NORTHEAST: return new Vector2d(1,1);
            case EAST:      return new Vector2d(1,0);
            case SOUTHEAST: return new Vector2d(1,-1);
            case SOUTH:     return new Vector2d(0,-1);
            case SOUTHWEST: return new Vector2d(-1,-1);
            case WEST:      return new Vector2d(-1,0);
            case NORTHWEST: return new Vector2d(-1,1);
            default:        return null;
        }
    }

    public int toInt() {
        return this.ordinal();
    }

    public static MapDirection intToDir(int num) {
        return MapDirection.values()[num];
    }

    public static Vector2d intToVec (int num) {
        return MapDirection.values()[num].toVector();
    }
}
