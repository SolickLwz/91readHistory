/*
package com.nine.one.yuedu.read.utils.GX;
*/
/**
 * 敏感词过滤 DFA算法
 *
 * @author
 * @create 2017-08-11 9:04
 **//*



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

*/
/**
 * 敏感词过滤工具类
 *//*

public class SensitiveWordUtil {

    */
/**
     * 字符编码
     *//*

    private String encoding = "UTF-8";
    */
/**
     * 定义初始化hashMap存放读取配置(sensitiveWord.txt)文件中的敏感词
     *//*

    @SuppressWarnings("rawtypes")
    private HashMap sensitiveWordMapForTxt;
    */
/**
     * 程序启动时加载该Map
     *//*

    private Map sensitiveWordMap = null;
    */
/**
     * 最小匹配规则 如敏感字中有[法轮、法轮功]  将优先匹配[法轮]
     *//*

    private static int minMatchType = 1;      //最小匹配规则
    */
/**
     * 最大匹配规则 如敏感字中有[法轮、法轮功]  将优先匹配[法轮功]
     *//*

//    private static int maxMatchType = 2;      //最大匹配规则

    */
/**
     * 构造函数，初始化敏感词库 ，将敏感词加入到HashMap中，构建DFA算法模型
     *//*

    public SensitiveWordUtil() {
        //程序启动时加载
        sensitiveWordMap = BaseConfig.sensitiveWord;
        //主动加载
//        sensitiveWordMap =  initKeyWord();
    }

    */
/**
     * 初始化读取存放敏感词至map 调用该方法
     *
     * @return return
     *//*

    @SuppressWarnings("rawtypes")
    public Map initKeyWord() {
        try {
            //读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFile();
            //将敏感词库加入到HashMap中
            addSensitiveWordToHashMap(keyWordSet);
            //spring获取application，然后application.setAttribute("sensitiveWordMapForTxt",sensitiveWordMapForTxt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMapForTxt;
    }

    */
/**
     * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
     * 中 = {
     * isEnd = 0
     * 国 = {<br>
     * isEnd = 1
     * 人 = {isEnd = 0
     * 民 = {isEnd = 1}
     * }
     * 男  = {
     * isEnd = 0
     * 人 = {
     * isEnd = 1
     * }
     * }
     * }
     * }
     * 五 = {
     * isEnd = 0
     * 星 = {
     * isEnd = 0
     * 红 = {
     * isEnd = 0
     * 旗 = {
     * isEnd = 1
     * }
     * }
     * }
     * }
     *
     * @param keyWordSet 敏感词库
     *//*

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMapForTxt = new HashMap(keyWordSet.size());     //初始化敏感词容器，减少扩容操作
        String key = null;
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //迭代keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while (iterator.hasNext()) {
            key = iterator.next();    //关键字
            nowMap = sensitiveWordMapForTxt;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);       //转换成char型
                Object wordMap = nowMap.get(keyChar);       //获取

                if (wordMap != null) {        //如果存在该key，直接赋值
                    nowMap = (Map) wordMap;
                } else {     //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
                    newWorMap = new HashMap<String, String>();
                    newWorMap.put("isEnd", "0");     //不是最后一个
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }

                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");    //最后一个
                }
            }
        }
    }

    */
/**
     * 读取敏感词库中的内容，将内容添加到set集合中
     *
     * @return return
     * @throws Exception Exception
     *//*

    @SuppressWarnings("resource")
    private Set<String> readSensitiveWordFile() throws Exception {
        Set<String> set = null;
        //获取当前class文件路径
        String path = this.getClass().getResource("/").getPath();

//        System.out.println(path + "/sensitiveWord.txt");
        File file = new File(path + "/sensitiveWord.txt");    //读取文件
        InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
        try {
            if (file.isFile() && file.exists()) {      //文件流是否存在
                set = new HashSet<String>();
                BufferedReader bufferedReader = new BufferedReader(read);
                String txt = null;
                while ((txt = bufferedReader.readLine()) != null) {    //读取文件，将文件内容放入到set中
                    set.add(txt);
                }
//                System.out.println("敏感词库为："+ set);
            } else {         //不存在抛出异常信息
                throw new Exception("敏感词库文件不存在");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            read.close();     //关闭文件流
        }
        return set;
    }

    */
