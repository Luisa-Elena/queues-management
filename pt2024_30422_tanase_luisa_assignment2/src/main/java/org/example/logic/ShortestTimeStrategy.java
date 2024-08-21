package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ShortestTimeStrategy implements Strategy{

    @Override
    public void addTask(ArrayList<Server> servers, Task task) {
        AtomicInteger shortestTime = new AtomicInteger(Integer.MAX_VALUE);
        Server x = null;

        for(Server server : servers) {
            if(server.getWaitingPeriod().get() < shortestTime.get()) {
                shortestTime.set(server.getWaitingPeriod().get());
                x = server;
            }
        }

        if(x != null) {
            task.setTaskWaitingPeriod(x.getWaitingPeriod().get());
            x.addTask(task);
        }
    }
}
