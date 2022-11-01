package com.myblockchain;

public class Miner extends Thread {
    Blockchain chain;

    public Miner(Blockchain blockchain) {
        chain = blockchain;
    }

    @Override
    public void run() {
        chain.generateNewBlock(chain, getName());
    }

}
