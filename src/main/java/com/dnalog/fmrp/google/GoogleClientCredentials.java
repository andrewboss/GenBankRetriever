/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

/**
 * Created with IntelliJ IDEA.
 * User: andrejus
 * Date: 30/06/2013
 * Time: 22:00
 * To change this template use File | Settings | File Templates.
 */

package com.dnalog.fmrp.google;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dnalog.fmrp.Main;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.bigquery.model.*;

public class GoogleClientCredentials {

    private static GoogleAuthorizationCodeFlow flow = null;

    public static GoogleClientSecrets loadClientSecrets(String clientSecretsPath) {
        try {
            return GoogleClientSecrets.load(new JacksonFactory(), new FileInputStream(clientSecretsPath));
        } catch (Exception e) {
            System.out.println("Could not load client_secrets.json");
            e.printStackTrace();
        }

        return null;
    }

    public static List loadScopes(String scopeName) throws Exception {

        String scope = BigqueryScopes.BIGQUERY;

        if(!scopeName.equalsIgnoreCase("bigquery")) {
            throw new Exception("Only BigQuery Scope is currently implemented!");
        }

        return Arrays.asList(scope);
    }

    public static Credential authorize(GoogleClientSecrets clientSecrets, List scopes) throws Exception{

        String authorizationCode = "";
        String authorizeUrl = new GoogleAuthorizationCodeRequestUrl(clientSecrets,
                (clientSecrets.getDetails().getRedirectUris().get(0)), scopes).setState("").build();

        System.out.println("Paste this URL into a web browser to authorize BigQuery Access:\n" + authorizeUrl);

        System.out.println("... and type the code you received here: ");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        authorizationCode = in.readLine();

        GoogleAuthorizationCodeFlow flow = getFlow(clientSecrets, scopes);

        GoogleTokenResponse response = flow.newTokenRequest(authorizationCode).setRedirectUri (
                clientSecrets.getDetails().getRedirectUris().get(0)
        ).execute();

        System.out.println("Token response: " + response);

        return flow.createAndStoreCredential(response, null);
    }

    public static GoogleAuthorizationCodeFlow getFlow(GoogleClientSecrets clientSecrets, List scopes) {
        if (flow == null) {
            HttpTransport httpTransport = new NetHttpTransport();
            JacksonFactory jsonFactory = new JacksonFactory();

            flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport,
                    jsonFactory,
                    clientSecrets,
                    Arrays.asList(
                            "https://www.googleapis.com/auth/bigquery"))
                    .setAccessType("offline").setApprovalPrompt("force").build();
        }
        return flow;
    }

}
