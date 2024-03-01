package com.asierso.mcserverloader.settings;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Settings {
    private String uri;
    private ArrayList<Flag> flagList;
    private static Settings settings;
    public static synchronized Settings getInstance(String uri){
        if(settings == null)
            settings= new Settings(uri);
        return settings;
    }
    private Settings(String uri){
        this.uri = uri;
        this.flagList = new ArrayList<>();
        if(!Files.exists(Path.of(uri))){
            try {
                boolean newFile = new File(uri).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadSettings();
    }

    private void loadSettings() {
        try {
            FileReader reader = new FileReader(uri);
            Scanner sc = new Scanner(reader);
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();

                if (!line.isBlank()) {
                    String key = line.split("=")[0].toLowerCase();
                    String value = line.split("=")[1];
                    if(isFlag(key)) {
                        flagList = (ArrayList<Flag>) flagList.stream().map(obj -> {
                            if (obj.getKey().equals(key))
                                return new Flag(key, value);
                            else
                                return obj;

                        }).collect(Collectors.toList());
                    }
                    else
                        flagList.add(new Flag(key,value));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Flag getFlag(String key){
        if(isFlag(key))
            return flagList.stream().filter(obj -> obj.getKey().equals(key)).findFirst().get();
        else
            return null;
    }

    public boolean isFlag(String key){
        return flagList.stream().anyMatch(obj -> obj.getKey().equals(key));
    }
}
