package com.shetu.entity;

import com.shetu.utility.Constants;
import org.apache.commons.lang3.Validate;

import java.time.ZonedDateTime;
import java.util.List;

public class City {
//  private String id;
//  private String type;
  private String name;
  private String country;
  private String state;
  private boolean capital;
  private Long population;
  private List<String> regions;

  public City(String name, String country, String state, boolean capital, Long population, List<String> regions) {
    this.name = name;
    this.country = country;
    this.state = state;
    this.capital = capital;
    this.population = population;
    this.regions = regions;
  }

  public String getName() {
    return name;
  }

  public String getCountry() {
    return country;
  }

  public String getState() {
    return state;
  }

  public boolean isCapital() {
    return capital;
  }

  public Long getPopulation() {
    return population;
  }

  public List<String> getRegions() {
    return regions;
  }

  // todo: Uncomment to understand the builder pattern
//  // Builder class
//  public City(Builder builder){
//    id = IdGenerator.uuid();
//    type = this.getClass().getSimpleName();
//    name = Validate.notBlank(builder.name, Constants.Msg.NOT_NULL_OR_EMPTY,"name");
//    country = Validate.notBlank(builder.country, Constants.Msg.NOT_NULL_OR_EMPTY,"country");
//    state = Validate.notBlank(builder.state, Constants.Msg.NOT_NULL_OR_EMPTY,"state");
//    capital = builder.capital;
//    population = builder.population;
//    regions = builder.regions;
//  }
//
//  //===========================================================================
//  // BUILDER
//  //===========================================================================
//  public static class Builder<T extends Builder> {
//    private String name;
//    private String country;
//    private String state;
//    private boolean capital;
//    private Long population;
//    private List<String> regions;
//    private ZonedDateTime generatedAt;
//
//    /**
//     * Builder constructor.
//     * @param name name
//     * @param country country
//     * @param state state
//     * @param capital capital
//     * @param population population
//     * @param regions regions
//     */
//    public Builder(String name, String country, String state, boolean capital, Long population, List<String> regions) {
//      this.name = name;
//      this.country = country;
//      this.state = state;
//      this.capital = capital;
//      this.population = population;
//      this.regions = regions;
//    }
//
//    public T generatedAt(ZonedDateTime val){
//      generatedAt = val;
//      return getInstance();
//    }
//
//    public T getInstance(){
//      return (T) this;
//    }
//  }

  // todo: Watch rakizo-libs>Event.java
  //=============================================================================
  // UTILITY METHODS

  //=============================================================================
}
