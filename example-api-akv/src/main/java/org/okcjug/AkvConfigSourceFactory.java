package org.okcjug;

import io.smallrye.config.ConfigSourceContext;
import io.smallrye.config.ConfigSourceFactory;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.util.List;

public class AkvConfigSourceFactory implements ConfigSourceFactory.ConfigurableConfigSourceFactory<AkvRuntimeConfig> {
    @Override
    public Iterable<ConfigSource> getConfigSources(ConfigSourceContext configSourceContext, AkvRuntimeConfig config) {
        return List.of(new AkvConfigSource(new AkvClient(config.servicePrincipal(), config.vaultUrl())));
    }
}
