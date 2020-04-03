/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.ai.astar;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author oliver
 */
public class Graph<T extends GraphNode> {
    private Set<T> nodes;
    private Map<String, Set<String>> connections;
    
    public Graph(Set<T> nodes, Map<String, Set<String>> connections) {
        this.nodes = nodes;
        this.connections = connections;
    }
    
    public T getNode(String id) {
        return nodes.stream()
            .filter(node -> node.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("No node found with ID"));
    }
 
    public Set<T> getConnections(T node) {
        return connections.get(node.getId()).stream()
            .map(this::getNode)
            .collect(Collectors.toSet());
    }
}
