package com.example.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.example.entity.RelateDTO;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class UserCF {
    public static List<Integer> recommend(Integer userId, List<RelateDTO> list) {
        Map<Integer, List<RelateDTO>> userMap = list.stream().collect(Collectors.groupingBy(RelateDTO::getUseId));
        Map<Integer, Double> userDisMap = CoreMath.computeNeighbor(userId, userMap, 0);
        if (CollectionUtil.isEmpty(userDisMap)) {
            return Collections.emptyList();
        }
        double maxValue = Collections.max(userDisMap.values());
        Set<Integer> userIds = userDisMap.entrySet().stream().filter(e -> e.getValue() == maxValue).map(Map.Entry::getKey).collect(Collectors.toSet());
        Integer nearestUserId = userIds.stream().findAny().orElse(null);
        if (nearestUserId == null) {
            return Collections.emptyList();
        }
        List<Integer> neighborItems = userMap.get(nearestUserId).stream().map(RelateDTO::getGoodsId).collect(Collectors.toList());
        List<Integer> userItems = userMap.get(userId).stream().map(RelateDTO::getGoodsId).collect(Collectors.toList());
        neighborItems.removeAll(userItems);
        return neighborItems;
    }

}