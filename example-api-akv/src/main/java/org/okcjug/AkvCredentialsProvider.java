package org.okcjug;

import io.quarkus.arc.Unremovable;
import io.quarkus.credentials.CredentialsProvider;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;

@ApplicationScoped
@Unremovable
public class AkvCredentialsProvider implements CredentialsProvider {



    @Override
    public Map<String, String> getCredentials(String credentialsProviderName) {
        return null;
    }
}
