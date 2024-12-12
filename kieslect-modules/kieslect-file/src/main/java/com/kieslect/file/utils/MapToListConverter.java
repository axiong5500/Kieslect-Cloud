package com.kieslect.file.utils;


import com.kieslect.file.domain.vo.SportKlocationVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapToListConverter {
    public static List<double[]> convertListToCoordinates(List<SportKlocationVO> sportKlocationList) {
        return sportKlocationList.stream()
                .map(sportKlocation -> new double[]{
                        sportKlocation.getLatitude(),
                        sportKlocation.getLongitude()
                })
                .collect(Collectors.toList());
    }
    public static List<double[]> convertMapToList(Map<Double, Double> map) {
        List<double[]> list = new ArrayList<>();
        for (Map.Entry<Double, Double> entry : map.entrySet()) {
            double[] pair = {entry.getKey(), entry.getValue()};
            list.add(pair);
        }
        return list;
    }
}
