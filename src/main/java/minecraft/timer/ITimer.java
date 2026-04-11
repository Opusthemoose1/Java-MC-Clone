package minecraft.timer;

public interface ITimer {

    long getEnd();

    void startTimer();

    void endTimer();

    double getTimeInMilliseconds();

    double getTimeInSeconds();

    void reset();

}
