package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;

public class ShortestQueueStrategy implements Strategy{
    @Override
    public void addTask(ArrayList<Server> servers, Task task) {
        int minSize = Integer.MAX_VALUE;
        Server x = null;

        for(Server server : servers) {
            if(server.getTasks().size() < minSize) {
                minSize = server.getTasks().size();
                x = server;
            }
        }

        if(x != null) {
            task.setTaskWaitingPeriod(x.getWaitingPeriod().get());
            x.addTask(task);
        }
    }
}
