package com.myblockchain;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class Blockchain implements Serializable {
    private final List<Block> generatedBlocks = new CopyOnWriteArrayList<>();
    private final List<String> messages = new CopyOnWriteArrayList<>();

    public synchronized void addBlock(Block b) {
        generatedBlocks.add(b);
    }

    public synchronized String getLastBlockHash() {
        return generatedBlocks.size() == 0 ? "0" : generatedBlocks.get(generatedBlocks.size() - 1).getHash();
    }

    public String generateNewBlock(Blockchain blockchain, String creator) {
        Block b = new Block(blockchain, creator);
//            saveToFile();
        return b.getHash();
    }

    public void resetMessages() {
        this.messages.clear();
    }

    private static void saveToFile() {

    }

    public static void loadFromFile(File f) {

    }

    public static boolean isValid(Block b) {

        return true;
    }

    public boolean isValid() {
        for (Block b : generatedBlocks) {
            if (!isValid(b)) {
                return false;
            }
        }
        return true;
    }

    public synchronized List<Block> getGeneratedBlocks() {
        return this.generatedBlocks;
    }

    public List<String> getMessages() {
        return this.messages;
    }
}

