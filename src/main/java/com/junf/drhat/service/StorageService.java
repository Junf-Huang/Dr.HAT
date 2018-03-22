package com.junf.drhat.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init(Integer uid);

    void store(MultipartFile file, Integer uid, String filename);

    Stream<Path> loadAll();

    Path load(String filename, Integer uid);

    Resource loadAsResource(String filename,Integer uid);

    void deleteAll(Integer uid);

}
