package org.example.logic;
import org.example.model.Server;
import org.example.model.Task;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;

public class Scheduler {
    private ArrayList<Server> servers;
    private Strategy strategy;

    public Scheduler(int Q, SelectionPolicy policy, CyclicBarrier barrier) {
        servers = new ArrayList<>();
        for (int i = 0; i < Q; i++) {
            Server server = new Server(barrier);
            Thread thread = new Thread(server);
            thread.start();
            servers.add(server);
        }
        if(policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ShortestQueueStrategy();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ShortestTimeStrategy();
        }
    }


    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
    }
    public ArrayList<Server> getServers() {
        return servers;
    }
}
