package minecraft.timer;

public class Timer {
    private long start;
    private long end;
    public Timer() {
        start = 0;
        end = 0;
    }
    public void startTimer()
    {
        start = System.nanoTime();
    }
    public void endTimer()
    {
        end = System.nanoTime();
    }
    public double getTimeInNanoseconds()
    {
        return (this.end - this.start);
    }
    public double getTimeInMilliseconds()
    {
        return (this.end - this.start) / 1_000_000.0;
    }
    public double getTimeInSeconds()
    {
        return (this.end - this.start) / 1_000_000_000.0;
    }


}
