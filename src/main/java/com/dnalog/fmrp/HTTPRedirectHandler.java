/**
 * Copyright 2013 www.smartdeveloping.com
 * User: andrewboss
 * Date: 04/08/2013
 * Time: 21:03
 */

package com.dnalog.fmrp;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.HttpContext;

public class HTTPRedirectHandler extends DefaultRedirectHandler {

    public URI lastRedirectedUri;

    @Override
    public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
        boolean isRedirect = super.isRedirectRequested(response, context);
        if (!isRedirect) {
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 301 || responseCode == 302) {
                return true;
            }
        }
        return isRedirect;
    }

    @Override
    public URI getLocationURI(HttpResponse response, HttpContext context)
            throws ProtocolException {

        lastRedirectedUri = super.getLocationURI(response, context);

        return lastRedirectedUri;
    }

}
