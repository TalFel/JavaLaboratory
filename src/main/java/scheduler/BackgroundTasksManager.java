package scheduler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class BackgroundTasksManager implements ServletContextListener {
	private Timer timer;
	@Override
	public void contextInitialized(ServletContextEvent event) {
		//set the task to the timer.
		timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
            	DailyJob job = new DailyJob();
                job.run();
            };
        };
        //set timer to do the task every day at midnight.
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrowAtMidnight = now.plusDays(1).truncatedTo(ChronoUnit.DAYS);
        Date date = Date.from(tomorrowAtMidnight.atZone(ZoneId.systemDefault()).toInstant());
        timer.scheduleAtFixedRate(timerTask, date, 1000 * 60 * 60 * 24);
        System.out.println("Scheduler initialized successfully");
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		timer.cancel();
		System.out.println("Scheduler Destroyed");
	}
}
