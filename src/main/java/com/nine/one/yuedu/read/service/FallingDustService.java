package com.nine.one.yuedu.read.service;

public interface FallingDustService {
    String booklist(Integer page) throws Exception;

    String book(Integer bid)throws Exception;

    String chepters(Integer bid);

    String content(Integer cid);
}
