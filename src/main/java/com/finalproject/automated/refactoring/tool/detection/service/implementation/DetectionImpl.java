package com.finalproject.automated.refactoring.tool.detection.service.implementation;

import com.finalproject.automated.refactoring.tool.classes.detection.service.ClassesDetection;
import com.finalproject.automated.refactoring.tool.code.smells.detection.service.CodeSmellsDetection;
import com.finalproject.automated.refactoring.tool.detection.service.Detection;
import com.finalproject.automated.refactoring.tool.files.detection.model.FileModel;
import com.finalproject.automated.refactoring.tool.files.detection.service.FilesDetection;
import com.finalproject.automated.refactoring.tool.methods.detection.service.MethodsDetection;
import com.finalproject.automated.refactoring.tool.model.ClassModel;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author M. Reza Pahlevi
 * @version 1.0.0
 * @since 29 April 2019
 */

@Service
public class DetectionImpl implements Detection {

    @Autowired
    private FilesDetection filesDetection;

    @Autowired
    private MethodsDetection methodsDetection;

    @Autowired
    private ClassesDetection classesDetection;

    @Autowired
    private CodeSmellsDetection codeSmellsDetection;

    @Value("${files.mime.type}")
    private String mimeType;

    @Override
    public Map<String, List<MethodModel>> detect(@NonNull String path) {
        return detect(Collections.singletonList(path))
                .get(path);
    }

    @Override
    public Map<String, List<ClassModel>> detectClass(String path) {
        return detectClass(Collections.singletonList(path))
                .get(path);
    }

    @Override
    public Map<String, Map<String, List<MethodModel>>> detect(@NonNull List<String> paths) {
        return filesDetection.detect(paths, mimeType)
                .entrySet()
                .parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, this::detectMethods));
    }

    @Override
    public Map<String, Map<String, List<ClassModel>>> detectClass(List<String> paths) {
        return filesDetection.detect(paths, mimeType)
                .entrySet()
                .parallelStream()
                .collect(Collectors.toMap(Map.Entry::getKey, this::detectClasses));
    }

    private Map<String, List<ClassModel>> detectClasses(Map.Entry<String, List<FileModel>> entry) {
        Map<String, List<ClassModel>> classes = classesDetection.detect(entry.getValue());
        classes.forEach(this::detectCodeSmellsClass);

        return classes;
    }

    private Map<String, List<MethodModel>> detectMethods(Map.Entry<String, List<FileModel>> entry) {
        Map<String, List<MethodModel>> methods = methodsDetection.detect(entry.getValue());
        methods.forEach(this::detectCodeSmells);

        return methods;
    }

    private void detectCodeSmells(String filename, List<MethodModel> methods) {
        codeSmellsDetection.detect(methods);
    }

    private void detectCodeSmellsClass(String filename, List<ClassModel> classes) {
        codeSmellsDetection.detectClass(classes);
    }
}
