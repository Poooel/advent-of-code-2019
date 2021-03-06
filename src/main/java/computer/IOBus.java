package computer;

import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class IOBus {
    private final static int IO_BUS_SIZE = 1_000;

    private BlockingQueue<Long> bus;

    public IOBus() {
        this.bus = new ArrayBlockingQueue<>(IO_BUS_SIZE);
    }

    @SneakyThrows
    public void put(long value) {
        bus.put(value);
    }

    @SneakyThrows
    public long take() {
        return bus.take();
    }

    public Long poll() {
        return bus.poll();
    }

    @SneakyThrows
    public Long poll(long timeout, TimeUnit timeUnit) {
        return bus.poll(timeout, timeUnit);
    }

    public void clear() {
        bus.clear();
    }
}
