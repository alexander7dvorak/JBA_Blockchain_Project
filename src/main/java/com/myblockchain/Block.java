package com.myblockchain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.random.RandomGenerator;

class Block implements Serializable {
    private static long counter = 0;
    private static final AtomicInteger nFirstZerosInHash = new AtomicInteger(0);

    //    private final Signature signaturePublicKey;
    private final String messageOutput;
    private final String creator;
    private final long id;
    private final List<String> messages = new ArrayList<>();
    private final long timeStamp;
    private final double timeOfWork;
    private String previousBlockHash;
    private long magicNumber;

    Block(Blockchain blockchain, String creator) {
        this.previousBlockHash = blockchain.getLastBlockHash();
        this.creator = creator;
        long startMillis = System.currentTimeMillis();
        while (true) {
            this.previousBlockHash = blockchain.getLastBlockHash();
            this.magicNumber = RandomGenerator.getDefault().nextLong();
            synchronized (Miner.class) {
                if (isPreviousBlockHashLast(blockchain)) {
                    if (isProved()) {
                        this.messages.addAll(blockchain.getMessages());
                        this.id = counter++;
                        this.timeStamp = new Date().getTime();
                        this.timeOfWork = ((double) (System.currentTimeMillis() - startMillis)) / 1000;
                        if (this.timeOfWork < 0.1) {
                            nFirstZerosInHash.incrementAndGet();
                            messageOutput = "N was increased to %s".formatted(nFirstZerosInHash);
                        } else if (this.timeOfWork < 1) {
                            messageOutput = "N stays the same";
                        } else {
                            messageOutput = "N was decreased by 1";
                            nFirstZerosInHash.decrementAndGet();
                        }
                        blockchain.addBlock(this);
                        blockchain.resetMessages();
                        System.err.println(this);
                        break;
                    }
                } else {
                    startMillis = System.currentTimeMillis();
                }
            }
        }
    }

    public boolean isPreviousBlockHashLast(Blockchain blockchain) {
        return previousBlockHash.equals(blockchain.getLastBlockHash());
    }

    public String getHash() {
        return StringUtil.applySha256(creator + previousBlockHash + magicNumber);
    }

    public boolean isProved() {
        return getHash().startsWith("0".repeat(nFirstZerosInHash.get()));
    }

    @Override
    public String toString() {
        StringBuilder outputSB = new StringBuilder();
        outputSB.append("Block:\n");
        outputSB.append("Created by: \"%s\"\n".formatted(creator));
        outputSB.append("%s gets 100 VC\n".formatted(creator));
        outputSB.append("Id: %d\n".formatted(id));
        outputSB.append("Timestamp: %d\n".formatted(timeStamp));
        outputSB.append("Magic number: %d\n".formatted(magicNumber));
        outputSB.append("Hash of the previous block:\n%s\n".formatted(previousBlockHash));
        outputSB.append("Hash of the block:\n%s\n".formatted(getHash()));
        outputSB.append("Block data:\n");
        if (messages.size() == 0) {
            outputSB.append("No transactions\n");
        } else {
            for (String message : messages) {
                outputSB.append(message);
                outputSB.append("\n");
            }
        }
        outputSB.append("Block was generating for %f seconds\n".formatted(timeOfWork));
        outputSB.append(messageOutput);
        return outputSB.toString();
    }
}


