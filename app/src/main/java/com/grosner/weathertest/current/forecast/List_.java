package com.grosner.weathertest.current.forecast;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.List;

/**
 * This is generated by Jetty. Safe to modify as needed. */
@JsonObject(
    fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS
)
public class List_ {
  long dt;

  Temp temp;

  double pressure;

  int humidity;

  List<Weather_> weather;

  double speed;

  int deg;

  int clouds;

  double rain;
}
