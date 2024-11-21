package com.bteam.fantasychess_server.utils;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class UriUtils {
    public static Map<String,String> getQueryParameters(URI uri) {
        var queryMap = new HashMap<String, String>();
        Arrays.stream(uri.getQuery().split("&"))
                .map(s -> s.split("="))
                .forEach(s -> queryMap.put(s[0], s[1]));
        return queryMap;
    }
}
