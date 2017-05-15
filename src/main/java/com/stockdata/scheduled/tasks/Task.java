package com.stockdata.scheduled.tasks;

/**
 * Created by mandarinka on 09.05.17.
 */

public interface Task {
    void execute() throws Exception;
    boolean isAppliable();
}
