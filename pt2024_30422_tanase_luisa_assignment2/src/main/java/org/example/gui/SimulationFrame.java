package org.example.gui;

import org.example.logic.SelectionPolicy;
import org.example.model.Server;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SimulationFrame extends JFrame {

    private JPanel contentPanel;
    private JTextField N;
    private JLabel NLabel;
    private JTextField Q;
    private JLabel QLabel;
    private JTextField arrivalTime;
    private JLabel arrivalTimeLabel;
    private JTextField serviceTime;
    private JLabel serviceTimeLabel;
    private JButton startSimulation;
    private JLabel simulationTimeLabel;
    private JTextField simulationTime;
    private JLabel selectionPolicyLabel;
    private JComboBox<SelectionPolicy> selectionPolicyComboBox;
    private JTextArea serverStatusTextArea;
    private JLabel geneartedTasksLabel;
    private JLabel averageWaitingTimeLabel;
    private JLabel averageServiceTimeLabel;
    private JLabel peakHourLabel;

    Controller controller = new Controller(this);

    public SimulationFrame(String name) {
        super(name);
        this.prepareGui();
        this.setContentPane(this.contentPanel);
    }

    public void prepareGui(){
        this.setSize(800, 600);

        // Initialize components
        contentPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(8, 2));
        JPanel queuesPanel = new JPanel();
        JPanel tasksPanel  = new JPanel();
        JPanel inputAndTaskPanel = new JPanel(new BorderLayout());
        JPanel computations = new JPanel(new BorderLayout());

        NLabel = new JLabel("Number of clients:");
        N = new JTextField();
        QLabel = new JLabel("Number of queues:");
        Q = new JTextField();
        arrivalTimeLabel = new JLabel("Arrival time interval:");
        arrivalTime = new JTextField();
        serviceTimeLabel = new JLabel("Service time interval:");
        serviceTime = new JTextField();
        simulationTimeLabel = new JLabel("Simulation time:");
        simulationTime = new JTextField();
        startSimulation = new JButton("Start simulation");
        selectionPolicyLabel = new JLabel("Selection policy:");
        selectionPolicyComboBox = new JComboBox<>(SelectionPolicy.values());
        serverStatusTextArea = new JTextArea();
        serverStatusTextArea.setPreferredSize(new Dimension(500, 100));
        geneartedTasksLabel = new JLabel("");
        averageWaitingTimeLabel = new JLabel("Average waiting time: ");
        averageServiceTimeLabel = new JLabel("Average service time: ");
        peakHourLabel = new JLabel("Peak Hour: ");

        // Add components to the content pane
        inputPanel.add(NLabel);
        inputPanel.add(N);
        inputPanel.add(QLabel);
        inputPanel.add(Q);
        inputPanel.add(arrivalTimeLabel);
        inputPanel.add(arrivalTime);
        inputPanel.add(serviceTimeLabel);
        inputPanel.add(serviceTime);
        inputPanel.add(simulationTimeLabel);
        inputPanel.add(simulationTime);
        inputPanel.add(selectionPolicyLabel);
        inputPanel.add(selectionPolicyComboBox);

        inputPanel.add(startSimulation);
        startSimulation.setActionCommand("START");
        startSimulation.addActionListener(this.controller);

        tasksPanel.add(geneartedTasksLabel);

        inputAndTaskPanel.add(inputPanel, BorderLayout.NORTH);
        inputAndTaskPanel.add(tasksPanel, BorderLayout.CENTER);

        contentPanel.add(inputAndTaskPanel, BorderLayout.NORTH);

        queuesPanel.add(serverStatusTextArea);
        contentPanel.add(queuesPanel, BorderLayout.CENTER);

        computations.add(averageWaitingTimeLabel, BorderLayout.NORTH);
        computations.add(averageServiceTimeLabel, BorderLayout.CENTER);
        computations.add(peakHourLabel, BorderLayout.SOUTH);

        contentPanel.add(computations, BorderLayout.SOUTH);
    }

    public void updateServerStatus(ArrayList<Server> servers, int currentTime) {
        StringBuilder status = new StringBuilder();
        status.append("Current Time: ").append(currentTime).append("\n");

        int i=1;
        for (Server server : servers) {
            status.append("Server ").append(i).append(": ").append(server.getQueueAsString()).append("\n");
            i++;
        }

        serverStatusTextArea.setText(status.toString());
    }

    public void setGeneartedTasksLabel(String geneartedTasksLabel) {
        this.geneartedTasksLabel.setText(geneartedTasksLabel);
    }

    public String getN() { return N.getText(); }

    public String getQ() { return Q.getText(); }

    public String getArrivalTimeInterval() { return arrivalTime.getText(); }

    public String getServiceTimeInterval() { return serviceTime.getText(); }

    public String getSimulationTime() { return simulationTime.getText(); }

    public SelectionPolicy getSelectionPolicy() {
        return (SelectionPolicy) selectionPolicyComboBox.getSelectedItem();
    }

    public void setAverageWaitingTimeLabel(String averageWaitingTime) {
        this.averageWaitingTimeLabel.setText(averageWaitingTime);
    }
    public void setAverageServiceTimeLabel(String averageServiceTime) {
        this.averageServiceTimeLabel.setText(averageServiceTime);
    }

    public void setPeakHourLabel(String peakHour) {
        this.peakHourLabel.setText(peakHour);
    }
}