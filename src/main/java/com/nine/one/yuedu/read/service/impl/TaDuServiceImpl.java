package com.nine.one.yuedu.read.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nine.one.yuedu.read.mapper.CsBookinfoMapper;
import com.nine.one.yuedu.read.mapper.CsChapterinfoMapper;
import com.nine.one.yuedu.read.mapper.TempMapper;
import com.nine.one.yuedu.read.mapper.TimeTaduToJxtcMapper;
import com.nine.one.yuedu.read.service.TaDuService;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:李王柱
 * 2020/8/28
 */


@Service(value = "taDuService")
public class TaDuServiceImpl implements TaDuService {
    @Autowired
    private CsChapterinfoMapper csChapterinfoMapper;

    @Autowired
    private TimeTaduToJxtcMapper timeTaduToJxtcMapper;

    @Autowired
    private TempMapper tempMapper;

    @Autowired
    private CsBookinfoMapper csBookinfoMapper;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String uptateChapter(Integer page) throws Exception {

        /*String getBookCountUrl="http://topenapi.tadu.com:8098/api/getBookCount";//成功,查询书籍总数接口 {"code":0,"message":"success!","result":{"count":243}}
        String getBookListUrl="http://topenapi.tadu.com:8098/api/getBookList";//成功,查询书籍列表接口 { "code": 0, "message": "success!", "result": [ { "cpid": 1155, "bookName": "%E9%9C%B8%E5%A4%A9%E7%A5%96%E9%BE%99%E5%86%B3" },{ "cpid": 1156, "bookName": "%E7%81%B5%E5%BC%82%E7%9B%B8%E5%86%8C" },{ "cpid": 1157, "bookName": "%E8%B1%AA%E5%AE%85%E6%80%A8%E9%AD%82" } ] }
        String getChapterListUrl="http://topenapi.tadu.com:8098/api/getChapterList";
        int copyrightid=292;
        String secre="90cb352f4f36e289d6d9abc778b23748";
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*/
        String getBookCountUrl="http://openapi.tadu.com/api/getBookCount";//成功,查询书籍总数接口 {"code":0,"message":"success!","result":{"count":243}}
        String getBookListUrl="http://openapi.tadu.com/api/getBookList";//成功,查询书籍列表接口 { "code": 0, "message": "success!", "result": [ { "cpid": 1155, "bookName": "%E9%9C%B8%E5%A4%A9%E7%A5%96%E9%BE%99%E5%86%B3" },{ "cpid": 1156, "bookName": "%E7%81%B5%E5%BC%82%E7%9B%B8%E5%86%8C" },{ "cpid": 1157, "bookName": "%E8%B1%AA%E5%AE%85%E6%80%A8%E9%AD%82" } ] }
        String getChapterListUrl="http://openapi.tadu.com/api/getChapterList";
        int copyrightid=295;
        String secre="d66ff2bb7b43b0866f8f9eed286362e9";
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";//key=SHA-1(copyrightid+secre)

        //查出塔读授权的书列表,遍历
        //在遍历中,调用塔读的查询章节目录接口
List<Integer> cpBookIdInfo =csBookinfoMapper.selectCpBookIdInfo(1049);
for (Integer cpid :cpBookIdInfo){
    //创建一个请求参数map集合
    Map<String, Object> requestDataMap = new HashMap<String, Object>();
    requestDataMap.put("copyrightid",copyrightid);
    requestDataMap.put("key",key);
    requestDataMap.put("cpid",cpid);
    String HttpResult = HttpClientUtilsGX.doPost(getChapterListUrl, requestDataMap);
    System.out.println(HttpResult);
    JSONObject jsonObject = JSONObject.parseObject(HttpResult);
    JSONArray resultArray = jsonObject.getJSONArray("result");

}

/*for(int i=0;i<resultArray.size();i++){
            JSONObject jsonObject1 = resultArray.getJSONObject(i);
            String cpid = jsonObject1.getString("cpid");
            System.out.println(cpid);
            String decode = URLDecoder.decode(jsonObject1.getString("bookName"), "utf-8");
            System.out.println(decode);
            System.out.println(i);
        }*/

        return "成功";
    }

    @Override
    public String setCsChapterInfoIdByTimeTaDu() {
        List<CsChapterinfo> select = csChapterinfoMapper.CsChapterInfoIdByTimeTaDu();//查询出查询出所有(数据库中)的授权章节id 和(time表中) 8/12的part_id相同的 这些章节会因为id不同而推送给塔读
        for (CsChapterinfo csChapterinfo : select) {
            TimeTaduToJxtc timeTaduToJxtc = new TimeTaduToJxtc();
            timeTaduToJxtc.setPartId(csChapterinfo.getId());
            TimeTaduToJxtc repeat = timeTaduToJxtcMapper.selectOne(timeTaduToJxtc);//根据章节id找到time表中对应的数据

            Example example = new Example(TimeTaduToJxtc.class);//根据这条数据查出,除了创建时间其它都一样的 (奇葩:有可能章节名还不一样..title暂时不在这判断).andEqualTo("title", repeat.getTitle())
            example.createCriteria().andEqualTo("bookName", repeat.getBookName()).andNotEqualTo("createDate", repeat.getCreateDate()).andEqualTo("partNumb", repeat.getPartNumb());
            List<TimeTaduToJxtc> timeTaduToJxtcs = timeTaduToJxtcMapper.selectByExample(example);
            if (timeTaduToJxtcs.size()==0){
                Temp temp = new Temp();
                temp.setType("塔读的奇葩数据");
                String desc=repeat.getBookName()+"下的"+repeat.getPartNumb()+" 景像ID是"+repeat.getPartId()+"有8月12的数据但是没有其它!";
                System.out.println(desc);
                temp.setHavedesc(desc);

                    tempMapper.insertSelective(temp);

            }
            if (timeTaduToJxtcs.size()>1){//如果查出来奇葩数据
                Temp temp = new Temp();
                temp.setType("奇葩塔读含有两条以上数据!");
                String desc=repeat.getBookName()+"下的"+repeat.getTitle()+"排序是"+repeat.getPartNumb()+" 景像ID是"+repeat.getPartId()+"有多条数据,看看要怎么改!";
                System.out.println(desc);
                temp.setHavedesc(desc);
                if (null==tempMapper.selectOne(temp)){
                    tempMapper.insertSelective(temp);
                }
                    return "数据奇葩!";
            }else {//这就说明数据是正常的两条重复,但是不行,再判断一下

                Integer ids= timeTaduToJxtcs.get(0).getId()-repeat.getId();
                int value = Math.abs(ids);
                if (value>3){
                    Temp temp = new Temp();
                    temp.setType("奇葩塔读两个重复章节id隔得远!");
                    temp.setHavedesc(repeat.getBookName()+"下的"+repeat.getTitle()+" 景像ID是"+repeat.getPartId()+"id隔得远!");
                    if (null==tempMapper.selectOne(temp)){
                        tempMapper.insertSelective(temp);
                    }
                    return "id隔得远,看一下吧";
                }else {
                    //到这里应该能替换了,有异样id,这个id在塔读有对应旧数据,旧数据条数不超标,旧数据id相隔不远
                    //判断:修改后的id在chapterInfo没有,并且修改钱的id在chapterInfo有了
                    if (null==csChapterinfoMapper.selectByPrimaryKey(timeTaduToJxtcs.get(0).getPartId())){
                        if (csChapterinfo.getId()< timeTaduToJxtcs.get(0).getPartId() ){//这里预防一下,旧id怎么比新id数值大,看一下
                            return "旧id"+timeTaduToJxtcs.get(0).getPartId()+"比新id"+csChapterinfo.getId()+"数值还要大?";
                        }else {//在这里终于能进行修改了
                            int flag = csChapterinfoMapper.updateIdByBeforToAfter(csChapterinfo.getId(),timeTaduToJxtcs.get(0).getPartId());
                            if (flag<0){
                                return csChapterinfo.getId()+"修改失败!";
                            }else {
                                Temp temp= new Temp();
                                temp.setType("第二次修改塔读,逻辑是塔读的修改时间8/12");
                                temp.setOld(csChapterinfo.getId()+csChapterinfo.getName());
                                temp.setThat(timeTaduToJxtcs.get(0).getPartId()+timeTaduToJxtcs.get(0).getBookName());
                                tempMapper.insertSelective(temp);
                            }
                        }
                    }else {//在这就是:8/12推送的 bookName相同,但是排序不同,在这里看看章节名是否相同,并且id间隔是否超标
                        System.out.println("这个id是修改对象在time表的同序号:"+timeTaduToJxtcs.get(0).getPartId()+timeTaduToJxtcs.get(0).getTitle());

                        Example exampleTitle = new Example(TimeTaduToJxtc.class);//根据这条数据查出,奇葩:章节名还不一样
                        exampleTitle.createCriteria().andEqualTo("bookName", repeat.getBookName()).andEqualTo("title", repeat.getTitle()).andNotEqualTo("createDate", repeat.getCreateDate());
                        List<TimeTaduToJxtc> timeTaduToJxtcTitle = timeTaduToJxtcMapper.selectByExample(exampleTitle);
                        if (timeTaduToJxtcTitle.size() !=1){
                           for (TimeTaduToJxtc timeTaduToJxtc1:timeTaduToJxtcTitle){
                               System.out.println( timeTaduToJxtc1.getBookName()+timeTaduToJxtc1.getTitle());
                           }
                            return repeat.getBookName()+"下的"+repeat.getTitle()+repeat.getPartId()+"有章节名奇葩,看一下怎么改";
                        }else if (Math.abs(timeTaduToJxtcTitle.get(0).getId()-repeat.getId())<18){//id间隔小于6可以修改了
                            int flag = csChapterinfoMapper.updateIdByBeforToAfter(csChapterinfo.getId(),timeTaduToJxtcTitle.get(0).getPartId());
                            System.out.println("修改"+csChapterinfo.getId()+"为"+timeTaduToJxtcTitle.get(0).getPartId()+timeTaduToJxtcTitle.get(0).getBookName());
                            if (flag<0){
                                return csChapterinfo.getId()+"修改失败!";
                            }else {
                                Temp temp= new Temp();
                                temp.setType("第二次修改塔读,逻辑变化,排序不同,但是章节名相同");
                                temp.setOld(csChapterinfo.getId()+csChapterinfo.getName());
                                temp.setThat(timeTaduToJxtcTitle.get(0).getPartId()+timeTaduToJxtcTitle.get(0).getBookName());
                                tempMapper.insertSelective(temp);
                            }
                        }else {
                           return repeat.getBookName()+"下的"+repeat.getTitle()+repeat.getPartId()+"ID间隔过大,看看";
                        }
                    }
                }
            }
        }
        return "成功";
    }

    @Override
    public JXResult addBookByTaDu() throws Exception {
        int same=0;
        int noHave=0;
        int taduFalse=0;
        StringBuilder stringBuilder=new StringBuilder();
        //拿到景像授权书单
        //遍历书单,从书单获取书籍对象,将书籍对象的信息取出来,调用静态方法
        List<Integer> cpBookIdInfo =csBookinfoMapper.selectCpBookIdInfo(1049);
        for (int bookid:cpBookIdInfo){
            CsBookinfo csBookinfo = csBookinfoMapper.selectByPrimaryKey(bookid);
            if (null==csBookinfo){
                noHave+=1;
                continue;
            }
            int cpid=bookid;
            String bookname=csBookinfo.getName();
            String authorname=csBookinfo.getAuthor();
            String intro=csBookinfo.getIntro();
            if (intro.length()<1){
                intro="快来看看吧!";
            }
            int classid=getClassId(csBookinfo.getCategoryname());
            String coverimage="http://res.nxstory.com/cover/"+csBookinfo.getCover();
            int serial=csBookinfo.getSerial();
            int isvip=1;
            JSONObject jsonObject = addTaDuTestBookUrl(cpid,bookname,authorname,intro,classid,coverimage,serial,isvip);
            if (StringUtils.equals("success!",jsonObject.getString("message"))){
                System.out.println(jsonObject);
            }else {
                if (StringUtils.equals("Repeat to add the same book!",jsonObject.getString("message"))){
                    same+=1;
                }else {
                    taduFalse+=1;
                    stringBuilder.append(jsonObject);
                    System.out.println("有"+taduFalse+"个失败了");
                }

            }
        }
        System.out.println("跳过了"+noHave+"条已经不存在的");
        return new JXResult(true, ApiConstant.StatusCode.OK, "失败总数:"+taduFalse+"有"+same+"本已经添加过的"+stringBuilder.toString());
    }

