package minecraft.chunk.location;

public interface YawPitchPublisher {

    void attachYawPitchObserver(YawPitchObserver observer);

    void detachYawPitchObserver(YawPitchObserver observer);

}
