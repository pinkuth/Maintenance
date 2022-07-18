package com.skulkstudio.maintenance.tasks;

import dev.waterdog.waterdogpe.scheduler.Task;
import com.skulkstudio.maintenance.Maintenance;

import java.util.*;

//TODO: Check for end of maintenance and disable if needed
public class CheckTemporaryTask extends Task {
    public Maintenance main;

    public CheckTemporaryTask(Maintenance main) {
        this.main = main;
    }

    @Override
    public void onRun(int currentTick) {
    }

    @Override
    public void onCancel() {

    }
}
