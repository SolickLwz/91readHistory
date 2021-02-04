package com.nine.one.yuedu.read.utils.GX;

import java.util.Map;

/**
 * Author:李王柱
 * 2020/11/3
 */
public class mapToXmlUtil {
    private static final String PREFIX_CDATA = "<![CDATA[";

    private static final String SUFFIX_CDATA = "]]>";
    /**
     * 转化成xml, 单层无嵌套
     * @param parm
     * @param isAddCDATA
     * @return
     */
    public static String mapToXml(Map<Object, Object> parm, boolean isAddCDATA) {//,String node ,String opNode
        //StringBuffer strbuff = new StringBuffer(PREFIX_XML);
        //StringBuilder strbuff = new StringBuilder("<?xml version='1.0' encoding=\"UTF-8\"?>"+node);
        StringBuilder strbuff = new StringBuilder();
        if (null != parm) {
            for (Map.Entry<Object, Object> entry : parm.entrySet()) {
                strbuff.append("<").append(entry.getKey()).append(">");
                if (isAddCDATA) {
                    strbuff.append(PREFIX_CDATA);
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                    strbuff.append(SUFFIX_CDATA);
                } else {
                    if (null != entry.getValue()) {
                        strbuff.append(entry.getValue());
                    }
                }
                strbuff.append("</").append(entry.getKey()).append(">");
            }
        }
        //return strbuff.append(SUFFIX_XML).toString();
        //strbuff.append(opNode).toString();
        return strbuff.toString();
    }
}
