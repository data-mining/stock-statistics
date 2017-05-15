package com.stockdata.scheduled;

import com.stockdata.scheduled.tasks.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.List;

/**
 * Created by mandarinka on 09.05.17.
 */

@Component
public class ScheduledTasks {

    @Autowired
    private List<Task> sheduledTasks;

    @Scheduled(cron = "${cron.base}")
    public void tick() throws Exception {
        for (Task a: sheduledTasks) {
            if (a.isAppliable()) {
                System.out.println("10second");
                a.execute();
            }
        }
        System.out.println("1 second");
    }
}