package minecraft.timer;

public interface ITimer {

    long getEnd();

    void startTimer();

    double getTimeInSeconds();

    void reset();

}
