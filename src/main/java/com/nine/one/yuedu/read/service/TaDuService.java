package com.nine.one.yuedu.read.service;

import com.nine.one.yuedu.read.config.JXResult;

import java.io.UnsupportedEncodingException;

public interface TaDuService {
    String uptateChapter(Integer page) throws Exception;

    String setCsChapterInfoIdByTimeTaDu();

    JXResult addBookByTaDu() throws Exception;

    JXResult addTaDuChapter();

    JXResult queryDontHave();

    JXResult addTaDuChapterBySelective();

    JXResult addTaDuChapterByGetChapterList();

    JXResult updateChapterBySerialization() throws Exception;

    JXResult TestWords(Integer csBookid,Integer csChapterid);

    JXResult updateChapterByBookid(Integer bookid) throws Exception;

    JXResult updateChapterByBookidAndChapterSortBetwn(Integer bookid, Integer startsort, Integer endsort);

    //JXResult sortInconsistent(Integer csBookid);
}
