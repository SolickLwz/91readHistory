package com.nine.one.yuedu.read.controller;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.BookInfo;
import com.nine.one.yuedu.read.service.BookInfoService;
import com.nine.one.yuedu.read.utils.AliyunOSSUtil;
import com.nine.one.yuedu.read.utils.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

@Api(tags = "图书接口", value = "图书接口")
@RequestMapping(value = "/91yuedu/bookInfo")
@RestController
public class BookInfoController {


    @Resource(name = "bookInfoService")
    private BookInfoService bookInfoService;

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    //打印日志
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @PostMapping(value = "/bookUrl/upload")
    @ApiOperation(value = "上传封面图", notes = "上传封面图")
    public JXResult uploadHead(@RequestParam("file") @ApiParam(value = "上传的文件") MultipartFile uploadFile) {
        AliyunOSSUtil.FileUploadResult uploadResult = aliyunOSSUtil.upload(uploadFile);
        return new JXResult(true, ApiConstant.StatusCode.OK, "上传成功", uploadResult);
    }

    /**
     * 添加图书
     *
     * @param bookInfo
     */
    @PostMapping(value = "/add")
    @ApiOperation(value = "添加图书", notes = "添加图书")
    public JXResult addBookInfo(@ApiParam(value = "图书对象", required = true)
                                @RequestBody BookInfo bookInfo) {
        bookInfoService.addBookInfo(bookInfo);
        return new JXResult(true, ApiConstant.StatusCode.OK, "添加成功");
    }


    /**
     * 删除图书
     *
     * @param id
     */
    @DeleteMapping(value = "/del/{id}")
    @ApiOperation(value = "删除图书", notes = "删除图书")
    public JXResult delBookInfoById(@ApiParam(value = "图书ID") @PathVariable Integer id) {
        bookInfoService.delBookInfoById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");
    }


    /**
     * 修改图书信息
     *
     * @param bookInfo
     */
    @PutMapping(value = "/update")
    @ApiOperation(value = "修改图书信息", notes = "修改图书信息")
    public JXResult updateBookInfo(@ApiParam(value = "图书对象", required = true)
                                   @RequestBody BookInfo bookInfo) {
        bookInfoService.updateBookInfo(bookInfo);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }
    /**
     * 修改图书价格
     */
    /*@PutMapping(value = "/updateFeebysum")
    @ApiOperation(value = "修改图书价格", notes = "修改图书价格")
    public JXResult updateBookFeebysum(@ApiParam(value = "书名") @PathVariable String bookName,
                                       @ApiParam(value = "价格") @PathVariable Integer feebysum) {
        bookInfoService.updateBookFeebysum(bookName,feebysum);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }*/

    /**
     * 根据ID查询图书详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "通过Id查询图书详情", notes = "通过Id查询图书详情")
    public JXResult findById(@ApiParam(value = "图书ID") @PathVariable Integer id) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", bookInfoService.findById(id));
    }


    /**
     * 根据不同的条件分页查询书籍
     *
     * @param pageIndex 开始页
     * @param pageSize  每页显示的数量
     * @param privoder  作品提供者
     * @param bookName  书的名字
     * @param author    作者
     * @param id        图书Id
     * @param category  图书类型
     * @param valid     是否上线
     * @return
     */
    @GetMapping(value = "/list/{pageIndex}/{pageSize}")
    @ApiOperation(value = "分页根据条件查询书籍列表", notes = "分页根据条件查询书籍列表")
    public JXResult getBookInfo4PageAndParam(@ApiParam(value = "页码") @PathVariable Integer pageIndex,
                                             @ApiParam(value = "每页显示的数量") @PathVariable Integer pageSize,
                                             @RequestParam(required = false, value = "privoder") @ApiParam(value = "书籍提供者") String privoder,
                                             @RequestParam(required = false, value = "bookName") @ApiParam(value = "书名") String bookName,
                                             @RequestParam(required = false, value = "author") @ApiParam(value = "作者") String author,
                                             @RequestParam(required = false, value = "id") @ApiParam(value = "书籍id") Integer id,
                                             @RequestParam(required = false, value = "category") @ApiParam(value = "书籍类型") String category,
                                             @RequestParam(required = false, value = "valid") @ApiParam(value = "是否上架") Integer valid) {

        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", bookInfoService.getBookInfo4PageAndParam(pageIndex, pageSize, privoder, bookName, author, id, category, valid));

    }

