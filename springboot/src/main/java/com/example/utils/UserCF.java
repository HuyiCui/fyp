package com.example.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.RelateDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserCF {
    public static List<Integer> recommend(Integer userId, List<RelateDTO> records) {

        //Data is grouped by user: {userId -> List<RelateDTO>}
        Map<Integer, List<RelateDTO>> byUser = records.stream()
                .collect(Collectors.groupingBy(RelateDTO::getUseId));

        // Calculate the similarity between the target user and other users
        Map<Integer, Double> simMap = CoreMath.computeNeighbor(userId, byUser, 0);
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

}