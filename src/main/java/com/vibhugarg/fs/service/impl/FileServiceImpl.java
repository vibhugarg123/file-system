package com.vibhugarg.fs.service.impl;

import com.vibhugarg.fs.domain.Datablock;
import com.vibhugarg.fs.service.IFile;
import com.vibhugarg.fs.service.IMemory;

import java.util.List;

public class FileServiceImpl implements IFile {

    private IMemory memoryService;

    public FileServiceImpl(IMemory memoryService) {
        this.memoryService = memoryService;
    }

    @Override
    public List<Datablock> create(String fileName, String fileData) {
        List<Datablock> datablocks = memoryService.write(fileData);
        return datablocks;
    }

    @Override
    public String read(List<Datablock> dataBlocks, int size) {
        String fileData = memoryService.read(dataBlocks, size);
        return fileData;
    }

    @Override
    public void delete(List<Datablock> dataBlockList) {
        memoryService.delete(dataBlockList);
    }

    @Override
    public List<Datablock> update(List<Datablock> dataBlockList, int oldFileSize, String newFileData) {
        List<Datablock> datablocks = memoryService.update(dataBlockList, oldFileSize, newFileData);
        return datablocks;
    }
}
