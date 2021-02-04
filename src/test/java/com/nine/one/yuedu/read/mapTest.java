package com.nine.one.yuedu.read;

import com.nine.one.yuedu.read.utils.HttpClientUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Author:李王柱
 * 2020/6/24
 */
public class mapTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
       /* HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        String book="大概是在九几年吧，那个时候正值夏季，西南山区很多浓密的山林中还有一些散布的村落，因为那个时候又没有通公路，所以在大山里几乎都是比较偏僻。\n" +
                "不通水电，一些村子很难和外界有任何联系。不过就在那个很炎热的八月，我还清楚地记得，那个中午，我和村子五六个孩子在溪水边洗澡避暑，不知为何，这烈日几乎都要将周围烧着了，十分要命。\n" +
                "所以，我们闲着无事的时候，就在溪水里面消夏。我和隔壁二狗几个孩子正玩儿得酣畅的时候，突然，我感觉到了一股窒息的寒意，阴冷、我身体瞬间几乎都要窒息了，溪水居然冒着寒气，吓得我们几个人赶紧从水里钻出来了。\n" +
                "我抬头一看，发现就在山路那边一群人走了过来。\n" +
                "要知道，那山路到处都是茅草，一米多高，那一群人十来个就朝着我们走过来了。那些人距离我们越近，寒气也越来越盛，虽然这是大中午的，烈日顶头，但是那些人一靠近我们，我还是感觉自己像是被冻在冰窖里一样。\n" +
                "不过这些人很奇怪，全身上下都包裹着一层厚厚的棉衣，而且那棉衣上面敷着一层层的烂泥，好像是刚从地里面挖出来的一样，每个人都紧紧裹着身体。就连脸也用一块布包着，只看见黑洞洞的眼眶。";
        stringObjectHashMap.put("content", new String(book.getBytes(),"utf-8"));
        System.out.println(stringObjectHashMap.get("content"));*/
        //String jsonBookInfo= HttpClientUtil.doGet("http://api.baiwancangshu.com/Book/Books");
        String jsonBookInfo= HttpClientUtil.doGet("http://api.baiwancangshu.com/Book/BookInfo/book_id/"+556);
        //System.out.println(jsonBookInfo);

        final String ss="　　“萧落晚上总是忘记洗脸，你要提醒他。”\n" +
                "　　“还有你要看着他护肤，敷面膜。”\n" +
                "　　“他喜欢吃甜的，但是你不能让他多吃，不对，是不能让他吃。”\n" +
                "　　“甜品吃多了长痘，还发胖，啧啧。”经纪人一边摇头一边唾沫横飞的和林茉说着，说一条在他手里的书上划掉一条。\n" +
                "　　林茉看着经纪人的头皮清晰可见的头顶哈欠连天，一路敷衍着应着，这都不知道是萧落的第几条注意事项了。\n" +
                "　　萧落，萧落，萧落，她现在满脑子都是萧落二字。";

        System.out.println(ss.replace("\n","</p><p>"));//.replace("\r\n","</p><p>")

    }
}