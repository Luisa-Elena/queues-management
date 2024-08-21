package org.example;

import org.example.gui.SimulationFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new SimulationFrame("Queue management system");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}