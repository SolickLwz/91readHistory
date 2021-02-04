package com.nine.one.yuedu.read;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Author:李王柱
 * 2020/8/28
 */
public class taDuTest {
    public static void main(String[] args) throws Exception {
        //System.out.println(addBookUrl(33553,"最强说谎系统","苏笙","诚实无比的安辰在这座大城市里面备受打击。他不懂得花言巧语，不会吹嘘拍马。张嘴闭嘴说出来的都是实话。意外获得说谎系统，教你说谎，教你混迹都市。完成任务，获得牛逼值，让别人更加相信你的鬼话。“拿错卡了，换一张，哎呀对不起还是错了，哦哦哦再换一下……”“恭喜宿主用同一个谎言连续n次欺骗同一个人，开启妹子收集模板任务。要后宫不要种马哦。”且看安辰如何靠说谎混迹都市。",81,"http://res.nxstory.com/cover/2019/1/14/33553.jpg",0,0));
        //System.out.println(getUpdateInfoUrl(34845));
        String content="他的指腹轻轻摩挲着她的唇角，眼底是抑制已久的意图。\n" +
                "夏婉儿小脸发红，心跳如雷，她哪里听不出乔明邺的言外之意。\n" +
                "但她仍没忘记自己的目的，纵使心有期待，也不能表露出来。\n" +
                "“回去也不行！”她小声表明了自己的态度。\n" +
                "乔明邺闻言，眼里闪过几分诧异，旋即贴近她的耳边，似乎是在认真的询问：“那还是在这？”\n" +
                "低沉的嗓音仿佛带了神奇的魔力，在她的脑海里掀起轩然大波。\n" +
                "“你……”\n" +
                "“一点都不热，你什么意思！你有没有脑子，怎么连这点事情都做不好？一整天跟哑巴了似的？一声不吭，还是说我咖位不够夏婉儿，还不配让你开金口？”胡宣玲那尖细的声音，不适时的透过门缝插了过来。\n" +
                "听到提及自己，夏婉儿的脸色沉了下来。\n" +
                "乔明邺也收起了调笑的心思，站起来并拉了她起身，贴心帮她整理了一下被自己压皱了的裙子。\n" +
                "胡宣玲的奚落声一字不落的传来：“不过是让你倒杯水过来，三番四次倒不好，你是故意的吧！”\n" +
                "“行了玲玲，小心吴导知道了，若是告诉了慈编，少不了一顿说。玲玲，算了算了，你别气了，都怪我不好，没事想喝什么水，今日不同往日，我自己去倒吧。”何娴柔那娇柔做作的声音，也比之前大了不少，看似在劝架，实则在往烈火上添柴。\n" +
                "说着，她又朝着冉沁道，“冉沁是吧，我使唤不起你，你去忙吧。”\n" +
                "果不其然，胡宣玲声音里的怒气更胜，指着冉沁尖声冷笑：“哦，我倒是忘了。三天两头往吴导的房间跑，是生怕别人不知道你爬了吴导的床是吧？你还敢瞪我，果然是什么主子带什么奴才，现在仔细一看，你长得倒是真有几分夏婉儿那狐狸精样，怎么，你是得了她的真传了？”\n" +
                "“你们别太过份！”冉沁气得发抖，终于是忍不住，咬牙道。\n" +
                "“过份？我就过份了，怎么样？”胡宣玲冷笑，手里头拎着空荡荡的水杯，当着她的面，直接松开，扔在了地上，发起了清脆的破碎声，语气阴毒不已，“你，用手，一块一块给我捡起来，再粘起来，倒杯45度的热水过来。否则，就一块一块给我吞进去。”\n" +
                "何娴柔唇角泛起阴冷的笑意，又迅速敛起，继续扮演老好人角色，为难的看了一眼冉沁，上前拉住了胡宣玲：“玲玲。”\n" +
                "胡宣玲当即冷笑：“怕什么？监控我早就让人关了，现在门外守着的也是我们的人，我今天这口气是非出不可！我现在倒是十分怀疑，是不是她给吴洋吹得枕边风，让他将我们的戏份删掉了一大——啊！谁！”\n" +
                "说得正起劲且未能察觉隔间的门被打开的胡宣玲，被一壶温开水泼了个正着，连站在她身旁的何娴柔也无法避免。\n" +
                "两人吓得上蹿下跳，好一会才反应了过来只是温开水，自己也没有毁容，这才心有余悸的瞪着拿着保温瓶的夏婉儿：“姓夏的，你要找死啊！”";
      //System.out.println(getChapterListUrl(31972));//查询章节目录  34865

     getChapterDetailUrl(31972 ,11898591);//查询章节详情,参数1:书籍id,参数2:章节id


        /*String taduContentString=getChapterDetailUrl(32392,12129049).getJSONObject("result").getString("content");
        //System.out.println(content.length());
        System.out.println(URLDecoder.decode(taduContentString).length());*/
        //成功:{"code":0,"message":"success!","result":{"content":"%E2%80%9C%E5%94%89%EF%BC%8C%E7%AD%89%E7%AD%89%EF%BC%8C%E7%AD%89%E7%AD%89%EF%BC%8C%E4%BB%8A%E5%A4%A9%E5%85%AC%E4%BA%A4%E6%98%AF%E4%B8%8D%E6%98%AF%E5%8F%88%E6%97%A9%E5%BC%80%E4%BA%86%E3%80%82%E2%80%9D%0D%0A%E5%AE%89%E8%BE%B0%E5%9C%A8%E5%90%8E%E9%9D%A2%E4%B8%8D%E5%81%9C%E7%9A%84%E8%BF%BD%E8%B5%B6%E7%9D%80%E5%89%8D%E9%9D%A2%E5%BF%AB%E8%A6%81%E5%85%B3%E9%97%A8%E7%9A%84%E5%85%AC%E4%BA%A4%E8%BD%A6%EF%BC%8C%E7%BB%88%E4%BA%8E%EF%BC%8C%E5%9C%A8%E6%9C%80%E5%90%8E%E4%BA%94%E7%A7%92%E9%92%9F%E7%9A%84%E6%97%B6%E5%80%99%E8%B5%B6%E4%B8%8A%E4%BA%86%E8%BF%99%E7%8F%AD%E6%9C%AB%E7%8F%AD%E8%BD%A6%E3%80%82%0D%0A%E2%80%9C%E5%B0%8F%E4%BC%99%E5%AD%90%EF%BC%8C%E5%8F%88%E6%98%AF%E4%BD%A0%E5%95%8A%EF%BC%8C%E6%88%91%E5%A4%A9%E5%A4%A9%E4%B8%BA%E4%BA%86%E7%AD%89%E4%BD%A0%E5%8F%AF%E6%98%AF%E9%83%BD%E7%89%B9%E6%84%8F%E6%99%9A%E4%B8%80%E5%88%86%E9%92%9F%E5%8F%91%E8%BD%A6%E7%9A%84%E5%99%A2%EF%BC%81%E2%80%9D%0D%0A%E2%80%9C%E5%97%AF%EF%BC%8C%E8%B0%A2%E8%B0%A2%E8%80%81%E5%A4%A7%E7%88%B7%E3%80%82%E2%80%9D%E5%AE%89%E8%BE%B0%E9%81%93%E8%B0%A2%E4%B9%8B%E5%90%8E%EF%BC%8C%E4%B9%92%E9%93%83%E4%B9%93%E5%95%B7%E7%9A%84%E4%B8%A2%E4%BA%86%E4%B8%80%E4%B8%AA%E7%A1%AC%E5%B8%81%E5%88%B0%E9%92%B1%E7%AE%B1%E9%87%8C%E9%9D%A2%E3%80%82%0D%0A%E2%80%9C%E5%B0%8F%E4%BC%99%E5%AD%90%EF%BC%8C%E5%81%9A%E4%BB%80%E4%B9%88%E5%B7%A5%E4%BD%9C%E7%9A%84%E5%91%A2%EF%BC%9F%E2%80%9D%E5%85%AC%E4%BA%A4%E8%BD%A6%E5%8F%B8%E6%9C%BA%E8%AF%A2%E9%97%AE%E5%88%B0%EF%BC%8C%E4%B8%80%E5%8F%AA%E6%89%8B%E8%BF%98%E4%B8%8D%E5%81%9C%E7%9A%84%E5%9C%A8%E5%81%9A%E7%9D%80%E6%95%B0%E9%92%B1%E7%9A%84%E5%A7%BF%E5%8A%BF%E3%80%82%0D%0A%E2%80%9C%E5%97%A8%EF%BC%8C%E5%88%AB%E6%8F%90%E4%BA%86%EF%BC%8C%E5%BA%94%E8%81%98%E4%BA%86%E5%87%A0%E6%AC%A1%E5%B7%A5%E4%BD%9C%EF%BC%8C%E9%83%BD%E8%A2%AB%E7%82%92%E4%BA%86%E3%80%82%E4%BB%8A%E5%A4%A9%E8%BF%99%E4%B8%80%E6%AC%A1%EF%BC%8C%E5%8F%88%E8%A2%AB%E7%82%92%E4%BA%86%E3%80%82%E2%80%9D%E5%AE%89%E8%BE%B0%E6%97%A0%E5%A5%88%E7%9A%84%E6%91%87%E4%BA%86%E6%91%87%E5%A4%B4%EF%BC%8C%E4%BB%96%E7%9A%84%E7%94%9F%E6%B4%BB%EF%BC%8C%E5%A4%AA%E6%83%A8%E6%B7%A1%E4%BA%86%E3%80%82%0D%0A%E2%80%9C%E8%BF%99%E6%A0%B7%E5%95%8A%EF%BC%8C%E5%B0%8F%E4%BC%99%E5%AD%90%EF%BC%8C%E4%BD%A0%E6%90%AC%E4%B8%8A%E4%B8%8A%E9%9D%A2%E7%9A%84%E6%89%B6%E6%89%8B%EF%BC%8C%E5%B0%8F%E5%BF%83%E5%88%AB%E6%91%94%E8%B7%A4%E5%92%AF%E3%80%82%E2%80%9D%E8%80%81%E5%A4%A7%E7%88%B7%E7%BC%A9%E4%BC%9A%E9%82%A3%E5%8F%AA%E5%81%9A%E7%9D%80%E7%AE%97%E9%92%B1%E7%9A%84%E5%A7%BF%E5%8A%BF%E7%9A%84%E6%89%8B%EF%BC%8C%E4%B9%9F%E4%B8%8D%E5%86%8D%E7%BB%A7%E7%BB%AD%E5%92%8C%E5%AE%89%E8%BE%B0%E9%97%B2%E8%B0%88%EF%BC%8C%E5%AE%89%E7%A8%B3%E7%9A%84%E5%BC%80%E8%B5%B7%E8%BD%A6%E6%9D%A5%E4%BA%86%E3%80%82%0D%0A%E5%AE%89%E8%BE%B0%E7%9C%BC%E7%A5%9E%E7%A9%BA%E6%B4%9E%E7%9A%84%E6%9C%9B%E7%9D%80%E7%AA%97%E5%A4%96%EF%BC%8C%E5%8F%B9%E6%81%AF%E7%9A%84%E5%A3%B0%E9%9F%B3%E4%BB%8E%E5%98%B4%E5%B7%B4%E9%87%8C%E9%9D%A2%E4%BC%A0%E5%87%BA%E6%9D%A5%E3%80%82%E8%87%AA%E5%B7%B1%E7%9A%84%E6%9C%AA%E6%9D%A5%EF%BC%8C%E5%9C%A8%E5%93%AA%E9%87%8C%EF%BC%9F%0D%0A%E8%87%AA%E5%B7%B1%E6%98%AF%E4%B8%8D%E6%98%AF%E5%9C%A8%E8%BF%99%E5%BA%A7%E5%A4%A7%E5%9F%8E%E5%B8%82%EF%BC%8C%E6%B7%B7%E4%B8%8D%E4%B8%8B%E5%8E%BB%E4%BA%86%E3%80%82%0D%0A%E8%BF%99%E8%AE%A9%E4%BB%96%E6%83%B3%E8%B5%B7%E4%BA%86%E4%B8%80%E5%8F%A5%E8%AF%9D%EF%BC%8C%E5%9F%8E%E5%B8%82%E5%A5%97%E8%B7%AF%E6%B7%B1%EF%BC%8C%E6%88%91%E8%A6%81%E5%9B%9E%E5%86%9C%E6%9D%91%E3%80%82%0D%0A%E5%AE%89%E8%BE%B0%E6%9C%AC%E5%B0%B1%E6%98%AF%E4%B9%A1%E4%B8%8B%E5%AD%A9%E5%AD%90%EF%BC%8C%E4%B8%BA%E4%BA%BA%E4%B9%9F%E5%BE%88%E8%80%81%E5%AE%9E%EF%BC%8C%E5%8F%AF%E6%98%AF%E4%B8%8A%E5%A4%A9%E5%8D%B4%E5%A6%82%E5%90%8C%E6%8A%8A%E4%BB%96%E9%81%97%E5%BC%83%E4%BA%86%E4%B8%80%E8%88%AC%E3%80%82%E4%B8%8D%E7%AE%A1%E4%BB%96%E5%81%9A%E4%BB%80%E4%B9%88%E5%B7%A5%E4%BD%9C%EF%BC%8C%E8%80%81%E6%9D%BF%E9%83%BD%E5%9B%A0%E4%B8%BA%E4%BB%96%E5%A4%AA%E8%AF%9A%E5%AE%9E%E8%80%8C%E7%82%92%E6%8E%89%E4%BB%96%E3%80%82%0D%0A%E6%83%B3%E8%B5%B7%E8%87%AA%E5%B7%B1%E5%89%8D%E5%87%A0%E6%AC%A1%E6%89%BE%E5%B7%A5%E4%BD%9C%E7%9A%84%E7%BB%8F%E5%8E%86%EF%BC%8C%E5%AE%89%E8%BE%B0%E7%8E%B0%E5%9C%A8%E9%83%BD%E8%A7%89%E5%BE%97%E4%B8%8D%E7%9F%A5%E9%81%93%E5%82%BB%E7%9A%84%E5%8F%AF%E4%BB%A5%E3%80%82%E6%98%AF%E7%9A%84%EF%BC%8C%E9%82%A3%E5%B7%B2%E7%BB%8F%E4%B8%8D%E8%83%BD%E5%8F%AB%E5%81%9A%E8%AF%9A%E5%AE%9E%E5%AE%88%E4%BF%A1%E4%BA%86%EF%BC%8C%E9%82%A3%E5%B0%B1%E6%98%AF%E5%82%BB%EF%BC%81%0D%0A%E7%AC%AC%E4%B8%80%E6%AC%A1%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%8E%BB%E5%A4%A7%E5%95%86%E5%9C%BA%E5%BA%94%E8%81%98%E3%80%82%E6%9C%AC%E6%9D%A5%E8%80%81%E6%9D%BF%E5%BE%88%E7%9C%8B%E5%A5%BD%EF%BC%8C%E7%BB%93%E6%9E%9C%E4%BB%96%E5%92%8C%E8%80%81%E6%9D%BF%E5%81%9A%E7%94%B5%E6%A2%AF%E7%9A%84%E6%97%B6%E5%80%99%EF%BC%8C%E4%B8%8D%E5%B7%A7%E7%9A%84%E6%98%AF%E8%80%81%E6%9D%BF%E5%9C%A8%E7%94%B5%E6%A2%AF%E9%87%8C%E9%9D%A2%E6%89%93%E4%BA%86%E4%B8%AA%E5%B1%81%EF%BC%8C%E5%BD%93%E6%97%B6%E8%80%81%E6%9D%BF%E5%8F%88%E7%AB%99%E5%9C%A8%E4%BB%96%E8%BA%AB%E8%BE%B9%E3%80%82%0D%0A%E5%99%97%E7%9A%84%E4%B8%80%E5%A3%B0%E8%AE%A9%E4%BB%96%E8%A7%89%E5%BE%97%E5%A4%A7%E8%85%BF%E4%B8%8A%E9%9D%A2%E6%9C%89%E4%B8%80%E9%98%B5%E7%83%AD%E9%A3%8E%E5%90%B9%E6%9D%A5%EF%BC%8C%E4%B8%80%E4%B8%8B%E6%B2%A1%E5%BF%8D%E4%BD%8F%E7%AC%91%E4%BA%86%E5%87%BA%E6%9D%A5%E3%80%82%E5%BD%93%E6%97%B6%E5%9C%A8%E7%94%B5%E6%A2%AF%E4%B8%8A%E9%9D%A2%E5%B0%B1%E7%9C%9F%E7%9A%84%E4%B8%8D%E5%BF%8C%E8%AE%B3%E7%9A%84%E8%AF%B4%E5%87%BA%E8%80%81%E6%9D%BF%E6%89%93%E4%BA%86%E5%B1%81%EF%BC%8C%E8%BF%98%E4%B8%80%E7%9B%B4%E5%93%88%E5%93%88%E5%A4%A7%E7%AC%91%E3%80%82%0D%0A%E5%85%B6%E4%BB%96%E5%90%8C%E4%BA%8B%E6%B2%A1%E6%9C%89%E6%8D%82%E4%BD%8F%E5%98%B4%E5%B7%B4%EF%BC%8C%E6%83%B3%E7%AC%91%E5%8F%88%E4%B8%8D%E6%95%A2%E7%AC%91%EF%BC%8C%E4%BB%96%E4%B9%9F%E4%B8%8D%E4%BC%9A%E7%9C%8B%E7%9C%BC%E8%89%B2%EF%BC%8C%E5%BC%84%E5%BE%97%E8%80%81%E6%9D%BF%E5%BE%88%E6%98%AF%E4%B8%A2%E8%84%B8%EF%BC%8C%E4%BA%8E%E6%98%AF%E8%80%81%E6%9D%BF%E4%B8%80%E6%B0%94%E4%B9%8B%E4%B8%8B%EF%BC%8C%E5%B0%B1%E8%AE%A9%E4%BB%96%E6%BB%9A%E4%BA%86%E3%80%82%0D%0A%E7%AC%AC%E4%BA%8C%E6%AC%A1%EF%BC%8C%E4%BB%96%E5%9C%A8%E5%81%9A%E5%8C%BB%E7%94%9F%EF%BC%8C%E6%8A%8A%E4%B8%80%E4%B8%AA%E4%B9%85%E6%B2%BB%E4%B8%8D%E6%84%88%E7%9A%84%E7%97%85%E4%BA%BA%E6%B2%BB%E5%A5%BD%E4%BA%86%EF%BC%8C%E8%BF%98%E5%BE%97%E6%84%8F%E6%B4%8B%E6%B4%8B%E4%B8%80%E7%9B%B4%E5%9C%A8%E7%82%AB%E8%80%80%E3%80%82%0D%0A%E6%99%9A%E4%B8%8A%E8%A2%AB%E7%A7%81%E4%BA%BA%E8%AF%8A%E6%89%80%E7%9A%84%E8%80%81%E6%9D%BF%E5%8F%AB%E8%BF%87%E5%8E%BB%E8%B0%88%E8%AF%9D%EF%BC%8C%E8%BF%98%E4%BB%A5%E4%B8%BA%E8%80%81%E6%9D%BF%E6%98%AF%E5%9B%A0%E4%B8%BA%E4%BB%96%E5%8C%BB%E6%9C%AF%E9%AB%98%E6%98%8E%EF%BC%8C%E8%A6%81%E7%BB%99%E4%BB%96%E5%8D%87%E8%81%8C%E5%8A%A0%E8%96%AA%E3%80%82%0D%0A%E6%B2%A1%E6%83%B3%E5%88%B0%E8%80%81%E6%9D%BF%E5%91%8A%E8%AF%89%E4%BB%96%EF%BC%8C%E9%82%A3%E4%B8%AA%E7%97%85%E4%BA%BA%E5%B0%B1%E6%98%AF%E5%90%83%E4%B8%8D%E5%AE%8C%E7%9A%84%E8%A5%BF%E7%93%9C%E7%9A%AE%EF%BC%8C%E4%BB%96%E6%98%AF%E6%95%85%E6%84%8F%E4%B8%8D%E6%B2%BB%E5%A5%BD%E7%9A%84%EF%BC%8C%E7%8E%B0%E5%9C%A8%E5%AE%89%E8%BE%B0%E6%B2%BB%E5%A5%BD%E4%BA%86%EF%BC%8C%E5%B0%B1%E5%B0%91%E4%BA%86%E4%B8%80%E4%B8%AA%E5%AE%9A%E6%9C%9F%E9%80%81%E9%92%B1%E7%9A%84%E4%BA%86%E3%80%82%0D%0A%E4%BA%8E%E6%98%AF%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%B0%B1%E8%A2%AB%E2%80%A6%E2%80%A6%0D%0A%E2%80%9C%E5%94%89%EF%BC%81%E2%80%9D%E5%AE%89%E8%BE%B0%E5%8F%88%E5%8F%B9%E4%BA%86%E4%B8%80%E5%8F%A3%E6%B0%94%EF%BC%8C%E6%8A%8A%E8%A7%86%E7%BA%BF%E7%A7%BB%E5%90%91%E4%BA%86%E7%AA%97%E6%88%B7%E7%9A%84%E8%BF%9C%E5%A4%84%E3%80%82%E2%80%9C%E5%92%A6%EF%BC%81%E2%80%9D%0D%0A%E7%9C%8B%E7%9D%80%E8%BF%9C%E5%A4%84%E7%9A%84%E4%BA%BA%E5%BD%B1%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%A5%BD%E5%83%8F%E8%A7%89%E5%BE%97%E6%9C%89%E4%BA%9B%E7%86%9F%E6%82%89%EF%BC%8C%E4%BB%94%E7%BB%86%E4%B8%80%E7%9C%8B%EF%BC%8C%E6%89%8D%E5%8F%91%E7%8E%B0%E6%98%AF%E8%87%AA%E5%B7%B1%E7%9A%84%E5%A5%B3%E6%9C%8B%E5%8F%8B%E3%80%82%0D%0A%E5%AE%89%E8%BE%B0%E7%9A%84%E5%A5%B3%E6%9C%8B%E5%8F%8B%E5%8F%AB%E6%9D%A8%E6%99%93%E6%99%93%EF%BC%8C%E4%B8%80%E4%B8%AA%E7%89%B9%E5%88%AB%E6%BC%82%E4%BA%AE%E7%9A%84%E5%A5%B3%E5%AD%A9%E5%AD%90%E3%80%82%E8%BA%AB%E6%9D%90%E4%B9%9F%E5%A5%BD%EF%BC%8C%E8%84%BE%E6%B0%94%E4%B9%9F%E5%A5%BD%E3%80%82%E5%9C%A8%E5%AE%B6%E9%87%8C%EF%BC%8C%E4%B8%80%E5%89%AF%E8%B4%A4%E5%A6%BB%E8%89%AF%E6%AF%8D%E7%9A%84%E8%A1%A8%E6%83%85%E3%80%82%0D%0A%E8%80%8C%E4%B8%94%E5%AF%B9%E5%AE%89%E8%BE%B0%E7%9A%84%E8%A6%81%E6%B1%82%E4%B9%9F%E4%B8%8D%E9%AB%98%EF%BC%8C%E7%8E%B0%E5%9C%A8%E5%AE%89%E8%BE%B0%E6%AF%8F%E5%A4%A9%E5%8E%BB%E5%81%9A%E4%B8%B4%E6%97%B6%E5%B7%A5%E4%B9%8B%E7%B1%BB%E7%9A%84%E4%BA%8B%E6%83%85%EF%BC%8C%E4%B8%80%E4%B8%AA%E6%9C%88%E5%B7%AE%E4%B8%8D%E5%A4%9A%E5%8F%AA%E8%83%BD%E6%B7%B7%E5%88%B0%E4%B8%A4%E5%8D%83%E5%9D%97%E9%92%B1%EF%BC%8C%E5%A5%B9%E4%B9%9F%E6%AF%AB%E6%97%A0%E6%80%A8%E8%A8%80%E3%80%82%0D%0A%E5%A5%B9%E8%AF%B4%E8%BF%87%EF%BC%8C%E4%BC%9A%E5%92%8C%E5%AE%89%E8%BE%B0%E4%B8%80%E8%B5%B7%E5%8A%AA%E5%8A%9B%EF%BC%8C%E6%9C%89%E4%B8%80%E4%B8%AA%E8%87%AA%E5%B7%B1%E7%9A%84%E5%B0%8F%E7%AA%9D%E3%80%82%E5%B0%B1%E7%AE%97%E5%B0%8F%E7%AA%9D%E5%86%8D%E5%B0%8F%EF%BC%8C%E4%B9%9F%E6%AF%94%E8%BF%99%E4%B8%AA%E6%9C%88%E7%A7%9F%E9%87%91%E4%B8%89%E7%99%BE%E7%9A%84%E5%9C%B0%E6%96%B9%E8%A6%81%E5%A5%BD%E3%80%82%0D%0A%E5%94%AF%E4%B8%80%E8%AE%A9%E5%AE%89%E8%BE%B0%E6%84%9F%E5%88%B0%E6%AC%A3%E6%85%B0%E7%9A%84%EF%BC%8C%E5%B0%B1%E6%98%AF%E5%A5%B9%E8%BF%99%E4%B8%AA%E5%A5%B3%E6%9C%8B%E5%8F%8B%E4%BA%86%E3%80%82%E7%8E%B0%E5%9C%A8%EF%BC%8C%E4%BB%96%E4%BB%AC%E4%B9%9F%E6%89%93%E7%AE%97%E5%A5%BD%E4%BA%86%EF%BC%8C%E5%86%8D%E8%BF%87%E4%B8%A4%E4%B8%AA%E6%9C%88%E5%88%B0%E4%BA%86%E5%9B%BD%E5%BA%86%E8%8A%82%EF%BC%8C%E4%BB%96%E4%BB%AC%E5%B0%B1%E7%BB%93%E5%A9%9A%E3%80%82%E6%9D%A8%E6%99%93%E6%99%93%E4%B9%9F%E7%AD%94%E5%BA%94%E4%BB%96%EF%BC%8C%E7%AD%89%E7%BB%93%E5%A9%9A%E7%9A%84%E9%82%A3%E5%A4%A9%E5%B0%B1%E4%BC%9A%E6%8A%8A%E8%87%AA%E5%B7%B1%E4%BA%A4%E7%BB%99%E4%BB%96%E3%80%82%0D%0A%E7%AA%81%E7%84%B6%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%8F%91%E7%8E%B0%E5%A5%B3%E6%9C%8B%E5%8F%8B%E7%9A%84%E5%8A%A8%E4%BD%9C%E6%9C%89%E4%BA%9B%E5%88%AB%E6%89%AD%EF%BC%8C%E5%88%B0%E5%BA%95%E6%98%AF%E6%80%8E%E4%B9%88%E5%88%AB%E6%89%AD%EF%BC%8C%E8%BF%99%E4%B8%AA%E8%A7%92%E5%BA%A6%E4%B9%9F%E7%9C%8B%E4%B8%8D%E5%A4%AA%E6%B8%85%E6%A5%9A%E3%80%82%0D%0A%E4%BD%86%E6%98%AF%EF%BC%8C%E7%AD%89%E5%85%AC%E4%BA%A4%E8%BD%A6%E5%BC%80%E5%88%B0%E5%90%88%E9%80%82%E8%A7%92%E5%BA%A6%E7%9A%84%E6%97%B6%E5%80%99%EF%BC%8C%E4%BB%96%E7%9C%8B%E6%B8%85%E6%A5%9A%E4%BA%86%E3%80%82%E8%80%8C%E7%9C%8B%E6%B8%85%E6%A5%9A%E4%B9%8B%E5%90%8E%EF%BC%8C%E6%95%B4%E4%B8%AA%E4%BA%BA%E9%83%BD%E6%87%B5%E4%BA%86%E3%80%82%0D%0A%E6%9D%A8%E6%99%93%E6%99%93%EF%BC%8C%E6%AD%A3%E8%A2%AB%E4%B8%80%E4%B8%AA%E5%AF%8C%E4%BA%8C%E4%BB%A3%E6%A0%B7%E7%9A%84%E7%9A%84%E6%90%82%E7%9D%80%E6%9F%B3%E6%9D%A1%E7%BB%86%E8%85%B0%EF%BC%8C%E8%84%B8%E4%B8%8A%E4%B9%9F%E6%98%AF%E6%9C%89%E8%AF%B4%E6%9C%89%E7%AC%91%E7%9A%84%E3%80%82%E8%A6%81%E7%9F%A5%E9%81%93%EF%BC%8C%E6%9D%A8%E6%99%93%E6%99%93%E8%AF%B4%E7%BB%93%E5%A9%9A%E4%B9%8B%E5%89%8D%E5%8F%AA%E8%AE%A9%E5%AE%89%E8%BE%B0%E7%89%B5%E6%89%8B%EF%BC%8C%E8%BF%99%E8%85%B0%EF%BC%8C%E5%AE%89%E8%BE%B0%E7%A2%B0%E9%83%BD%E6%B2%A1%E7%A2%B0%E8%BF%87%E3%80%82%0D%0A%E6%8B%B3%E5%A4%B4%EF%BC%8C%E6%8F%A1%E7%B4%A7%E4%BA%86%EF%BC%8C%E7%89%99%E9%BD%BF%EF%BC%8C%E5%92%AC%E5%90%88%E4%BA%86%E3%80%82%E4%BD%86%E6%98%AF%E4%B8%8B%E4%B8%80%E7%A7%92%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%8F%88%E6%94%BE%E6%9D%BE%E4%B8%8B%E6%9D%A5%E4%BA%86%E3%80%82%E4%BB%96%E5%BF%83%E9%87%8C%E5%AE%89%E6%85%B0%E8%87%AA%E5%B7%B1%EF%BC%8C%E8%AF%B4%E4%B8%8D%E5%AE%9A%EF%BC%8C%E9%82%A3%E5%8F%AA%E6%98%AF%E5%A5%B9%E7%9A%84%E5%93%A5%E5%93%A5%E5%91%A2%EF%BC%9F%0D%0A%E5%8F%AA%E4%B8%8D%E8%BF%87%E8%BF%99%E4%BB%85%E4%BB%85%E5%8F%AA%E6%98%AF%E5%AE%89%E6%85%B0%E7%BD%A2%E4%BA%86%EF%BC%8C%E6%9C%89%E5%93%AA%E4%B8%AA%E5%93%A5%E5%93%A5%E4%BC%9A%E5%9C%A8%E5%A4%A7%E5%BA%AD%E5%B9%BF%E4%BC%97%E4%B9%8B%E4%B8%8B%E6%90%82%E8%87%AA%E5%B7%B1%E5%A6%B9%E5%A6%B9%E7%9A%84%E8%85%B0%E3%80%82%E8%BF%99%E9%83%BD%E7%AE%97%E4%B8%8D%E4%B8%8A%E5%AE%89%E6%85%B0%EF%BC%8C%E5%8F%AA%E8%83%BD%E7%AE%97%E8%87%AA%E6%AC%BA%E6%AC%BA%E4%BA%BA%E4%BA%86%E3%80%82%0D%0A%E2%80%A6%E2%80%A6%0D%0A%E8%BF%87%E4%BA%86%E4%B8%8D%E4%B9%85%EF%BC%8C%E5%85%AC%E4%BA%A4%E8%BD%A6%E5%90%AC%E4%BA%86%EF%BC%8C%E5%88%9A%E5%A5%BD%E5%88%B0%E7%AB%99%E3%80%82%E5%AE%89%E8%BE%B0%E5%92%8C%E5%85%AC%E4%BA%A4%E8%BD%A6%E5%8F%B8%E6%9C%BA%E9%81%93%E5%88%AB%E4%B9%8B%E5%90%8E%EF%BC%8C%E5%B0%B1%E4%B8%8B%E8%BD%A6%E4%BA%86%E3%80%82%0D%0A%E5%A4%B1%E9%AD%82%E8%90%BD%E9%AD%84%E7%9A%84%E8%B8%A9%E7%9D%80%E5%8F%B0%E9%98%B6%EF%BC%8C%E5%A4%B1%E9%AD%82%E8%90%BD%E9%AD%84%E7%9A%84%E6%89%93%E5%BC%80%E6%88%BF%E9%97%A8%EF%BC%8C%E9%87%8C%E9%9D%A2%E5%9D%90%E7%9D%80%E4%B8%80%E4%B8%AA%E6%B8%85%E7%BA%AF%E5%8F%AF%E7%88%B1%E7%9A%84%E5%A5%B3%E5%AD%A9%E5%AD%90%E3%80%82%E8%BF%99%E4%B8%AA%E5%A5%B3%E5%AD%A9%E5%AD%90%EF%BC%8C%E6%AD%A3%E6%98%AF%E6%9D%A8%E6%99%93%E6%99%93%E3%80%82%0D%0A%E2%80%9C%E6%99%93%E6%99%93%E5%95%8A%EF%BC%8C%E4%BB%8A%E5%A4%A9%E5%9C%A8%E5%93%AA%E9%87%8C%E5%81%9A%E4%BA%8B%E5%95%8A%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E6%AD%A5%E8%A1%8C%E8%A1%97%E5%95%8A%EF%BC%8C%E6%88%91%E5%9C%A8%E9%82%A3%E9%87%8C%E6%91%86%E5%9C%B0%E6%91%8A%E5%90%A7%EF%BC%81%E2%80%9D%0D%0A%E5%AE%89%E8%BE%B0%E7%9A%84%E5%98%B4%E8%A7%92%E9%9C%B2%E5%87%BA%E4%B8%80%E6%8A%B9%E8%8B%A6%E7%AC%91%EF%BC%8C%E8%BF%99%E5%9C%B0%E7%82%B9%EF%BC%8C%E5%80%92%E6%98%AF%E6%B2%A1%E6%9C%89%E8%B0%8E%E6%8A%A5%E3%80%82%0D%0A%E2%80%9C%E8%BF%99%E6%A0%B7%E5%95%8A%EF%BC%8C%E9%99%A4%E4%BA%86%E4%BD%A0%E8%BF%98%E6%9C%89%E8%B0%81%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E6%B2%A1%E4%BA%BA%E4%BA%86%E5%91%80%EF%BC%8C%E6%88%91%E4%B8%80%E4%B8%AA%E4%BA%BA%E5%9C%A8%E5%81%9A%E4%BA%8B%E5%91%80%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E7%9C%9F%E7%9A%84%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E6%80%8E%E4%B9%88%E5%91%80%EF%BC%81%E4%BD%A0%E7%9B%B8%E4%BF%A1%E6%88%91%E4%B9%88%EF%BC%9F%E2%80%9D%E6%9D%A8%E6%99%93%E6%99%93%E7%9A%84%E8%AF%AD%E6%B0%94%E5%BC%B1%E5%BC%B1%E7%9A%84%EF%BC%8C%E4%B8%80%E5%89%AF%E6%A5%9A%E6%A5%9A%E5%8F%AF%E6%80%9C%E7%9A%84%E6%A0%B7%E5%AD%90%E7%9C%8B%E7%9D%80%E5%AE%89%E8%BE%B0%E3%80%82%0D%0A%E5%A6%82%E6%9E%9C%E6%98%AF%E4%BB%A5%E5%89%8D%EF%BC%8C%E5%AE%89%E8%BE%B0%E4%B8%80%E5%AE%9A%E8%A2%AB%E6%9F%94%E5%8C%96%E4%BA%86%EF%BC%8C%E7%8E%B0%E5%9C%A8%EF%BC%8C%E4%BB%96%E5%8F%AA%E8%A7%89%E5%BE%97%E6%81%B6%E5%BF%83%E3%80%82%0D%0A%E7%BB%88%E4%BA%8E%EF%BC%8C%E5%AE%89%E8%BE%B0%E6%B2%A1%E6%9C%89%E7%BB%99%E5%87%BA%E8%82%AF%E5%AE%9A%E7%9A%84%E5%9B%9E%E7%AD%94%EF%BC%8C%E5%8F%AA%E6%98%AF%E5%90%90%E5%87%BA%E4%BA%86%E7%AE%80%E5%8D%95%E7%9A%84%E5%87%A0%E4%B8%AA%E5%AD%97%E3%80%82%E2%80%9C%E6%88%91%E7%9C%8B%E8%A7%81%E4%BA%86%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E4%BD%A0%E8%B7%9F%E8%B8%AA%E6%88%91%EF%BC%9F%E2%80%9D%E6%9D%A8%E6%99%93%E6%99%93%E4%B8%8B%E6%84%8F%E8%AF%86%E7%9A%84%E7%AB%99%E4%B8%8B%E6%9D%A5%EF%BC%8C%E7%94%A8%E9%82%A3%E5%8F%8C%E6%9D%8F%E7%9C%BC%E6%80%92%E8%A7%86%E7%9D%80%E5%AE%89%E8%BE%B0%E3%80%82%0D%0A%E2%80%9C%E6%B2%A1%E6%9C%89%EF%BC%8C%E6%88%91%E5%9D%90%E8%BD%A6%E7%BB%8F%E8%BF%87%E9%82%A3%E9%87%8C%E3%80%82%E9%82%A3%E6%98%AF%E4%BD%A0%E8%B0%81%EF%BC%8C%E4%BD%A0%E6%96%B0%E7%9A%84%E7%94%B7%E6%9C%8B%E5%8F%8B%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E5%AE%89%E8%BE%B0%EF%BC%8C%E9%9A%BE%E9%81%93%E6%88%91%E4%B8%8D%E9%9C%80%E8%A6%81%E5%88%B0%E5%A4%96%E9%9D%A2%E5%8E%BB%E8%B5%9A%E9%92%B1%EF%BC%8C%E4%BD%A0%E4%BB%A5%E4%B8%BA%E5%B0%B1%E5%87%AD%E4%BD%A0%E6%88%91%E4%BB%AC%E5%8F%AF%E4%BB%A5%E6%9C%89%E5%A4%9A%E4%B9%88%E5%A5%BD%E7%9A%84%E7%94%9F%E6%B4%BB%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E4%BD%A0%E8%B5%9A%E9%92%B1%E7%9A%84%E6%96%B9%E6%B3%95%E5%B0%B1%E6%98%AF%E5%82%8D%E5%A4%A7%E6%AC%BE%EF%BC%8C%E7%84%B6%E5%90%8E%E7%BB%99%E4%BB%96%E4%BB%AC%E4%B9%B1%E6%91%B8%E4%B8%80%E9%80%9A%EF%BC%9F%E2%80%9D%0D%0A%E2%80%9C%E6%88%91%E6%B2%A1%E6%9C%89%EF%BC%81%E2%80%9D%E6%9D%A8%E6%99%93%E6%99%93%E5%98%9F%E8%B5%B7%E5%98%B4%E5%B7%B4%EF%BC%8C%E5%8F%8C%E7%9C%BC%E9%83%BD%E8%A6%81%E5%86%92%E5%87%BA%E7%81%AB%E6%9D%A5%E4%BA%86%E3%80%82%0D%0A%E2%80%9C%E6%88%91%E7%9C%8B%E8%A7%81%E4%BA%86%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E6%88%91%E2%80%A6%E2%80%A6%E2%80%9D%0D%0A%E2%80%9C%E6%88%91%E4%BB%AC%E5%88%86%E6%89%8B%E5%90%A7%E3%80%82%E2%80%9D%E8%AF%B4%E5%AE%8C%E8%BF%99%E5%8F%A5%E8%AF%9D%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%B0%B1%E7%A6%BB%E5%BC%80%E4%BA%86%E8%BF%99%E9%97%B4%E5%B0%8F%E5%87%BA%E7%A7%9F%E6%88%BF%E3%80%82%E6%88%BF%E5%86%85%E7%95%99%E4%B8%8B%E6%9D%A5%E7%9A%84%E6%9D%A8%E6%99%93%E6%99%93%EF%BC%8C%E6%B2%A1%E6%9C%89%E5%9B%A0%E4%B8%BA%E5%88%86%E6%89%8B%E8%80%8C%E4%BC%A4%E5%BF%83%E7%9A%84%E6%A0%B7%E5%AD%90%EF%BC%8C%E5%8F%8D%E5%80%92%E6%98%AF%E5%BE%88%E5%BC%80%E5%BF%83%E3%80%82%0D%0A%E5%98%B4%E8%A7%92%E5%8B%BE%E5%87%BA%E4%B8%80%E6%8A%B9%E7%AC%91%E6%84%8F%EF%BC%8C%E6%8B%BF%E8%B5%B7%E6%89%8B%E4%B8%AD%E7%9A%84%E6%89%8B%E6%9C%BA%EF%BC%8C%E6%8B%A8%E6%89%93%E8%BF%87%E5%8E%BB%E3%80%82%E2%80%9C%E5%96%82%EF%BC%8C%E8%BD%A9%E5%93%A5%E4%BB%8A%E5%A4%A9%E8%B0%A2%E8%B0%A2%E4%BD%A0%E5%92%AF%EF%BC%81%E2%80%9D%0D%0A%E2%80%9C%E4%B8%8D%E7%94%A8%E8%B0%A2%EF%BC%8C%E4%B8%80%E7%82%B9%E5%B0%8F%E5%BF%99%EF%BC%8C%E9%82%A3%E6%9D%A1%E5%92%B8%E9%B1%BC%E5%92%8C%E4%BD%A0%E5%88%86%E6%89%8B%E4%BA%86%EF%BC%9F%E2%80%9D%E7%94%B5%E8%AF%9D%E9%82%A3%E8%BE%B9%EF%BC%8C%E6%98%AF%E4%B8%80%E4%B8%AA%E7%94%B7%E4%BA%BA%E7%9A%84%E5%A3%B0%E9%9F%B3%E3%80%82%0D%0A%E2%80%9C%E6%98%AF%E5%95%8A%EF%BC%8C%E6%88%91%E7%BB%88%E4%BA%8E%E5%92%8C%E8%BF%99%E4%B8%AA%E5%92%B8%E9%B1%BC%E5%88%86%E6%89%8B%E4%BA%86%EF%BC%8C%E6%98%AF%E4%BB%96%E4%B8%BB%E5%8A%A8%E6%8F%90%E5%87%BA%E6%9D%A5%E7%9A%84%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E5%97%AF%EF%BC%8C%E8%BF%99%E6%A0%B7%E5%B0%B1%E5%A5%BD%EF%BC%8C%E5%BD%95%E9%9F%B3%E4%BA%86%E5%90%97%EF%BC%9F%E4%B8%8D%E7%84%B6%E4%BB%A5%E5%90%8E%E9%82%A3%E5%87%A0%E4%B8%AA%E4%BA%BA%E6%9D%A5%E6%89%BE%E9%BA%BB%E7%83%A6%E8%A6%81%E4%BD%A0%E4%BB%AC%E5%A4%8D%E5%90%88%EF%BC%8C%E4%BD%A0%E8%BF%98%E6%98%AF%E4%B8%8D%E5%A5%BD%E8%AF%B4%E3%80%82%E2%80%9D%0D%0A%E2%80%9C%E5%BD%95%E9%9F%B3%E5%BD%95%E4%BA%86%EF%BC%8C%E6%8A%8A%E5%89%8D%E9%9D%A2%E7%9A%84%E5%AF%B9%E8%AF%9D%E5%88%A0%E6%8E%89%EF%BC%8C%E5%B0%B1%E7%AE%97%E4%BB%A5%E5%90%8E%E4%BB%96%E4%BB%AC%E6%9D%A5%E6%89%BE%E9%BA%BB%E7%83%A6%EF%BC%8C%E6%88%91%E4%B9%9F%E4%B8%8D%E7%94%A8%E6%80%95%E4%BA%86%E3%80%82%E2%80%9D%0D%0A%E8%AF%B4%E5%AE%8C%EF%BC%8C%E6%9D%A8%E6%99%93%E6%99%93%E8%BA%BA%E5%88%B0%E4%BA%86%E6%B2%99%E5%8F%91%E4%B8%8A%E9%9D%A2%E3%80%82%0D%0A%E8%80%8C%E6%AD%A4%E6%97%B6%E7%9A%84%E5%AE%89%E8%BE%B0%EF%BC%8C%E4%B8%80%E4%B8%AA%E4%BA%BA%E5%A4%B1%E9%AD%82%E8%90%BD%E9%AD%84%E7%9A%84%E8%B5%B0%E5%9C%A8%E5%A4%A7%E8%A1%97%E4%B8%8A%E9%9D%A2%EF%BC%8C%E4%BB%96%E5%B0%B1%E8%AF%B4%E5%98%9B%EF%BC%8C%E9%82%A3%E4%B9%88%E5%A5%BD%E4%B8%80%E4%B8%AA%E5%A5%B3%E5%AD%A9%E6%80%8E%E4%B9%88%E5%8F%AF%E8%83%BD%E4%B8%BB%E5%8A%A8%E6%89%BE%E4%B8%8A%E8%87%AA%E5%B7%B1%EF%BC%8C%E8%BF%98%E8%AF%B4%E6%98%AF%E8%A2%AB%E9%80%BC%E6%97%A0%E5%A5%88%E3%80%82%E8%99%BD%E7%84%B6%E7%8E%B0%E5%9C%A8%E5%AE%89%E8%BE%B0%E8%BF%98%E4%B8%8D%E7%9F%A5%E9%81%93%E6%9D%A8%E6%99%93%E6%99%93%E6%89%BE%E8%87%AA%E5%B7%B1%E7%9A%84%E7%94%A8%E6%84%8F%EF%BC%8C%E5%8F%8D%E6%AD%A3%E6%B2%A1%E5%A5%BD%E4%BA%8B%E5%B0%B1%E5%AF%B9%E4%BA%86%E3%80%82%0D%0A%E4%B8%8D%E7%9F%A5%E4%B8%8D%E8%A7%89%EF%BC%8C%E7%AA%81%E7%84%B6%E4%B8%8B%E8%B5%B7%E4%BA%86%E9%9B%A8%E6%9D%A5%EF%BC%8C%E4%BC%B4%E9%9A%8F%E7%9D%80%E5%87%A0%E5%A3%B0%E8%BD%B0%E9%B8%A3%E5%A3%B0%EF%BC%8C%E9%9B%A8%E5%93%97%E5%95%A6%E5%95%A6%E7%9A%84%E8%90%BD%E4%B8%8B%E3%80%82%E5%AE%89%E8%BE%B0%E5%8A%A0%E5%BF%AB%E8%84%9A%E4%B8%8B%E7%9A%84%E7%9A%84%E5%8A%A8%E4%BD%9C%EF%BC%8C%E5%9C%A8%E9%9B%A8%E9%87%8C%E4%B9%B1%E8%B7%91%E3%80%82%E6%AD%A4%E5%88%BB%EF%BC%8C%E4%BB%96%E6%83%B3%E7%9A%84%E6%98%AF%EF%BC%8C%E8%87%AA%E5%B7%B1%E8%A6%81%E6%80%8E%E4%B9%88%E6%AD%BB%EF%BC%9F%0D%0A%E6%98%AF%E7%9A%84%EF%BC%8C%E4%BB%96%E5%B7%B2%E7%BB%8F%E4%B8%80%E5%BF%83%E5%AF%BB%E6%AD%BB%E4%BA%86%E3%80%82%0D%0A%E8%AF%9A%E5%AE%9E%EF%BC%8C%E5%9F%8E%E5%B8%82%E6%9C%89%E4%BB%80%E4%B9%88%E7%94%A8%EF%BC%8C%E7%8E%B0%E5%9C%A8%E4%BC%81%E4%B8%9A%E8%80%81%E6%9D%BF%E5%8F%88%E4%B8%8D%E4%BB%A5%E8%BF%99%E4%B8%AA%E6%94%B6%E4%BA%BA%E3%80%82%E6%88%91%E5%B0%B1%E4%B8%8D%E4%BC%9A%E6%8B%8D%E9%A9%AC%E5%B1%81%EF%BC%8C%E4%B8%8D%E4%BC%9A%E8%AF%B4%E5%A5%BD%E8%AF%9D%EF%BC%8C%E5%BC%84%E5%BE%97%E7%8E%B0%E5%9C%A8%E9%83%BD%E6%B2%A1%E6%9C%89%E5%B7%A5%E8%B5%84%E3%80%82%E5%B0%B1%E8%BF%9E%E9%82%A3%E4%B9%88%E5%A5%BD%E4%B8%80%E4%B8%AA%E5%A5%B3%E6%9C%8B%E5%8F%8B%EF%BC%8C%E4%B9%9F%E8%A2%AB%E5%88%AB%E4%BA%BA%E6%8A%A2%E8%B5%B0%E4%BA%86%E3%80%82%0D%0A%E9%9B%A8%E6%B0%B4%E6%B7%B7%E6%9D%82%E7%9D%80%E6%B3%AA%E6%B0%B4%E5%9C%A8%E8%84%B8%E4%B8%8A%E6%B5%81%E5%8A%A8%EF%BC%8C%E5%AE%89%E8%BE%B0%E4%B8%8D%E5%81%9C%E7%9A%84%E6%93%A6%E6%8B%AD%E3%80%82%E4%B8%8D%E7%9F%A5%E9%81%93%E6%98%AF%E4%B8%8D%E6%98%AF%E5%A4%AA%E7%B4%AF%E4%BA%86%EF%BC%8C%E5%BD%93%E8%B7%91%E5%88%B0%E4%B8%80%E9%A2%97%E6%A0%91%E4%B8%8B%E7%9A%84%E6%97%B6%E5%80%99%EF%BC%8C%E5%AE%89%E8%BE%B0%E5%9D%90%E4%B8%8B%E6%9D%A5%E5%87%86%E5%A4%87%E4%BC%91%E6%81%AF%E4%B8%80%E4%B8%8B%E3%80%82%0D%0A%E5%9D%90%E5%9C%A8%E5%9C%B0%E4%B8%8A%EF%BC%8C%E5%88%9A%E5%88%9A%E5%91%BC%E5%90%B8%E5%BC%80%E5%A7%8B%E5%9D%87%E5%8C%80%EF%BC%8C%E5%8F%AF%E6%98%AF%E8%BF%99%E4%B8%AA%E6%97%B6%E5%80%99%EF%BC%8C%E4%B8%80%E9%81%93%E5%A4%A9%E9%9B%B7%E9%A1%BA%E7%9D%80%E6%A0%91%E6%9E%9D%EF%BC%8C%E6%89%93%E5%88%B0%E4%BA%86%E5%AE%89%E8%BE%B0%E7%9A%84%E5%A4%A9%E7%81%B5%E7%9B%96%E4%B8%8A%E9%9D%A2%E3%80%82%0D%0A","title":"%E7%AC%AC%E4%B8%80%E7%AB%A0+%E5%A4%B1%E9%AD%82%E8%90%BD%E9%AD%84","chapternum":1,"chapterid":11966943}}
        // 失败,不存在:{"code":-20302,"message":"There is no part!"}

        //getBookCountUrl();
        //System.out.println(getUpdateInfoUrl(31971));
        //System.out.println(addChapterUrl(335530292,"第二章 说谎系统",content,2,0,11029164,1));
        //(int bookid,String title,String content,int chapternum,int isvip,int chapterid,int updatemode)
        /*String chapterListUrl = getChapterListUrl("1155");
        System.out.println(chapterListUrl);
        JSONObject jsonObject = JSONObject.parseObject(chapterListUrl);
        JSONArray resultArray = jsonObject.getJSONArray("result");

        for(int i=0;i<resultArray.size();i++){
            JSONObject jsonObject1 = resultArray.getJSONObject(i);
            String chapterid = jsonObject1.getString("chapterid");
            System.out.println(chapterid);
        }*/
        /*String bookListUrl = getBookListUrl();
        JSONObject jsonObject = JSONObject.parseObject(bookListUrl);
        JSONArray resultArray = jsonObject.getJSONArray("result");

        for(int i=0;i<resultArray.size();i++){
            JSONObject jsonObject1 = resultArray.getJSONObject(i);
            String cpid = jsonObject1.getString("cpid");
            System.out.println(cpid);
            //System.out.println(getBookgetBookDetailUrl(cpid));
            //System.out.println(getUpdateInfoUrl(cpid));//chapterid":3407       chapterid":1091
        }*/

    }
    public static JSONObject getChapterListUrl(int cpid) throws Exception {
        /*String getUpdateInfoUrl="http://topenapi.tadu.com:8098/api/getChapterList";//查询章节目录
        int copyrightid=292;
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*/
        String getUpdateInfoUrl="http://openapi.tadu.com/api/getChapterList";//正式环境
        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";

        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("cpid",cpid);
        String HttpResult = HttpClientUtilsGX.doPost(getUpdateInfoUrl, requestDataMap);
        JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        return jsonObject;
    }


