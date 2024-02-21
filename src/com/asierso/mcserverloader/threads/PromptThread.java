package com.asierso.mcserverloader.threads;

import java.io.IOException;
import java.util.Scanner;

public class PromptThread extends Thread{
    private Scanner sc;
    private long mcPid = 0; // pid del proceso de mc

    public void setPid(long mcPid) {
        this.mcPid = mcPid;
    }

    public PromptThread(Scanner sc, long mcPid) {
        this.sc = sc;
        this.mcPid = mcPid;
    }

    @Override
    public void run() {
        while (true) { // inyeccion a stdin de comandos al server de mc (via pid)
            ProcessBuilder input = new ProcessBuilder();
            input.command("/bin/bash", "-c", "echo " + sc.nextLine() + " > /proc/" + mcPid + "/fd/0");
            try {
                input.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
