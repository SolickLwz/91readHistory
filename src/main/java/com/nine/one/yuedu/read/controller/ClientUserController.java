package com.nine.one.yuedu.read.controller;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.User;
import com.nine.one.yuedu.read.entity.UserAuthor;
import com.nine.one.yuedu.read.entity.WxToAuthuser;
import com.nine.one.yuedu.read.mapper.UserMapper;
import com.nine.one.yuedu.read.mapper.WxToAuthuserMapper;
import com.nine.one.yuedu.read.service.UserService;
import com.nine.one.yuedu.read.service.WeiXinUserService;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * Author:李王柱
 * 2020/11/10
 */
@RestController
@Api(tags = "client用户相关", value = "client用户相关")
@RequestMapping("/91yuedu/clientUser")
public class ClientUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private WeiXinUserService weiXinUserService;
    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    @ApiOperation(value = "client用户登录")
    public JXResult login(@RequestParam("username") @ApiParam("登录用户名") String username,
                          @RequestParam("password") @ApiParam("密码") String password) {
        JXResult loginResult = userService.login(username, password);
        return loginResult;
    }

    @GetMapping("/judgment/{bookId}/{chapterId}")
    @ApiOperation("判断用户是否可以阅读此章节,以及给用户什么样的反馈")
    public JXResult judgment(@ApiParam ("书籍id") @PathVariable Integer bookId,
                             @ApiParam ("章节id") @PathVariable Integer chapterId,
                             @RequestParam("clientUserId") @ApiParam("用户id") Integer clientUserId,
                             @RequestParam("isTrue") @ApiParam("是否确认付费") String isTrue){
        return userService.judgment(bookId,chapterId,clientUserId,isTrue);
    }

    @GetMapping(value = "/clientUserWeiXinLogin")
    @ApiOperation(value = "微信登录普通用户", notes = "微信登录普通用户")
    public JXResult clientUserWeiXinLogin(@RequestParam(value = "code") @ApiParam(value = "微信返回的code", required = true) String code) throws Exception {
        String appid ="wx9013ed93344e6cbe";//"wx2e9c2975c24f2f3c";//报联文学的
        String secret = "ca110c2b36cf7ba789dcca6825268393";//"4222adfc8b18a1361d56280c6d74017f";

        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appid +
                "&secret=" + secret + "&code=" + code + "&grant_type=authorization_code";
        String token = HttpClientUtilsGX.doGet(url);
        //{"access_token":/* 	接口调用凭证*/
        // "35_8u2MXnuMkVGojPbMR6AsXRmA5ShNukj6Fd6yTtCGE7rvBslXCa8RdBMqOSc8z158NYOSgOskZUEx6ngcgFMt7Lm6HsjfiVW7qYDmO3DfxCg",
        // "expires_in":7200,/* 	access_token接口调用凭证超时时间，单位（秒）*/
        // "refresh_token":/*用户刷新access_token*/
        // "35_gjL3eDJbDyTxhaKx9LXsiODl3a06GqlNtbdJ-6HMCDVnE-N7g8R3pvj2LwPiyCazOsEKn5Sa2g0ugcy-laNVCTk965Gk3rQ0zJAbHyVYlOo",
        // "openid":"oNoP208_r6Qsox0Ob1WgRx3H3cGM",/* 	授权用户唯一标识*/
        // "scope":"snsapi_login",/*用户授权的作用域，使用逗号（,）分隔*/
        // "unionid":"od_VcxHHCgoM-w4o1o2G7eDNBlOo"}/*当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。*/
        JSONObject tokenJson = JSONObject.parseObject(token);
        String access_token = tokenJson.getString("access_token");//接口调用凭证
        String openid = tokenJson.getString("openid");//授权用户唯一标识
        String userInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token + "&openid=" + openid;
        String userInfo = HttpClientUtilsGX.doGet(userInfoUrl);
        //{"openid":"oNoP208_r6Qsox0Ob1WgRx3H3cGM",//普通用户的标识，对当前开发者帐号唯一
        // "nickname":"solick",
        // "sex":1,
        // "language":"zh_CN",
        // "city":"",
        // "province":"",
        // "country":"BM",
        // "headimgurl":"http:\/\/thirdwx.qlogo.cn\/mmopen\/vi_32\/iandiac5cIlru5WzuK9eGAibdzia198m7qLQMsC86rfaFQgxLMs5vDoTYuIiag2bRqq3Kpia4Y7yvU9IGZicrRBHPJjJg\/132",
        // "privilege":[],
        // "unionid":"od_VcxHHCgoM-w4o1o2G7eDNBlOo"}// 	用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
        JSONObject userJson = JSONObject.parseObject(userInfo);
        //检查微信中有没有这个用户,没有就创建一个作者和微信用户,并为他登录[为他登录到client!]
        WxToAuthuser wxToAuthuser= weiXinUserService.getAuthorUserByUnionid(userJson.getString("unionid"));
        if (null==wxToAuthuser){
            System.out.println("还没有这个client用户,创建");
            User userByRegister = new User();
            userByRegister.setUsername(""+userJson.getString("nickname"));//用户名是微信名
            userByRegister.setNickname("微信用户"+userJson.getString("nickname"));
            userByRegister.setSex(userJson.getInteger("sex"));
            userByRegister.setPassword(userJson.getString("nickname") + "91yuedu");//用户密码
            userService.register(userByRegister);//进行注册

            //注册完,从数据库中获取这个作者,把他的id给微信对象
            User userByWeixinAdd= userService.getClientUserByUser(userByRegister);
            if (null==userByWeixinAdd){
                System.out.println("没有取到该user!");
            }
            wxToAuthuser = new WxToAuthuser();
            wxToAuthuser.setAppid(appid);

            wxToAuthuser.setAuthorid(userByWeixinAdd.getId());
            wxToAuthuser.setNickname(userByWeixinAdd.getNickname());
            wxToAuthuser.setOpenid(userJson.getString("openid"));
            wxToAuthuser.setUnionid(userJson.getString("unionid"));
            //保存微信对象
            int flag=weiXinUserService.addWxToAuthorUser(wxToAuthuser);
        }
        //走到这就是有微信用户了,拿到这个微信用户,获取authid字段,并完成登录
        Integer clientid = wxToAuthuser.getAuthorid();//获取用户id,注册过的用户已经保存在微信对象里里,新注册的在上面的操作中取出了

        User clientUserByUser = userMapper.selectByPrimaryKey(clientid);
        JXResult loginResult = userService.login(clientUserByUser.getUsername(), clientUserByUser.getPassword());
        return loginResult;
        /*WxToAuthuser have = weiXinUserService.getHaveUserByWxToAuxthor("fsaf");
        System.out.println(have.getNickname());*/
    }








    @GetMapping(value = "/clientUserQQLogin")
    @ApiOperation(value = "QQ登录普通用户", notes = "QQ登录普通用户")
    public JXResult clientUserQQLogin(@RequestParam(value = "code") @ApiParam(value = "QQ返回的code") String code) throws Exception {
        //Step2：通过Authorization Code获取Access Token
        String appid ="101888327";//"wx2e9c2975c24f2f3c";//91阅读的
        String appkey = "c08e40c4d3df146855cbf65aef1e789b";//"4222adfc8b18a1361d56280c6d74017f";
        String redirect_uri="http%3A%2F%2Fclient.91yuedu.com%2F%23%2Fmain%2FbookList";
        String qqUrl= "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id="+appid+
                "&client_secret="+appkey+"&code="+code+"&redirect_uri="+redirect_uri;


        String token = HttpClientUtilsGX.doGet(qqUrl);
        //access_token=E2E8B4A5A8824DD85588A25058EFC7EB&expires_in=7776000&refresh_token=81EE3647840581074D8E87AF3BF1395F
        System.out.println("得到的token:"+token);
        //获取用户OpenID_OAuth2.0
        int access_tokenIndex = token.indexOf("access_token=");
        int and = token.indexOf("&");
        String access_token = token.substring(access_tokenIndex+13, and);

        String getOpenIdUrl="https://graph.qq.com/oauth2.0/me?access_token="+access_token;
        String callback = HttpClientUtilsGX.doGet(getOpenIdUrl);
        System.out.println("得到的callback:"+callback);//callback( {"client_id":"101888327","openid":"B55D0588AC14BBD253EFC222F900E77F"} );
        String qqJson = getQQJson(callback);
        System.out.println("方法算出的qqJson:");
        System.out.println(qqJson);
        JSONObject tokenJson = JSONObject.parseObject(qqJson);
        String openid = tokenJson.getString("openid");//授权用户唯一标识

        //检查微信中有没有这个用户,没有就创建一个client和QQ用户,并为他登录[为他登录到client!]
        WxToAuthuser haveUserByWxToAuxthor = weiXinUserService.getHaveUserByWxToAuxthor(openid);
        if (null==haveUserByWxToAuxthor){
            //用户第一次登录,可以获取用户信息了
            String userInfoUrl = "https://graph.qq.com/user/get_user_info?" +
                    "access_token=" + access_token +
                    "&oauth_consumer_key=" + appid + "&openid=" + openid;
            String userInfo = HttpClientUtilsGX.doGet(userInfoUrl);
            System.out.println(userInfo);
            JSONObject userJson = JSONObject.parseObject(userInfo);

            //client用户
            User userByRegister = new User();
            userByRegister.setNickname("QQ用户"+userJson.getString("nickname"));
            userByRegister.setUsername(userJson.getString("nickname")+System.currentTimeMillis());
            userByRegister.setPassword(userJson.getString("nickname")+"91yuedu");
            userByRegister.setSex(userJson.getString("nickname").equals("男") ? 1:2);
            //用户头像
            String headImage="";
            if (null!=userJson.getString("figureurl_2")){
                headImage=userJson.getString("figureurl_2");
            }else if (null!=userJson.getString("figureurl_1")){
                headImage=userJson.getString("figureurl_1");
            }else if (null!=userJson.getString("figureurl")){
                headImage=userJson.getString("figureurl_1");
            }else if (null!=userJson.getString("figureurl_qq_2")){
                headImage=userJson.getString("figureurl_qq_2");
            }else if (null!=userJson.getString("figureurl_qq_1")){
                headImage=userJson.getString("figureurl_qq_1");
            }
            userByRegister.setHeadImage(headImage);
            userService.register(userByRegister);//进行注册
            User clientUserByUser = userService.getClientUserByUser(userByRegister);//注册完,从数据库中获取这个用户,把他的id给微信对象
            WxToAuthuser wxToAuthuserByAdd = new WxToAuthuser();
            wxToAuthuserByAdd.setOpenid(openid);
            wxToAuthuserByAdd.setNickname("QQ用户"+userJson.getString("nickname"));
            wxToAuthuserByAdd.setAuthorid(clientUserByUser.getId());
            //保存微信对象
            weiXinUserService.addWxToAuthorUser(wxToAuthuserByAdd);
            //保存完再次执行上面这行,取得对象
            haveUserByWxToAuxthor = weiXinUserService.getHaveUserByWxToAuxthor(openid);
        }

        //走到这就是有微信用户了,拿到这个微信用户,获取authid字段,并完成登录
        Integer clientid = haveUserByWxToAuxthor.getAuthorid();
        User clientUserByUser = userMapper.selectByPrimaryKey(clientid);
        JXResult loginResult = userService.login(clientUserByUser.getUsername(), clientUserByUser.getPassword());
        return loginResult;
    }



    @GetMapping(value = "/findByUid/{uid}")
    @ApiOperation(value = "根据id查询作者接口", notes = "根据uid查询作者接口")
    public JXResult findByUid(@PathVariable @ApiParam(value = "uid") Integer uid) {
        User clientUser = userService.findByUid(uid);
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", clientUser);
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改用户信息接口", notes = "修改用户信息接口")
    public JXResult updateClientUser(@RequestBody @ApiParam(value = "用户对象") User clientUser) {
        userService.updateClientUser(clientUser);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }

    @GetMapping(value = "/judge/{username}")
    @ApiOperation(value = "判断client用户名是否可以被注册", notes = "判断client用户名是否可以被注册")
    public JXResult judgeClientUsername(@ApiParam(value = "用户名") @PathVariable String username) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", userService.judgeClientUsername(username));
    }

    @GetMapping(value = "/reminder/{author}/{bookName}/{reminderVal}/{clientUid}")
    @ApiOperation(value = "用户给作品催更", notes = "用户给作品催更")
    public JXResult Reminder(@ApiParam(value = "打赏的作者笔名") @PathVariable String author,
                             @ApiParam(value = "书名") @PathVariable String bookName,
                             @ApiParam(value = "多少金币,一块钱100币") @PathVariable Long reminderVal,
                             @ApiParam(value = "用户id") @PathVariable Integer clientUid) {
        JXResult result= userService.reminder(author,bookName,reminderVal,clientUid);
        return result;
    }

    public static String getQQJson(String qqString) {
        if (StringUtils.equals("callback", qqString.substring(0, 8))) {
            return qqString.substring(9, qqString.length() - 3);
        }
        return "{\"message\":\"返回错误值\",\"messageCont\":\"+qqString+\"}";
    }

}
