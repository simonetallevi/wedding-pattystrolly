package it.pattystrolly.wedding.manager;

import com.google.api.client.auth.oauth2.Credential;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import it.pattystrolly.wedding.service.datastore.DatastoreService;
import it.pattystrolly.wedding.model.Config;
import it.pattystrolly.wedding.model.TechUser;
import it.pattystrolly.wedding.service.credential.CredentialService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public abstract class Manager {

    protected Gson gson = new Gson();

    protected DatastoreService datastoreService;

    private static TechUser techUser;

    public static Cache<String, Credential> techUserCredential = CacheBuilder.newBuilder()
            .expireAfterWrite(45L, TimeUnit.MINUTES)
            .build();

    public Manager() {
        datastoreService = new DatastoreService();
    }

    public Credential getTechUserCredential() {
        try {
            Credential credential = techUserCredential.getIfPresent(Config.K.techUser.get());
            if (credential != null) {
                return credential;
            }
            if (techUser == null) {
                log.info("loading tech user {}", Config.K.techUser.get());
                techUser = datastoreService.ofy().load().type(TechUser.class).id(Config.K.techUser.get()).now();
            }
            return CredentialService.getCredential(techUser, Config.K.clientId.get(), Config.K.clientSecret.get());
        } catch (Throwable throwable) {
            throw new RuntimeException("Error loading credentials", throwable);
        }
    }
}
