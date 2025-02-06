package com.mmstechnology.dmw.api_keycloak.util;

import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientImpl;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;

public class KeycloakProvider {

    private static final String SERVER_URL = "http://localhost:9090";
    //private static final String REALM_NAME = "spring-boot-realm-dev";
    private static final String REALM_NAME = "spring-client-api-rest";
    private static final String REALM_MASTER = "master";
    private static final String ADMIN_CLI = "admin_cli";
    private static final String USER_CONSOLE = "admin";
    private static final String PASSWORD_CONSOLE = "admin";
    private static final String CLIENT_SECRET = "JOea88i0bWCigDHLYkozmCUfAqAIxMHu";

    public static RealmResource getRealmResource(){
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(SERVER_URL)
                .realm(REALM_MASTER)
                .clientId(ADMIN_CLI)
                .username(USER_CONSOLE)
                .password(PASSWORD_CONSOLE)
                .clientSecret(CLIENT_SECRET)
                .resteasyClient(new ResteasyClientBuilderImpl()
                        .connectionPoolSize(10)
                        .build())
                .build();
    }

}
