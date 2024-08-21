package org.example.logic;
import org.example.gui.SimulationFrame;
import org.example.model.Server;
import org.example.model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

public class SimulationManager implements Runnable {
    private int simulationTime;
    private int Q;
    private int N;
    private LinkedBlockingQueue<Task> tasks;
    private SimulationFrame simulationFrame;
    private Scheduler scheduler;
    private SelectionPolicy selectionPolicy;
    private String filePath = "Test_1000clients.txt";

    public SimulationManager (SimulationFrame simulationFrame, int N, int Q, String arrivalTimeInterval, String serviceTimeInterval, int simulationTime, SelectionPolicy policy){
        this.simulationFrame = simulationFrame;
        this.simulationTime = simulationTime;
        this.Q = Q;
        this.N = N;
        this.selectionPolicy = policy;

        this.tasks = new LinkedBlockingQueue<>();
        this.generateRandomTasks(N, arrivalTimeInterval, serviceTimeInterval);
    }

    private int[] generateNRandomValues(int N, int min, int max) {
        int[] result = new int[N];
        Random random = new Random();

        for(int i=0; i<N; i++) {
            result[i] = random.nextInt(max - min + 1) + min;
        }

        return result;
    }

    private void generateRandomTasks(int N, String arrivalTimeInterval, String serviceTimeInterval) {
        String[] arrivalTime = arrivalTimeInterval.split(",");
        int minArrivalTime = Integer.parseInt(arrivalTime[0]);
        int maxArrivalTime = Integer.parseInt(arrivalTime[1]);
        String[] serviceTime = serviceTimeInterval.split(",");
        int minServiceTime = Integer.parseInt(serviceTime[0]);
        int maxServiceTime = Integer.parseInt(serviceTime[1]);

        int[] arrivalTimes = generateNRandomValues(N, minArrivalTime, maxArrivalTime);
        Arrays.sort(arrivalTimes);
        int[] serviceTimes = generateNRandomValues(N, minServiceTime, maxServiceTime);

        for(int i=0; i<N; i++) {
            Task task = new Task(i+1, arrivalTimes[i], serviceTimes[i]);
            this.tasks.add(task);
        }
    }

    private void clearFile(String fileName) {
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        StringBuilder str = new StringBuilder();
        str.append("Generated tasks: ");
        for(Task task : this.tasks) {
            String taskString = "(" + task.getId() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")" + "  ";
            str.append(taskString);
        }
        simulationFrame.setGeneartedTasksLabel(str.toString());

        FileWriter writer = null;
        try {
            clearFile(filePath);
            writer = new FileWriter(filePath, true);
            writer.write("Generated tasks:\n");
            for (Task t : this.tasks) {
                writer.write("(" + t.getId() + "," + t.getArrivalTime() + "," + t.getServiceTime() + ")\n");
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        CyclicBarrier barrier = new CyclicBarrier(Q);
        this.scheduler = new Scheduler(this.Q, this.selectionPolicy, barrier);

        int sumOfWaitingPeriods = 0;
        int sumOfServicePeriods = 0;
        int currentTime = 0;

        while (currentTime < simulationTime) {
            for (Task task : this.tasks) {
                if (task.getArrivalTime() == currentTime) {
                    this.scheduler.dispatchTask(task);
                    //this.tasks.remove(task);
                }
            }

            try {
                writer = new FileWriter(filePath, true);
                writer.write("Current time = " + currentTime + "\n");
                int index = 1;
                for(Server server : scheduler.getServers()) {
                    writer.write("\tServer " + index + ": " + server.getQueueAsString() + "\n");
                    index++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            simulationFrame.updateServerStatus(scheduler.getServers(), currentTime);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentTime++;
        }

        int maxWaitingTime = 0;
        Task x = null;
        for(Task t : tasks) {
            if(t.getTaskWaitingPeriod() >= maxWaitingTime) {
                maxWaitingTime = t.getTaskWaitingPeriod();
                x = t;
            }
            sumOfWaitingPeriods += t.getTaskWaitingPeriod();
            sumOfServicePeriods += t.getInitialServiceTime();
        }

        float averageWaitingPeriod = (float)sumOfWaitingPeriods / N;
        float averageServiceTime = (float)sumOfServicePeriods / N;
        simulationFrame.setAverageWaitingTimeLabel("Average waiting time: " + averageWaitingPeriod);
        simulationFrame.setAverageServiceTimeLabel("Average service time: " + averageServiceTime);
        simulationFrame.setPeakHourLabel("Peak hour: " + x.getArrivalTime());

        try {
            writer = new FileWriter(filePath, true);
            writer.write("\nAverage waiting time: " + averageWaitingPeriod + " seconds");
            writer.write("\nAverage service time: " + averageServiceTime + " seconds");
            writer.write("\nPeak hour: " + x.getArrivalTime());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}