package com.vibhugarg.fs.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/*
    Metadata has unique UUID, filename, startIndex and lastIndex of files.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Metadata {
    private UUID uuid;
    private String fileName;
    private int size;
    private List<Datablock> datablockList;
}
