package ua.anton.tsa.testassignment;

import lombok.experimental.UtilityClass;

/**
 * Utility class for saving constants
 */
@UtilityClass
@SuppressWarnings("RedundantModifiersUtilityClassLombok")
public class Constants {

    // SERVICE
    public static final String SERVICE_PACKAGE = "ua.anton.tsa";
    public static final String SERVICE_NAME = "testassignment";
    public static final String ROOT_PACKAGE = SERVICE_PACKAGE + "." + SERVICE_NAME;

    // REST
    public static final String URL_SEPARATOR = "/";
    public static final String API_V1 = "/api/v1";
}