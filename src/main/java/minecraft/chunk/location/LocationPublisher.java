package minecraft.chunk.location;

public interface LocationPublisher {

    void attachLocationObserver(LocationObserver observer);

    void detachLocationObserver(LocationObserver observer);

}
