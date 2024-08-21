package org.example.logic;

import org.example.model.Server;
import org.example.model.Task;

import java.util.ArrayList;

public interface Strategy {
    public void addTask(ArrayList<Server> servers, Task task);
}
