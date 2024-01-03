package com.sifradigital.framework.util;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ResourceUtils {

    private static final Logger Log = LoggerFactory.getLogger(ResourceUtils.class);

    public static <T> T loadFromResource(String path, Class<T> classOfT) {
        try (InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (is != null) {
                return new Gson().fromJson(new InputStreamReader(is), classOfT);
            }
            Log.error("Cannot read input stream for resource {}", path);
        }
        catch (IOException e) {
            Log.error("Error loading from resource", e);
        }
        return null;
    }

    public static <T> T loadFromResource(String path, Type type) {
        try (InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(path)) {
            if(is != null) {
                return new Gson().fromJson(new InputStreamReader(is), type);
            }
            Log.error("Cannot read input stream for resource {}", path);
        }
        catch (IOException e){
            Log.error("Error loading from resource", e);
        }
        return null;
    }

    public static String resourceAsString(String path) {
        try (InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (is != null) {
                return IOUtils.toString(is, StandardCharsets.UTF_8);
            }
            Log.error("Cannot read input stream for resource {}", path);
        }
        catch (IOException e) {
            Log.error("Error reading resource", e);
        }
        return null;
    }

    public static List<String> resourceAsLines(String path) {
        try (InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (is != null) {
                return IOUtils.readLines(is, StandardCharsets.UTF_8);
            }
            Log.error("Cannot read input stream for resource {}", path);
        }
        catch (IOException e) {
            Log.error("Error reading resource", e);
        }
        return null;
    }

}