    @Override
    public JXResult addTaDuChapter() {
        /*String ids="10696825,10696826,10696827,10696828,10696829,10696830,10696831,10696832,10696833,10696834,10696835,10696836,10696837,10696838,10696839,10696840,10696841,10696842,10696843,10696844,10696845,10696846,10696847,10696848,10696849,10696850,10696851,10696852,10696853,10696854,10696855,10696856,10696857,10696858,10696859,10696860,10696861,10696862,10696863,10696864,10696865,10696866,10696867,10696868,10696869,10696870,10696871,10696872,10696873,10696874,10696875,10696876,10696877,10696878,10696879,10696880,10696881,10696882,10696883,10696884,10696885,10696886,10696887,10696888,10696889,10696890,10696891,10696892,10696893,10696894,10696895,10696896,10696897,10696898,10696899,10696900,10696901,10696902,10696903,10696904,10696905,10696906,10696907,10696908,10696909,10696910,10696911,10696912,10696913,10696914,10696915,10696916,10696917,10696918,10696919,10696920,10696921,10696922,10696923,10696924,10696925,10696926,10696927,10696928,10696929,10696930,10696931,10696932,10696933,10696934,10696935,10696936,10696937,10696938,10696939,10696940,10696941,10696942,10696943,10696944,10696945,10696946,10696947,10696948,10696949,10696950,10696951,10696952,10696953,10696954,10696955,10696956,10696957,10696958,10696959,10696960,10696961,10696962,10696963,10696964,10696965,10696966,10696967,10696968,10696969,10696970,10696971,10696972,10696973,10696974,10696975,10696976,10696977,10696978,10696979,10696980,10696981,10696982,10696983,10696984,10696985,10696986,10696987,10696988,10696989,10696990,10696991,10696992,10696993,10696994,10696995,10696996,10696997,10696998,10696999,10697000,10697001,10697002,10697003,10697004,10697005,10697006,10697007,10697008,10697009,10697010,10697011,10697012,10697013,10697014,10697015,10697016,10697017,10697018,10697019,10697020,10697021,10697022,10697023,10697024,10697025,10697026,10697027,10697028,10697029,10697030,10697031,10697032,10697033,10697034,10697035,10697036,10697037,10697038,10697039,10697040,10697041,10697042,10697043,10697044,10697045,10697046,10697047,10697048,10697049,10697050,10697051,10697052,10697053,10697054,10697055,10697056,10697057,10697058,10697059,10697060,10697061,10697062,10697063,10697064,10697065,10697066,10697067,10697068,10697069,10697070,10697071,10697072,10697073,10697074,10697075,10697076,10697077,10697078,10697079,10697080,10697081,10697082,10697083,10697084,10697085,10697086,10697087,10697088,10697089,10697090,10697091,10697092,10697093,10697094,10697095,10697096,10697097,10697098,10697099,10697100,10697101,10697102,10697103,10697104,10697105,10697106,10697107,10697108,10697109,10697110,10697111,10697112,10697113,10697114,10697115,10697116,10697117,10697118,10697119,10697120,10697121,10697122,10697123,10697124,10697125,10697126";
        String[] split = ids.split("[,]");
        for (int i=0;i<split.length;i++){
            System.out.println(split[i]);
            CsChapterinfo csChapterinfo = csChapterinfoMapper.selectByPrimaryKey(split[i]);
            System.out.println("nadao"+split[i]);
        }*/

        //目前塔读给返回的bookid就是在原cpid后面加上0292,如果后面有变动,这个接口可能不可用!
        int success=0;
        int same=0;
        int noHave=0;
        int taduFalse=0;
        StringBuilder DontHaveChapter=new StringBuilder("不存在的章节:");
        StringBuilder stringBuilder=new StringBuilder();
        //拿到景像授权书单
        //遍历书单,从书单获取书籍对象,将书籍对象的信息取出来,调用静态方法
        //和添加书不同,这个是从静态方法的结果中,获取塔读的书id
        List<Integer> cpBookIdInfo =csBookinfoMapper.selectCpBookIdInfo(1049); //从景像获取塔读授权书id
        logger.info("接收到景像给塔读的授权书单id");
        //遍历,循环体子对象中,把这个id作为bookid拿到章节list,给静态方法传参
        for (int bookid:cpBookIdInfo){//在这里遍历书籍id,这个id有可能存在已经没有的书id
            CsChapterinfo csChapterEx = new CsChapterinfo();
            csChapterEx.setBookid(bookid);
            List<CsChapterinfo> select = csChapterinfoMapper.select(csChapterEx);
            if (select.size()<1){
                stringBuilder.append("这个id的书在景像已经不存在了:"+bookid);
                noHave+=1;
            }
            String taduBookId=bookid+"0295";
            //String taduBookId=bookid+"0292";
            for (CsChapterinfo csChapterinfo:select){//在这里遍历章节
                //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
                String lwzPath="/data2/nfs/www/booktxt/" + bookid + "/" + csChapterinfo.getId()+ ".txt";
                File haveFile=new File(lwzPath);
                if (!haveFile.exists()){
                    logger.info("章节id"+csChapterinfo.getId()+csChapterinfo.getName()+"在景像不存在了");
                    DontHaveChapter.append(csChapterinfo.getId()+",");
                    continue;
                }
                String content = read(lwzPath);
                JSONObject resultJson = addChapterUrl(Integer.parseInt(taduBookId),csChapterinfo.getName(),content,csChapterinfo.getSort(),csChapterinfo.getIsvip(),csChapterinfo.getId(),1);
                if (StringUtils.equals("success!",resultJson.getString("message"))){
                   // logger.info("书id"+bookid+"章节id"+csChapterinfo.getId()+csChapterinfo.getName()+"成功");
                   success+=1;
                }else {
                    if (StringUtils.equals("parts repeat!",resultJson.getString("message"))){
                        same+=1;
                    }else {
                        taduFalse+=1;
                        stringBuilder.append(resultJson);
                        logger.info("有"+taduFalse+"个失败了"+csChapterinfo.getName()+"章节id:"+csChapterinfo.getId());
                    }
                }
            }
        }
        logger.info("跳过了"+noHave+"条已经不存在的");
        return new JXResult(true, ApiConstant.StatusCode.OK, DontHaveChapter+"成功了"+success+"条"+"失败总数:"+taduFalse+"有"+same+"本已经添加过的"+stringBuilder.toString());
    }

    @Override
    public JXResult queryDontHave() {
        //目前塔读给返回的bookid就是在原cpid后面加上0292,如果后面有变动,这个接口可能不可用!
        int success=0;
        int same=0;
        int noHave=0;
        int taduFalse=0;
        StringBuilder DontHaveChapter=new StringBuilder("不存在的章节:");
        StringBuilder stringBuilder=new StringBuilder();
        //拿到景像授权书单
        //遍历书单,从书单获取书籍对象,将书籍对象的信息取出来,调用静态方法
        //和添加书不同,这个是从静态方法的结果中,获取塔读的书id
        List<Integer> cpBookIdInfo =csBookinfoMapper.selectCpBookIdInfo(1049); //从景像获取塔读授权书id
        logger.info("获取到景像给塔读的授权书单id");
        //遍历,循环体子对象中,把这个id作为bookid拿到章节list,给静态方法传参
        for (int bookid:cpBookIdInfo){//在这里遍历书籍id,这个id有可能存在已经没有的书id
            CsChapterinfo csChapterEx = new CsChapterinfo();
            csChapterEx.setBookid(bookid);
            List<CsChapterinfo> select = csChapterinfoMapper.select(csChapterEx);
            if (select.size()<1){
                logger.info("这个id的书在景像已经不存在了:"+bookid);
                stringBuilder.append("这个id的书在景像已经不存在了:"+bookid);
                noHave+=1;
            }
            for (CsChapterinfo csChapterinfo:select){//在这里遍历章节
                //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
                String lwzPath="/data2/nfs/www/booktxt/" + bookid + "/" + csChapterinfo.getId()+ ".txt";
                File haveFile=new File(lwzPath);
                if (!haveFile.exists()){
                    logger.info("章节id"+csChapterinfo.getId()+csChapterinfo.getName()+"在景像不存在了");
                    DontHaveChapter.append(csChapterinfo.getId()+",");
                    continue;
                }
                    success+=1;
            }
        }
        logger.info("跳过了"+noHave+"条已经不存在的");
        return new JXResult(true, ApiConstant.StatusCode.OK, DontHaveChapter+"成功了"+success+"条"+stringBuilder.toString());
    }

    @Override
    public JXResult addTaDuChapterBySelective() {
        //目前塔读给返回的bookid就是在原cpid后面加上0292,如果后面有变动,这个接口可能不可用!
        int success=0;
        int same=0;
        int noHave=0;
        int taduFalse=0;
        StringBuilder DontHaveChapter=new StringBuilder("不存在的章节:");
        StringBuilder stringBuilder=new StringBuilder();
        //拿到景像授权书单
        //遍历书单,从书单获取书籍对象,将书籍对象的信息取出来,调用静态方法
        //和添加书不同,这个是从静态方法的结果中,获取塔读的书id
        List<Integer> cpBookIdInfo =csBookinfoMapper.selectCpBookIdInfo(1049); //从景像获取塔读授权书id
        logger.info("接收到景像给塔读的授权书单id");
        //遍历,循环体子对象中,把这个id作为bookid拿到章节list,给静态方法传参
        for (int bookid:cpBookIdInfo){//在这里遍历书籍id,这个id有可能存在已经没有的书id
            int bookFalse=0;
            CsChapterinfo csChapterEx = new CsChapterinfo();
            csChapterEx.setBookid(bookid);
            List<CsChapterinfo> select = csChapterinfoMapper.select(csChapterEx);
            if (select.size()<1){
                stringBuilder.append("这个id的书在景像已经不存在了:"+bookid);
                noHave+=1;
            }
            String taduBookId=bookid+"0295";
            //String taduBookId=bookid+"0292";
            for (CsChapterinfo csChapterinfo:select){//在这里遍历章节
                //先调用塔读的查询章节详情接口,如果没有就向塔读上传这个章节
                JSONObject chapterDetailJson = getChapterDetailUrl(csChapterinfo.getBookid(),csChapterinfo.getId());
                String chapterDetailMessage = chapterDetailJson.getString("message");
                if (StringUtils.equals("success!",chapterDetailMessage)){
                    same+=1;
                    continue;//查询到章节,就已存在+1,跳过这次循环
                }
                if (!StringUtils.equals("There is no part!",chapterDetailMessage)){
                    taduFalse+=1;//如果没有跳过循环,并且返回的结果不是章节不存在
                    stringBuilder.append(chapterDetailMessage);
                    System.out.println(chapterDetailMessage);
                    continue;
                }
                //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
                String lwzPath="/data2/nfs/www/booktxt/" + bookid + "/" + csChapterinfo.getId()+ ".txt";
                File haveFile=new File(lwzPath);
                if (!haveFile.exists()){
                    logger.info("章节id"+csChapterinfo.getId()+csChapterinfo.getName()+"在景像不存在了");
                    DontHaveChapter.append(csChapterinfo.getId()+",");
                    continue;
                }
                String content = read(lwzPath);
                JSONObject resultJson = addChapterUrl(Integer.parseInt(taduBookId),csChapterinfo.getName(),content,csChapterinfo.getSort(),csChapterinfo.getIsvip(),csChapterinfo.getId(),1);
                if (StringUtils.equals("success!",resultJson.getString("message"))){
                    // logger.info("书id"+bookid+"章节id"+csChapterinfo.getId()+csChapterinfo.getName()+"成功");
                    success+=1;
                }else {
                    if (StringUtils.equals("parts repeat!",resultJson.getString("message"))){
                        same+=1;
                    }else {
                        bookFalse+=1;
                        taduFalse+=1;
                        stringBuilder.append(resultJson);
                        System.out.println(resultJson);
                        logger.info("这本书有"+bookFalse+"个失败了,这次失败的章节:"+csChapterinfo.getName()+"章节id:"+csChapterinfo.getId());
                    }
                }
            }
            System.out.println(bookid+"循环完毕");
        }
        logger.info("跳过了"+noHave+"条已经不存在的");
        return new JXResult(true, ApiConstant.StatusCode.OK, DontHaveChapter+"成功了"+success+"条"+"失败总数:"+taduFalse+"有"+same+"本已经添加过的"+stringBuilder.toString());
    }

