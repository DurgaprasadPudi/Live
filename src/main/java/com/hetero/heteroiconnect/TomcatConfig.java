package com.hetero.heteroiconnect;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return container -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                ((TomcatEmbeddedServletContainerFactory) container).addConnectorCustomizers((Connector connector) -> {
                    connector.setMaxPostSize(-1); // Allow unlimited POST size
                    connector.setMaxSavePostSize(-1);
                });
            }
        };
    }
}
