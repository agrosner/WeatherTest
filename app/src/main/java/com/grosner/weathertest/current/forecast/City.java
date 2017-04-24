package com.grosner.weathertest.current.forecast;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.lang.String;

/**
 * This is generated by Jetty. Safe to modify as needed. */
@JsonObject(
    fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS
)
public class City {
  int id;

  String name;

  Coord coord;

  String country;

  int population;
}