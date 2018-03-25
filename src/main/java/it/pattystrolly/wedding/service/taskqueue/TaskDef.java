package it.pattystrolly.wedding.service.taskqueue;

import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.base.Strings;
import lombok.Data;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@Data
public class TaskDef {

    private TaskOptions.Method method;

    private final String uuid = UUID.randomUUID().toString();

    private String name;

    private URI uri;

    private Map<String, String> params;

    private String payload;

    private boolean randomizedName = false;

    public TaskDef(TaskOptions.Method method,
                   URI uri, Map<String, String> params, String name) {
        this.uri = uri;
        this.name = name;
        this.params = params;
        this.method = method;
    }

    public TaskDef(TaskOptions.Method method,
                   URI uri, String payload, String name) {
        this.uri = uri;
        this.name = name;
        this.payload = payload;
        this.method = method;
    }

    public String getName() {
        return (isRandomizedName() ? uuid : name);
    }

    public boolean hasParams(){
        return params != null && !params.isEmpty();
    }

    public boolean hasPayload(){
        return !Strings.isNullOrEmpty(payload);
    }

}
