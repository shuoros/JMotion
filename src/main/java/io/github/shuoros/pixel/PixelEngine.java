package io.github.shuoros.pixel;

import io.github.shuoros.pixel.graphics.Graphic;
import io.github.shuoros.pixel.window.Panel;
import io.github.shuoros.pixel.window.Window;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.*;
import java.time.Duration;

@Service
public class PixelEngine extends SpringApplication implements Runnable {

    private static final Log log = LogFactory.getLog(PixelEngine.class);
    private static final SpringBootHelper springBootHelper = new SpringBootHelper();
    private static boolean running;
    private final String[] args;
    private final Dimension dimension = new Dimension(100, 100);
    private boolean logStartupInfo;
    private Class<?> mainApplicationClass;
    private ConfigurableApplicationContext context;
    private FrameRate frameRate;
    private Graphic graphic;
    private Window window;
    private Panel panel;

    public PixelEngine(String[] args, Class<?>... primarySources) {
        super(primarySources);
        super.setBanner(new PixelEngineBanner());
        super.setLogStartupInfo(false);
        this.logStartupInfo = true;
        this.args = args;
        this.mainApplicationClass = springBootHelper.deduceMainApplicationClass();
        this.frameRate = new FrameRate((short) 60);
        this.graphic = new PixelGraphic(dimension);
        this.window = new PixelWindow(this.mainApplicationClass.getName(), dimension);
        this.panel = new PixelPanel();
        this.panel.construct(this.window.getDimension());
        this.window.construct(this.panel);
    }

    @Override
    public void run() {
        final long startTime = System.nanoTime();

        this.context = runSpring();
        logSpringStartupDetails(this.context, startTime);

        runEngine();
        logEngineStartupDetails(startTime);

        logLoopStartUpDetails(startTime);
        loop();
    }

    private void loop() {
        this.running = true;
        frameRate.init();
        int lastFPS = 0;
        while (running) {
            if (frameRate.shouldRender()) {
                update();
                input();
                render();
                draw();
                frameRate.calculate();
            }
            lastFPS = logFPS(lastFPS);
        }
    }

    private void update() {
        this.panel.update();
    }

    public void input() {
        //this.panel.input(this.controller);
    }

    private void render() {
        this.panel.render(this.graphic.get2DGraphic());
    }

    private void draw() {
        Graphics g = (Graphics) this.panel.getGraphics();
        g.drawImage(
                this.graphic.getBufferedImage(),
                0,
                0,
                this.window.getDimension().width,
                this.window.getDimension().height, null);
        g.dispose();
    }

    private ConfigurableApplicationContext runSpring() {
        return super.run(this.args);
    }

    private void runEngine() {
        runWindow();
    }

    private void runWindow() {
        this.window.construct(this.panel);
    }

    private int logFPS(int lastFPS) {
        if (frameRate.getFPS() != lastFPS) {
            lastFPS = frameRate.getFPS();
            log.debug("The Engine is running on " + lastFPS + "FPS");
        }
        return lastFPS;
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

    private void logLoopStartUpDetails(long startTime) {
        if (this.logStartupInfo) {
            this.logLoopStarted(getTimeTakenToStartup(startTime));
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

    private void logLoopStarted(Duration timeTakenToStartup) {
        (new StartupInfoLogger(this.mainApplicationClass))
                .logLoopStarted(this.getApplicationLog(), timeTakenToStartup);
    }

    private static Duration getTimeTakenToStartup(long startTime) {
        return Duration.ofNanos(System.nanoTime() - startTime);
    }

}