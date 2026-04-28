package minecraft.timer;

public class Timer implements ITimer {
    private long start;
    private long end = -1;

    public Timer() {
        start = 0;
    }

    public long getEnd() {
        return end == -1 ? System.nanoTime() : end;
    }

    public void startTimer()
    {
        start = System.nanoTime();
    }

//    public void endTimer()
//    {
//        end = System.nanoTime();
//    }
//
//    public double getTimeInMilliseconds()
//    {
//        return (getEnd() - this.start) / 1_000_000.0;
//    }

    public double getTimeInSeconds()
    {
        return (getEnd() - this.start) / 1_000_000_000.0;
    }

    public void reset() {
        end = -1;
        startTimer();
    }
}
