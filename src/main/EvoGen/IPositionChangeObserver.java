package EvoGen;

public interface IPositionChangeObserver {
    void positionChanged(Animal animal, Vector2d oldPosition);
}
