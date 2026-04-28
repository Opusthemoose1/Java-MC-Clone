package minecraft.timer;

public class TimerFactory implements ITimerFactory {

    @Override
    public ITimer createTimer() {
        return new Timer();
    }

}
