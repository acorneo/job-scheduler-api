package me.acorneo.jobschedulerapi.configuration;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.workers.TaskProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class WorkerLauncher implements CommandLineRunner {
    private final ApplicationContext applicationContext;
    private final AsyncTaskExecutor workerExecutor;

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 10; i++) {
            TaskProcessor worker = applicationContext.getBean(TaskProcessor.class);
            workerExecutor.execute(worker);
        }
    }
}
