package com.ellactron.configuration;

/**
 * Created by ji.wang on 2017-07-24.
 */

public class ServiceSchema {
    String protocol;
    String hostname;
    int port;
    Boolean requestClientCert;
    Boolean requestAuth;
    public ServiceSchema(
            String protocol,
            String hostname,
            int port,
            Boolean requestClientCert,
            Boolean requestAuth){
        this.protocol = protocol;
        this.hostname = hostname;
        this.port = port;
        this.requestClientCert=requestClientCert;
        this.requestAuth=requestAuth;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHostname() {
        return hostname;
    }

    public String getPort() {
        return 0==port?"8080":String.valueOf(port);
    }

    public Boolean getRequestClientCert() {
        return requestClientCert;
    }

    public Boolean getRequestAuth() {
        return requestAuth;
    }
}
