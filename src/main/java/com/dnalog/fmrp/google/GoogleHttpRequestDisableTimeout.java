/**
 * Created with IntelliJ IDEA.
 * User: andrejus
 * Date: 01/07/2013
 * Time: 00:04
 * To change this template use File | Settings | File Templates.
 */

package com.dnalog.fmrp.google;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;

public class GoogleHttpRequestDisableTimeout implements HttpRequestInitializer {

    public void initialize(HttpRequest request) {
        request.setConnectTimeout(0);
        request.setReadTimeout(0);
    }
}