    @Override
    public JXResult addTaDuChapterByGetChapterList() {
        //遍历授权id 根据id向塔读获取他们的章节list 根据id获取景像章节list 遍历景像list,把景像list里面不存在于塔读的推送
        int success=0;//所有成功的章节
        int successBook=0;//添加成功的书
        int same=0;//已经添加过塔读的章节
        int taduFalseBook=0;//添加塔读出现错误的书
        int taduFalse=0;//调用塔读接口失败的章节
        StringBuilder DontHaveChapter=new StringBuilder("不存在的章节:");
        int noHave=0;//景像不存在的书
        StringBuilder stringBuilder=new StringBuilder();
        //拿到景像授权书单
        //遍历书单,从书单获取书籍对象,将书籍对象的信息取出来,调用静态方法
        //和添加书不同,这个是从静态方法的结果中,获取塔读的书id
        List<Integer> cpBookIdInfo =csBookinfoMapper.selectCpBookIdInfo(1049); //从景像获取塔读授权书id
        logger.info("接收到景像给塔读的授权书单id");


        //遍历,循环体子对象中,把这个id作为bookid拿到章节list,给静态方法传参
        for (int bookid:cpBookIdInfo){//在这里遍历书籍id,这个id有可能存在已经没有的书id
            String taduBookId=bookid+"0295";//塔读正式环境的书id是在原来的上面加上0295
            //String taduBookId=bookid+"0292";//塔读测试环境的书id是在原来的上面加上0292

            CsChapterinfo csChapterEx = new CsChapterinfo();//根据书id获取景像章节列表
            csChapterEx.setBookid(bookid);
            List<CsChapterinfo> select = csChapterinfoMapper.select(csChapterEx);//得到景像这本书的章节list
            if (select.size()<1){
                stringBuilder.append("这个id的书在景像已经不存在了:"+bookid);
                noHave+=1;
            }

            //遍历授权id 根据id向塔读获取他们的章节list 遍历景像list,把景像list里面不存在于塔读的推送
            JSONObject taduChapterListJson = getChapterListUrl(bookid);
            String message=taduChapterListJson.getString("message");
            JSONArray result = taduChapterListJson.getJSONArray("result");//得到这本书的塔读章节list
            if (StringUtils.equals("success!",message)){//如果访问塔读章节列表接口返回成功,曾经往里加过章节

                for (CsChapterinfo csChapterinfo:select){//遍历景像在这本书的章节list,将塔读不存在的章节进行推送
                    Integer id = csChapterinfo.getId();
                    int flag=0;
                    for (int i=0;i<result.size();i++){
                        JSONObject taduOne = result.getJSONObject(i);
                        String taduChapterid=taduOne.getString("chapterid");
                        if (Integer.parseInt(taduChapterid)==id){
                            same+=1;
                            flag=1;
                            continue;
                        }
                    }
                    //如果循环完毕还没有这章,就向塔读推送
                    if (0==flag){
                        System.out.println("已推送过的书中章节不全,向塔读补推"+id);
                        //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
                        String lwzPath="/data2/nfs/www/booktxt/" + bookid + "/" + csChapterinfo.getId()+ ".txt";
                        File haveFile=new File(lwzPath);
                        if (!haveFile.exists()){
                            logger.info("章节id"+csChapterinfo.getId()+csChapterinfo.getName()+"在景像不存在了");
                            DontHaveChapter.append(csChapterinfo.getId()+",");
                            continue;
                        }
                        String content = read(lwzPath);
                        JSONObject resultJson = addChapterUrl(Integer.parseInt(taduBookId),csChapterinfo.getName(),content,csChapterinfo.getSort(),csChapterinfo.getIsvip(),csChapterinfo.getId(),1);
                        if (StringUtils.equals("success!",resultJson.getString("message"))){
                            logger.info("书id"+bookid+"章节id"+csChapterinfo.getId()+csChapterinfo.getName()+"补推成功");
                            success+=1;
                        }else {
                            taduFalse+=1;
                            logger.info(resultJson.toJSONString());
                        }
                    }
                }
            }else if (StringUtils.equals("There is no part!",message)){//这个说明这本书还没有添加章节,直接往里加就行了
                System.out.println("这本书没有添加过章节,向塔读推送");
                for (CsChapterinfo csChapterinfo:select){//遍历景像在这本书的章节list,将新的章节进行推送
                    Integer id = csChapterinfo.getId();
                        //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
                        String lwzPath="/data2/nfs/www/booktxt/" + bookid + "/" + csChapterinfo.getId()+ ".txt";
                        File haveFile=new File(lwzPath);
                        if (!haveFile.exists()){
                            logger.info("章节id"+csChapterinfo.getId()+csChapterinfo.getName()+"在景像不存在了");
                            DontHaveChapter.append(csChapterinfo.getId()+",");
                            continue;
                        }
                        String content = read(lwzPath);
                        JSONObject resultJson = addChapterUrl(Integer.parseInt(taduBookId),csChapterinfo.getName(),content,csChapterinfo.getSort(),csChapterinfo.getIsvip(),csChapterinfo.getId(),1);
                        if (StringUtils.equals("success!",resultJson.getString("message"))){
                            //logger.info("书id"+bookid+"章节id"+csChapterinfo.getId()+csChapterinfo.getName()+"新推送成功");
                            success+=1;
                        }else {
                            taduFalse+=1;
                            logger.info(resultJson.toJSONString());
                        }
                }
            }else { //章节列表接口返回失败,stringBuilder把调用失败信息加上去,taduFalseBook+=1
                taduFalseBook+=1;
                stringBuilder.append(message);
            }
            successBook+=1;
            System.out.println(bookid+"循环完毕"+"目前成功"+success+"条章节  "+successBook+"本书  "+"有"+taduFalse+"个塔读访问失败的章节   "+same+"章已经添加过的");
        }
        logger.info("跳过了"+noHave+"条已经不存在的");

        return new JXResult(true, ApiConstant.StatusCode.OK, DontHaveChapter+"成功了"+success+"条"+"失败总数:"+taduFalse+"有"+same+"章已经添加过的"+stringBuilder.toString());
    }

    @Override
    public JXResult updateChapterBySerialization() throws Exception {
        int success=0;//所有成功的章节
        int successBook=0;//添加成功的书
        int same=0;//已经添加过塔读的章节
        int taduFalseBook=0;//塔读出现错误的书
        int taduFalse=0;//调用塔读接口失败的章节
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder DontHaveChapter=new StringBuilder();

        //1.获取塔读在景像连载并上线的书
        List<CsBookinfo> taduBookList= csBookinfoMapper.selectSerialAndOnlineByCpid(1049);
        System.out.println(taduBookList.size());
        for (CsBookinfo csBookinfo:taduBookList){
            String taduBookId=csBookinfo.getId()+"0295";//String taduBookId=csBookinfo.getId()+"0292";
            //2.查询塔读在这本书的最新章节,
            JSONObject taduUpdate = getUpdateInfoUrl(csBookinfo.getId());
            if ( !StringUtils.equals("success!",taduUpdate.getString("message")) ){
                stringBuilder.append(taduUpdate);
                taduFalse+=1;
            }else {
                JSONObject taduResult = taduUpdate.getJSONObject("result");
                Integer chapternum = taduResult.getInteger("chapternum");
                CsChapterinfo jingxiangNewest = csChapterinfoMapper.selectMaxChapterByBookid(csBookinfo.getId());
                Integer sort = jingxiangNewest.getSort();
                if (chapternum.intValue()!=sort.intValue()){
                //if (true){
                    //3.如果小于景像这本书的最新章节,
                    System.out.println(csBookinfo.getName()+chapternum+"在景像更新到了:"+sort);
                     List<CsChapterinfo> afterThatChapterList=csChapterinfoMapper.selectAfterThatChapterList(csBookinfo.getId(),chapternum);
                    /*CsChapterinfo byselect = new CsChapterinfo();
                    byselect.setBookid(31971);
                    List<CsChapterinfo> afterThatChapterList = csChapterinfoMapper.select(byselect);*/

                    System.out.println("获取准备向塔读推送的章节:"+afterThatChapterList.size());
                    for (CsChapterinfo csChapterinfo:afterThatChapterList){//获取景像在这个章节之后的list,遍历推送
                        Integer id = csChapterinfo.getId();
                        //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
                        String lwzPath="/data2/nfs/www/booktxt/" + csChapterinfo.getBookid() + "/" + id+ ".txt";
                        File haveFile=new File(lwzPath);
                        if (!haveFile.exists()){
                            logger.info("章节id"+id+csChapterinfo.getName()+"在景像不存在了");
                            DontHaveChapter.append(id+",");
                            continue;
                        }
                        //String content = read(lwzPath);
                        String content = txt2String(haveFile);
                        JSONObject resultJson = addChapterUrl(Integer.parseInt(taduBookId),csChapterinfo.getName(),content,csChapterinfo.getSort(),csChapterinfo.getIsvip(),id,1);
                        if (StringUtils.equals("success!",resultJson.getString("message"))){
                            logger.info(csChapterinfo.getName()+"新推送成功"+"从景像获取的章节字数是:"+content.length());
                            success+=1;
                        }else {
                            if (StringUtils.equals("parts repeat!",resultJson.getString("message"))){
                                System.out.println("已推送过了章节");
                                same+=1;
                            }else {
                                taduFalse+=1;
                                logger.info(resultJson.toJSONString());
                            }
                        }
                    }
                    //推送完了以后,获取前面的20章,获取景像的章节名,如果在塔读没有这个章节名,就推送
                    List<CsChapterinfo> supplement=csChapterinfoMapper.selectAfterThatChapterListBySupplement(csBookinfo.getId(),chapternum-20,chapternum);
                    System.out.println("获取准备向塔读补推的章节:"+supplement.size());
                    for (CsChapterinfo csChapterinfoBySupplement:supplement){//遍历推送
                        Integer id = csChapterinfoBySupplement.getId();
                        //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
                        String lwzPath="/data2/nfs/www/booktxt/" + csChapterinfoBySupplement.getBookid() + "/" + id+ ".txt";
                        File haveFile=new File(lwzPath);
                        if (!haveFile.exists()){
                            logger.info("章节id"+id+csChapterinfoBySupplement.getName()+"在景像不存在了");
                            DontHaveChapter.append(id+",");
                            continue;
                        }
                        String content = txt2String(haveFile);
                        JSONObject resultJson = addChapterUrl(Integer.parseInt(taduBookId),csChapterinfoBySupplement.getName(),content,csChapterinfoBySupplement.getSort(),csChapterinfoBySupplement.getIsvip(),id,1);
                        if (StringUtils.equals("success!",resultJson.getString("message"))){
                            logger.info(csChapterinfoBySupplement.getName()+"补推成功");
                            Temp temp = new Temp();
                            temp.setType("塔读章节序号和景像不一致,补充推送");
                            temp.setHavedesc("书籍"+csBookinfo.getId()+"章节"+csChapterinfoBySupplement.getName()+"排序"+csChapterinfoBySupplement.getSort());
                            int tempResult = tempMapper.insertSelective(temp);
                        }else {
                            if (StringUtils.equals("parts repeat!",resultJson.getString("message"))){

                            }else {
                                taduFalse+=1;
                                logger.info(resultJson.toJSONString());
                            }
                        }
                    }
                    successBook+=1;//
                    System.out.println("成功了"+successBook+"本书;"+csBookinfo.getName()+"循环完毕,成功章节有:"+success+"失败了:"+taduFalse);
                }
            }
        }
        if (DontHaveChapter.length()<1){
            DontHaveChapter=new StringBuilder("景像已不存在的章节!").append(DontHaveChapter);
        }
        return new JXResult(true, ApiConstant.StatusCode.OK, "成功了"+successBook+"本书;"+success+"章"+"失败总数:"+taduFalse+"有"+same+"章已经添加过的"+stringBuilder.toString()+DontHaveChapter);
    }

    @Override
    public JXResult TestWords(Integer csBookid,Integer csChapterid) {
        //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
        String lwzPath="/data2/nfs/www/booktxt/" + csBookid + "/" + csChapterid+ ".txt";
        File haveFile=new File(lwzPath);
        if (!haveFile.exists()){
            logger.info("章节:"+csBookid+csChapterid+"在景像不存在了");
            return new JXResult(false,ApiConstant.StatusCode.ERROR,"本地失败","章节:"+csBookid+csChapterid+"在景像不存在了");
        }
        //String content = read(lwzPath);
        //readTxt(lwzPath);
        String content = txt2String(haveFile);
        //System.out.println("从景像获取的章节字数是:"+content);
        return new JXResult(true,ApiConstant.StatusCode.OK,"查询到了"+content,"fdf");
        //return new JXResult(true,ApiConstant.StatusCode.OK,"查询到了字数:"+content.length(),content);
    }

