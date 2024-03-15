import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
public class MyHost extends Host {
    private volatile Task inRun;
    private volatile boolean run;
    private final PriorityQueue<Task> waitList;
    private final Lock lock;

    public MyHost() {
        waitList = new PriorityQueue<>(Comparator.comparingInt(Task::getPriority).reversed());
        lock = new ReentrantLock();
    }

    @Override
    public void run() {
        run = true;
        while (run) {
            processTasks();
        }
    }

    private void processTasks() {
        if (!waitList.isEmpty() || inRun != null) {
            if (inRun == null) {
                inRun = waitList.poll();
            }
            if (inRun.isPreemptible()) {
                preemptTask();
            }

            try {
                lock.lock();
                handleInterruptedException();
                lock.unlock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (inRun.getLeft() == 0) {
                inRun.finish();
                inRun = null;
            }
        }
    }
    private void handleInterruptedException() throws InterruptedException {
        inRun.setLeft(inRun.getLeft() - 500L);
        Thread.sleep(500L);
    }

    private void preemptTask() {
        if (!waitList.isEmpty() && waitList.peek().getPriority() > inRun.getPriority()) {
            waitList.add(inRun);
            inRun = waitList.poll();
        }
    }

    @Override
    public void addTask(Task task) {
        waitList.add(task);
    }

    @Override
    public int getQueueSize() {
        return waitList.size() + (inRun != null ? 1 : 0);
    }

    @Override
    public long getWorkLeft() {
        long sum = waitList.stream().mapToLong(Task::getLeft).sum();
        if (inRun != null) {
            sum += inRun.getLeft();
        }
        return sum;
    }

    @Override
    public void shutdown() {
        run = false;
    }
}
