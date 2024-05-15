package ua.anton.tsa.testassignment.wire.request;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.LinkedHashMap;


@JsonRootName("data")
public class ModifyUserRequest extends LinkedHashMap<String, String> {}
