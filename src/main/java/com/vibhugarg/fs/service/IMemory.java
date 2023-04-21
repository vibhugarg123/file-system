package com.vibhugarg.fs.service;

import com.vibhugarg.fs.domain.Datablock;

import java.util.List;

public interface IMemory {
    void createDataBlocks() throws Exception;

    List<Datablock> write(String fileData);

    String read(List<Datablock> dataBlocks, int size);

    void delete(List<Datablock> dataBlockList);

    List<Datablock> update(List<Datablock> dataBlockList, int oldFileSize, String newFileData);
}
