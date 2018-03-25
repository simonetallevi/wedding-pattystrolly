package it.pattystrolly.wedding.servlet.task;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import it.pattystrolly.wedding.servlet.AbstractServlet;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
public abstract class AbstractTaskServlet extends AbstractServlet {

    protected String taskName;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer executionCount = getExecutionCount(req);
        Map<String, String> parameters = Maps.newHashMap();
        taskName = getTaskName(req);
        log.info("Execution n {} of {}", executionCount, taskName);
        for (Map.Entry<String, String[]> entry : ((Map<String, String[]>)req.getParameterMap()).entrySet()) {
            parameters.put(entry.getKey(), entry.getValue()[0]);
        }
        get(parameters, req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer executionCount = getExecutionCount(req);
        taskName = getTaskName(req);
        log.info("Execution n {} of {}", executionCount, taskName);
        Gson gson = new Gson();
        JsonObject input = gson.fromJson(new JsonReader(req.getReader()), JsonObject.class);
        post(input, req, resp);
        log.info("done");
    }

    protected Integer getExecutionCount(HttpServletRequest req) {
        String header = req.getHeader("X-AppEngine-TaskExecutionCount");
        if (header == null) {
            return 0;
        } else {
            return Integer.parseInt(header);
        }
    }

    protected String getTaskName(HttpServletRequest req){
        return req.getHeader("X-AppEngine-TaskName");
    }
}
