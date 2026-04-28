package minecraft;

import minecraft.timer.ITimer;
import minecraft.timer.ITimerFactory;

public class TestTimerFactory implements ITimerFactory {

    @Override
    public ITimer createTimer() {
        return new TestTimer();
    }
}
