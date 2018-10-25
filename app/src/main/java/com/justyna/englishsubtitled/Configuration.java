package com.justyna.englishsubtitled;

/**
 * I need this for variables being available from a static context (unlike strings.xml)
 */
public class Configuration {
    private static final Configuration ourInstance = new Configuration();

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
    }

    private final String backendUrl = "https://ec2-34-215-76-93.us-west-2.compute.amazonaws.com"; //"http://10.0.2.2:8080";

    public String getBackendUrl() {
        return backendUrl;
    }
}
