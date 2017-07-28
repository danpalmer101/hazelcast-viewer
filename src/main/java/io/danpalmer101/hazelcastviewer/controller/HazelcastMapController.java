package io.danpalmer101.hazelcastviewer.controller;

import com.hazelcast.core.IMap;
import com.hazelcast.map.impl.MapService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * REST controller for operations on Hazelcast maps
 */
@RestController
@RequestMapping("/maps")
public class HazelcastMapController extends AbstractHazelcastController {

    /**
     * Get a list of the names of all maps
     * @return list of map names
     */
    @RequestMapping(method = GET)
    public List<String> getMaps() {
        logger.debug("Getting list of maps");

        return getDistributedObjectNames(MapService.SERVICE_NAME);
    }

    /**
     * Get the contents of a map
     * @param mapName the name of the map
     * @return the contents of a map
     */
    @RequestMapping(method = GET, value = "/{mapName}")
    public Map<?, ?> getMap(@PathVariable("mapName") String mapName) {
        logger.debug("Viewing map: {}", mapName);

        return toMap(hazelcastInstance.getMap(mapName));
    }

    /**
     * Get the contents of a specific entry in the map
     * @param mapName the name of the map
     * @param mapKey the key of the map entry
     * @return the contents of a specific entry in the map
     */
    @RequestMapping(method = GET, value = "/{mapName}/{mapKey}")
    public Object getMapObject(@PathVariable("mapName") String mapName,
                               @PathVariable("mapKey") String mapKey) {
        logger.debug("Viewing map object: {}[{}]", mapName, mapKey);

        return hazelcastInstance.getMap(mapName).get(mapKey);
    }

    private <K, V> Map<K, V> toMap(IMap<K, V> hazelcastMap) {
        Map<K, V> map = new HashMap<>();
        for (K key : hazelcastMap.keySet()) {
            map.put(key, hazelcastMap.get(key));
        }
        return map;
    }

}
