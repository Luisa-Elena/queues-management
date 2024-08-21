package org.example.model;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private LinkedBlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private CyclicBarrier barrier;

    public Server(CyclicBarrier barrier) {
        this.waitingPeriod = new AtomicInteger(0);
        this.tasks = new LinkedBlockingQueue<>();
        this.barrier = barrier;
    }

    public void addTask(Task task) {
        try {
            this.tasks.put(task);
            this.waitingPeriod.addAndGet(task.getServiceTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getQueueAsString()
    {
        StringBuilder result = new StringBuilder();
        for(Task task : this.tasks) {
            String taskString = "(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")" + "  ";
            result.append(taskString);
        }

        return result.toString();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);

                if (!this.tasks.isEmpty()) {
                    Task task = tasks.peek();

                    task.decrementServiceTime();
                    waitingPeriod.addAndGet(-1);

                    if (task.getServiceTime() == 0) {
                        tasks.take();
                    }
                }
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public LinkedBlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }
}
