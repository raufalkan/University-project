package ua.com.foxminded.task.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

@TestConfiguration
@ComponentScan(basePackages = { "ua.com.foxminded.task" })
@EnableAspectJAutoProxy
@PropertySource("classpath:application.properties")
public class TestConfig {

}
