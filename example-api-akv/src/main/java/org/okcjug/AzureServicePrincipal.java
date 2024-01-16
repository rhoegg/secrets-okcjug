package org.okcjug;

import io.quarkus.runtime.annotations.StaticInitSafe;
import io.smallrye.config.ConfigMapping;

@StaticInitSafe
@ConfigMapping(prefix = "azure.sp")
public interface AzureServicePrincipal {
    String tenant();
    String client();
    String secret();
}
