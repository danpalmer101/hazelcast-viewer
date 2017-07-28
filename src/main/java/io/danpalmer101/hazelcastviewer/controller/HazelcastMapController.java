package io.danpalmer101.hazelcastviewer.controller;

import com.hazelcast.map.impl.MapService;
import io.danpalmer101.hazelcastviewer.domain.HateoasResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * REST controller for operations on Hazelcast maps
 */
@RestController
@RequestMapping("/maps")
public class HazelcastMapController extends AbstractHazelcastController {

    private static final String PARENT = "parent";

    /**
     * Get a list of the names of all maps
     * @return list of map names
     */
    @RequestMapping(method = GET)
    public HateoasResponse<List<HateoasResponse<String>>> getMaps() {
        logger.debug("Getting list of maps");

        List<HateoasResponse<String>> value = getDistributedObjectNames(MapService.SERVICE_NAME)
            .map(mapName -> {
                HateoasResponse<String> h = new HateoasResponse(mapName);
                h.add(linkTo(methodOn(getClass()).getMap(mapName)).withSelfRel());
                h.add(linkTo(methodOn(getClass()).getMaps()).withRel(PARENT));
                return h;
            })
            .collect(Collectors.toList());

        HateoasResponse<List<HateoasResponse<String>>> response = new HateoasResponse<>(value);
        response.add(linkTo(methodOn(getClass()).getMaps()).withSelfRel());

        return response;
    }

    /**
     * Get the contents of a map
     * @param mapName the name of the map
     * @return the contents of a map
     */
    @RequestMapping(method = GET, value = "/{mapName}")
    public HateoasResponse<List<HateoasResponse<Object>>> getMap(@PathVariable("mapName") String mapName) {
        logger.debug("Viewing map: {}", mapName);

        List<HateoasResponse<Object>> value = hazelcastInstance.getMap(mapName).keySet().stream()
                .map(mapKey -> {
                    HateoasResponse<Object> h = new HateoasResponse<>(mapKey);
                    if (mapKey instanceof String) {
                        h.add(linkTo(methodOn(getClass()).getMapObject(mapName, (String) mapKey)).withSelfRel());
                        h.add(linkTo(methodOn(getClass()).getMap(mapName)).withRel(PARENT));
                    }
                    return h;
                })
                .collect(Collectors.toList());

        HateoasResponse<List<HateoasResponse<Object>>> response = new HateoasResponse<>(value);
        response.add(linkTo(methodOn(getClass()).getMap(mapName)).withSelfRel());
        response.add(linkTo(methodOn(getClass()).getMaps()).withRel(PARENT));

        return response;
    }

    /**
     * Get the contents of a specific entry in the map
     * @param mapName the name of the map
     * @param mapKey the key of the map entry
     * @return the contents of a specific entry in the map
     */
    @RequestMapping(method = GET, value = "/{mapName}/{mapKey}")
    public HateoasResponse<Object> getMapObject(@PathVariable("mapName") String mapName,
                               @PathVariable("mapKey") String mapKey) {
        logger.debug("Viewing map object: {}[{}]", mapName, mapKey);

        Object value = hazelcastInstance.getMap(mapName).get(mapKey);

        HateoasResponse<Object> response = new HateoasResponse<>(value);
        response.add(linkTo(methodOn(getClass()).getMapObject(mapName, mapKey)).withSelfRel());
        response.add(linkTo(methodOn(getClass()).getMap(mapName)).withRel(PARENT));

        return response;
    }

}
