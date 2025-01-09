package com.github.gerdanyjr.simple_transit.util;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UriUtil {

    public static URI createUri(String path, String uriVariable) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path(path)
                .buildAndExpand(uriVariable)
                .toUri();
    }

}