    //添加、更新章节接口
    public static JSONObject addChapterUrl(int bookid,String title,String content,int chapternum,int isvip,int chapterid,int updatemode) throws Exception {
       //{"code":-20202,"message":"parts repeat!"} 如果已经推送过这个章节了
        /*String addChapterUrl="http://topenapi.tadu.com:8098/api/addChapter";//添加、更新章节
        int copyrightid=292;
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*/
        String addChapterUrl="http://openapi.tadu.com/api/addChapter";//添加、更新章节
        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";

        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("bookid",bookid);
        //requestDataMap.put("cpid",bookid);
        requestDataMap.put("title",title);
        requestDataMap.put("content",content);
        requestDataMap.put("chapternum",chapternum);
        requestDataMap.put("isvip",isvip);
        requestDataMap.put("chapterid",chapterid);
        requestDataMap.put("updatemode",updatemode);
        String HttpResult = HttpClientUtilsGX.doPost(addChapterUrl, requestDataMap);
        System.out.println(HttpResult);
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
        String HttpResult = HttpClientUtilsGX.doPost(getUpdateInfoUrl, requestDataMap);
        JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        return jsonObject;
    }


    public static JSONObject addBookUrl(int cpid,String bookname,String authorname,String intro,int classid,String coverimage,int serial,int isvip) throws Exception {
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
        String HttpResult = HttpClientUtilsGX.doPost(addBookUrl, requestDataMap);
        JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        return jsonObject;
    }
    public static String getUpdateInfoUrl(String cpid) throws Exception {
        /*String getUpdateInfoUrl="http://topenapi.tadu.com:8098/api/getUpdateInfo";//成功,查询最新章节信息接口
        int copyrightid=292;
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*/
        String getUpdateInfoUrl="http://openapi.tadu.com/api/getUpdateInfo";//成功,查询最新章节信息接口
        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";

        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("cpid",cpid);
        String HttpResult = HttpClientUtilsGX.doPost(getUpdateInfoUrl, requestDataMap);
        return HttpResult;
    }

