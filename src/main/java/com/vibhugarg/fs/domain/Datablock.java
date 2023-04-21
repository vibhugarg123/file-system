package com.vibhugarg.fs.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Datablock {
    private int start;
    private Boolean isOccupied;
}

