import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyDispatcher extends Dispatcher {

    private int id;
    private final Lock mutex;

    private final Map<String, Integer> taskTypeToHostIndex;

    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
        this.id = 0;
        this.mutex = new ReentrantLock();

        taskTypeToHostIndex = new HashMap<>();
        taskTypeToHostIndex.put("SHORT", 0);
        taskTypeToHostIndex.put("MEDIUM", 1);
        taskTypeToHostIndex.put("LONG", 2);
    }

    @Override
    public void addTask(Task task) {

        switch (algorithm) {
            case ROUND_ROBIN -> roundRobinTaskAssignment(task);
            case SHORTEST_QUEUE -> shortestQueueTaskAssignment(task);
            case SIZE_INTERVAL_TASK_ASSIGNMENT -> sizeIntervalTaskAssignment(task);
            case LEAST_WORK_LEFT -> leastWorkLeftTaskAssignment(task);
            default -> {
            }
        }
    }

    private void roundRobinTaskAssignment(Task task) {
        mutex.lock();
        try {
            hosts.get(id).addTask(task);
            id = (id + 1) % hosts.size();
        } finally {
            mutex.unlock();
        }
    }

    private void shortestQueueTaskAssignment(Task task) {
        mutex.lock();
        try {
            Host minNumber = hosts.stream().min(Comparator.comparingInt(Host::getQueueSize)).orElse(null);
            Objects.requireNonNull(minNumber).addTask(task);
        } finally {
            mutex.unlock();
        }
    }

    private void sizeIntervalTaskAssignment(Task task) {
        mutex.lock();
        try {
            String taskType = task.getType().toString();
            Integer hostIndex = taskTypeToHostIndex.get(taskType);
            if (hostIndex != null) {
                hosts.get(hostIndex).addTask(task);
            }
        } finally {
            mutex.unlock();
        }
    }

    private void leastWorkLeftTaskAssignment(Task task) {
        mutex.lock();
        try {
            Host minNumberLw = hosts.stream().min(Comparator.comparingLong(Host::getWorkLeft)).orElse(null);
            assert minNumberLw != null;
            minNumberLw.addTask(task);
        } finally {
            mutex.unlock();
        }
    }
}
