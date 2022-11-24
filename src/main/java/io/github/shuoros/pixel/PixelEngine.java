package io.github.shuoros.pixel;

import io.github.shuoros.pixel.engine.FrameRate;
import io.github.shuoros.pixel.engine.GameStateManager;
import io.github.shuoros.pixel.graphics.Graphic;
import io.github.shuoros.pixel.graphics.PixelGraphic;
import io.github.shuoros.pixel.window.AbstractPanel;
import io.github.shuoros.pixel.window.AbstractWindow;
import io.github.shuoros.pixel.window.PixelPanel;
import io.github.shuoros.pixel.window.PixelWindow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Service
public class PixelEngine extends SpringApplication implements Runnable {

    private static final Log log = LogFactory.getLog(PixelEngine.class);
    private final String[] args;
    private boolean logStartupInfo;
    private Class<?> mainApplicationClass;
    private ConfigurableApplicationContext context;
    private Thread gameLoop;
    private FrameRate frameRate;
    private GameStateManager gameStateManager;
    private Graphic graphic;
    private AbstractWindow window;

    public PixelEngine(String[] args, Class<?>... primarySources) {
        super(primarySources);
        super.setBanner(new PixelEngineBanner());
        super.setLogStartupInfo(false);
        this.logStartupInfo = true;
        this.args = args;
        this.mainApplicationClass = SpringBootHelper.deduceMainApplicationClass();
    }

    @Override
    public void run() {
        final long startTime = System.nanoTime();
        this.context = runSpring();
        logSpringStartupDetails(this.context, startTime);
        runEngine();
        logEngineStartupDetails(startTime);
        runLoop();
        logLoopStartedDetails(startTime);
    }

    private void runLoop() {
        gameLoop = new Thread(() -> loop());
        gameLoop.start();
    }

    private void loop() {
        final boolean forever = true;
        frameRate.init();
        int lastFPS = 0;
        while (forever) {
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
        this.window.getPanel().update();
    }

    public void input() {
        //this.panel.input(this.controller);
    }

    private void render() {
        this.window.getPanel().render(this.graphic.get2DGraphic(), gameStateManager.currentScene());
    }

    private void draw() {
        Graphics g = (Graphics) this.window.getPanel().getGraphics();
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
        setupEngine();
        runWindow();
    }

    private void setupEngine() {
        this.frameRate = createFrameRateInstance();
        this.gameStateManager = new GameStateManager();
    }

    private FrameRate createFrameRateInstance() {
        return new FrameRate(
                Integer.parseInt(
                        this.context.getEnvironment().getProperty("pixel.engine.frame-rate", "60")
                )
        );
    }

    private void runWindow() {
        this.window = getWindowInstance();
        this.graphic = new PixelGraphic(this.window.getDimension());
        final AbstractPanel panel = getPanelInstance();
        panel.construct(this.window.getDimension());

        this.window.construct(panel);
    }

    private AbstractWindow getWindowInstance() {
        if (getClassByWindowAnnotation().isPresent()) {
            return getClassByWindowAnnotation().get();
        }
        return createPixelWindowInstance();
    }

    private Optional<AbstractWindow> getClassByWindowAnnotation() {
        return getBeanByTypeAndAnnotation(AbstractWindow.class, io.github.shuoros.pixel.annotation.Window.class);
    }

    private AbstractPanel getPanelInstance() {
        if (getClassByPanelAnnotation().isPresent()) {
            return getClassByPanelAnnotation().get();
        }
        return new PixelPanel(
                this.context.getEnvironment().getProperty("pixel.panel.background-color.r", Integer.class, 240),
                this.context.getEnvironment().getProperty("pixel.panel.background-color.g", Integer.class, 148),
                this.context.getEnvironment().getProperty("pixel.panel.background-color.b", Integer.class, 73)
        );
    }

    private Optional<AbstractPanel> getClassByPanelAnnotation() {
        return getBeanByTypeAndAnnotation(AbstractPanel.class, io.github.shuoros.pixel.annotation.Panel.class);
    }

    private <T> Collection<T> getBeansByTypeAndAnnotation(Class<T> clazz, Class<? extends Annotation> annotationType) {
        Map<String, T> typedBeans = this.context.getBeansOfType(clazz);
        Map<String, Object> annotatedBeans = this.context.getBeansWithAnnotation(annotationType);
        typedBeans.keySet().retainAll(annotatedBeans.keySet());
        return typedBeans.values();
    }

    private <T> Optional<T> getBeanByTypeAndAnnotation(Class<T> clazz, Class<? extends Annotation> annotationType) {
        Collection<T> beans = getBeansByTypeAndAnnotation(clazz, annotationType);
        return beans.stream().findFirst();
    }

    private PixelWindow createPixelWindowInstance() {
        if (this.context.getEnvironment().getProperty("pixel.window.fullscreen", Boolean.class, false)) {
            return new PixelWindow(
                    this.context.getEnvironment().getProperty("pixel.window.title", this.mainApplicationClass.getSimpleName()),
                    this.context.getEnvironment().getProperty("pixel.window.undecorated", Boolean.class, false)
            );
        }
        return new PixelWindow(
                this.context.getEnvironment().getProperty("pixel.window.title", this.mainApplicationClass.getSimpleName()),
                this.context.getEnvironment().getProperty("pixel.window.undecorated", Boolean.class, false),
                this.context.getEnvironment().getProperty("pixel.window.width", Integer.class, 400),
                this.context.getEnvironment().getProperty("pixel.window.height", Integer.class, 400)
        );
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

    private void logLoopStartedDetails(long startTime) {
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