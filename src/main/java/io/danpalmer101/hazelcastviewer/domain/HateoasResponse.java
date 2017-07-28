package io.danpalmer101.hazelcastviewer.domain;

import org.springframework.hateoas.ResourceSupport;

public class HateoasResponse<T> extends ResourceSupport {

    private final T value;

    public HateoasResponse(final T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
