package com.nine.one.yuedu.read.mapper;

import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.entity.vo.BookInfoVO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.MyMapper;

import java.util.Date;
import java.util.List;

public interface BookInfoMapper extends MyMapper<BookInfo> {
    Integer getCurrentMaxId();

    List<BookInfoVO> selectBookInfo4PageAndParam(@Param("privoder") String privoder,@Param("bookName") String bookName,@Param("author") String author,@Param("id") Integer id,@Param("category") String category,@Param("valid") Integer valid);

    List<Integer> selectBookIdOnPageOderByUpdateTime(@Param("cpAuthId") Integer cpAuthId,@Param("page") Integer page);

    List<Integer> selectBookIdOnCpidAndCompletestate(@Param("cpAuthId") int cpAuthId,@Param("complete_state") int complete_state);

    List<BookInfoVO> selectBookInfo4PageAndParamFormal(@Param("privoder") String privoder,@Param("bookName") String bookName,@Param("author") String author,@Param("id") Integer id,@Param("category") String category,@Param("valid") Integer valid);

    List<Integer> selectAiqiyiBookIdListByLastUpdateTime(@Param("lastUpdateTime") Date lastUpdateTimeDate);

    List<BookInfo> selectHaveFeeBySumBookList();
}