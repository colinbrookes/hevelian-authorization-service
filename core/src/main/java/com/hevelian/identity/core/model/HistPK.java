package com.hevelian.identity.core.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Composite key to be used in history tables.
 */
@Getter
@Setter
public class HistPK implements Serializable {
    private static final long serialVersionUID = -4066367640815260129L;
    private Long id;
    private Integer version;

    public HistPK() {
    }
}