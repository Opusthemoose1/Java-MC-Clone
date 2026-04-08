package minecraft.chunk.location;

public interface YawPitchPublisher {

    void attach(YawPitchObserver observer);

    void detach(YawPitchObserver observer);

}