    @Override
    public JXResult updateChapterByBookid(Integer bookid) throws Exception {
        int success=0;//所有成功的章节
        int same=0;//已经添加过塔读的章节
        int taduFalseBook=0;//塔读出现错误的书
        int taduFalse=0;//调用塔读接口失败的章节
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder DontHaveChapter=new StringBuilder();
        //1.获取这本书的塔读id
        String taduBookId=bookid+"0295";

        //2.查询塔读在这本书的最新章节,
        JSONObject taduUpdate = getUpdateInfoUrl(bookid);
        if ( !StringUtils.equals("success!",taduUpdate.getString("message")) ){
            return new JXResult(false,ApiConstant.StatusCode.ERROR,"塔读查询最新章节失败",taduUpdate);
        }else{
            JSONObject taduResult = taduUpdate.getJSONObject("result");
            Integer chapternum = taduResult.getInteger("chapternum");
            CsChapterinfo jingxiangNewest = csChapterinfoMapper.selectMaxChapterByBookid(bookid);
            Integer sort = jingxiangNewest.getSort();
            if (chapternum.intValue()!=sort.intValue()){
                //if (true){
                //3.如果小于景像这本书的最新章节,
                System.out.println(bookid+" 在塔读的最新:"+chapternum+" 在景像更新到了:"+sort);
                List<CsChapterinfo> afterThatChapterList=csChapterinfoMapper.selectAfterThatChapterList(bookid,chapternum);

                System.out.println("获取准备向塔读推送的章节:"+afterThatChapterList.size());
                for (CsChapterinfo csChapterinfo:afterThatChapterList){//获取景像在这个章节之后的list,遍历推送
                    Integer id = csChapterinfo.getId();
                    //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
                    String lwzPath="/data2/nfs/www/booktxt/" + csChapterinfo.getBookid() + "/" + id+ ".txt";
                    File haveFile=new File(lwzPath);
                    if (!haveFile.exists()){
                        logger.info("章节id"+id+csChapterinfo.getName()+"在景像不存在了");
                        DontHaveChapter.append(id+",");
                        continue;
                    }
                    //String content = read(lwzPath);
                    String content = txt2String(haveFile);
                    JSONObject resultJson = addChapterUrl(Integer.parseInt(taduBookId),csChapterinfo.getName(),content,csChapterinfo.getSort(),csChapterinfo.getIsvip(),id,1);
                    if (StringUtils.equals("success!",resultJson.getString("message"))){
                        logger.info(csChapterinfo.getName()+"新推送成功");
                        success+=1;
                    }else {
                        if (StringUtils.equals("parts repeat!",resultJson.getString("message"))){
                            System.out.println("已推送过了章节");
                            same+=1;
                        }else {
                            taduFalse+=1;
                            logger.info(resultJson.toJSONString());
                        }
                    }
                }
                //推送完了以后,获取前面的20章,获取景像的章节名,如果在塔读没有这个章节名,就推送
                List<CsChapterinfo> supplement=csChapterinfoMapper.selectAfterThatChapterListBySupplement(bookid,chapternum-20,chapternum);
                System.out.println("获取准备向塔读推送的章节:"+supplement.size());
                for (CsChapterinfo csChapterinfoBySupplement:supplement){//遍历推送
                    Integer id = csChapterinfoBySupplement.getId();
                    //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
                    String lwzPath="/data2/nfs/www/booktxt/" + csChapterinfoBySupplement.getBookid() + "/" + id+ ".txt";
                    File haveFile=new File(lwzPath);
                    if (!haveFile.exists()){
                        logger.info("章节id"+id+csChapterinfoBySupplement.getName()+"在景像不存在了");
                        DontHaveChapter.append(id+",");
                        continue;
                    }
                    String content = txt2String(haveFile);
                    JSONObject resultJson = addChapterUrl(Integer.parseInt(taduBookId),csChapterinfoBySupplement.getName(),content,csChapterinfoBySupplement.getSort(),csChapterinfoBySupplement.getIsvip(),id,1);
                    if (StringUtils.equals("success!",resultJson.getString("message"))){
                        logger.info(csChapterinfoBySupplement.getName()+"补推成功");
                        Temp temp = new Temp();
                        temp.setType("塔读章节序号和景像不一致,补充推送");
                        temp.setHavedesc("书籍"+bookid+"章节"+csChapterinfoBySupplement.getName()+"排序"+csChapterinfoBySupplement.getSort());
                        int tempResult = tempMapper.insertSelective(temp);
                    }else {
                        if (StringUtils.equals("parts repeat!",resultJson.getString("message"))){

                        }else {
                            taduFalse+=1;
                            logger.info(resultJson.toJSONString());
                        }
                    }
                }
            }
        }
        return new JXResult(true,ApiConstant.StatusCode.OK,"完毕","成功了:"+success+"个章节"+stringBuilder+"失败了:"+taduFalse+DontHaveChapter);
    }

    @Override
    public JXResult updateChapterByBookidAndChapterSortBetwn(Integer bookid, Integer startsort, Integer endsort) {
        StringBuilder DontHaveChapter = new StringBuilder();
        int taduFalse=0;
        int same=0;
        int success=0;
        String taduBookId=bookid+"0295";
        //推送完了以后,获取前面的20章,获取景像的章节名,如果在塔读没有这个章节名,就推送
        List<CsChapterinfo> supplement=csChapterinfoMapper.selectAfterThatChapterListBySupplement(bookid,startsort,endsort);
        System.out.println("获取准备向塔读推送的章节:"+supplement.size());
        for (CsChapterinfo csChapterinfoBySupplement:supplement){//遍历推送
            Integer id = csChapterinfoBySupplement.getId();
            //从本地获取章节,这些文件在nfs服务器上,其他地方运行代码会找不到文件
            String lwzPath="/data2/nfs/www/booktxt/" + csChapterinfoBySupplement.getBookid() + "/" + id+ ".txt";
            File haveFile=new File(lwzPath);
            if (!haveFile.exists()){
                logger.info("章节id"+id+csChapterinfoBySupplement.getName()+"在景像不存在了");
                DontHaveChapter.append(id+",");
                continue;
            }
            String content = txt2String(haveFile);
            JSONObject resultJson = addChapterUrl(Integer.parseInt(taduBookId),csChapterinfoBySupplement.getName(),content,csChapterinfoBySupplement.getSort(),csChapterinfoBySupplement.getIsvip(),id,1);
            if (StringUtils.equals("success!",resultJson.getString("message"))){
                logger.info(csChapterinfoBySupplement.getName()+"补推成功");
                Temp temp = new Temp();
                temp.setType("塔读章节序号和景像不一致,补充推送");
                temp.setHavedesc("书籍"+bookid+"章节"+csChapterinfoBySupplement.getName()+"排序"+csChapterinfoBySupplement.getSort());
                tempMapper.insertSelective(temp);
                success+=1;
            }else {
                if (StringUtils.equals("parts repeat!",resultJson.getString("message"))){
                    same+=1;
                }else {
                    taduFalse+=1;
                    logger.info(resultJson.toJSONString());
                }
            }
        }
        return new JXResult(true,ApiConstant.StatusCode.OK,"完毕","成功了:"+success+"个章节;塔读已经有的:"+same+",失败了:"+taduFalse+DontHaveChapter);
    }

    /*@Override
    public JXResult sortInconsistent(Integer csBookid) {
        //1.调用塔读获取章节目录
        JSONObject taduChapterListUrl = getChapterListUrl(csBookid);
        JSONArray result = taduChapterListUrl.getJSONArray("result");
        //2.遍历章节目录,从单个json中获取数据
        for (int i=300;i<result.size();i++){
            JSONObject oneJson = result.getJSONObject(i);
            Integer chapterid = oneJson.getInteger("chapterid");
            String taduTitle = oneJson.getString("title");
            String title = URLEncoder.encode(taduTitle);
            Integer chapternum = oneJson.getInteger("chapternum");

            CsChapterinfo csChapterinfo = csChapterinfoMapper.selectByPrimaryKey(chapterid.intValue());
            if (null==csChapterinfo){
                System.out.println("取得null");
            }
            String name = csChapterinfo.getName();
            Integer sort = csChapterinfo.getSort();


            if (StringUtils.equals(name,title)){
                return new JXResult(false,ApiConstant.StatusCode.ERROR,"章节名不同","景像名:"+name+"塔读:"+title);
            }
            if (sort.intValue()!=chapternum.intValue()){
                return new JXResult(false,ApiConstant.StatusCode.ERROR,"章节序号不同","景像序号:"+sort+"塔读:"+chapternum);
            }
        }
        return new JXResult(true,ApiConstant.StatusCode.OK,"完毕","完毕");
    }*/

