package com.example.spotbugsexample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.umd.cs.findbugs.PluginException;
import edu.umd.cs.findbugs.PluginLoader;

@SpringBootApplication
public class SpotbugsExampleApplication {

    public static File getResourceAsFile(String resourcePath) throws IOException {
        InputStream in = SpotbugsExampleApplication.class.getResourceAsStream("/" + resourcePath);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + resourcePath);
        }

        File tempFile = File.createTempFile(String.valueOf(in.hashCode()), ".tmp");
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            in.transferTo(out);
        }
        return tempFile;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpotbugsExampleApplication.class, args);
        try {
            File pluginJar = getResourceAsFile("sb-contrib-7.7.2.jar");
            PluginLoader.validate(pluginJar);
            PluginLoader.getPluginLoader(pluginJar.toURI().toURL(), PluginLoader.class.getClassLoader(), false,
                    false);
        } catch (IOException | PluginException e) {
            e.printStackTrace();
        }
    }
}
