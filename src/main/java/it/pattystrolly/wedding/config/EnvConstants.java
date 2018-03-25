package it.pattystrolly.wedding.config;

import com.google.appengine.api.utils.SystemProperty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EnvConstants {

    public static final String APP_NAME = "Simo & Stefi Wedding";

    public static String APP_ID;

    public static EnvConfig CONFIG;

    static{
        CONFIG = EnvConfig.get();
        APP_ID = CONFIG.applicationId;
    }

    public static String getSenderEmail() {
        return "noreply@" + CONFIG.applicationId + ".appspotmail.com";
    }

    public static boolean isStandalone() {
        return SystemProperty.environment.value() == null;
    }

    public static boolean isDev() {
        return SystemProperty.environment.value() == SystemProperty.Environment.Value.Development;
    }

    public static String getBaseURL() {
        if (isStandalone() || isDev()) {
            return "http://localhost:8080/";
        }
        return "https://" + CONFIG.applicationId + ".appspot.com/";
    }
}
