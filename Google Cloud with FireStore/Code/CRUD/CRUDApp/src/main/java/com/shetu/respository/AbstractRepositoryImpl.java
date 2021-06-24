package com.shetu.respository;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AbstractRepositoryImpl implements AbstractRepository{
  @Override
  public Object insert(Object model) {
    return null;
  }

  @Override
  public Object find(String key) {
    return null;
  }

  @Override
  public List findAll() {
    return null;
  }

  @Override
  public Object update(Object model, Object updateModel) {
    return null;
  }

  @Override
  public Object delete(String key) {
    return null;
  }
}
