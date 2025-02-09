package com.mmstechnology.dmw.api_keycloak_server.util;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;

public class KeycloakProviderV2 {

    private static final String SERVER_URL = "http://localhost:9090";
    private static final String REALM_NAME = "DigitalMoneyWallet";  // Target realm
    private static final String REALM_MASTER = "master";  // Admin login realm
    private static final String ADMIN_CLI = "admin-cli";  // Correct client ID for admin access
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String CLIENT_SECRET = "JOea88i0bWCigDHLYkozmCUfAqAIxMHu";

    // 🔹 **Static Keycloak instance** to reuse across multiple requests
    private static Keycloak keycloakInstance = null;

    /**
     * ✅ Returns a singleton Keycloak instance to avoid multiple connections.
     */
    public static synchronized Keycloak getKeycloakInstance() {
        if (keycloakInstance == null) {
            keycloakInstance = KeycloakBuilder.builder()
                    .serverUrl(SERVER_URL)
                    .realm(REALM_MASTER)  // Admin authentication realm
                    .clientId(ADMIN_CLI)  // Must match Keycloak client
                    .username(ADMIN_USER)
                    .password(ADMIN_PASSWORD)
                    .clientSecret(CLIENT_SECRET)
                    .resteasyClient(new ResteasyClientBuilderImpl()
                            .connectionPoolSize(10)
                            .build())
                    .build();
        }
        return keycloakInstance;
    }

    /**
     * ✅ Returns the realm resource for "DigitalMoneyWallet".
     */
    public static RealmResource getRealmResource() {
        return getKeycloakInstance().realm(REALM_NAME);
    }

    /**
     * ✅ Returns the users resource from the target realm.
     */
    public static UsersResource getUserResource() {
        return getRealmResource().users();
    }
}
