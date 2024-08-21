package org.example.gui;
import org.example.logic.SelectionPolicy;
import org.example.logic.SimulationManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {
    private final SimulationFrame simulationFrame;
    private SimulationManager simulationManager;

    public Controller (SimulationFrame simulationFrame){
        this.simulationFrame = simulationFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if(command.equals("START")) {
            int N = Integer.parseInt(simulationFrame.getN());
            int Q = Integer.parseInt(simulationFrame.getQ());
            String arrivalTimeInterval = simulationFrame.getArrivalTimeInterval();
            String serviceTimeInterval = simulationFrame.getServiceTimeInterval();
            int simulationTime = Integer.parseInt(simulationFrame.getSimulationTime());
            SelectionPolicy policy = simulationFrame.getSelectionPolicy();

            this.simulationManager = new SimulationManager(this.simulationFrame, N, Q, arrivalTimeInterval, serviceTimeInterval, simulationTime, policy);
            Thread thread = new Thread(simulationManager);
            thread.start();
        }
    }
}
