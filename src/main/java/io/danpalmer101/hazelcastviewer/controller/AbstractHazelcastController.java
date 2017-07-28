package io.danpalmer101.hazelcastviewer.controller;

import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Common Hazelcast operations
 */
public abstract class AbstractHazelcastController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected HazelcastInstance hazelcastInstance;

    protected List<String> getDistributedObjectNames(final String serviceName) {
        return hazelcastInstance.getDistributedObjects().stream()
                .filter(d -> d.getServiceName().equals(serviceName))
                .map(d -> d.getName())
                .collect(Collectors.toList());
    }

}
