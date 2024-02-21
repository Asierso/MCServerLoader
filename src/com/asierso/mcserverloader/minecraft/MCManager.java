package com.asierso.mcserverloader.minecraft;

import java.io.IOException;

public class MCManager {
    private Process mcProcess;

    public MCManager(Process mcProcess) {
        this.mcProcess = mcProcess;
    }

    // Tomar pid de minecraft
    public long getPid() {
        return mcProcess.pid();
    }

    // Devuelve proceso de minecraft
    public Process getProcess() {
        return mcProcess;
    }

    // Cierra el servidor de minecraft
    public void close() {
        if (!mcProcess.isAlive())
            return;
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            ProcessBuilder input = new ProcessBuilder();
            input.command("/bin/bash", "-c",
                    "echo /stop > /proc/" + this.getPid() + "/fd/0");
            try {
                input.start();
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mcProcess.destroy();
    }

    // Lista jugadores en el server
    public void listUsers() throws IOException {
        if (!mcProcess.isAlive())
            return;
        if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
            ProcessBuilder input = new ProcessBuilder();
            input.command("/bin/bash", "-c",
                    "echo /list > /proc/" + this.getPid() + "/fd/0");
            input.start();
        }
    }
}