    /*public static String getChapterListUrl(String cpid) throws Exception {
        *//*String getChapterListUrl="http://topenapi.tadu.com:8098/api/getChapterList";//成功,查询章节目录
        int copyrightid=292;
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)*//*
        String getChapterListUrl="http://openapi.tadu.com/api/getChapterList";//正式环境
        int copyrightid=295;
        String key="d66ff2bb7b43b0866f8f9eed286362e9";

        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("cpid",cpid);
        String HttpResult = HttpClientUtilsGX.doPost(getChapterListUrl, requestDataMap);
        return HttpResult;
    }*/

    public static JSONObject getChapterDetailUrl(Integer cpid,Integer chapterid){//查询章节详情接口
        //{"code":-20202,"message":"parts repeat!"} 如果已经推送过这个章节了
       /* String addChapterUrl="http://topenapi.tadu.com:8098/api/getChapterDetail";
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
                    System.out.println("章节列表3次查询失败,请检查网络");
                    e2.printStackTrace();
                }
            }
        }
        System.out.println(HttpResult);
        JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        return jsonObject;
    }

    public static String getBookgetBookDetailUrl(String cpid) throws Exception {
        String getBookDetailUrl="http://openapi.tadu.com/api/getBookDetail";
       // String getBookDetailUrl="http://topenapi.tadu.com:8098/api/getBookDetail";
       //int copyrightid=292;
        String secre="d66ff2bb7b43b0866f8f9eed286362e9";//90cb352f4f36e289d6d9abc778b23748
       // String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)

        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";

        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        requestDataMap.put("cpid",cpid);
        String HttpResult = HttpClientUtilsGX.doPost(getBookDetailUrl, requestDataMap);
        return HttpResult;
    }

    public static String getBookListUrl() throws Exception {
        String getBookListUrl="http://openapi.tadu.com/api/getBookList";
        //String getBookListUrl="http://topenapi.tadu.com:8098/api/getBookList";
        //成功,查询书籍列表接口 { "code": 0, "message": "success!", "result": [ { "cpid": 1155, "bookName": "%E9%9C%B8%E5%A4%A9%E7%A5%96%E9%BE%99%E5%86%B3" },{ "cpid": 1156, "bookName": "%E7%81%B5%E5%BC%82%E7%9B%B8%E5%86%8C" },{ "cpid": 1157, "bookName": "%E8%B1%AA%E5%AE%85%E6%80%A8%E9%AD%82" } ] }
        //int copyrightid=292;
        String secre="90cb352f4f36e289d6d9abc778b23748";
        //String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)

        int copyrightid=295;
        String key="21d271be5793f82fc4191b0ad331c63ed3b22dbc";


        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",copyrightid);
        requestDataMap.put("key",key);
        int startPage=0;
        requestDataMap.put("startPage",startPage);
        String HttpResult = HttpClientUtilsGX.doPost(getBookListUrl, requestDataMap);
        return HttpResult;
        /*JSONObject jsonObject = JSONObject.parseObject(HttpResult);
        JSONArray resultArray = jsonObject.getJSONArray("result");

        for(int i=0;i<resultArray.size();i++){
            JSONObject jsonObject1 = resultArray.getJSONObject(i);
            String cpid = jsonObject1.getString("cpid");
            System.out.println(cpid);
            String decode = URLDecoder.decode(jsonObject1.getString("bookName"), "utf-8");
            System.out.println(decode);
        }*/
    }

    public static void getBookCountUrl() throws Exception {
        String getBookCountUrl="http://topenapi.tadu.com:8098/api/getBookCount";//成功,查询书籍总数接口 {"code":0,"message":"success!","result":{"count":243}}
      int copyrightid=292;
        String secre="90cb352f4f36e289d6d9abc778b23748";
        String key="0e722a00eb8e61df168fc4f63770ed64f85180ab";//key=SHA-1(copyrightid+secre)

        //创建一个请求参数map集合
        Map<String, Object> requestDataMap = new HashMap<String, Object>();
        requestDataMap.put("copyrightid",292);
        requestDataMap.put("key",key);
        String HttpResult = HttpClientUtilsGX.doPost(getBookCountUrl, requestDataMap);
        System.out.println(HttpResult);
    }
}