    //添加、更新章节接口
    public static JSONObject addChapterUrl(int bookid,String title,String content,int chapternum,int isvip,int chapterid,int updatemode){
        //{"code":-20202,"message":"parts repeat!"} 如果已经推送过这个章节了
        /*String addChapterUrl="http://topenapi.tadu.com:8098/api/addChapter";//添加、更新章节
        int copyrightid=292;
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*/
        String addChapterUrl="http://openapi.tadu.com/api/addChapter";//添加、更新章节
        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";//key=SHA-1(copyrightid+secre)
        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("bookid",bookid);//塔读的bookid,
        requestDataMap.put("title",title);
        requestDataMap.put("content",content);
        requestDataMap.put("chapternum",chapternum);
        requestDataMap.put("isvip",isvip);
        requestDataMap.put("chapterid",chapterid);
        requestDataMap.put("updatemode",updatemode);//状态：1 为新增章节 2 为修改章节
        String HttpResult = null;
        try {
            HttpResult = HttpClientUtilsGX.doPost(addChapterUrl, requestDataMap);
        } catch (Exception e) {
            System.out.println("添加章节网络失败,再次请求"+bookid+title+chapternum);
            try {
                HttpResult = HttpClientUtilsGX.doPost(addChapterUrl, requestDataMap);
                System.out.println("第二次添加章节 网络请求成功");
            } catch (Exception e1) {
                System.out.println("第二次请求也失败,网络状况不佳!");
                try {
                    HttpResult = HttpClientUtilsGX.doPost(addChapterUrl, requestDataMap);
                    System.out.println("第三次添加章节 网络请求成功");
                } catch (Exception e2) {
                    System.out.println("第三次请求也失败,请检查网络!");
                }
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        return jsonObject;
    }

    public static JSONObject getChapterListUrl(int cpid){//查询章节目录接口
       /* String getUpdateInfoUrl="http://topenapi.tadu.com:8098/api/getChapterList";//这本书没添加过章节时:{"code":-20302,"message":"There is no part!"}
        int copyrightid=292;
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*/
        String getUpdateInfoUrl="http://openapi.tadu.com/api/getChapterList";//这本书没添加过章节时:{"code":-20302,"message":"There is no part!"}
        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";//key=SHA-1(copyrightid+secre)
        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("cpid",cpid);
        String HttpResult = null;
        try {
            HttpResult = HttpClientUtilsGX.doPost(getUpdateInfoUrl, requestDataMap);
        } catch (Exception e) {
            System.out.println(cpid+"获取章节列表第一次网络失败");
            try {
                HttpResult = HttpClientUtilsGX.doPost(getUpdateInfoUrl, requestDataMap);
            } catch (Exception e1) {
                System.out.println(cpid+"获取章节列表第2次网络失败");
                try {
                    HttpResult = HttpClientUtilsGX.doPost(getUpdateInfoUrl, requestDataMap);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    System.out.println(cpid+"网络访问出错!可能是塔读关闭接口了");
                }
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        return jsonObject;
    }

    public static JSONObject addTaDuTestBookUrl(int cpid, String bookname, String authorname, String intro, int classid, String coverimage, int serial, int isvip) {
        /*String addBookUrl="http://topenapi.tadu.com:8098/api/addBook";//添加书籍接口
        int copyrightid=292;
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*/
        String addBookUrl="http://openapi.tadu.com/api/addBook";//添加书籍接口
        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";

        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("cpid",cpid);requestDataMap.put("bookname",bookname);requestDataMap.put("authorname",authorname);requestDataMap.put("intro",intro);requestDataMap.put("classid",classid);requestDataMap.put("coverimage",coverimage);requestDataMap.put("serial",serial);requestDataMap.put("isvip",isvip);
        String HttpResult = null;
        try {
            HttpResult = HttpClientUtilsGX.doPost(addBookUrl, requestDataMap);
        } catch (Exception e) {
            System.out.println("添加书籍网络失败,再次请求");
            try {
                HttpResult = HttpClientUtilsGX.doPost(addBookUrl, requestDataMap);
                System.out.println("第二次添加书籍 网络请求成功");
            } catch (Exception e1) {
                System.out.println("第二次请求也失败,请检查网络!");
                e1.printStackTrace();
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        return jsonObject;
    }

    public static JSONObject getChapterDetailUrl(Integer cpid,Integer chapterid){//查询章节详情接口
        //查询章节详情,参数1:书籍id,参数2:章节id
        //成功:{"code":0,"message":"success!","result":{"content":"%E2%80%9C%E5%94%89%EF%BC%8C%E7%AD%89%E7%AD%89%EF%BC%8C%E7%AD%89%E7%AD%89%EF%BC%8C%E4%BB%8A%E5%A4%A9%E5%85%AC%E4%BA%A4%E6%98%AF%E4%B8%8D%E6%98%AF%E5%8F%88%E6%97%A9%E5%BC%80%E4%BA%86%E3%80%82%E2%80%9D%0D%0A%E5%AE%89%E8%BE%B0%E5%9C%A8%E5%90%8E%E9%9D%A2%E4%B8%8D%E5%81%9C%E7%9A%84%E8%BF%BD%E8%B5%B6%E7%9D%80%E5%89%8D%E9%9D%A2%E5%BF%AB%E8%A6%81%E5%85%B3%E9%97%A8%E7%9A%84%E5%85%AC%E4%BA%A4%E8%BD%A6%EF%BC%8C%E7%BB%88%E4%BA%8E%EF%BC%8C%E5%9C%A8%E6%9C%80%E5%90%8E%E4%BA%94%E7%A7%92%E9%92%9F%E7%9A%84%E6%97%B6%E5%80%99%E8%B5%B6%E4%B8%8A%E4%BA%86%E8%BF%99%E7%8F%AD%E6%9C%AB%E7%8F%AD%E8%BD%A6%E3%80%82%0D%0A%E2%80%9C%E5%B0%8F%E4%BC%99%E5%AD%90%EF%BC%8C%E5%8F%88%E6%98%AF%E4%BD%A0%E5%95%8A%EF%BC%8C%E6%88%91%E5%A4%A9%E5%A4%A9%E4%B8%BA%E4%BA%86%E7%AD%89%E4%BD%A0%E5%8F%AF%E6%98%AF%E9%83%BD%E7%89%B9%E6%84%8F%E6%99%9A%E4%B8%80%E5%88%86%E9%92%9F%E5%8F%91%E8%BD%A6%E7%9A%84%E5%99%A2%EF%BC%81%E2%80%9D%0D%0A%E2%80%9C%E5%97%AF%EF%BC%8C%E8%B0%A2%E8%B0%A2%E8%80%81%E5%A4%A7%E7%88%B7%E3%80%82%E2%80%9D%E5%AE%89%E8%BE%B0%E9%81%93%E8%B0%A2%E4%B9%8B%E5%90%8E%EF%BC%8C%E4%B9%92%E9%93%83%E4%B9%93%E5%95%B7%E7%9A%84%E4%B8%A2%E4%BA%86%E4%B8%80%E4%B8%AA%E7%A1%AC%E5%B8%81%E5%88%B0%E9%92%B1%E7%AE%B1%E9%87%8C%E9%9D%A2%E3%80%82%0D%0A%E2%80%9C%E5%B0%8F%E4%BC%99%E5%AD%90%EF%BC%8C%E5%81%9A%E4%BB%80%E4%B9%88%E5%B7%A5%E4%BD%9C%E7%9A%84%E5%91%A2%EF%BC%9F%E2%80%9D%E5%85%AC%E4%BA%A4%E8%BD%A6%E5%8F%B8%E6%9C%BA%E8%AF%A2%E9%97%AE%E5%88%B0%EF%BC%8C%E4%B8%80%E5%8F%AA%E6%89%8B%E8%BF%98%E4%B8%8D%E5%81%9C%E7%9A%84%E5%9C%A8%E5%81%9A%E7%9D%80%E6%95%B0%E9%92%B1%E7%9A%84%E5%A7%BF%E5%8A%BF%E3%80%82%0D%0A%E2%80%9C%E5%97%A8%EF%BC%8C%E5%88%AB%E6%8F%90%E4%BA%86%EF%BC%8C%E5%BA%94%E8%81%98%E4%BA%86%E5%87%A0%E6%AC%A1%E5%B7%A5%E4%BD%9C%EF%BC%8C%E9%83%BD%E8%A2%AB%E7%82%92%E4%BA%86%E3%80%82%E4%BB%8A%E5%A4%A9%E8%BF%99%E4%B8%80%E6%AC%A1%EF%BC%8C%E5%8F%88%E8%A2%AB%E7%82%92%E4%BA%86%E3%80%82%E2%80%9D%E5%AE%89%E8%BE%B0%E6%97%A0%E5%A5%88%E7%9A%84%E6%91%87%E4%BA%86%E6%91%87%E5%A4%B4%EF%BC%8C%E4%BB%96%E7%9A%84%E7%94%9F%E6%B4%BB%EF%BC%8C%E5%A4%AA%E6%83%A8%E6%B7%A1%E4%BA%86%E3%80%82%0D%0A%E2%80%9C%E8%BF%99%E6%A0%B7%E5%95%8A%EF%BC%8C%E5%B0%8F%E4%BC%99%E5%AD%90%EF%BC%8C%E4%BD%A0%E6%90%AC%E4%B8%8A%E4%B8%8A%E9%9D%A2%E7%9A%84%E6%89%B6%E6%89%8B%EF%BC%8C%E5%B0%8F%E5%BF%83%E5%88%AB%E6%91%94%E8%B7%A4%E5%92%AF%E3%80%82%E2%80%9D%E8%80%81%E5%A4%A7%E7%88%B7%E7%BC%A9%E4%BC%9A%E9%82%A3%E5%8F%AA%E5%81%9A%E7%9D%80%E7%AE%97%E9%92%B1%E7%9A%84%E5%A7%BF%E5%8A%BF%E7%9A%84%E6%89%8B%EF%BC%8C%E4%B9%9F%E4%B8%8D%E5%86%8D%E7%BB%A7%E7%BB%AD%E5%92%8C%E5%AE%89%E8%BE%B0%E9%97%B2%E8%B0%88%EF%BC%8C%E5%AE%89%E7%A8%B3%E7%9A%84%E5%BC%80%E8%B5%B7%E8%BD%A6%E6%9D%A5%E4%BA%86%E3%80%82%0D%0A%E5%AE%89%E8%BE%B0%E7%9C%BC%E7%A5%9E%E7%A9%BA%E6%B4%9E%E7%9A%84%E6%9C%9B%E7%9D%80%E7%AA%97%E5%A4%96%EF%BC%8C%E5%8F%B9%E6%81%AF%E7%9A%84%E5%A3%B0%E9%9F%B3%E4%BB%8E%E5%98%B4%E5%B7%B4%E9%87%8C%E9%9D%A2%E4%BC%A0%E5%87%BA%E6%9D%A5%E3%80%82%E8%87%AA%E5%B7%B1%E7%9A%84%E6%9C%AA%E6%9D%A5%EF%BC%8C%E5%9C%A8%E5%93%AA%E9%87%8C%EF%BC%9F%0D%0A%E8%87%AA%E5%B7%B1%E6%98%AF%E4%B8%8D%E6%98%AF%E5%9C%A8%E8%BF%99%E5%BA%A7%E5%A4%A7%E5%9F%8E%E5%B8%82%EF%BC%8C%E6%B7%B7%E4%B8%8D%E4%B8%8B%E5%8E%BB%E4%BA%86%E3%80%82%0D%0A%E8%BF%99%E8%AE%A9%E4%BB%96%E6%83%B3%E8%B5%B7%E4%BA%86%E4%B8%80%E5%8F%A5%E8%AF%9D%EF%BC%8C%E5%9F%8E%E5%B8%82%E5%A5%97%E8%B7%AF%E6%B7%B1%EF%BC%8C%E6%88%91%E8%A6%81%E5%9B%9E%E5%86%9C%E6%9D%91%E3%80%82%0D%0A%E5%AE%89%E8%BE%B0%E6%9C%AC%E5%B0%B1%E6%98%AF%E4%B9%A1%E4%B8%8B%E5%AD%A9%E5%AD%90%EF%BC%8C%E4%B8%BA%E4%BA%BA%E4%B9%9F%E5%BE%88%E8%80%81%E5%AE%9E%EF%BC%8C%E5%8F%AF%E6%98%AF%E4%B8%8A%E5%A4%A9%E5%8D%B4%E5%A6%82%E5%90%8C%E6%8A%8A%E4%BB%96%E9%81%97%E5%BC%83%E4%BA%86%E4%B8%80%E8%88%AC%E3%80%82%E4%B8%8D%E7%AE%A1%E4%BB%96%E5%81%9A%E4%BB%80%E4%B9%88%E5%B7%A5%E4%BD%9C%EF%BC%8C%E8%80%81%E6%9D%BF%E9%83%BD%E5%9B%A0%E4%B8%BA%E4%BB%96%E5%A4%AA%E8%AF%9A%E5%AE%9E%E8%80%8C%E7%82%92%E6%8E%89%E4%BB%96%E3%80%82%0D%0A%E6%83%B3%E8%B5%B7%E8%87%AA%E5%B7%B1%E5%89%8D%E5%87%A0%E6%AC%A1%E6%89%BE%E5%B7%A5%E4%BD%9C%E7%9A%84%E7%BB%8F%E5%8E%86%EF%BC%8C%E5%AE%89%E8%BE%B0%E7%8E%B0%E5%9C%A8%E9%83%BD%E8%A7%89%E5%BE%97%E4%B8%8D%E7%9F%A5%E9%81%93%E5%82%BB%E7%9A%84%E5%8F%AF%E4%BB%A5%E3%80%82%E6%98%AF%E7%9A%84%EF%BC%8C%E9%82%A3%E5%B7%B2%E7%BB%8F%E4%B8%8D%E8%83%BD%E5%8F%AB%E5%81%9A%E8%AF%9A%E5%AE%9E%E5%AE%88%E4%BF%A1%E4%BA%86%EF%BC%8C%E9%82%A3%E5%B0%B1%E6%98%AF%E5%82%BB%EF%BC%81%0D%0A%E7%AC%AC%E4%B8%80%E6%AC%A1%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%8E%BB%E5%A4%A7%E5%95%86%E5%9C%BA%E5%BA%94%E8%81%98%E3%80%82%E6%9C%AC%E6%9D%A5%E8%80%81%E6%9D%BF%E5%BE%88%E7%9C%8B%E5%A5%BD%EF%BC%8C%E7%BB%93%E6%9E%9C%E4%BB%96%E5%92%8C%E8%80%81%E6%9D%BF%E5%81%9A%E7%94%B5%E6%A2%AF%E7%9A%84%E6%97%B6%E5%80%99%EF%BC%8C%E4%B8%8D%E5%B7%A7%E7%9A%84%E6%98%AF%E8%80%81%E6%9D%BF%E5%9C%A8%E7%94%B5%E6%A2%AF%E9%87%8C%E9%9D%A2%E6%89%93%E4%BA%86%E4%B8%AA%E5%B1%81%EF%BC%8C%E5%BD%93%E6%97%B6%E8%80%81%E6%9D%BF%E5%8F%88%E7%AB%99%E5%9C%A8%E4%BB%96%E8%BA%AB%E8%BE%B9%E3%80%82%0D%0A%E5%99%97%E7%9A%84%E4%B8%80%E5%A3%B0%E8%AE%A9%E4%BB%96%E8%A7%89%E5%BE%97%E5%A4%A7%E8%85%BF%E4%B8%8A%E9%9D%A2%E6%9C%89%E4%B8%80%E9%98%B5%E7%83%AD%E9%A3%8E%E5%90%B9%E6%9D%A5%EF%BC%8C%E4%B8%80%E4%B8%8B%E6%B2%A1%E5%BF%8D%E4%BD%8F%E7%AC%91%E4%BA%86%E5%87%BA%E6%9D%A5%E3%80%82%E5%BD%93%E6%97%B6%E5%9C%A8%E7%94%B5%E6%A2%AF%E4%B8%8A%E9%9D%A2%E5%B0%B1%E7%9C%9F%E7%9A%84%E4%B8%8D%E5%BF%8C%E8%AE%B3%E7%9A%84%E8%AF%B4%E5%87%BA%E8%80%81%E6%9D%BF%E6%89%93%E4%BA%86%E5%B1%81%EF%BC%8C%E8%BF%98%E4%B8%80%E7%9B%B4%E5%93%88%E5%93%88%E5%A4%A7%E7%AC%91%E3%80%82%0D%0A%E5%85%B6%E4%BB%96%E5%90%8C%E4%BA%8B%E6%B2%A1%E6%9C%89%E6%8D%82%E4%BD%8F%E5%98%B4%E5%B7%B4%EF%BC%8C%E6%83%B3%E7%AC%91%E5%8F%88%E4%B8%8D%E6%95%A2%E7%AC%91%EF%BC%8C%E4%BB%96%E4%B9%9F%E4%B8%8D%E4%BC%9A%E7%9C%8B%E7%9C%BC%E8%89%B2%EF%BC%8C%E5%BC%84%E5%BE%97%E8%80%81%E6%9D%BF%E5%BE%88%E6%98%AF%E4%B8%A2%E8%84%B8%EF%BC%8C%E4%BA%8E%E6%98%AF%E8%80%81%E6%9D%BF%E4%B8%80%E6%B0%94%E4%B9%8B%E4%B8%8B%EF%BC%8C%E5%B0%B1%E8%AE%A9%E4%BB%96%E6%BB%9A%E4%BA%86%E3%80%82%0D%0A%E7%AC%AC%E4%BA%8C%E6%AC%A1%EF%BC%8C%E4%BB%96%E5%9C%A8%E5%81%9A%E5%8C%BB%E7%94%9F%EF%BC%8C%E6%8A%8A%E4%B8%80%E4%B8%AA%E4%B9%85%E6%B2%BB%E4%B8%8D%E6%84%88%E7%9A%84%E7%97%85%E4%BA%BA%E6%B2%BB%E5%A5%BD%E4%BA%86%EF%BC%8C%E8%BF%98%E5%BE%97%E6%84%8F%E6%B4%8B%E6%B4%8B%E4%B8%80%E7%9B%B4%E5%9C%A8%E7%82%AB%E8%80%80%E3%80%82%0D%0A%E6%99%9A%E4%B8%8A%E8%A2%AB%E7%A7%81%E4%BA%BA%E8%AF%8A%E6%89%80%E7%9A%84%E8%80%81%E6%9D%BF%E5%8F%AB%E8%BF%87%E5%8E%BB%E8%B0%88%E8%AF%9D%EF%BC%8C%E8%BF%98%E4%BB%A5%E4%B8%BA%E8%80%81%E6%9D%BF%E6%98%AF%E5%9B%A0%E4%B8%BA%E4%BB%96%E5%8C%BB%E6%9C%AF%E9%AB%98%E6%98%8E%EF%BC%8C%E8%A6%81%E7%BB%99%E4%BB%96%E5%8D%87%E8%81%8C%E5%8A%A0%E8%96%AA%E3%80%82%0D%0A%E6%B2%A1%E6%83%B3%E5%88%B0%E8%80%81%E6%9D%BF%E5%91%8A%E8%AF%89%E4%BB%96%EF%BC%8C%E9%82%A3%E4%B8%AA%E7%97%85%E4%BA%BA%E5%B0%B1%E6%98%AF%E5%90%83%E4%B8%8D%E5%AE%8C%E7%9A%84%E8%A5%BF%E7%93%9C%E7%9A%AE%EF%BC%8C%E4%BB%96%E6%98%AF%E6%95%85%E6%84%8F%E4%B8%8D%E6%B2%BB%E5%A5%BD%E7%9A%84%EF%BC%8C%E7%8E%B0%E5%9C%A8%E5%AE%89%E8%BE%B0%E6%B2%BB%E5%A5%BD%E4%BA%86%EF%BC%8C%E5%B0%B1%E5%B0%91%E4%BA%86%E4%B8%80%E4%B8%AA%E5%AE%9A%E6%9C%9F%E9%80%81%E9%92%B1%E7%9A%84%E4%BA%86%E3%80%82%0D%0A%E4%BA%8E%E6%98%AF%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%B0%B1%E8%A2%AB%E2%80%A6%E2%80%A6%0D%0A%E2%80%9C%E5%94%89%EF%BC%81%E2%80%9D%E5%AE%89%E8%BE%B0%E5%8F%88%E5%8F%B9%E4%BA%86%E4%B8%80%E5%8F%A3%E6%B0%94%EF%BC%8C%E6%8A%8A%E8%A7%86%E7%BA%BF%E7%A7%BB%E5%90%91%E4%BA%86%E7%AA%97%E6%88%B7%E7%9A%84%E8%BF%9C%E5%A4%84%E3%80%82%E2%80%9C%E5%92%A6%EF%BC%81%E2%80%9D%0D%0A%E7%9C%8B%E7%9D%80%E8%BF%9C%E5%A4%84%E7%9A%84%E4%BA%BA%E5%BD%B1%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%A5%BD%E5%83%8F%E8%A7%89%E5%BE%97%E6%9C%89%E4%BA%9B%E7%86%9F%E6%82%89%EF%BC%8C%E4%BB%94%E7%BB%86%E4%B8%80%E7%9C%8B%EF%BC%8C%E6%89%8D%E5%8F%91%E7%8E%B0%E6%98%AF%E8%87%AA%E5%B7%B1%E7%9A%84%E5%A5%B3%E6%9C%8B%E5%8F%8B%E3%80%82%0D%0A%E5%AE%89%E8%BE%B0%E7%9A%84%E5%A5%B3%E6%9C%8B%E5%8F%8B%E5%8F%AB%E6%9D%A8%E6%99%93%E6%99%93%EF%BC%8C%E4%B8%80%E4%B8%AA%E7%89%B9%E5%88%AB%E6%BC%82%E4%BA%AE%E7%9A%84%E5%A5%B3%E5%AD%A9%E5%AD%90%E3%80%82%E8%BA%AB%E6%9D%90%E4%B9%9F%E5%A5%BD%EF%BC%8C%E8%84%BE%E6%B0%94%E4%B9%9F%E5%A5%BD%E3%80%82%E5%9C%A8%E5%AE%B6%E9%87%8C%EF%BC%8C%E4%B8%80%E5%89%AF%E8%B4%A4%E5%A6%BB%E8%89%AF%E6%AF%8D%E7%9A%84%E8%A1%A8%E6%83%85%E3%80%82%0D%0A%E8%80%8C%E4%B8%94%E5%AF%B9%E5%AE%89%E8%BE%B0%E7%9A%84%E8%A6%81%E6%B1%82%E4%B9%9F%E4%B8%8D%E9%AB%98%EF%BC%8C%E7%8E%B0%E5%9C%A8%E5%AE%89%E8%BE%B0%E6%AF%8F%E5%A4%A9%E5%8E%BB%E5%81%9A%E4%B8%B4%E6%97%B6%E5%B7%A5%E4%B9%8B%E7%B1%BB%E7%9A%84%E4%BA%8B%E6%83%85%EF%BC%8C%E4%B8%80%E4%B8%AA%E6%9C%88%E5%B7%AE%E4%B8%8D%E5%A4%9A%E5%8F%AA%E8%83%BD%E6%B7%B7%E5%88%B0%E4%B8%A4%E5%8D%83%E5%9D%97%E9%92%B1%EF%BC%8C%E5%A5%B9%E4%B9%9F%E6%AF%AB%E6%97%A0%E6%80%A8%E8%A8%80%E3%80%82%0D%0A%E5%A5%B9%E8%AF%B4%E8%BF%87%EF%BC%8C%E4%BC%9A%E5%92%8C%E5%AE%89%E8%BE%B0%E4%B8%80%E8%B5%B7%E5%8A%AA%E5%8A%9B%EF%BC%8C%E6%9C%89%E4%B8%80%E4%B8%AA%E8%87%AA%E5%B7%B1%E7%9A%84%E5%B0%8F%E7%AA%9D%E3%80%82%E5%B0%B1%E7%AE%97%E5%B0%8F%E7%AA%9D%E5%86%8D%E5%B0%8F%EF%BC%8C%E4%B9%9F%E6%AF%94%E8%BF%99%E4%B8%AA%E6%9C%88%E7%A7%9F%E9%87%91%E4%B8%89%E7%99%BE%E7%9A%84%E5%9C%B0%E6%96%B9%E8%A6%81%E5%A5%BD%E3%80%82%0D%0A%E5%94%AF%E4%B8%80%E8%AE%A9%E5%AE%89%E8%BE%B0%E6%84%9F%E5%88%B0%E6%AC%A3%E6%85%B0%E7%9A%84%EF%BC%8C%E5%B0%B1%E6%98%AF%E5%A5%B9%E8%BF%99%E4%B8%AA%E5%A5%B3%E6%9C%8B%E5%8F%8B%E4%BA%86%E3%80%82%E7%8E%B0%E5%9C%A8%EF%BC%8C%E4%BB%96%E4%BB%AC%E4%B9%9F%E6%89%93%E7%AE%97%E5%A5%BD%E4%BA%86%EF%BC%8C%E5%86%8D%E8%BF%87%E4%B8%A4%E4%B8%AA%E6%9C%88%E5%88%B0%E4%BA%86%E5%9B%BD%E5%BA%86%E8%8A%82%EF%BC%8C%E4%BB%96%E4%BB%AC%E5%B0%B1%E7%BB%93%E5%A9%9A%E3%80%82%E6%9D%A8%E6%99%93%E6%99%93%E4%B9%9F%E7%AD%94%E5%BA%94%E4%BB%96%EF%BC%8C%E7%AD%89%E7%BB%93%E5%A9%9A%E7%9A%84%E9%82%A3%E5%A4%A9%E5%B0%B1%E4%BC%9A%E6%8A%8A%E8%87%AA%E5%B7%B1%E4%BA%A4%E7%BB%99%E4%BB%96%E3%80%82%0D%0A%E7%AA%81%E7%84%B6%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%8F%91%E7%8E%B0%E5%A5%B3%E6%9C%8B%E5%8F%8B%E7%9A%84%E5%8A%A8%E4%BD%9C%E6%9C%89%E4%BA%9B%E5%88%AB%E6%89%AD%EF%BC%8C%E5%88%B0%E5%BA%95%E6%98%AF%E6%80%8E%E4%B9%88%E5%88%AB%E6%89%AD%EF%BC%8C%E8%BF%99%E4%B8%AA%E8%A7%92%E5%BA%A6%E4%B9%9F%E7%9C%8B%E4%B8%8D%E5%A4%AA%E6%B8%85%E6%A5%9A%E3%80%82%0D%0A%E4%BD%86%E6%98%AF%EF%BC%8C%E7%AD%89%E5%85%AC%E4%BA%A4%E8%BD%A6%E5%BC%80%E5%88%B0%E5%90%88%E9%80%82%E8%A7%92%E5%BA%A6%E7%9A%84%E6%97%B6%E5%80%99%EF%BC%8C%E4%BB%96%E7%9C%8B%E6%B8%85%E6%A5%9A%E4%BA%86%E3%80%82%E8%80%8C%E7%9C%8B%E6%B8%85%E6%A5%9A%E4%B9%8B%E5%90%8E%EF%BC%8C%E6%95%B4%E4%B8%AA%E4%BA%BA%E9%83%BD%E6%87%B5%E4%BA%86%E3%80%82%0D%0A%E6%9D%A8%E6%99%93%E6%99%93%EF%BC%8C%E6%AD%A3%E8%A2%AB%E4%B8%80%E4%B8%AA%E5%AF%8C%E4%BA%8C%E4%BB%A3%E6%A0%B7%E7%9A%84%E7%9A%84%E6%90%82%E7%9D%80%E6%9F%B3%E6%9D%A1%E7%BB%86%E8%85%B0%EF%BC%8C%E8%84%B8%E4%B8%8A%E4%B9%9F%E6%98%AF%E6%9C%89%E8%AF%B4%E6%9C%89%E7%AC%91%E7%9A%84%E3%80%82%E8%A6%81%E7%9F%A5%E9%81%93%EF%BC%8C%E6%9D%A8%E6%99%93%E6%99%93%E8%AF%B4%E7%BB%93%E5%A9%9A%E4%B9%8B%E5%89%8D%E5%8F%AA%E8%AE%A9%E5%AE%89%E8%BE%B0%E7%89%B5%E6%89%8B%EF%BC%8C%E8%BF%99%E8%85%B0%EF%BC%8C%E5%AE%89%E8%BE%B0%E7%A2%B0%E9%83%BD%E6%B2%A1%E7%A2%B0%E8%BF%87%E3%80%82%0D%0A%E6%8B%B3%E5%A4%B4%EF%BC%8C%E6%8F%A1%E7%B4%A7%E4%BA%86%EF%BC%8C%E7%89%99%E9%BD%BF%EF%BC%8C%E5%92%AC%E5%90%88%E4%BA%86%E3%80%82%E4%BD%86%E6%98%AF%E4%B8%8B%E4%B8%80%E7%A7%92%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%8F%88%E6%94%BE%E6%9D%BE%E4%B8%8B%E6%9D%A5%E4%BA%86%E3%80%82%E4%BB%96%E5%BF%83%E9%87%8C%E5%AE%89%E6%85%B0%E8%87%AA%E5%B7%B1%EF%BC%8C%E8%AF%B4%E4%B8%8D%E5%AE%9A%EF%BC%8C%E9%82%A3%E5%8F%AA%E6%98%AF%E5%A5%B9%E7%9A%84%E5%93%A5%E5%93%A5%E5%91%A2%EF%BC%9F%0D%0A%E5%8F%AA%E4%B8%8D%E8%BF%87%E8%BF%99%E4%BB%85%E4%BB%85%E5%8F%AA%E6%98%AF%E5%AE%89%E6%85%B0%E7%BD%A2%E4%BA%86%EF%BC%8C%E6%9C%89%E5%93%AA%E4%B8%AA%E5%93%A5%E5%93%A5%E4%BC%9A%E5%9C%A8%E5%A4%A7%E5%BA%AD%E5%B9%BF%E4%BC%97%E4%B9%8B%E4%B8%8B%E6%90%82%E8%87%AA%E5%B7%B1%E5%A6%B9%E5%A6%B9%E7%9A%84%E8%85%B0%E3%80%82%E8%BF%99%E9%83%BD%E7%AE%97%E4%B8%8D%E4%B8%8A%E5%AE%89%E6%85%B0%EF%BC%8C%E5%8F%AA%E8%83%BD%E7%AE%97%E8%87%AA%E6%AC%BA%E6%AC%BA%E4%BA%BA%E4%BA%86%E3%80%82%0D%0A%E2%80%A6%E2%80%A6%0D%0A%E8%BF%87%E4%BA%86%E4%B8%8D%E4%B9%85%EF%BC%8C%E5%85%AC%E4%BA%A4%E8%BD%A6%E5%90%AC%E4%BA%86%EF%BC%8C%E5%88%9A%E5%A5%BD%E5%88%B0%E7%AB%99%E3%80%82%E5%AE%89%E8%BE%B0%E5%92%8C%E5%85%AC%E4%BA%A4%E8%BD%A6%E5%8F%B8%E6%9C%BA%E9%81%93%E5%88%AB%E4%B9%8B%E5%90%8E%EF%BC%8C%E5%B0%B1%E4%B8%8B%E8%BD%A6%E4%BA%86%E3%80%82%0D%0A%E5%A4%B1%E9%AD%82%E8%90%BD%E9%AD%84%E7%9A%84%E8%B8%A9%E7%9D%80%E5%8F%B0%E9%98%B6%EF%BC%8C%E5%A4%B1%E9%AD%82%E8%90%BD%E9%AD%84%E7%9A%84%E6%89%93%E5%BC%80%E6%88%BF%E9%97%A8%EF%BC%8C%E9%87%8C%E9%9D%A2%E5%9D%90%E7%9D%80%E4%B8%80%E4%B8%AA%E6%B8%85%E7%BA%AF%E5%8F%AF%E7%88%B1%E7%9A%84%E5%A5%B3%E5%AD%A9%E5%AD%90%E3%80%82%E8%BF%99%E4%B8%AA%E5%A5%B3%E5%AD%A9%E5%AD%90%EF%BC%8C%E6%AD%A3%E6%98%AF%E6%9D%A8%E6%99%93%E6%99%93%E3%80%82%0D%0A%E2%80%9C%E6%99%93%E6%99%93%E5%95%8A%EF%BC%8C%E4%BB%8A%E5%A4%A9%E5%9C%A8%E5%93%AA%E9%87%8C%E5%81%9A%E4%BA%8B%E5%95%8A%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E6%AD%A5%E8%A1%8C%E8%A1%97%E5%95%8A%EF%BC%8C%E6%88%91%E5%9C%A8%E9%82%A3%E9%87%8C%E6%91%86%E5%9C%B0%E6%91%8A%E5%90%A7%EF%BC%81%E2%80%9D%0D%0A%E5%AE%89%E8%BE%B0%E7%9A%84%E5%98%B4%E8%A7%92%E9%9C%B2%E5%87%BA%E4%B8%80%E6%8A%B9%E8%8B%A6%E7%AC%91%EF%BC%8C%E8%BF%99%E5%9C%B0%E7%82%B9%EF%BC%8C%E5%80%92%E6%98%AF%E6%B2%A1%E6%9C%89%E8%B0%8E%E6%8A%A5%E3%80%82%0D%0A%E2%80%9C%E8%BF%99%E6%A0%B7%E5%95%8A%EF%BC%8C%E9%99%A4%E4%BA%86%E4%BD%A0%E8%BF%98%E6%9C%89%E8%B0%81%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E6%B2%A1%E4%BA%BA%E4%BA%86%E5%91%80%EF%BC%8C%E6%88%91%E4%B8%80%E4%B8%AA%E4%BA%BA%E5%9C%A8%E5%81%9A%E4%BA%8B%E5%91%80%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E7%9C%9F%E7%9A%84%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E6%80%8E%E4%B9%88%E5%91%80%EF%BC%81%E4%BD%A0%E7%9B%B8%E4%BF%A1%E6%88%91%E4%B9%88%EF%BC%9F%E2%80%9D%E6%9D%A8%E6%99%93%E6%99%93%E7%9A%84%E8%AF%AD%E6%B0%94%E5%BC%B1%E5%BC%B1%E7%9A%84%EF%BC%8C%E4%B8%80%E5%89%AF%E6%A5%9A%E6%A5%9A%E5%8F%AF%E6%80%9C%E7%9A%84%E6%A0%B7%E5%AD%90%E7%9C%8B%E7%9D%80%E5%AE%89%E8%BE%B0%E3%80%82%0D%0A%E5%A6%82%E6%9E%9C%E6%98%AF%E4%BB%A5%E5%89%8D%EF%BC%8C%E5%AE%89%E8%BE%B0%E4%B8%80%E5%AE%9A%E8%A2%AB%E6%9F%94%E5%8C%96%E4%BA%86%EF%BC%8C%E7%8E%B0%E5%9C%A8%EF%BC%8C%E4%BB%96%E5%8F%AA%E8%A7%89%E5%BE%97%E6%81%B6%E5%BF%83%E3%80%82%0D%0A%E7%BB%88%E4%BA%8E%EF%BC%8C%E5%AE%89%E8%BE%B0%E6%B2%A1%E6%9C%89%E7%BB%99%E5%87%BA%E8%82%AF%E5%AE%9A%E7%9A%84%E5%9B%9E%E7%AD%94%EF%BC%8C%E5%8F%AA%E6%98%AF%E5%90%90%E5%87%BA%E4%BA%86%E7%AE%80%E5%8D%95%E7%9A%84%E5%87%A0%E4%B8%AA%E5%AD%97%E3%80%82%E2%80%9C%E6%88%91%E7%9C%8B%E8%A7%81%E4%BA%86%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E4%BD%A0%E8%B7%9F%E8%B8%AA%E6%88%91%EF%BC%9F%E2%80%9D%E6%9D%A8%E6%99%93%E6%99%93%E4%B8%8B%E6%84%8F%E8%AF%86%E7%9A%84%E7%AB%99%E4%B8%8B%E6%9D%A5%EF%BC%8C%E7%94%A8%E9%82%A3%E5%8F%8C%E6%9D%8F%E7%9C%BC%E6%80%92%E8%A7%86%E7%9D%80%E5%AE%89%E8%BE%B0%E3%80%82%0D%0A%E2%80%9C%E6%B2%A1%E6%9C%89%EF%BC%8C%E6%88%91%E5%9D%90%E8%BD%A6%E7%BB%8F%E8%BF%87%E9%82%A3%E9%87%8C%E3%80%82%E9%82%A3%E6%98%AF%E4%BD%A0%E8%B0%81%EF%BC%8C%E4%BD%A0%E6%96%B0%E7%9A%84%E7%94%B7%E6%9C%8B%E5%8F%8B%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E5%AE%89%E8%BE%B0%EF%BC%8C%E9%9A%BE%E9%81%93%E6%88%91%E4%B8%8D%E9%9C%80%E8%A6%81%E5%88%B0%E5%A4%96%E9%9D%A2%E5%8E%BB%E8%B5%9A%E9%92%B1%EF%BC%8C%E4%BD%A0%E4%BB%A5%E4%B8%BA%E5%B0%B1%E5%87%AD%E4%BD%A0%E6%88%91%E4%BB%AC%E5%8F%AF%E4%BB%A5%E6%9C%89%E5%A4%9A%E4%B9%88%E5%A5%BD%E7%9A%84%E7%94%9F%E6%B4%BB%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E4%BD%A0%E8%B5%9A%E9%92%B1%E7%9A%84%E6%96%B9%E6%B3%95%E5%B0%B1%E6%98%AF%E5%82%8D%E5%A4%A7%E6%AC%BE%EF%BC%8C%E7%84%B6%E5%90%8E%E7%BB%99%E4%BB%96%E4%BB%AC%E4%B9%B1%E6%91%B8%E4%B8%80%E9%80%9A%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E6%88%91%E6%B2%A1%E6%9C%89%EF%BC%81%E2%80%9D%E6%9D%A8%E6%99%93%E6%99%93%E5%98%9F%E8%B5%B7%E5%98%B4%E5%B7%B4%EF%BC%8C%E5%8F%8C%E7%9C%BC%E9%83%BD%E8%A6%81%E5%86%92%E5%87%BA%E7%81%AB%E6%9D%A5%E4%BA%86%E3%80%82%0D%0A%E2%80%9C%E6%88%91%E7%9C%8B%E8%A7%81%E4%BA%86%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E6%88%91%E2%80%A6%E2%80%A6%E2%80%9D%0D%0A%E2%80%9C%E6%88%91%E4%BB%AC%E5%88%86%E6%89%8B%E5%90%A7%E3%80%82%E2%80%9D%E8%AF%B4%E5%AE%8C%E8%BF%99%E5%8F%A5%E8%AF%9D%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%B0%B1%E7%A6%BB%E5%BC%80%E4%BA%86%E8%BF%99%E9%97%B4%E5%B0%8F%E5%87%BA%E7%A7%9F%E6%88%BF%E3%80%82%E6%88%BF%E5%86%85%E7%95%99%E4%B8%8B%E6%9D%A5%E7%9A%84%E6%9D%A8%E6%99%93%E6%99%93%EF%BC%8C%E6%B2%A1%E6%9C%89%E5%9B%A0%E4%B8%BA%E5%88%86%E6%89%8B%E8%80%8C%E4%BC%A4%E5%BF%83%E7%9A%84%E6%A0%B7%E5%AD%90%EF%BC%8C%E5%8F%8D%E5%80%92%E6%98%AF%E5%BE%88%E5%BC%80%E5%BF%83%E3%80%82%0D%0A%E5%98%B4%E8%A7%92%E5%8B%BE%E5%87%BA%E4%B8%80%E6%8A%B9%E7%AC%91%E6%84%8F%EF%BC%8C%E6%8B%BF%E8%B5%B7%E6%89%8B%E4%B8%AD%E7%9A%84%E6%89%8B%E6%9C%BA%EF%BC%8C%E6%8B%A8%E6%89%93%E8%BF%87%E5%8E%BB%E3%80%82%E2%80%9C%E5%96%82%EF%BC%8C%E8%BD%A9%E5%93%A5%E4%BB%8A%E5%A4%A9%E8%B0%A2%E8%B0%A2%E4%BD%A0%E5%92%AF%EF%BC%81%E2%80%9D%0D%0A%E2%80%9C%E4%B8%8D%E7%94%A8%E8%B0%A2%EF%BC%8C%E4%B8%80%E7%82%B9%E5%B0%8F%E5%BF%99%EF%BC%8C%E9%82%A3%E6%9D%A1%E5%92%B8%E9%B1%BC%E5%92%8C%E4%BD%A0%E5%88%86%E6%89%8B%E4%BA%86%EF%BC%9F%E2%80%9D%E7%94%B5%E8%AF%9D%E9%82%A3%E8%BE%B9%EF%BC%8C%E6%98%AF%E4%B8%80%E4%B8%AA%E7%94%B7%E4%BA%BA%E7%9A%84%E5%A3%B0%E9%9F%B3%E3%80%82%0D%0A%E2%80%9C%E6%98%AF%E5%95%8A%EF%BC%8C%E6%88%91%E7%BB%88%E4%BA%8E%E5%92%8C%E8%BF%99%E4%B8%AA%E5%92%B8%E9%B1%BC%E5%88%86%E6%89%8B%E4%BA%86%EF%BC%8C%E6%98%AF%E4%BB%96%E4%B8%BB%E5%8A%A8%E6%8F%90%E5%87%BA%E6%9D%A5%E7%9A%84%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E5%97%AF%EF%BC%8C%E8%BF%99%E6%A0%B7%E5%B0%B1%E5%A5%BD%EF%BC%8C%E5%BD%95%E9%9F%B3%E4%BA%86%E5%90%97%EF%BC%9F%E4%B8%8D%E7%84%B6%E4%BB%A5%E5%90%8E%E9%82%A3%E5%87%A0%E4%B8%AA%E4%BA%BA%E6%9D%A5%E6%89%BE%E9%BA%BB%E7%83%A6%E8%A6%81%E4%BD%A0%E4%BB%AC%E5%A4%8D%E5%90%88%EF%BC%8C%E4%BD%A0%E8%BF%98%E6%98%AF%E4%B8%8D%E5%A5%BD%E8%AF%B4%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E5%BD%95%E9%9F%B3%E5%BD%95%E4%BA%86%EF%BC%8C%E6%8A%8A%E5%89%8D%E9%9D%A2%E7%9A%84%E5%AF%B9%E8%AF%9D%E5%88%A0%E6%8E%89%EF%BC%8C%E5%B0%B1%E7%AE%97%E4%BB%A5%E5%90%8E%E4%BB%96%E4%BB%AC%E6%9D%A5%E6%89%BE%E9%BA%BB%E7%83%A6%EF%BC%8C%E6%88%91%E4%B9%9F%E4%B8%8D%E7%94%A8%E6%80%95%E4%BA%86%E3%80%82%E2%80%9D%0D%0A%E8%AF%B4%E5%AE%8C%EF%BC%8C%E6%9D%A8%E6%99%93%E6%99%93%E8%BA%BA%E5%88%B0%E4%BA%86%E6%B2%99%E5%8F%91%E4%B8%8A%E9%9D%A2%E3%80%82%0D%0A%E8%80%8C%E6%AD%A4%E6%97%B6%E7%9A%84%E5%AE%89%E8%BE%B0%EF%BC%8C%E4%B8%80%E4%B8%AA%E4%BA%BA%E5%A4%B1%E9%AD%82%E8%90%BD%E9%AD%84%E7%9A%84%E8%B5%B0%E5%9C%A8%E5%A4%A7%E8%A1%97%E4%B8%8A%E9%9D%A2%EF%BC%8C%E4%BB%96%E5%B0%B1%E8%AF%B4%E5%98%9B%EF%BC%8C%E9%82%A3%E4%B9%88%E5%A5%BD%E4%B8%80%E4%B8%AA%E5%A5%B3%E5%AD%A9%E6%80%8E%E4%B9%88%E5%8F%AF%E8%83%BD%E4%B8%BB%E5%8A%A8%E6%89%BE%E4%B8%8A%E8%87%AA%E5%B7%B1%EF%BC%8C%E8%BF%98%E8%AF%B4%E6%98%AF%E8%A2%AB%E9%80%BC%E6%97%A0%E5%A5%88%E3%80%82%E8%99%BD%E7%84%B6%E7%8E%B0%E5%9C%A8%E5%AE%89%E8%BE%B0%E8%BF%98%E4%B8%8D%E7%9F%A5%E9%81%93%E6%9D%A8%E6%99%93%E6%99%93%E6%89%BE%E8%87%AA%E5%B7%B1%E7%9A%84%E7%94%A8%E6%84%8F%EF%BC%8C%E5%8F%8D%E6%AD%A3%E6%B2%A1%E5%A5%BD%E4%BA%8B%E5%B0%B1%E5%AF%B9%E4%BA%86%E3%80%82%0D%0A%E4%B8%8D%E7%9F%A5%E4%B8%8D%E8%A7%89%EF%BC%8C%E7%AA%81%E7%84%B6%E4%B8%8B%E8%B5%B7%E4%BA%86%E9%9B%A8%E6%9D%A5%EF%BC%8C%E4%BC%B4%E9%9A%8F%E7%9D%80%E5%87%A0%E5%A3%B0%E8%BD%B0%E9%B8%A3%E5%A3%B0%EF%BC%8C%E9%9B%A8%E5%93%97%E5%95%A6%E5%95%A6%E7%9A%84%E8%90%BD%E4%B8%8B%E3%80%82%E5%AE%89%E8%BE%B0%E5%8A%A0%E5%BF%AB%E8%84%9A%E4%B8%8B%E7%9A%84%E7%9A%84%E5%8A%A8%E4%BD%9C%EF%BC%8C%E5%9C%A8%E9%9B%A8%E9%87%8C%E4%B9%B1%E8%B7%91%E3%80%82%E6%AD%A4%E5%88%BB%EF%BC%8C%E4%BB%96%E6%83%B3%E7%9A%84%E6%98%AF%EF%BC%8C%E8%87%AA%E5%B7%B1%E8%A6%81%E6%80%8E%E4%B9%88%E6%AD%BB%EF%BC%9F%0D%0A%E6%98%AF%E7%9A%84%EF%BC%8C%E4%BB%96%E5%B7%B2%E7%BB%8F%E4%B8%80%E5%BF%83%E5%AF%BB%E6%AD%BB%E4%BA%86%E3%80%82%0D%0A%E8%AF%9A%E5%AE%9E%EF%BC%8C%E5%9F%8E%E5%B8%82%E6%9C%89%E4%BB%80%E4%B9%88%E7%94%A8%EF%BC%8C%E7%8E%B0%E5%9C%A8%E4%BC%81%E4%B8%9A%E8%80%81%E6%9D%BF%E5%8F%88%E4%B8%8D%E4%BB%A5%E8%BF%99%E4%B8%AA%E6%94%B6%E4%BA%BA%E3%80%82%E6%88%91%E5%B0%B1%E4%B8%8D%E4%BC%9A%E6%8B%8D%E9%A9%AC%E5%B1%81%EF%BC%8C%E4%B8%8D%E4%BC%9A%E8%AF%B4%E5%A5%BD%E8%AF%9D%EF%BC%8C%E5%BC%84%E5%BE%97%E7%8E%B0%E5%9C%A8%E9%83%BD%E6%B2%A1%E6%9C%89%E5%B7%A5%E8%B5%84%E3%80%82%E5%B0%B1%E8%BF%9E%E9%82%A3%E4%B9%88%E5%A5%BD%E4%B8%80%E4%B8%AA%E5%A5%B3%E6%9C%8B%E5%8F%8B%EF%BC%8C%E4%B9%9F%E8%A2%AB%E5%88%AB%E4%BA%BA%E6%8A%A2%E8%B5%B0%E4%BA%86%E3%80%82%0D%0A%E9%9B%A8%E6%B0%B4%E6%B7%B7%E6%9D%82%E7%9D%80%E6%B3%AA%E6%B0%B4%E5%9C%A8%E8%84%B8%E4%B8%8A%E6%B5%81%E5%8A%A8%EF%BC%8C%E5%AE%89%E8%BE%B0%E4%B8%8D%E5%81%9C%E7%9A%84%E6%93%A6%E6%8B%AD%E3%80%82%E4%B8%8D%E7%9F%A5%E9%81%93%E6%98%AF%E4%B8%8D%E6%98%AF%E5%A4%AA%E7%B4%AF%E4%BA%86%EF%BC%8C%E5%BD%93%E8%B7%91%E5%88%B0%E4%B8%80%E9%A2%97%E6%A0%91%E4%B8%8B%E7%9A%84%E6%97%B6%E5%80%99%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%9D%90%E4%B8%8B%E6%9D%A5%E5%87%86%E5%A4%87%E4%BC%91%E6%81%AF%E4%B8%80%E4%B8%8B%E3%80%82%0D%0A%E5%9D%90%E5%9C%A8%E5%9C%B0%E4%B8%8A%EF%BC%8C%E5%88%9A%E5%88%9A%E5%91%BC%E5%90%B8%E5%BC%80%E5%A7%8B%E5%9D%87%E5%8C%80%EF%BC%8C%E5%8F%AF%E6%98%AF%E8%BF%99%E4%B8%AA%E6%97%B6%E5%80%99%EF%BC%8C%E4%B8%80%E9%81%93%E5%A4%A9%E9%9B%B7%E9%A1%BA%E7%9D%80%E6%A0%91%E6%9E%9D%EF%BC%8C%E6%89%93%E5%88%B0%E4%BA%86%E5%AE%89%E8%BE%B0%E7%9A%84%E5%A4%A9%E7%81%B5%E7%9B%96%E4%B8%8A%E9%9D%A2%E3%80%82%0D%0A","title":"%E7%AC%AC%E4%B8%80%E7%AB%A0+%E5%A4%B1%E9%AD%82%E8%90%BD%E9%AD%84","chapternum":1,"chapterid":11966943}}
        // 失败,不存在:{"code":-20302,"message":"There is no part!"}
        /*String addChapterUrl="http://topenapi.tadu.com:8098/api/getChapterDetail";
        int copyrightid=292;
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*/
        String addChapterUrl="http://openapi.tadu.com/api/getChapterDetail";
        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";

        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("cpid",cpid);//cp书籍id
        requestDataMap.put("chapterid",chapterid);//cp章节id
        String HttpResult = null;
        try {
            HttpResult = HttpClientUtilsGX.doPost(addChapterUrl, requestDataMap);
        } catch (Exception e) {
            System.out.println("塔读章节第一次查询网络错误,书cpid"+cpid+"章节id"+chapterid);
            try {
                HttpResult = HttpClientUtilsGX.doPost(addChapterUrl, requestDataMap);
                System.out.println("塔读章节第2次查询成功");
            } catch (Exception e1) {
                System.out.println("塔读章节第2次查询网络错误");
                try {
                    HttpResult = HttpClientUtilsGX.doPost(addChapterUrl, requestDataMap);
                    System.out.println("塔读章节第3次查询成功");
                } catch (Exception e2) {
                    System.out.println("章节查询失败,请检查网络");
                    e2.printStackTrace();
                }
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        return jsonObject;
    }

    //查询最新章节信息接口
    public static JSONObject getUpdateInfoUrl(int cpid) throws Exception {
        /*String getUpdateInfoUrl="http://topenapi.tadu.com:8098/api/getUpdateInfo";//查询最新章节(从这里可以获得塔读的bookid)
        int copyrightid=292;
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*/
        String getUpdateInfoUrl="http://openapi.tadu.com/api/getUpdateInfo";//查询最新章节(从这里可以获得塔读的bookid)
        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";
        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("cpid",cpid);
        String HttpResult;
        try {
            HttpResult= HttpClientUtilsGX.doPost(getUpdateInfoUrl, requestDataMap);
        }catch (Exception e){
            System.out.println("获取"+cpid+"的塔读最新信息第一次网络失败");
            try {
                System.out.println("第2次发起请求");
                HttpResult = HttpClientUtilsGX.doPost(getUpdateInfoUrl, requestDataMap);
            }catch (Exception e1){
                System.out.println("获取"+cpid+"的塔读最新信息第2次网络失败");
                HttpResult = HttpClientUtilsGX.doPost(getUpdateInfoUrl, requestDataMap);
                System.out.println("第三次请求成功");
            }
        }
        JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        return jsonObject;
    }

    public static void readTxt(String filePath) {
        try {
            File file = new File(filePath);
            if(file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    System.out.println(lineTxt);
                }
                br.close();
            } else {
                System.out.println("文件不存在!");
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }
    }

    public static String txt2String(File file){
                 String result = "";
                 try{
                        BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
                        String s = null;
                         while((s = br.readLine())!=null){//使用readLine方法，一次读一行
                                 result = result + "\n" +s;
                            }
                      br.close();
                 }catch(Exception e){
                       e.printStackTrace();
                }
               return result;
         }

    public static int getClassId(String category){
        switch (category){
            case "东方玄幻" :
                return 99;
            case "乡土小说" :case "出版影视" :case "近代民国" :
                return 93;
            case "仙侠" : case "仙侠修真" :case "仙侠情结" :case "仙侠武侠" :case "仙侠魔幻" :
                return 109;
            case "军事" : case "军事历史" :case "军事战争" :case "军事竞技"  :
                return 86;
            case "出版精品" :
                return 102;
            case "历史" : case "历史军事" :case "历史架空" :case "古代军事"  :
                return 84;
            case "古代言情" : case "古典浪漫" :case "古典言情" :case "古言"  :case "古言架空"  :
                return 129;
            case "奇幻" : case "奇幻小说" : case "奇幻玄幻":case "玄奇幻想":case "玄幻":case "玄幻仙侠":case "玄幻奇幻":case "玄幻小说":
                return 99;
            case "女强重生" : case "女生灵异" :
                return 288;
            case "女频古言" : case "妃嫔宫斗" :case "婚恋情感" :
                return 129;
            case "官场商战" :
                return 114;
            case "幻想异能" :
                return 271;
            case "幻想言情" :
                return 271;
            case "废材逆袭" :
                return 274;
            case "异术超能" : case "异能超能" :
                return 271;
            case "总裁虐恋" : case "总裁言情" : case "总裁豪门" :case "爱情婚姻" :case "现代言情" :case "现代都市" :case "现言" :case "豪门总裁" :
                return 104;
            case "恐怖" : case "恐怖惊悚" : case "恐怖灵异" :case "悬疑" :case "悬疑探险" :case "悬疑推理" :case "悬疑灵异" :case "灵异" :case "灵异奇谈" :case "灵异恐怖" :case "灵异悬疑" :case "灵异推理" :case "灵异文" :
                return 288;
            case "架空历史" :
                return 108;
            case "校园都市" : case "浪漫青春" :case "青春" :case "青春校园" :case "青春纯爱" :
                return 105;
            case "武侠仙侠" : case "武侠小说" : case "武侠玄幻" :case "武侠魔幻" :case "短篇小说-武侠仙侠" :
                return 10;
            case "游戏小说" :case "游戏竞技" :case "竞技小说" :
                return 112;
            case "热血都市" :case "男频都市" :case "都市" :case "都市婚恋" :case "都市小说" :case "都市异能" :case "都市暧昧" :case "都市游戏" :case "都市爽文" :case "都市生活" :case "都市言情" :
                return 103;
            case "男同" :case "耽美同人" :case "耽美纯爱" :
                return 291;
            case "科幻小说" :case "科幻悬疑" :case "科幻末世" :
                return 111;
            case "穿越" :case "穿越幻想" :case "穿越时空" :case "穿越架空" :case "穿越言情" :case "穿越重生" :
                return 135;
            case "育儿教育" :
                return 121;
            case "言情" :
                return 119;
            case "魔幻柔情" :
                return 113;
        }
        return 281;
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
}
