package com.shetu.respository;

import java.util.List;

public interface AbstractRepository<T> {
  T insert(T model);
  T find(String key);
  List<T> findAll();
  T update(T model, T updateModel);
  T delete(String key);
}
