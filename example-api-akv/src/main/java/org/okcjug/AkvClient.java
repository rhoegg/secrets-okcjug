package org.okcjug;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretAsyncClient;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;
import com.azure.security.keyvault.secrets.models.SecretProperties;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.concurrent.Flow;

@ApplicationScoped
public class AkvClient {

    private SecretAsyncClient secretClient;
    public AkvClient(AzureServicePrincipal sp, @ConfigProperty(name = "azure.keyvault.url") String url) {
        ClientSecretCredential cred = new ClientSecretCredentialBuilder()
                .tenantId(sp.tenant())
                .clientId(sp.client())
                .clientSecret(sp.secret())
                .build();
        secretClient = new SecretClientBuilder()
                .vaultUrl(url)
                .credential(cred)
                .buildAsyncClient();
    }

    public Uni<String> getSecret(String secretName) {
        return Uni.createFrom()
                .publisher((Flow.Publisher<KeyVaultSecret>) secretClient.getSecret(secretName))
                .map(secret -> secret.getValue());
    }

    public Uni<Set<String>> getSecretNames() {
        return Multi.createFrom()
                .publisher((Flow.Publisher<SecretProperties>)secretClient.listPropertiesOfSecrets())
                .map(secretProps -> secretProps.getName())
                .collect().asSet();
    }

}
