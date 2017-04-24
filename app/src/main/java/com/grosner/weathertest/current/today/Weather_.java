package com.grosner.weathertest.current.today;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.lang.String;

/**
 * This is generated by Jetty. Safe to modify as needed. */
@JsonObject(
    fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS
)
public class Weather_ {
  int id;

  @JsonField(
      name = "main"
  )
  String main_;

  String description;

  String icon;
}
