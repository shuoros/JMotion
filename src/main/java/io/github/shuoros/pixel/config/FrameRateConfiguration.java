package io.github.shuoros.pixel.config;

import io.github.shuoros.pixel.FrameRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class FrameRateConfiguration {

    @Value("{loop.renderer.frequency}")
    private short frequency;

    @Bean
    public FrameRate getFrameRate() {
        return new FrameRate(Optional.of(frequency).orElse((short) 60));
    }

}
