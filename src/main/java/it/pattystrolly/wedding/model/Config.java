package it.pattystrolly.wedding.model;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import it.pattystrolly.wedding.config.EnvConstants;
import it.pattystrolly.wedding.service.datastore.DatastoreService;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class Config {

    @Id
    private String id = EnvConstants.APP_ID;

    private Map<String, String> values = new HashMap<>();

    private static Config config = null;

    private Config(String id){
        this.id = id;
    }

    public static Config G() {
        return G(false);
    }

    public static Config G(Boolean force) {
        if (config != null && force == false) {
            return config;
        }
        DatastoreService datastoreService = new DatastoreService();
        config = datastoreService.ofy().load().type(Config.class).id(EnvConstants.APP_ID).now();
        return config != null ? config : getEmptyConfig();
    }

    public static Config G(JsonObject obj){
        Config c = new Config(EnvConstants.APP_ID);
        if(obj.has("values")) {
            JsonObject values = obj.getAsJsonObject("values");
            for(String key : values.keySet()){
                c.getValues().put(key, values.get(key).getAsString());
            }
        }
        config = c;
        DatastoreService datastoreService = new DatastoreService();
        datastoreService.ofy().save().entity(c);
        return c;
    }

    private static Config getEmptyConfig() {
        Config c = new Config(EnvConstants.APP_ID);
        for (K k : K.values()) {
            c.getValues().put(k.name(), "");
        }
        return c;
    }

    public enum K {
        bucket,
        clientId,
        clientSecret,
        scopes,
        techUser,
        input;

        public String get() {
            return G().getValues().get(this.name());
        }

        public Set<String> getSet() {
            return ImmutableSet.copyOf(G().getValues().get(this.name()).split(","));
        }
    }
}
