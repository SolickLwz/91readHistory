package com.nine.one.yuedu.read.service;

public interface ChangeService {
    void changeChapterName(Integer bookStr, Integer start, Integer end);

    void changeChapterNameForMin(Integer bookId, Integer start, Integer end);

    void changeChapterNameForNex(Integer bookId, Integer start, Integer end);

    String getChapterContentAndRemoveSpace(Integer bookId);

    String getChapterContentAndRemoveSpaceToJingXiang(int jingxiangBookId);

    String updateFormal(int bookid);

    String trimByBookId(int parseInt);

    String weedOut(Integer bookId, Integer start, Integer end, String dele);

    String changeSumWordsToAudit();
}
