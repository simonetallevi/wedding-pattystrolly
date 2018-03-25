package it.pattystrolly.wedding.service.taskqueue;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskQueueService {

    public static void purgeQueue(String queueName) {
        Queue queue = QueueFactory.getQueue(queueName);
        long numTasks = queue.fetchStatistics().getNumTasks();
        log.info("Purging all {} tasks in queue name {}", numTasks, queueName);
        queue.purge();
    }

    public static void runTask(String queueName, TaskDef task) {
        runTask(queueName, task, 0);
    }

    private static TaskOptions getTask(TaskDef task) {
        TaskOptions taskOptions = TaskOptions.Builder.withUrl(task.getUri().toString())
                .method(task.getMethod())
                .taskName(task.getName());
        if (task.hasParams()) {
            for (String k : task.getParams().keySet()) {
                if (task.getParams().get(k) != null) {
                    taskOptions.param(k, task.getParams().get(k));
                }
            }
        } else if (task.hasPayload()) {
            taskOptions.payload(task.getPayload());
        }
        return taskOptions;
    }

    public static void runTask(String queueName, TaskDef task, long delay) {
        log.info("Executing task name {} in queue {} with delay {}", task.getName(), queueName, delay);
        TaskOptions taskOptions = getTask(task);
        Queue queueDelete = QueueFactory.getQueue(queueName);
        taskOptions.countdownMillis(delay);
        queueDelete.add(taskOptions);
    }
}
