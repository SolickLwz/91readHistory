package com.nine.one.yuedu.read.cp;

import com.nine.one.yuedu.read.config.JXResult;

import java.io.FileNotFoundException;

public interface ZhongShiToNxstoryService {

    /**
     * 同步书籍到景像后台
     */
    void syncBookToNxstory();

    /**
     * 章节同步
     */
    void syncChapter();


    /**
     * 将这本书的内容全部更新到景像
     * */
    String updateChapterByBookId(Integer baolianId);

    /*
    * 将这本书更新到景像
    * */
    String updateBookByBookId(Integer parseBookId);

    String addTaDuToJxtcFromXml(Integer start, Integer end) throws FileNotFoundException, Exception;

    /*
    * 将临时表中的章节id替换到景像的数据库中
    * */
    String modifyOnlyChapterIdToJxtc();

    JXResult deleteJTaduToJxtcFromNoRepeat();

    String repair();

    String CompareList(String s);

    String updateChapterContentByDelete(Integer bookid, Integer start, Integer end);

    String updateChapterName(Integer zhongshiBookId);
}
