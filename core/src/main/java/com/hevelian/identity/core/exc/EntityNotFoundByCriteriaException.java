package com.hevelian.identity.core.exc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EntityNotFoundByCriteriaException extends Exception {
    private static final long serialVersionUID = 2313582704926199258L;
    private final String criteria;
    private final String value;

}
