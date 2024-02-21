package com.asierso.mcserverloader.threads;

import com.asierso.mcserverloader.minecraft.MCManager;

import java.time.LocalTime;

public class ServerHandlerThread extends Thread{
    private LocalTime startTime;
    private LocalTime endTime;
    private MCManager mc;

    public ServerHandlerThread(LocalTime startTime, LocalTime endTime, MCManager mc) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.mc = mc;
    }

    @Override
    public void run() {
        try {
            while (true) { // Deteccion de rangos de tiempo y jugadores en server
                Thread.sleep(60000);
                if (startTime != null && endTime != null && (startTime.isAfter(LocalTime.now()) // Deteccion de
                        // limite horario
                        || endTime.isBefore(LocalTime.now()))) {
                    mc.close();
                } else {
                    // Verificacion de jugadores (Linux)
                    mc.listUsers();
                }
            }
        } catch (InterruptedException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
