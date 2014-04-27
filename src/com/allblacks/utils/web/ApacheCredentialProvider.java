package com.allblacks.utils.web;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

public class ApacheCredentialProvider {

    private static CredentialsProvider credentialsProvider;

    public static synchronized CredentialsProvider getCredentialsProvider(final String username, final String password) {

        if (credentialsProvider == null) {
            credentialsProvider = new BasicCredentialsProvider();

            AuthScope scope = new AuthScope(AuthScope.ANY);
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username,password);

            credentialsProvider.setCredentials(scope, creds);
        }
        return credentialsProvider;
    }
}
