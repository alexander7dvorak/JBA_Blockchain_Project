package com.myblockchain;

import java.util.List;

public class MainApp {
    private static final int N = 15;

    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();
        Miner[] miners = new Miner[N];
        for (int i = 0; i < miners.length; i++) {
            miners[i] = new Miner(blockchain);
            miners[i].start();
        }
        while (blockchain.getGeneratedBlocks().size() < N) {
        }
        List<Block> blocks = blockchain.getGeneratedBlocks();
        for (Block b : blocks) {
            System.out.println(b.toString() + "\n");
        }
    }
}