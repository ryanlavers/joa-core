package ca.lavers.joa.sunhttp;

import ca.lavers.joa.core.AbstractResponse;

import java.io.InputStream;

public class SunHttpResponse extends AbstractResponse {

    protected InputStream getResponseStream() {
        return this.body;
    }

}
