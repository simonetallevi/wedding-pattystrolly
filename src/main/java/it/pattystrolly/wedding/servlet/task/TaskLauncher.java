package it.pattystrolly.wedding.servlet.task;

import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import it.pattystrolly.wedding.servlet.AbstractServlet;
import it.pattystrolly.wedding.service.taskqueue.TaskDef;
import it.pattystrolly.wedding.service.taskqueue.TaskQueueService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.UUID;


@Slf4j
public class TaskLauncher extends AbstractServlet {

    public enum Tasks {
        SEND("send-queue", URI.create("/task/send"));

        private String queue;

        private URI servlet;

        Tasks(String queue, URI servlet) {
            this.queue = queue;
            this.servlet = servlet;
        }

        public String getQueue() {
            return queue;
        }

        public URI getServlet() {
            return servlet;
        }
    }

    @Override
    protected void get(Map<String, String> parameters, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Tasks action = Tasks.valueOf(parameters.get("action"));

        switch (action) {
            case SEND: {
                String uuid = UUID.randomUUID().toString();
                TaskDef task = new TaskDef(
                        TaskOptions.Method.GET, action.getServlet(),
                        ImmutableMap.of(
                                "action", "STORE",
                                "id", uuid), "STORE"+uuid);
                TaskQueueService.runTask(action.getQueue(),task);
                break;
            }
            default:
                throw new IllegalStateException("Action not supported");
        }
        }


    @Override
    protected void post(JsonObject input, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        throw new IllegalStateException("Method not supported");
    }
}
