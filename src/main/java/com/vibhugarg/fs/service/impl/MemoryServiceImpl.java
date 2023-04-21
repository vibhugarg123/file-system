package com.vibhugarg.fs.service.impl;

import com.vibhugarg.fs.domain.Datablock;
import com.vibhugarg.fs.service.IMemory;

import java.util.ArrayList;
import java.util.List;

public class MemoryServiceImpl implements IMemory {
    int blockSize = 5;
    byte[] memory;
    int memorySize;
    List<Datablock> dataBlocks;

    public MemoryServiceImpl(int memorySize) throws Exception {
        this.memory = new byte[memorySize];
        this.memorySize = memorySize;
        this.dataBlocks = new ArrayList<>();
        createDataBlocks();
    }

    @Override
    public void createDataBlocks() throws Exception {
        if (memorySize == 0) {
            throw new RuntimeException("Memory Size given is 0");
        }

        int nBlocks = memorySize / blockSize;

        for (int block = 0; block < nBlocks; block++) {
            Datablock dataBlock = new Datablock(blockSize * block, false);
            dataBlocks.add(dataBlock);
        }
    }

    @Override
    public List<Datablock> write(String fileData) {
        byte[] fileBytes = fileData.getBytes();
        List<Datablock> dataBlockList = new ArrayList<>();

        int length = fileBytes.length;
        length = getDataBlockToWrite(dataBlockList, length);
        if (length > 0) {
            throw new RuntimeException("Memory is not available ");
        }
        int index = 0;
        int totalSize = fileBytes.length;
        for (Datablock dataBlock : dataBlockList) {
            dataBlock.setIsOccupied(true);
            for (int i = dataBlock.getStart(); i < (dataBlock.getStart() + blockSize); i++) {
                if (index == totalSize) {
                    break;
                }
                this.memory[i] = fileBytes[index];
                index = index + 1;
            }
        }
        return dataBlockList;
    }

    private int getDataBlockToWrite(List<Datablock> dataBlockList, int length) {
        for (Datablock dataBlock : this.dataBlocks) {
            if (length <= 0) {
                break;
            }
            if (dataBlock.getIsOccupied()) {
                continue;
            }
            dataBlockList.add(dataBlock);
            length = length - blockSize;
        }
        return length;
    }

    @Override
    public String read(List<Datablock> dataBlocks, int size) {
        byte[] fileBytes = new byte[size];
        int index = 0;
        for (Datablock dataBlock : dataBlocks) {
            for (int i = dataBlock.getStart(); i < (dataBlock.getStart() + blockSize); i++) {
                if (index == size) {
                    break;
                }
                fileBytes[index] = memory[i];
                index = index + 1;
            }
        }
        return new String(fileBytes);
    }

    @Override
    public void delete(List<Datablock> dataBlockList) {
        for (Datablock dataBlock : dataBlocks) {
            dataBlock.setIsOccupied(false);
        }
    }

    @Override
    public List<Datablock> update(List<Datablock> dataBlockList, int oldFileSize, String newFileData) {
        //oldFileBytes this is for rollback.
        byte[] oldFileBytes = new byte[oldFileSize];
        int numOfBlocks = dataBlockList.size();

        byte[] newFileBytes = newFileData.getBytes();
        int newFileLen = newFileData.getBytes().length;
        int remainingSize = newFileLen - numOfBlocks * blockSize;
        remainingSize = getMoreDataBlocksForUpdate(dataBlockList, remainingSize);
        if (remainingSize > 0) {
            throw new RuntimeException("Memory not available for update");
        }

        int index = 0;
        try {
            for (Datablock dataBlock : dataBlockList) {
                dataBlock.setIsOccupied(true);
                for (int i = dataBlock.getStart(); i < (dataBlock.getStart() + blockSize); i++) {
                    if (index == newFileLen) {
                        break;
                    }
                    if (index < oldFileSize) {
                        oldFileBytes[index] = memory[i];
                    }
                    memory[i] = newFileBytes[index];
                    index = index + 1;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occured " + e.getMessage());
            e.printStackTrace();
            rollBack(index, oldFileSize, dataBlockList);
        }

        return dataBlockList;
    }

    private int getMoreDataBlocksForUpdate(List<Datablock> dataBlockList, int remainingSize) {
        if (remainingSize > 0) {
            for (Datablock dataBlock : this.dataBlocks) {
                if (remainingSize <= 0) {
                    break;
                }
                if (dataBlock.getIsOccupied()) {
                    continue;
                }
                dataBlockList.add(dataBlock);
                remainingSize = remainingSize - blockSize;
            }
        }
        return remainingSize;
    }

    private void rollBack(int i, int index, List<Datablock> dataBlockList) {
    }
}
