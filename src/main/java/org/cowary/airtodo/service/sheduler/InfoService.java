package org.cowary.airtodo.service.sheduler;

import org.cowary.airtodo.entity.Task;

public interface InfoService {
    void sendListNotDoneTasks();

    void sendMessageIsDone(Task task);
}
