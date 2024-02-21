package com.asierso.mcserverloader;

import com.asierso.mcserverloader.minecraft.MCManager;
import com.asierso.mcserverloader.threads.PromptThread;
import com.asierso.mcserverloader.threads.ServerHandlerThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        String javaArgs = "-Xmx1024M -Xms1024M"; // Default -Xmx1024M -Xms1024M
        int logPointer = 33; // Vanilla = 33, Forge=60

        Scanner sc = new Scanner(System.in);
        String scBuffer = "";

        System.out.println("Minecraft Server loader - by Asierso");

        // Configuracion del puerto de mc
        System.out.print("Puerto establecido en servidor (default 25565): "); // Default 25565
        scBuffer = sc.nextLine();
        int port = scBuffer.isBlank() ? 25565 : Integer.parseInt(scBuffer);

        // Configuracion de server.jar
        System.out.print("Jar de inicio (default server.jar): ");
        scBuffer = sc.nextLine();
        String path = scBuffer.isBlank() ? "server.jar" : scBuffer;

        // Configuracion de limites horarios
        System.out.print("Desea configurar limites horarios (Y/N)?: ");
        LocalTime startTime = null;
        LocalTime endTime = null;
        if (sc.nextLine().toLowerCase().charAt(0) == 'y') {
            System.out.print("Hora desde (hh:mm:ss): ");
            startTime = LocalTime.parse(sc.nextLine(), DateTimeFormatter.ofPattern("HH:mm:ss"));
            System.out.print("Hora hasta (hh:mm:ss): ");
            endTime = LocalTime.parse(sc.nextLine(), DateTimeFormatter.ofPattern("HH:mm:ss"));
        }

        // Configuracion avanzada de JVM y logPointer
        System.out.print("Desea configurar propiedades avanzadas (Y/N)?: ");
        if (sc.nextLine().toLowerCase().charAt(0) == 'y') {
            System.out.print("Parametros JVM (default -Xmx1024M -Xms1024M): ");
            scBuffer = sc.nextLine();
            javaArgs = scBuffer.isBlank() ? javaArgs : scBuffer;
            System.out.print("Indice de logPointer (default 33): ");
            scBuffer = sc.nextLine();
            logPointer = scBuffer.isBlank() ? logPointer : Integer.parseInt(scBuffer);
        }

        // Hilo de ejecucion de prompts para interaccion con el PID correspondiente al
        // servidor de mc
        PromptThread pThread = null;
        MCManager mcManager = null;
        // Runtime principal
        while (true) {
            if ((startTime == null && endTime == null)
                    || (startTime.isBefore(LocalTime.now()) && endTime.isAfter(LocalTime.now()))) {
                // Escucha por puerto TCP - espera de conexion
                System.out.println("Conexion detectada (" + waitConection(port) + ") iniciando server");
                try {
                    ProcessBuilder builder = new ProcessBuilder();

                    // Ejecucion de server.jar
                    if (System.getProperty("os.name").toLowerCase().contains("windows"))
                        builder.command("cmd", "/c", "java " + javaArgs + " -jar " + path);
                    else
                        builder.command("/bin/bash", "-c", "java " + javaArgs + " -jar " + path + " nogui");

                    // Inicio proceso sv
                    mcManager = new MCManager(builder.start());

                    System.out.println("Servidor iniciado en PID: " + mcManager.getPid());

                    // Linux thread control (ejecutar comandos de administracion)

                    if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
                        if (pThread == null) {
                            pThread = new PromptThread(sc, mcManager.getPid());
                            pThread.start();
                        } else {
                            pThread.setPid(mcManager.getPid());
                        }
                    }

                    // Hilo asincrono de verificacion de tiempos
                    ServerHandlerThread hThead = new ServerHandlerThread(startTime, endTime, mcManager);
                    hThead.start();

                    // Mostrar logs del server
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(mcManager.getProcess().getInputStream()));
                    String line = "";
                    while ((line = reader.readLine()) != null && mcManager.getProcess().isAlive()) {
                        System.out.println(line);
                        try {
                            // Busqueda regex de /list en logs
                            Pattern pattern = Pattern.compile("There are (\\d+) of a max of (\\d+) players online");
                            Matcher matcher = pattern.matcher(line.substring(logPointer));

                            if (matcher.find()) {
                                if (Integer.parseInt(matcher.group(1)) == 0) {
                                    reader.close();
                                    mcManager.close();
                                    System.out.println("Sin jugadores. Desconectando servidor para ahorro de recursos");
                                }
                            }
                        } catch (Exception ignore) {
                        }
                    }
                    hThead.interrupt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Server tirado - Espera de 10s apra reapertura
                System.out.println("Conexion caida. Esperando timeout de 10s");
                try {
                    Thread.sleep(10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Reinicializando socket de escucha");
            } else { // Fuera de limites horarios. Poner hilo en espera
                try {
                    System.out.println(
                            "Servicio fuera de horas (" + startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                                    + " - " + endTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + "). En espera");
                    if (startTime.isAfter(LocalTime.now()))
                        Thread.sleep(Duration.between(LocalTime.now(), startTime).toMillis());
                    else
                        Thread.sleep(Duration.between(startTime, LocalTime.now()).toMillis());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String waitConection(int port) {
        String ip = "";
        try { // Abrir socket en el puerto indicado a espera de actividad
            System.out.println("Esperando ACK en :" + port);
            ServerSocket server = new ServerSocket(port);
            Socket s = server.accept();
            // IP activadora
            ip = (((InetSocketAddress) s.getRemoteSocketAddress()).getAddress()).toString().replace("/", "");
            // Cierre de socket, comunicacion detectada
            s.close();
            server.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

        return ip;
    }
}