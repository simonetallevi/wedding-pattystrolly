package it.pattystrolly.wedding.utils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.common.collect.ImmutableList;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author tallesi001 on 14/01/18.
 */
public class Utils {

    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public static HttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private static final Collection<Class<? extends Annotation>> DATASTORE_ENTITIES_ANNOTATIONS =
            ImmutableList.of(com.googlecode.objectify.annotation.Entity.class,
                    com.googlecode.objectify.annotation.Subclass.class);

    public static Set<Class<?>> getDatastoreClasses(String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        for (Class<? extends Annotation> c : DATASTORE_ENTITIES_ANNOTATIONS) {
            classes.addAll(getClassesAnnotatedWith(c, packageName));
        }
        return classes;
    }

    public static Set<Class<?>> getClassesAnnotatedWith(Class<? extends Annotation> annotation, String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(annotation);
    }
}
