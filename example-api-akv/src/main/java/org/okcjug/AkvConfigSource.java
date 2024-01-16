package org.okcjug;

import io.quarkus.arc.Arc;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.lang.annotation.Annotation;
import java.util.Set;

@ApplicationScoped
public class AkvConfigSource implements ConfigSource {
    @Override
    public Set<String> getPropertyNames() {
        return getClient().getSecretNames().await().indefinitely();
    }

    @Override
    public int getOrdinal() {
        return 127;
    }

    @Override
    public String getValue(String secretName) {
        return getClient().getSecret(secretName).await().indefinitely();
    }

    @Override
    public String getName() {
        return "azureKeyVault";
    }

    private AkvClient getClient() {
        return (AkvClient) Arc.container().instance(AkvClient.class);
    }
}
