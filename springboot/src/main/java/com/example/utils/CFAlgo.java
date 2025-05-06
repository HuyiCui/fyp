package com.example.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.RelateDTO;

import java.util.*;
import java.util.stream.Collectors;

public class CFAlgo {

    public static List<Integer> recommend(Integer userId, List<RelateDTO> records) {

        //Data is grouped by user: {userId -> List<RelateDTO>}
        Map<Integer, List<RelateDTO>> byUser = records.stream()
                .collect(Collectors.groupingBy(RelateDTO::getUseId));

        // Calculate the similarity between the target user and other users
        Map<Integer, Double> simMap = computeNeighbor(userId, byUser, 0);
        if (CollectionUtil.isEmpty(simMap)) {
            return Collections.emptyList();
        }

        // Take the nearest neighbor with the highest score (if there is a tie, choose any one)
        double bestScore = Collections.max(simMap.values());
        Integer nearest  = simMap.entrySet().stream()
                .filter(e -> e.getValue() == bestScore)
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
        if (nearest == null) {
            return Collections.emptyList();
        }

        // The nearest neighbor has consumed items
        List<Integer> neighborGoods = byUser.get(nearest).stream()
                .map(RelateDTO::getGoodsId)
                .collect(Collectors.toList());

        // Items consumed by the target user
        List<Integer> selfGoods = byUser.getOrDefault(userId, Collections.emptyList()).stream()
                .map(RelateDTO::getGoodsId)
                .collect(Collectors.toList());

        // Recommendation = nearest neighbor item - target user item
        neighborGoods.removeAll(selfGoods);
        return neighborGoods;
    }

    public static Map<Integer, Double> computeNeighbor(Integer anchorId,
                                                       Map<Integer, List<RelateDTO>> grouped,
                                                       int mode) {

        if (anchorId == null || CollectionUtil.isEmpty(grouped)) {
            return Collections.emptyMap();
        }
        List<RelateDTO> anchorList = grouped.get(anchorId);
        if (CollectionUtil.isEmpty(anchorList)) {
            return Collections.emptyMap();
        }

        Map<Integer, Double> similarity = new TreeMap<>();

        for (Map.Entry<Integer, List<RelateDTO>> entry : grouped.entrySet()) {
            Integer otherId = entry.getKey();
            if (otherId.equals(anchorId)) {
                continue;
            }
            double coef = correlation(entry.getValue(), anchorList, mode);
            similarity.put(otherId, Math.abs(coef));
        }
        return similarity;
    }

    /** Calculate the Pearson correlation coefficient between two sets of interaction records */
    private static double correlation(List<RelateDTO> lhs,
                                      List<RelateDTO> rhs,
                                      int mode) {

        List<Integer> xs = new ArrayList<>();
        List<Integer> ys = new ArrayList<>();

        for (RelateDTO a : lhs) {
            for (RelateDTO b : rhs) {
                boolean matched = (mode == 0)
                        ? a.getGoodsId().equals(b.getGoodsId())
                        : a.getUseId().equals(b.getUseId());

                if (matched) {
                    xs.add(a.getIndex());
                    ys.add(b.getIndex());
                }
            }
        }
        return pearson(xs, ys);
    }

    /** Pearson formula implementation (returns 0 when n < 2) */
    public static double pearson(List<Integer> xs, List<Integer> ys) {
        int n = xs.size();
        if (n < 2) {
            return 0D;          // There are too few samples, so the correlation is considered 0
        }

        double sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0, sumXY = 0;

        for (int i = 0; i < n; i++) {
            double x = xs.get(i);
            double y = ys.get(i);

            sumX  += x;
            sumY  += y;
            sumX2 += x * x;     // x²
            sumY2 += y * y;     // y²
            sumXY += x * y;     // x·y
        }

        double numerator   = sumXY - (sumX * sumY) / n;
        double denominator = Math.sqrt((sumX2 - (sumX * sumX) / n) *
                (sumY2 - (sumY * sumY) / n));

        return denominator == 0 ? 0D : numerator / denominator;
    }
}
