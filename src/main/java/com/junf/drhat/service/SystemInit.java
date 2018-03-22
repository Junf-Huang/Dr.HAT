package com.junf.drhat.service;

import com.junf.drhat.storage.StorageException;
import com.junf.drhat.storage.StorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SystemInit  {

    private final Path rootLocation;

    @Autowired
    public SystemInit(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }


    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
