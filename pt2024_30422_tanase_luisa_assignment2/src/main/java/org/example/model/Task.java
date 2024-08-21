package org.example.model;

public class Task {
    private int id;
    private int arrivalTime;
    private int serviceTime;
    private int taskWaitingPeriod;
    private int initialServiceTime;


    public Task(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.taskWaitingPeriod = 0;
        this.initialServiceTime = serviceTime;
    }

    public synchronized void decrementServiceTime(){
        this.serviceTime--;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public synchronized int getServiceTime() {
        return serviceTime;
    }

    public int getTaskWaitingPeriod() {
        return taskWaitingPeriod;
    }

    public void setTaskWaitingPeriod(int taskWaitingPeriod) {
        this.taskWaitingPeriod = taskWaitingPeriod;
    }

    public int getInitialServiceTime() {
        return initialServiceTime;
    }
}
