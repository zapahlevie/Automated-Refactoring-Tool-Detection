package com.finalproject.automated.refactoring.tool.detection.service;

import com.finalproject.automated.refactoring.tool.model.ClassModel;
import com.finalproject.automated.refactoring.tool.model.MethodModel;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * @author M. Reza Pahlevi
 * @version 1.0.0
 * @since 29 April 2019
 */

public interface Detection {

    Map<String, List<MethodModel>> detect(@NonNull String path);

    Map<String, List<ClassModel>> detectClass(@NonNull String path);

    Map<String, Map<String, List<MethodModel>>> detect(@NonNull List<String> paths);

    Map<String, Map<String, List<ClassModel>>> detectClass(@NonNull List<String> paths);
}
