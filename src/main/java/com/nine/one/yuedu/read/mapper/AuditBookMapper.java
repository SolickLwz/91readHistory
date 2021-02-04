package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.AuditBook;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

import java.util.List;

public interface AuditBookMapper extends MyMapper<AuditBook> {
    List<AuditBook> selectBookInfo4PageAndParam(@Param("privoder") String privoder,@Param("bookName") String bookName,@Param("author") String author,@Param("id") Integer id,@Param("category") String category,@Param("auditStatus") Integer auditStatus);

    List<AuditBook> getThatAuthorBookListByDraftStatus(@Param("author") String author,@Param("draftStatus") Integer draftStatus);
}