/**
     * 判断文字是否包含敏感字符
     *
     * @param txt       文字
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @return 若包含返回true，否则返回false
     * @author chenming
     * @date 2014年4月20日 下午4:28:30
     * @version 1.0
     *//*

    public boolean isContaintSensitiveWord(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = this.checkSensitiveWord(txt, i, matchType); //判断是否包含敏感字符
            if (matchFlag > 0) {    //大于0存在，返回true
                flag = true;
            }
        }
        return flag;
    }

    */
/**
     * 获取文字中的敏感词
     *
     * @param txt       文字
     * @param matchType 匹配规则&nbsp;1：最小匹配规则，2：最大匹配规则
     * @return return
     *//*

    public Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<String>();

        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i, matchType);    //判断是否包含敏感字符
            if (length > 0) {    //存在,加入list中
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;    //减1的原因，是因为for会自增
            }
        }

        return sensitiveWordList;
    }

    */
/**
     * 替换敏感字字符
     *
     * @param txt         验证String字符串
     * @param matchType   匹配规则 1最小2最大
     * @param replaceChar 替换字符，默认*
     * @return String
     *//*

    public String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType);     //获取所有的敏感词
        Iterator<String> iterator = set.iterator();
        String word = null;
        String replaceString = null;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    */
/**
     * 获取替换字符串
     *
     * @param replaceChar replaceChar
     * @param length      length
     * @return String
     *//*

    private String getReplaceChars(String replaceChar, int length) {
        String resultReplace = replaceChar;
        for (int i = 1; i < length; i++) {
            resultReplace += replaceChar;
        }

        return resultReplace;
    }

    */
/**
     * 检查文字中是否包含敏感字符，检查规则如下：<br>
     * 如果存在，则返回敏感词字符的长度，不存在返回0
     *
     * @param txt        txt
     * @param beginIndex beginIndex
     * @param matchType  matchType
     * @return return
     *//*

    @SuppressWarnings({"rawtypes"})
    public int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        boolean flag = false;    //敏感词结束标识位：用于敏感词只有1位的情况
        int matchFlag = 0;     //匹配标识数默认为0
        char word = 0;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);     //获取指定key
            if (nowMap != null) {     //存在，则判断是否为最后一个
                matchFlag++;     //找到相应key，匹配标识+1
                if ("1".equals(nowMap.get("isEnd"))) {       //如果为最后一个匹配规则,结束循环，返回匹配标识数
                    flag = true;       //结束标志位为true
                    if (SensitiveWordUtil.minMatchType == matchType) {    //最小规则，直接返回,最大规则还需继续查找
                        break;
                    }
                }
            } else {     //不存在，直接返回
                break;
            }
        }
        if (matchFlag <= 1 || !flag) {        //长度必须大于等于1，为词
            matchFlag = 0;
        }
        return matchFlag;
    }

    */
/**
     * 测试调用
     *
     * @param args args
     *//*

    public static void main(String[] args) {
        SensitiveWordUtil filter = new SensitiveWordUtil();
        System.out.println("敏感词的数量：" + filter.sensitiveWordMap.size());
        String string = "太多的伤感情怀也许只局限于饲养基地 荧幕中的情节，主人公尝试着去用某种方式渐渐的很潇洒地释自杀指南怀那些自己经历的伤感。"
                + "然后法轮功握草回民吃猪肉 我们的扮演的角色就是跟随着主人公的喜红客联盟 怒哀乐而过于牵强的把自己的情感也附加于银幕情节中，然后感动就流泪，"
                + "难过就躺在某一个人的怀里尽情的阐述心扉或者手机卡复制器一个人一杯红酒一部电影在夜三级片 深人静的晚上，关上电话静静的发呆着。";
        System.out.println("待检测语句字数：" + string.length());
        long beginTime = System.currentTimeMillis();
        Set<String> set = filter.getSensitiveWord(string, 1);
        long endTime = System.currentTimeMillis();
        System.out.println("语句中包含敏感词的个数为：" + set.size() + "。包含：" + set);
        String s = filter.replaceSensitiveWord(string, 1, "*");
        System.out.println("替换关键词前的旧串：" + string);
        System.out.println("替换关键词后的新串：" + s);
        System.out.println("总共消耗时间为：" + (endTime - beginTime));
    }
}*/
