package com.forzatune.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forzatune.backend.entity.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarDataService {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Car> carCache = new ConcurrentHashMap<>();
    private final Map<String, List<Car>> manufacturerCache = new ConcurrentHashMap<>();
    private final Map<String, List<Car>> categoryCache = new ConcurrentHashMap<>();
    private final Map<String, List<Car>> drivetrainCache = new ConcurrentHashMap<>();
    
    private String dataVersion;
    private String lastUpdated;
    private String gameVersion;
    
    @PostConstruct
    public void init() {
        loadCarData();
    }
    
    /**
     * 从静态文件加载车型数据
     */
    public void loadCarData() {
        try {
            ClassPathResource resource = new ClassPathResource("static/cars.json");
            JsonNode rootNode = objectMapper.readTree(resource.getInputStream());
            
            this.dataVersion = rootNode.get("version").asText();
            this.lastUpdated = rootNode.get("lastUpdated").asText();
            this.gameVersion = rootNode.get("gameVersion").asText();
            
            JsonNode carsNode = rootNode.get("cars");
            carCache.clear();
            manufacturerCache.clear();
            categoryCache.clear();
            drivetrainCache.clear();
            
            for (JsonNode carNode : carsNode) {
                Car car = objectMapper.treeToValue(carNode, Car.class);
                carCache.put(car.getId(), car);
                
                // 构建索引
                manufacturerCache.computeIfAbsent(car.getManufacturer(), k -> new ArrayList<>()).add(car);
                categoryCache.computeIfAbsent(car.getCategory().name(), k -> new ArrayList<>()).add(car);
                drivetrainCache.computeIfAbsent(car.getDrivetrain().name(), k -> new ArrayList<>()).add(car);
            }
            
            log.info("Loaded {} cars from static data, version: {}", carCache.size(), dataVersion);
            
        } catch (IOException e) {
            log.error("Failed to load car data from static file", e);
        }
    }
    
    /**
     * 获取所有车型
     */
    public List<Car> getAllCars() {
        return new ArrayList<>(carCache.values());
    }
    
    /**
     * 根据ID获取车型
     */
    public Car getCarById(String id) {
        return carCache.get(id);
    }
    
    /**
     * 根据制造商获取车型
     */
    public List<Car> getCarsByManufacturer(String manufacturer) {
        return manufacturerCache.getOrDefault(manufacturer, new ArrayList<>());
    }
    
    /**
     * 根据分类获取车型
     */
    public List<Car> getCarsByCategory(String category) {
        return categoryCache.getOrDefault(category, new ArrayList<>());
    }
    
    /**
     * 根据驱动方式获取车型
     */
    public List<Car> getCarsByDrivetrain(String drivetrain) {
        return drivetrainCache.getOrDefault(drivetrain, new ArrayList<>());
    }
    
    /**
     * 搜索车型
     */
    public List<Car> searchCars(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllCars();
        }
        
        String lowerKeyword = keyword.toLowerCase();
        return carCache.values().stream()
                .filter(car -> car.getName().toLowerCase().contains(lowerKeyword) ||
                             car.getManufacturer().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有制造商
     */
    public List<String> getAllManufacturers() {
        return new ArrayList<>(manufacturerCache.keySet());
    }
    
    /**
     * 根据PI范围获取车型
     */
    public List<Car> getCarsByPIRange(Integer minPI, Integer maxPI) {
        return carCache.values().stream()
                .filter(car -> car.getPi() >= minPI && car.getPi() <= maxPI)
                .sorted((c1, c2) -> Integer.compare(c2.getPi(), c1.getPi()))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取数据版本信息
     */
    public Map<String, String> getDataInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("version", dataVersion);
        info.put("lastUpdated", lastUpdated);
        info.put("gameVersion", gameVersion);
        info.put("totalCars", String.valueOf(carCache.size()));
        return info;
    }
    
    /**
     * 重新加载数据
     */
    public void reloadData() {
        loadCarData();
    }
} 