package ca.lavers.joa.sunhttp;

import ca.lavers.joa.core.AbstractResponse;

import java.io.InputStream;

public class SunHttpResponse extends AbstractResponse {

    protected InputStream getResponseStream() {
        if(this.body != null) {
            return this.body;
        }
        else {
            // An empty InputStream
            return new InputStream() {
                @Override
                public int read() {
                    return -1;  // end of stream
                }
            };
        }
    }

}
