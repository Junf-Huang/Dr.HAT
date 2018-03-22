package com.junf.drhat.service;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import com.junf.drhat.controller.UserController;
import com.junf.drhat.storage.StorageException;
import com.junf.drhat.storage.StorageFileNotFoundException;
import com.junf.drhat.storage.StorageProperties;
import com.junf.drhat.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final static Logger logger= LoggerFactory.getLogger(FileSystemStorageService.class);
    private Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file, Integer uid, String filename) {
        if (filename != "profile-photo.png")
            filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            logger.info("rootLocation={}", this.rootLocation);
            this.rootLocation = Paths.get(this.rootLocation + "/" + uid);
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename, Integer uid) {
        Path uidpath = Paths.get(rootLocation+ "/"+ uid);
        return uidpath.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename, Integer uid) {
        try {
            Path file = load(filename, uid);
            logger.info("path={}", file.toString());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll(Integer uid) {
        Path uidpath = Paths.get(rootLocation+"/"+uid);
        FileSystemUtils.deleteRecursively(uidpath.toFile());
    }

    @Override
    public void init(Integer uid) {
        try {

            File file = new File(rootLocation+"/"+uid);
            if (!file.exists()) {
                if (file.mkdirs()) {
                    logger.info("Directory is created!");
                } else {
                    logger.info("Failed to create directory!");
                }
            }
            logger.info("file={}", file.getPath()+"/");
            logger.info("file={}", file.getParentFile());

            Path targetPath = Paths.get(file.getPath() + "/");
            Path sourcePath = Paths.get(file.getParent() + "/default.png");
            Files.copy(sourcePath, targetPath.resolve("profile-photo.png"), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
