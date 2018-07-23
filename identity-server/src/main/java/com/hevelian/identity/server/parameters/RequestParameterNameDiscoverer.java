/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Modifications copyright (C) 2018 Hevelian.
 * NOTICE: This class is an adapted version of
 * org.springframework.security.core.parameters.AnnotationParameterNameDiscoverer
 * class. Some sources including class name have been modified.
 */
package com.hevelian.identity.server.parameters;

import com.google.common.base.Strings;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Discover parameters name by @{@link RequestParam} annotation (name/value field).
 * If not found - get method parameters name using {@link DefaultParameterNameDiscoverer}.
 * This discoverer was implemented specifically for RESTful API parameters validation in
 * Spring Controllers, like 'page', 'size', etc., but will also work with other layers
 * through {@link DefaultParameterNameDiscoverer}.
 */
public class RequestParameterNameDiscoverer implements ParameterNameDiscoverer {

  private final DefaultParameterNameDiscoverer discoverer;

  private final ParameterNameFactory<Constructor<?>> constructorParamFactory = new ParameterNameFactory<Constructor<?>>() {

    public Annotation[][] findParameterAnnotations(Constructor<?> constructor) {
      return constructor.getParameterAnnotations();
    }

    @Override
    public String[] findParameterNames(Constructor<?> constructor) {
      return discoverer.getParameterNames(constructor);
    }
  };

  private final ParameterNameFactory<Method> methodParamFactory = new ParameterNameFactory<Method>() {

    public Annotation[][] findParameterAnnotations(Method method) {
      return method.getParameterAnnotations();
    }

    @Override
    public String[] findParameterNames(Method method) {
      return discoverer.getParameterNames(method);
    }
  };

  public RequestParameterNameDiscoverer() {
    this.discoverer = new DefaultParameterNameDiscoverer();
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.core.ParameterNameDiscoverer#getParameterNames(java
   * .lang.reflect.Method)
   */
  public String[] getParameterNames(Method method) {
    Method originalMethod = BridgeMethodResolver.findBridgedMethod(method);
    String[] paramNames = lookupParameterNames(methodParamFactory,
        originalMethod);
    if (paramNames != null) {
      return paramNames;
    }
    Class<?> declaringClass = method.getDeclaringClass();
    Class<?>[] interfaces = declaringClass.getInterfaces();
    for (Class<?> intrfc : interfaces) {
      Method intrfcMethod = ReflectionUtils.findMethod(intrfc, method.getName(),
          method.getParameterTypes());
      if (intrfcMethod != null) {
        return lookupParameterNames(methodParamFactory, intrfcMethod);
      }
    }
    return paramNames;
  }

  /*
   * (non-Javadoc)
   *
   * @see org.springframework.core.ParameterNameDiscoverer#getParameterNames(java
   * .lang.reflect.Constructor)
   */
  public String[] getParameterNames(Constructor<?> constructor) {
    return lookupParameterNames(constructorParamFactory, constructor);
  }

  /**
   * Gets the parameters names or null if not found.
   *
   * @param parameterNameFactory The {@link ParameterNameFactory} to use
   * @param t                    The {@link AccessibleObject} to find the parameters names on (i.e. Method
   *                             or Constructor)
   * @return The parameters names or null
   */
  private <T extends AccessibleObject> String[] lookupParameterNames(ParameterNameFactory<T> parameterNameFactory, T t) {
    Annotation[][] parameterAnnotations = parameterNameFactory.findParameterAnnotations(t);
    String[] methodParameterNames = parameterNameFactory.findParameterNames(t);
    int parameterCount = parameterAnnotations.length;
    String[] paramNames = new String[parameterCount];
    boolean found = false;
    for (int i = 0; i < parameterCount; i++) {
      Annotation[] annotations = parameterAnnotations[i];
      String parameterName = findParameterName(annotations);
      if (parameterName != null) {
        found = true;
        paramNames[i] = parameterName;
      } else if (methodParameterNames != null) {
        found = true;
        paramNames[i] = methodParameterNames[i];
      }
    }
    return found ? paramNames : null;
  }

  /**
   * Finds the parameters name from the provided {@link Annotation}s if {@link RequestParam} annotation is found
   * or null if it could not find it.
   *
   * @param parameterAnnotations The {@link Annotation}'s to search.
   * @return {@link RequestParam} name/value or null
   */
  
  private String findParameterName(Annotation[] parameterAnnotations) {
    for (Annotation paramAnnotation : parameterAnnotations) {
      if (RequestParam.class.getName().equals(paramAnnotation.annotationType()
          .getName())) {
        String value = (String) AnnotationUtils.getValue(paramAnnotation, "name");
        if (Strings.isNullOrEmpty(value)) {
          value = (String) AnnotationUtils.getValue(paramAnnotation, "value");
        }
        return Strings.isNullOrEmpty(value) ? null : value;
      }
    }
    return null;
  }

  /**
   * Strategy interface for looking up the parameters names.
   *
   * @param <T> The type to inspect (i.e. {@link Method} or {@link Constructor})
   */
  private interface ParameterNameFactory<T extends AccessibleObject> {

    /**
     * Gets the {@link Annotation}s at a specified index.
     *
     * @param t
     * @return
     */
    Annotation[][] findParameterAnnotations(T t);

    /**
     * Gets the parameters names from {@link RequestParam} annotation.
     *
     * @param t
     * @return
     */
    String[] findParameterNames(T t);
  }
}