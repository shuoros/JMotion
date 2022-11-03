package io.github.shuoros.pixel;

import io.github.shuoros.pixel.window.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.awt.*;
import java.time.Duration;

public class PixelEngine extends SpringApplication implements Runnable {

    private static final Log log = LogFactory.getLog(PixelEngine.class);
    private static final SpringBootHelper springBootHelper = new SpringBootHelper();
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final String[] args;
    private boolean logStartupInfo;
    private Class<?> mainApplicationClass;
    private ConfigurableApplicationContext context;
    private Window window;

    public PixelEngine(String[] args, Class<?>... primarySources) {
        super(primarySources);
        super.setBanner(new PixelEngineBanner());
        super.setLogStartupInfo(false);
        this.logStartupInfo = true;
        this.mainApplicationClass = springBootHelper.deduceMainApplicationClass();
        this.args = args;
        this.window = new PixelWindow(this.mainApplicationClass.getName(), screenSize);
    }

    @Override
    public void run() {
        final long startTime = System.nanoTime();

        this.context = runSpring();
        logSpringStartupDetails(this.context, startTime);

        runEngine();
        logEngineStartupDetails(startTime);
    }

    private ConfigurableApplicationContext runSpring() {
        return super.run(this.args);
    }

    private void runEngine() {

    }

    private void logSpringStartupDetails(ConfigurableApplicationContext context, long startTime) {
        if (this.logStartupInfo) {
            this.logStartupInfo(context.getParent() == null);
            this.logStartupProfileInfo(context);
            this.logSpringStarted(getTimeTakenToStartup(startTime));
        }
    }

    private void logEngineStartupDetails(long startTime) {
        if (this.logStartupInfo) {
            this.logEngineStarted(getTimeTakenToStartup(startTime));
        }
    }

    private void logSpringStarted(Duration timeTakenToStartup) {
        (new StartupInfoLogger(this.mainApplicationClass))
                .logSpringStarted(this.getApplicationLog(), timeTakenToStartup);
    }

    private void logEngineStarted(Duration timeTakenToStartup) {
        (new StartupInfoLogger(this.mainApplicationClass))
                .logEngineStarted(this.getApplicationLog(), timeTakenToStartup);
    }

    private static Duration getTimeTakenToStartup(long startTime) {
        return Duration.ofNanos(System.nanoTime() - startTime);
    }

}