    @GetMapping(value = "/find/bookInfo/{id}")
    @ApiOperation(value = "通过Id查询图书和章节详情", notes = "通过Id查询图书和章节详情")
    public JXResult findBookInfoById(@ApiParam(value = "图书ID") @PathVariable Integer id) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", bookInfoService.findBookInfoById(id));
    }

    @GetMapping(value = "/downbook/{bookId}")
    @ApiOperation(value = "下载一本书")
    public void downBookById(@PathVariable @ApiParam(value = "书籍ID") Integer bookId, HttpServletResponse response) {
        String fileName = bookId + ".txt";
        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(), "ISO8859-1"));
            String sb = bookInfoService.downLoadBook(bookId);

            byte[] bytes = sb.getBytes("UTF-8");
            os = response.getOutputStream();
            // 将字节流传入到响应流里,响应到浏览器
            os.write(bytes);
            os.close();
        } catch (Exception ex) {
            logger.error("导出失败:", ex);
            throw new RuntimeException("导出失败");
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException ioEx) {
                logger.error("导出失败:", ioEx);
            }
        }
    }

    /**
     * 导出报表
     *
     * @return
     */
    @RequestMapping(value = "/export")
    @ApiOperation(value = "导出excel")
    @ResponseBody
    public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //获取数据
        List<BookInfo> list = bookInfoService.getBookInfos();


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //excel标题
        String[] title = {"书籍ID", "书名", "作者", "频道", "分类", "标签", "字数", "状态",
                "简介", "最新更新时间", "合作商", "链接","是否重点", "重点理由"};

        //excel文件名
        String fileName = "书籍表" + System.currentTimeMillis() + ".xls";

        //sheet名
        String sheetName = "书籍表";
        String[][] content = new String[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            BookInfo obj = list.get(i);
            content[i][0] = obj.getId().toString();
            content[i][1] = obj.getBookName();
            content[i][2] = obj.getAuthor();
            content[i][3] = getBoyAndGirl(obj.getCategory());
            content[i][4] = obj.getCategory();
            content[i][5] = obj.getKeywords();
            content[i][6] = obj.getWords().toString();
            content[i][7] = obj.getCompleteState() == 1 ? "连载" : "完结";
            content[i][8] = obj.getDescription();
            content[i][9] = simpleDateFormat.format(obj.getUpdateTime());
            content[i][10] = "报联中视";
            //插入一个,书的链接地址
            content[i][11]="http://client.91yuedu.com/#/main/bookInfo/"+obj.getId().toString();
            content[i][12] = "";
            content[i][13] = "";
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据cpid导出报表
     *
     * @return
     */
    @RequestMapping(value = "/exportByCpId")//{cpId}
    @ApiOperation(value = "根据cpid导出excel,参数二选一")
    @ResponseBody
    public void exportByCpId(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(required = false,value = "cpId") @ApiParam(value = "cp方的id") Integer cpId,
                             @RequestParam(required = false,value = "notCpId") @ApiParam(value = "不要包含的cp_id(悦读坊的是1003)") Integer notCpId
    ) throws Exception {
        /*if (null==cpId){
            List<BookInfo> list = bookInfoService.getBookInfos();
        }*/
        List<BookInfo> list;
       if (null==notCpId){
           System.out.println("访问根据cpid的接口");
           list=bookInfoService.getBookInforByCpAuthId(cpId);//悦读坊的cpid是17
           System.out.println("取到cpid为"+cpId+"的参数一共"+list.size());
       }else {
           System.out.println("访问除去cpid的接口");
           list=bookInfoService.getBookInforByNotCpAuthId(notCpId);//悦读坊的cpid是17
           System.out.println("取到除去cpid为"+notCpId+"的参数一共"+list.size());
       }
/*        int i1=0;
for (BookInfo bookInfo:list){
    i1++;
    System.out.println("拿到的id是:"+bookInfo.getId()+"书名是:"+bookInfo.getBookName()+"第"+i1+"本");
}*///打印拿到的数据
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //excel标题
        String[] title = {"书籍ID", "书名", "作者", "频道", "分类", "标签", "字数", "状态",
                "简介", "最新更新时间", "合作商", "链接","是否重点", "重点理由"};

        //excel文件名
        String fileName = "书籍表" + System.currentTimeMillis() + ".xls";

        //sheet名
        String sheetName = "书籍表";
        String[][] content = new String[list.size()][5];
        for (int i = 0; i < list.size(); i++) {
            content[i] = new String[title.length];
            BookInfo obj = list.get(i);
            content[i][0] = obj.getId().toString();
            content[i][1] = obj.getBookName();
            content[i][2] = obj.getAuthor();
            content[i][3] = getBoyAndGirl(obj.getCategory());
            content[i][4] = obj.getCategory();
            content[i][5] = obj.getKeywords();
            content[i][6] = obj.getWords().toString();
            content[i][7] = obj.getCompleteState() == 1 ? "连载" : "完结";
            content[i][8] = obj.getDescription();
            content[i][9] = simpleDateFormat.format(obj.getUpdateTime());
            content[i][10] = "报联中视";
            //插入一个,书的链接地址
            content[i][11]="http://client.91yuedu.com/#/main/bookInfo/"+obj.getId().toString();
            content[i][12] = "";
            content[i][13] = "";
        }

        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/downBackBook/{bookId}")
    @ApiOperation(value = "下载备份的书")
    public void downBackBookById(@PathVariable @ApiParam(value = "书籍ID") Integer bookId, HttpServletResponse response) {
        String fileName = bookId + ".txt";
        OutputStream os = null;
        try {
            response.reset();
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes(), "ISO8859-1"));
            /*String sb = bookInfoService.downLoadBook(bookId);*/
            String sb = bookInfoService.downBackLoadBook(bookId);
            byte[] bytes = sb.getBytes("UTF-8");
            os = response.getOutputStream();
            // 将字节流传入到响应流里,响应到浏览器
            os.write(bytes);
            os.close();
        } catch (Exception ex) {
            logger.error("导出失败:", ex);
            throw new RuntimeException("导出失败");
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
            } catch (IOException ioEx) {
                logger.error("导出失败:", ioEx);
            }
        }
    }



    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String getBoyAndGirl(String category) {
        String type = "";
        switch (category) {
            case "现代言情":
            case "古代言情":
            case "幻想言情":
            case "豪门总裁":
            case "女生灵异":
            case "浪漫青春":
                type = "女频";
                break;
            case "悬疑灵异":
            case "历史军事":
            case "玄幻奇幻":
            case "仙侠武侠":
            case "都市生活":
            case "玄幻仙侠":
            case "科幻末世":
            case "现代都市":
            case "恐怖惊悚":
                type = "男频";
                break;
        }
        return type;
    }
}
