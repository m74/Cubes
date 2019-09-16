package ru.com.m74.extjs;

import org.slf4j.LoggerFactory;
import ru.com.m74.cubes.common.TimeLogger;
import ru.com.m74.extjs.dto.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeTools {

    public static <T extends Node> Iterable<T> getRootNodes(Iterable<T> nodes) {
        TimeLogger.init(LoggerFactory.getLogger(TreeTools.class));
        Map<Object, List<T>> map = createMap(nodes);
        List<T> children = map.get(null);
        applyChildren(map, children);
        TimeLogger.debug("getRootNodes() done");
        return children;
    }

    private static <T extends Node> Map<Object, List<T>> createMap(Iterable<T> nodes) {
        Map<Object, List<T>> map = new HashMap<>();
        for (T node : nodes) {
            map.computeIfAbsent(node.getParentId(), k -> new ArrayList<>()).add(node);
        }
        return map;
    }

    private static <T extends Node> void applyChildren(Map<Object, List<T>> map, List<T> nodes) {
        if (nodes != null) {
            for (Node node : nodes) {
                List<T> children = map.get(node.getId());
                node.setChildren(children);
                applyChildren(map, children);
            }
        }
    }
}
