package com.example.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.RelateDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class CoreMath {
    public static Map<Integer, Double> computeNeighbor(Integer key, Map<Integer, List<RelateDTO>> map, int type) {
        Map<Integer, Double> distMap = new TreeMap<>();
        List<RelateDTO> userItems = map.get(key);
        if (CollectionUtil.isNotEmpty(userItems)) {
            map.forEach((k, v) -> {
                if (!k.equals(key)) {
                    double coefficient = relateDist(v, userItems, type);
                    double distance = Math.abs(coefficient);
                    distMap.put(k, distance);
                }
            });
        }
        return distMap;
    }

    private static double relateDist(List<RelateDTO> xList, List<RelateDTO> yList, int type) {
        List<Integer> xs = new ArrayList<>();
        List<Integer> ys = new ArrayList<>();
        xList.forEach(x -> yList.forEach(y -> {
            if (type == 0) {
                if (x.getGoodsId().equals(y.getGoodsId())) {
                    xs.add(x.getIndex());
                    ys.add(y.getIndex());
                }
            } else {
                if (x.getUseId().equals(y.getUseId())) {
                    xs.add(x.getIndex());
                    ys.add(y.getIndex());
                }
            }
        }));
        return getRelate(xs, ys);
    }

    public static double getRelate(List<Integer> xs, List<Integer> ys) {
        int n = xs.size();
        if (n < 2) {
            return 0D;
        }
        double Ex = xs.stream().mapToDouble(x -> x).sum();
        double Ey = ys.stream().mapToDouble(y -> y).sum();
        double Ex2 = xs.stream().mapToDouble(x -> Math.pow(x, 2)).sum();
        double Ey2 = ys.stream().mapToDouble(y -> Math.pow(y, 2)).sum();
        double Exy = IntStream.range(0, n).mapToDouble(i -> xs.get(i) * ys.get(i)).sum();
        double numerator = Exy - Ex * Ey / n;
        double denominator = Math.sqrt((Ex2 - Math.pow(Ex, 2) / n) * (Ey2 - Math.pow(Ey, 2) / n));
        if (denominator == 0) {
            return 0D;
        }
        return numerator / denominator;
    }

}