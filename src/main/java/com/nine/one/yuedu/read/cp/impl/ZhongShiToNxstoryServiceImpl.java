package com.nine.one.yuedu.read.cp.impl;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.cp.ZhongShiToNxstoryService;
import com.nine.one.yuedu.read.entity.*;
import com.nine.one.yuedu.read.mapper.*;
import com.nine.one.yuedu.read.utils.AliyunOSSUtil;
import com.nine.one.yuedu.read.utils.FileOptionUtil;
import com.nine.one.yuedu.read.utils.HttpClientUtil;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service(value = "zhongShiToNxstoryService")
public class ZhongShiToNxstoryServiceImpl implements ZhongShiToNxstoryService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BookInfoMapper bookInfoMapper;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;
    @Autowired
    private CsBookinfoMapper csBookinfoMapper;
    @Autowired
    private FileOptionUtil fileOptionUtil;
    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;
    @Autowired
    private CsChapterinfoMapper csChapterinfoMapper;
    @Autowired
    private TaduToJxtcMapper taduToJxtcMapper;

    @Autowired
    private TimeTaduToJxtcMapper timeTaduToJxtcMapper;

    @Autowired
    private TempMapper tempMapper;

    @Override
    public void syncBookToNxstory() {
        //获取所有91阅读网所有的书籍
        List<BookInfo> bookInfos = bookInfoMapper.selectAll();
        if (bookInfos != null && bookInfos.size() > 0) {
            for (BookInfo bookInfo : bookInfos) {
                //看这本书在景像后台有没有
                Example exampleCsBook = new Example(CsBookinfo.class);
                exampleCsBook.createCriteria().andEqualTo("cpid", 1056).andEqualTo("cpbookid", bookInfo.getId());
                List<CsBookinfo> csBookinfos = csBookinfoMapper.selectByExample(exampleCsBook);
                if (csBookinfos == null || csBookinfos.size() <= 0) {
                    logger.info("这本书没有被抓取过");
                    //书籍同步
                    CsBookinfo csBookinfoNew = packageCsbook(bookInfo);
                    //入库
                    csBookinfoMapper.insertSelective(csBookinfoNew);
                    logger.info("图书信息添加成功");
                } else {
                    CsBookinfo csBookinfoNew = packageCsbook(bookInfo);
                    csBookinfoNew.setId(csBookinfos.get(0).getId());
                    csBookinfoMapper.updateByPrimaryKey(csBookinfoNew);
                    logger.info("这本书已经被抓取过,内容更新");
                }
            }
        }
    }

    @Override
    public void syncChapter() {
        Example exampleCsBook = new Example(CsBookinfo.class);
        exampleCsBook.createCriteria().andEqualTo("cpid", 1056);
        List<CsBookinfo> csBookinfos = csBookinfoMapper.selectByExample(exampleCsBook);
        for (CsBookinfo csBookinfo : csBookinfos) {
            //根据91阅读这边的书籍ID查询章节列表
            Example example = new Example(ChapterInfo.class);
            example.createCriteria().andEqualTo("bookId", csBookinfo.getCpbookid());
            List<ChapterInfo> chapterInfos = chapterInfoMapper.selectByExample(example);
            for (ChapterInfo chapterInfo : chapterInfos) {
                //查询这本书的章节,在景像的数据库存在吗
                Example exampleCsNxChapter = new Example(CsChapterinfo.class);
                exampleCsNxChapter.createCriteria()
                        .andEqualTo("cpbookid", chapterInfo.getBookId())
                        .andEqualTo("cpchapterid", chapterInfo.getId());
                CsChapterinfo csChapterFlag = csChapterinfoMapper.selectOneByExample(exampleCsNxChapter);
                if (csChapterFlag == null) {
                    logger.info("这个章节没有抓取过");
                    CsChapterinfo csChapterinfo = new CsChapterinfo();
                    csChapterinfo.setName(chapterInfo.getChapterName());
                    csChapterinfo.setBookid(csBookinfo.getId());
                    csChapterinfo.setCpbookid(Long.valueOf(chapterInfo.getBookId()));
                    csChapterinfo.setCpchapterid(Long.valueOf(chapterInfo.getId()));
                    csChapterinfo.setVolumnid(0);
                    csChapterinfo.setSort(chapterInfo.getChapterId());
                    csChapterinfo.setSize(chapterInfo.getWords());
                    csChapterinfo.setCreateTime(new Date());
                    csChapterinfo.setStatus(1);
                    csChapterinfo.setIsvip(1);
                    logger.info("章节入库");
                    csChapterinfoMapper.insertSelective(csChapterinfo);
                    logger.info(chapterInfo.getChapterName() + "抓取成功");
                }

            }
            //抓取书籍的内容
            getChapterContentByBookId(csBookinfo.getId());
        }
    }

    @Override
    public String updateChapterByBookId(Integer zhongshiId) {
        Example exampleCsBook = new Example(CsBookinfo.class);//从景像中,根据这个报联书id获取景像的书对象
        exampleCsBook.createCriteria().andEqualTo("cpbookid", zhongshiId).andEqualTo("cpid",1056);
        List<CsBookinfo> csBookinfos = csBookinfoMapper.selectByExample(exampleCsBook);//从景像获取这本报联书对应的书籍

        if (csBookinfos.size()>1){
            System.out.println("找到了中视bookid:"+zhongshiId+"在景像对应的书籍:"+csBookinfos.size()+"本!请检查");
            return "找到了中视bookid:"+zhongshiId+"在景像对应的书籍:"+csBookinfos.size()+"本!请检查";
        }
        for (CsBookinfo oneBook : csBookinfos) {
            System.out.println("cpID是" + oneBook.getCpid());
            System.out.println("报联的书id是csBookInfo的Cpbookid" + oneBook.getCpbookid());
            System.out.println("景像书籍Id" + oneBook.getId());
            System.out.println("书籍名称" + oneBook.getName());

            //根据91阅读这边的书籍ID查询章节列表
            Example example = new Example(ChapterInfo.class);
            example.createCriteria().andEqualTo("bookId", oneBook.getCpbookid());//就是传过来的zhongshiId
            List<ChapterInfo> chapterInfos = chapterInfoMapper.selectByExample(example);
            for (ChapterInfo chapterInfo : chapterInfos) {
                //查询这本书的章节,在景像的数据库存在吗
                Example exampleCsNxChapter = new Example(CsChapterinfo.class);
                exampleCsNxChapter.createCriteria()
                        .andEqualTo("bookid",oneBook.getId())//.andEqualTo("bookid",chapterInfo.getBookId())//这里不对,如果是chapterinfo就是报联的书id了,肯定找不到的,修改为上面的onebook,从景像的书对象中获取id
                        .andEqualTo("cpbookid", chapterInfo.getBookId())
                        .andEqualTo("cpchapterid", chapterInfo.getId());
                CsChapterinfo csChapterFlag = csChapterinfoMapper.selectOneByExample(exampleCsNxChapter);
                if (null == csChapterFlag) {
                    logger.info(chapterInfo.getChapterName()+"这个章节没有抓取过");
                    CsChapterinfo csChapterinfo = new CsChapterinfo();
                    csChapterinfo.setName(chapterInfo.getChapterName());
                    csChapterinfo.setBookid(oneBook.getId());
                    csChapterinfo.setCpbookid(Long.valueOf(chapterInfo.getBookId()));
                    csChapterinfo.setCpchapterid(Long.valueOf(chapterInfo.getId()));
                    csChapterinfo.setVolumnid(0);
                    csChapterinfo.setSort(chapterInfo.getChapterId());
                    csChapterinfo.setSize(chapterInfo.getWords());
                    csChapterinfo.setCreateTime(new Date());
                    csChapterinfo.setStatus(1);
                    csChapterinfo.setIsvip(1);
                    logger.info("章节入库");
                    csChapterinfoMapper.insertSelective(csChapterinfo);
                    logger.info(chapterInfo.getChapterName() + "抓取成功");
                }else {
                    System.out.println(chapterInfo.getChapterName()+"这个章节已经抓取过了");
                }
            }
            //抓取书籍的内容
            getChapterContentByBookId(oneBook.getId());
        }
        return "通信成功";
    }

    @Override
    public String updateBookByBookId(Integer parseBookId) {
        //获取所有91阅读网对应id的书籍
        BookInfo bookInfo = bookInfoMapper.selectByPrimaryKey(parseBookId);
        if (bookInfo != null) {
            //看这本书在景像后台有没有
            Example exampleCsBook = new Example(CsBookinfo.class);
            exampleCsBook.createCriteria().andEqualTo("cpid", 1056).andEqualTo("cpbookid", bookInfo.getId());
            List<CsBookinfo> csBookinfos = csBookinfoMapper.selectByExample(exampleCsBook);
            if (csBookinfos == null || csBookinfos.size() <= 0) {
                logger.info("这本书没有被抓取过");
                //书籍同步
                CsBookinfo csBookinfoNew = packageCsbook(bookInfo);
                //入库
                csBookinfoMapper.insertSelective(csBookinfoNew);
                logger.info("图书信息添加成功");
            } else {
                CsBookinfo csBookinfoNew = packageCsbook(bookInfo);
                csBookinfoNew.setId(csBookinfos.get(0).getId());
                csBookinfoMapper.updateByPrimaryKey(csBookinfoNew);
                logger.info("这本书已经被抓取过,内容更新");
            }
        } else {
            return "中视没有" + parseBookId + "这本书";
        }
        return "通信成功";
    }

    @Override
    public String addTaDuToJxtcFromXml(Integer start, Integer end) throws Exception {
        StringBuilder result = new StringBuilder("结果");
        //创建工作簿并载入excel文件流
        jxl.Workbook wb = null;
        InputStream is = new FileInputStream("D:\\JXTC\\timetaDuToJxtc7.xls");
        wb = Workbook.getWorkbook(is);

        int sheetSize = wb.getNumberOfSheets();
        Sheet sheet = wb.getSheet(0);
        int row_total = sheet.getRows();//获取总行数
        System.out.println("总行数" + row_total);
        System.out.println("当前给的末尾行数:" + end);
        for (int j = start; j <= end; j++) {
            Cell[] cells = sheet.getRow(j);//获取这一行的数据
            //TaduToJxtc taduToJxtc = new TaduToJxtc();
            TimeTaduToJxtc timeTaduToJxtc = new TimeTaduToJxtc();
            timeTaduToJxtc.setBookName(cells[0].getContents());
            timeTaduToJxtc.setPartId(Integer.parseInt(cells[1].getContents()));
            timeTaduToJxtc.setTitle(cells[2].getContents());
            timeTaduToJxtc.setPartNumb(Integer.parseInt(cells[3].getContents()));
            SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date parse = format.parse(cells[4].getContents());
            timeTaduToJxtc.setCreateDate(parse);
            int flag = timeTaduToJxtcMapper.insertSelective(timeTaduToJxtc);

            if (flag < 0) {
                result.append("第" + j + "行添加失败!");
            }
        }
        /*for (int j = start; j <= end; j++) {
            Cell[] cells = sheet.getRow(j);//获取这一行的数据
            TaduToJxtc taduToJxtc = new TaduToJxtc();
            taduToJxtc.setBookName(cells[0].getContents());
            taduToJxtc.setPartId(Integer.parseInt(cells[1].getContents()));
            taduToJxtc.setTitle(cells[2].getContents());
            taduToJxtc.setPartNumb(Integer.parseInt(cells[3].getContents()));

//TaduToJxtc taduToJxtcHave = taduToJxtcMapper.selectOne(taduToJxtc);
//if (null == taduToJxtcHave){
            int flag = taduToJxtcMapper.insertSelective(taduToJxtc);
//System.out.println("从xsl添加章节成功:"+taduToJxtc.getBookName()+taduToJxtc.getTitle()+taduToJxtc.getPartId());
            if (flag < 0) {
                result.append("第" + j + "行添加失败!");
            }
//else {
//result.append("第"+j+"章节已存在!");
// }
            //}

        }*/

        return result.toString();
    }

    @Override
    public String modifyOnlyChapterIdToJxtc() {
        Integer cpid = 1049;//塔读的cpId是1049
        //(拿到了授权的章节list)
        List<CsChapterinfo> cpChapterAllList = csChapterinfoMapper.selectCpChapterAll(cpid);
        System.out.println("拿到授权的章节"+cpChapterAllList.size());
        //3.再去拿到重复的书单
        List<TaduToJxtc> taduToJxtcList = taduToJxtcMapper.selectAll();
        System.out.println("拿到taduToJxtc"+taduToJxtcList.size());
        //4.遍历授权章节list,如果和塔读list章节名一样,并且书名一样,就把章节id改成塔读list的
        for (CsChapterinfo csChapterinfo : cpChapterAllList) {
            for (TaduToJxtc taduToJxtc : taduToJxtcList) {
                if (csChapterinfo.getName().equals(taduToJxtc.getTitle()) && (csChapterinfo.getSort() == taduToJxtc.getPartNumb())) {
                    System.out.print("章节名排序");
                    Example example = new Example(CsBookinfo.class);//到这就说明章节名和排序都一致
                    example.createCriteria().andEqualTo("id", csChapterinfo.getBookid());
                    CsBookinfo csBookinfo = csBookinfoMapper.selectOneByExample(example);//根据章节的bookid找到书
                    if (taduToJxtc.getBookName().equals(csBookinfo.getName())) {//书名一致
                        System.out.print("书名一致");
                        if (taduToJxtc.getPartId()==csChapterinfo.getId()){//如果两者id一样了,就不用动
                            System.out.println(taduToJxtc.getPartId()+"id相同不用动"+csChapterinfo.getId());
                            continue;
                        }else {
                            System.out.print("id不一致");
                        }
                        if (null !=csChapterinfoMapper.selectByPrimaryKey(taduToJxtc.getPartId())){//如果塔读给了已经存在的id,那有可能是曾经授权过他们的
                            System.out.println("塔读给了已经存在的id");
                           continue;
                        }

                        System.out.println("准备修改"+csChapterinfo.getName()+csChapterinfo.getId()+"为"+taduToJxtc.getTitle()+taduToJxtc.getPartId());
                        int flag = csChapterinfoMapper.updateIdByBeforToAfter(csChapterinfo.getId(),taduToJxtc.getPartId());
                        if (flag < 0) {
                            Temp ex = new Temp();
                            ex.setType("失败");
                            ex.setOld(csChapterinfo.getId()+csChapterinfo.getName());
                            ex.setThat(taduToJxtc.getPartId()+taduToJxtc.getBookName());
                            tempMapper.insertSelective(ex);
                            return "临时表为:" + taduToJxtc.getTitle() + "更新失败!";
                        }else {
                            System.out.println("修改完成,准备记录到temp");
                            Temp temp = new Temp();
                            temp.setType("将景像的章节id改为塔读提供的之前id,逻辑是塔读的min");
                            temp.setOld(csChapterinfo.getId()+csChapterinfo.getName());
                            temp.setThat(taduToJxtc.getPartId()+taduToJxtc.getBookName());
                            tempMapper.insertSelective(temp);
                        }
                    } else {
                        System.out.println(csChapterinfo.getName()+" 和 "+ taduToJxtc.getTitle() + "    同章节名但是不同书  "+csBookinfo.getName()+"      "+taduToJxtc.getBookName());
                    }
                }
            }
        }
        return "成功";
    }

    @Override
    public JXResult deleteJTaduToJxtcFromNoRepeat() {
        //开头,保存befor
//如果this和before不同,看that和before,还不同就删除before,将this保存为before
        int yes=0;
        StringBuilder stringBuilder=new StringBuilder();
        TaduToJxtc thatObj = null;//上上一个对象数据
        TaduToJxtc beforObj = null;//之前
        //1,从临时表中获取所有数据
        List<TaduToJxtc> taduToJxtcList = taduToJxtcMapper.selectAll();
        System.out.println("从临时表中拿到了数据");
        //2.遍历,删掉上下id中,章节名都不重复的数据
        for (TaduToJxtc taduToJxtc : taduToJxtcList) {
            if (null == beforObj) {//这是开头的话,保存befor
                beforObj = taduToJxtc;
                System.out.println("这是开头"+taduToJxtc.getTitle());
                continue;
            }
            //到这就是有befor了
            else if (beforObj.getTitle().equals(taduToJxtc.getTitle())) {
                System.out.println("章节名为"+beforObj.getTitle()+taduToJxtc.getTitle()+"的章节名相同");
                thatObj = beforObj;//befor和当前章节名相同,保存that和befor
                beforObj = taduToJxtc;
                continue;
            } else {//this和before不同(确定中下)看that和before(中上)还不同就删除before,将this保存为before
                if (thatObj.getTitle().equals(beforObj.getTitle())) {//如果that和befor相同,保存即可
                    System.out.println("章节名为"+beforObj.getTitle()+taduToJxtc.getTitle()+"的章节名不相同,但是和上一章节"+thatObj.getTitle()+"相同");
                    thatObj = beforObj;
                    beforObj = taduToJxtc;
                    continue;
                }
                else {//befor和上下都不同,删除before,将this保存为before
                    System.out.println("章节名为"+beforObj.getTitle()+"与"+taduToJxtc.getTitle()+thatObj.getTitle()+"都不相同,准备删除");
                    int i = taduToJxtcMapper.deleteByPrimaryKey(beforObj.getId());
                    if (i > 0) {
                        Temp temp = new Temp();
                        temp.setType("删除塔读提供的书中,不存在重复的章节");
                        temp.setHavedesc("将" + beforObj.getBookName() + beforObj.getTitle() + "章节id" + beforObj.getPartId() + "排序" + beforObj.getPartNumb() + "进行了删除");
                        tempMapper.insert(temp);//记录这次的操作
                        i++;
                    } else {
                        stringBuilder.append(beforObj.getPartId() + beforObj.getTitle() + "删除失败");
                        System.out.println(beforObj.getPartId() + beforObj.getTitle() + "删除失败!");
                    }
                    beforObj = taduToJxtc; //删完将当前对象保存为befor,that还不用变
                }
            }
        }

        if (stringBuilder.length()<3){
            return new JXResult(false, ApiConstant.StatusCode.ERROR, "操作失败:"+yes+"条数据删除成功"+stringBuilder);
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "更新成功");


    }

    @Override
    public String repair() {
        int success=0;//成功恢复的章节条数
        int errChapter=0;//出现错误的章节条数
        int successBook=0;//成功恢复的书数量
        int csSort=0;//遍历到景像的第几本书
        StringBuilder result=new StringBuilder();
        //获取景像中,书源是报联的书list
        Example exampleCsBook = new Example(CsBookinfo.class);
        exampleCsBook.createCriteria().andEqualTo("cpid", 1056);
        List<CsBookinfo> csBookinfos = csBookinfoMapper.selectByExample(exampleCsBook);
        System.out.println("准备遍历"+csBookinfos.size()+"本景像书");

        for (CsBookinfo csBookinfo : csBookinfos){//最外循环,把景像书遍历完解术
            //根据景像书的cpbookid获取报联的章节list
            List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(csBookinfo.getCpbookid().intValue());
            int chapterSuccess=0;//报联list在景像本地章节成功条数
            for (ChapterInfo chapterInfo : chapterInfos) {//遍历报联章节list

                //如果这个章节的内容在景像数据库已经有了.就不用进行下面的操作,如果在景像没有,才需要恢复数据
                CsChapterinfo isHave= new CsChapterinfo();//第一次判断,在景像数据库没有的报联章节
                isHave.setCpbookid(chapterInfo.getBookId().longValue());//通过判断就是在报联有,景像没有
                isHave.setCpchapterid(chapterInfo.getChapterId().longValue());
                if (null!=csChapterinfoMapper.selectOne(isHave)){//非空就是查到了,跳过这个已经有的报联章节
                    continue;
                }
                //获取章节内容,如果匹配上了,就根据txt文件名为景像创建这个章节对象
                //开始抓取章节内容
                System.out.println(csBookinfo.getName()+chapterInfo.getChapterName()+"在景像数据库没有,准备从报联获取数据");
                final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", chapterInfo.getBookId(), chapterInfo.getChapterId());
                String bookContent = HttpClientUtil.doGet(URL);//获得报联的章节内容
                String content = bookContent.substring(10, 30);//为了防止内容过大,先截取一段,缩小压力
                //获取这个文件夹下的txt文件内容,逐一比较
                File file06 = new File("/data2/nfs/www/booktxt/"+csBookinfo.getId());
               // System.out.println("准备遍历"+file06.getPath());
                File[] files = file06.listFiles();//获取当前目录下的所有子文件以及子文件夹
                int oneSuccess=0;
                for (File f : files) {
                    String path = f.getPath();
                    if (!path.substring(path.length() - 3, path.length()).equals("txt")) {
                        continue;//跳过不是txt文件的,因为这里还有文件夹..无语
                    }
                    String read = read(f.getPath());//获得景像本地的内容
                    if (read.indexOf(content) > 0) {//把当前循环中的content和景像本地比对
                        System.out.println(csBookinfo.getName()+chapterInfo.getChapterName()+"报联的内容和景像匹配上了");
                        CsChapterinfo csChapterinfoByRepair = new CsChapterinfo();//如果匹配上了,给景像添加这个章节对象

                        String fileName=f.getName();
                        String substring = fileName.substring(0, fileName.length() - 4);
                        csChapterinfoByRepair.setId(Integer.parseInt(substring));//id就是本地的txt文件名

                        if (null != csChapterinfoMapper.selectOne(csChapterinfoByRepair)) {
                            //System.out.println(substring+"在景像数据库已经有了!??");
                            continue;
                        }
                        csChapterinfoByRepair.setName(chapterInfo.getChapterName());//章节名是报联的章节名
                        csChapterinfoByRepair.setBookid(csBookinfo.getId());//bookid是外循环的景像书id
                        csChapterinfoByRepair.setCpbookid(chapterInfo.getBookId().longValue());//cpbookid是报联的bookId
                        csChapterinfoByRepair.setCpchapterid( chapterInfo.getId().longValue());//cpChapterid字段就是报联的章节id
                        csChapterinfoByRepair.setVolumnid(0);//不分卷的,设为0
                        csChapterinfoByRepair.setSort(chapterInfo.getChapterId());//景像章节排序就是报联章节排序
                        csChapterinfoByRepair.setSize(chapterInfo.getWords());//字数
                        csChapterinfoByRepair.setCreateTime(new Date());//创建时间先设置为当前时间
                        csChapterinfoByRepair.setStatus(1);//1上架
                        csChapterinfoByRepair.setIsvip(1);//1收费
                        int insert = csChapterinfoMapper.insert(csChapterinfoByRepair);
                        if (insert<0){
                            errChapter+=1;
                            result.append("添加到数据库失败,景像本地:"+Integer.parseInt(substring)+"报联书id"+chapterInfo.getBookId()+"章节名"+chapterInfo.getChapterName());
                        }else {
                            System.out.println(chapterInfo.getChapterName()+"修复");
                            oneSuccess+=1;
                            success+=1;
                            chapterSuccess+=1;//报联list在景像本地章节成功条数
                        }
                    }
                }
                //如果循环完文件夹也没有
                if (oneSuccess==0){
                    System.out.println("报联的"+chapterInfo.getBookId()+chapterInfo.getChapterName()+"在景像本地没有");
                    result.append("报联的"+chapterInfo.getBookId()+chapterInfo.getChapterName()+"在景像本地没有");
                }
            }
            csSort+=1;
            System.out.println(csBookinfo.getName()+"完毕  "+ csBookinfo.getId()+"  这本书修复了"+chapterSuccess+"个章节"+"  总共修复"+success+"个章节"+ "  失败章节:"+errChapter+"  循环到第"+csSort+"本景像的书");
        }
        //遍历景像list,cpbookid就是报联书id,cpchapterId是报联章节id,
        //从报联中视获取景像的这本书的章节list,遍历章节
        result.append("总共修复"+success+"个章节"+"  失败章节:"+errChapter);
        return result.toString();
    }

    @Override
    public String CompareList(String s) {
        int jingXiangBookId = Integer.parseInt(s);

        //根据这个景像书id获取到景像章节list
        CsChapterinfo csChapterinfoBySelect = new CsChapterinfo();
        csChapterinfoBySelect.setBookid(jingXiangBookId);
        List<CsChapterinfo> jingXiangChapterList = csChapterinfoMapper.select(csChapterinfoBySelect);
        //先查询出这本景像书,景像书的cpbookid就是报联章节的bookid,从报联获取章节list
        CsBookinfo csBookinfo = csBookinfoMapper.selectByPrimaryKey(jingXiangBookId);
        Long cpbookid = csBookinfo.getCpbookid();
        int baoLianBookId = cpbookid.intValue();

        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(baoLianBookId);
        int difference= chapterInfos.size()-jingXiangChapterList.size();
        int baolianHave=0;//在报联找到的景像缺少的章节
        int doubt=0;//问题章节(1.报联章节在景像数据库有多条)
        if (chapterInfos.size()!=jingXiangChapterList.size()){
            System.out.println(jingXiangBookId+"章节书不同,报联数量:"+chapterInfos.size()+"景像数量:"+jingXiangChapterList.size()+"少了"+difference+"章");
        //章节数量不同,需要遍历报联章节list,看看是哪一章在景像没有
            for (ChapterInfo chapterInfo: chapterInfos){
                //在上面根据单条报联章节的信息去查询景像,如果没有就:
                CsChapterinfo cschapterBySelectJingXiang= new CsChapterinfo();
                cschapterBySelectJingXiang.setBookid(jingXiangBookId);//景像的 书id
                cschapterBySelectJingXiang.setCpchapterid(chapterInfo.getId().longValue());//Cpchapterid就是报联的章节id
                cschapterBySelectJingXiang.setName(chapterInfo.getChapterName());//章节名

                List<CsChapterinfo> csChapterinfoByHave = csChapterinfoMapper.select(cschapterBySelectJingXiang);
                if (csChapterinfoByHave.size()>1){
                    doubt+=1;
                    System.out.println(chapterInfo.getBookId()+"报联的"+chapterInfo.getChapterName()+"在景像数据库有多条"+csChapterinfoByHave.size());
                }
                if (csChapterinfoByHave.size()<1){
                    baolianHave+=1;
                    System.out.println(s+"报联的<<"+chapterInfo.getChapterName()+">>在景像数据库没有");
                }
            }
        }
        if (baolianHave!= (chapterInfos.size()-jingXiangChapterList.size())){
            System.out.println(s+"有问题");
        }else {
            System.out.println(s+"完毕");
        }
        boolean flag=true;
        if (difference>0){ System.out.print("少了"+difference+"章");flag=false; }
        if (baolianHave>0){ System.out.print("从报联找到了"+baolianHave+"章  ");flag=false; }
        if (doubt>0){ System.out.println("有"+doubt+"个疑惑章节");flag=false; }
        if (flag){
            return "成功";
        }
        return s+"少了"+difference+"章"+"从报联找到了"+baolianHave+"章  "+"有"+doubt+"个疑惑章节";
    }

    @Override
    public String updateChapterContentByDelete(Integer bookid, Integer start, Integer end) {
        //获取这本书的区间章节list
        List<ChapterInfo> rangeChapter= chapterInfoMapper.selectRangeChapter(bookid,start,end);
        //遍历这个list,
        // 1.从阿里云获取内容,2.根据报联章节对象得到景像的本地文件名,3.删除这个文件,然后创建同名文件,内容是阿里云获取到的
        for (ChapterInfo chapterInfo:rangeChapter){
            //获取景像对应的章节
            Example exampleChapter = new Example(CsChapterinfo.class);
            exampleChapter.createCriteria().andEqualTo("cpbookid", chapterInfo.getBookId()).andEqualTo("cpchapterid", chapterInfo.getId());
            CsChapterinfo csChapterinfo = csChapterinfoMapper.selectOneByExample(exampleChapter);
            //1.从阿里云获取内容
            final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", chapterInfo.getBookId(), chapterInfo.getChapterId());
            String bookContent = HttpClientUtil.doGet(URL);
            //2.根据报联章节对象得到景像的本地文件名
            File file = new File("/data2/nfs/www/booktxt/" + csChapterinfo.getBookid() + "/" + csChapterinfo.getId() + ".txt");
            if (file.exists()) {
                //如果存在就对了,替换
                try {
                    file.delete();
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //如果不存在,就直接返回错误
                return chapterInfo.getChapterId()+chapterInfo.getChapterName()+"出现错误!这个操作应该是替换更新,但是景像本地没有这个文件了!";
            }
            try {
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(bookContent);
                logger.info(csChapterinfo.getName() + ":章节内容保存成功,保存成功!");
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("章节替换成功");
        }
        return "通信成功";
    }

    @Override
    public String updateChapterName(Integer zhongshiBookId) {
        Long longBookid=(long) zhongshiBookId;
        //获取这本书的章节list
        List<ChapterInfo> chapterInfos = chapterInfoMapper.selectChapterListByBookId(zhongshiBookId);
        for (ChapterInfo chapterInfo:chapterInfos){//遍历章节list,根据报联的章节id和书id找到景像对应的章节
            CsChapterinfo csChapterinfoByselect = new CsChapterinfo();
            csChapterinfoByselect.setCpbookid(longBookid);
            csChapterinfoByselect.setCpchapterid((long) chapterInfo.getId());
            CsChapterinfo csChapterinfo = csChapterinfoMapper.selectOne(csChapterinfoByselect);
            if (null==csChapterinfo){
                System.out.println("在景像没有这个章节"+chapterInfo.getChapterName());
                return "在景像没有这个章节"+chapterInfo.getChapterName();
            }
            csChapterinfo.setName(chapterInfo.getChapterName());
            int i = csChapterinfoMapper.updateByPrimaryKeySelective(csChapterinfo);
            if (i<0){
                System.out.println("报联章节名"+chapterInfo.getChapterName()+"出错!");
                return "\"报联章节名\"+chapterInfo.getChapterName()+\"出错!\"";
            }
        }

        return "通信成功";
    }

    private void getChapterContentByBookId(Integer csBookId) {//从阿里云获取内容到景像
        //查询这本书的景像章节列表
        Example example = new Example(CsChapterinfo.class);
        example.createCriteria().andEqualTo("bookid", csBookId);
        List<CsChapterinfo> csChapterinfos = csChapterinfoMapper.selectByExample(example);
        for (CsChapterinfo csChapterinfo : csChapterinfos) {
            //看文件夹存在吗,创建并抓取
           /* File fileDir = new File("/data2/nfs/www/booktxt/" + csBookId + "/" + csChapterinfo.getBookid());*/
            File fileDir = new File("/data2/nfs/www/booktxt/" + csBookId );//一个id就行了,上面重复了,先注释掉
            if (!fileDir.exists() || !fileDir.isDirectory()) {
                //新建出文件夹
                System.out.println("新建出文件夹");
                fileDir.mkdirs();
            }
            //开始抓取章节内容
            //final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", csChapterinfo.getCpbookid(), csChapterinfo.getCpchapterid());//这里出错了!老代码也是获取序号!
            final String URL = String.format("%s/%s/%s/%s.txt", ApiConstant.Config.BASE_PATH, "booktxt", csChapterinfo.getCpbookid(), csChapterinfo.getSort());

            String bookContent = HttpClientUtil.doGet(URL);

            File file = new File("/data2/nfs/www/booktxt/" + csBookId + "/" + csChapterinfo.getId() + ".txt");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //如果已经存在,就直接
                continue;
            }
            try {
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(bookContent);
                logger.info(csChapterinfo.getName() + ":章节内容保存成功,保存成功!");
                bw.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("章节抓取成功");
        }
    }

    /**
     * 文件输入流，用于读取文件中的数据到Java程序中
     */
    public  static String  read(String lwzPath){
        FileInputStream fis=null;
        try {
            //1.定义文件输入流对象并指定要读取的文件的绝对路径 例如 d:/aa.txt
            fis=new FileInputStream(lwzPath);
            //2.定义一个字节数组，用于存放从文件中读取出来数据（它起到一个中转的作用）
            byte b[]=new byte[1024];
            //3.定义一个整型变量，表示每次中文件中读取多少个字节到Java程序中
            int len=0;
            //4.循环读取数据到程序中
            //读取数据到字节数组中，并返回本次读取了多少个字节，如果返回-1 表示文件读取完成没有更多的内容
            while((len=fis.read(b))>=0){
                //对文件中的数据进行操作，我们的操作就是将数据输出到控制台
                return new String(b,0,len);
                /*  System.out.println(new String(b,0,len));*/
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fis!=null){
                //5.关闭所用的流（必须关闭）
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private CsBookinfo packageCsbook(BookInfo bookInfo) {
        CsBookinfo csBookinfo = new CsBookinfo();
        csBookinfo.setName(bookInfo.getBookName());
        csBookinfo.setArticlename(bookInfo.getBookName());
        csBookinfo.setAuthor(bookInfo.getAuthor());
        //获取网络图片
        File tempFile = fileOptionUtil.getFileByUrl(bookInfo.getPicUrl());
        MultipartFile multipartFile = fileOptionUtil.file2MultipartFile(tempFile);
        //上传到北京区景像的OSS
        AliyunOSSUtil.FileUploadResult uploadResult = aliyunOSSUtil.uploadFileToNxstory(multipartFile);
        String picUrl = uploadResult.getName().replace("http://res.nxstory.com/cover/", "");
        csBookinfo.setCover(picUrl);
        csBookinfo.setCpid(1056);
        csBookinfo.setCpbookid(Long.valueOf(bookInfo.getId()));
        csBookinfo.setKeyword(bookInfo.getBookName());
        csBookinfo.setCategory(0);
        csBookinfo.setCategoryname(bookInfo.getCategory());
        csBookinfo.setWords(bookInfo.getWords());
        csBookinfo.setSerial(bookInfo.getCompleteState());
        csBookinfo.setStatus(1);
        csBookinfo.setReadtype(false);
        csBookinfo.setAddtime(new Date());
        csBookinfo.setUpdatetime(new Date());
        csBookinfo.setChargetype(0);
        csBookinfo.setUnitprice(0);
        csBookinfo.setTotalprice(0);
        csBookinfo.setIntro(bookInfo.getDescription());
        return csBookinfo;
    }
}
