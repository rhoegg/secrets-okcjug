package org.okcjug;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "azure")
public interface AkvRuntimeConfig {
    @WithName("sp")
    AzureServicePrincipal servicePrincipal();

    @WithName("keyvault.url")
    String vaultUrl();
}
