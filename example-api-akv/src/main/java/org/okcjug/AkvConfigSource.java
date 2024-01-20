package org.okcjug;

import io.quarkus.arc.Arc;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.spi.ConfigSource;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AkvConfigSource implements ConfigSource {

    AkvClient akvClient;
    private volatile boolean firstTime = true;
    private Set<String> propertyNames = new HashSet<>();

    public AkvConfigSource(AkvClient akvClient) {
        this.akvClient = akvClient;
    }
    @Override
    public Set<String> getPropertyNames() {
        return Collections.emptySet();
    }

    @Override
    public int getOrdinal() {
        return 50;
    }

    @Override
    public String getValue(String secretName) {
        if (firstTime) {
            propertyNames = akvClient.getSecretNames().await().indefinitely();
            firstTime = false;
        }
        if (! propertyNames.contains(secretName)) {
            return null;
        }
        return akvClient.getSecret(secretName).await().indefinitely();
    }

    @Override
    public String getName() {
        return "azureKeyVault";
    }

}
