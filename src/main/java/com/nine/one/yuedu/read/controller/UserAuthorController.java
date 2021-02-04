package com.nine.one.yuedu.read.controller;

import com.alibaba.fastjson.JSONObject;
import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.*;
import com.nine.one.yuedu.read.mapper.CsWxUsersMapper;
import com.nine.one.yuedu.read.service.QQUserService;
import com.nine.one.yuedu.read.service.RedisService;
import com.nine.one.yuedu.read.service.UserAuthorService;
import com.nine.one.yuedu.read.service.WeiXinUserService;
import com.nine.one.yuedu.read.utils.AliyunOSSUtil;
import com.nine.one.yuedu.read.utils.GX.HttpClientUtilsGX;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "管理员和作者接口", value = "管理员和作者接口")
@RequestMapping(value = "/91yuedu/userAuthor")
public class UserAuthorController {

    @Resource(name = "userAuthorService")
    private UserAuthorService userAuthorService;

    @Autowired
    private AliyunOSSUtil aliyunOSSUtil;

    @Autowired
    private RedisService redisService;

    @Autowired
    private WeiXinUserService weiXinUserService;

    @Autowired
    private QQUserService qqUserService;

    @PostMapping(value = "/head/upload")
    @ApiOperation(value = "上传头像", notes = "上传头像")
    public JXResult uploadHead(@RequestParam("file") @ApiParam(value = "上传的文件") MultipartFile uploadFile) {
        AliyunOSSUtil.FileUploadResult uploadResult = aliyunOSSUtil.upload(uploadFile);
        return new JXResult(true, ApiConstant.StatusCode.OK, "上传成功", uploadResult);
    }


    @PostMapping(value = "/add")
    @ApiOperation(value = "作者注册", notes = "作者注册")
    public JXResult addUserAuthor(@RequestBody @ApiParam(value = "作者对象", required = true) UserAuthor userAuthor) {

        userAuthorService.register(userAuthor);
        return new JXResult(true, ApiConstant.StatusCode.OK, "注册成功");
    }

    @DeleteMapping(value = "/del/{id}")
    @ApiOperation(value = "根据id删除作者接口", notes = "根据id删除作者接口")
    public JXResult delUserAuthorById(@PathVariable @ApiParam(value = "id") Integer id) {
        userAuthorService.delUserAuthorById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "删除成功");
    }

    @PutMapping(value = "/update")
    @ApiOperation(value = "修改作者信息接口", notes = "修改作者信息接口")
    public JXResult updateUserAuthor(@RequestBody @ApiParam(value = "作者对象", required = true) UserAuthor userAuthor) {
        userAuthorService.updateUserAuthor(userAuthor);
        return new JXResult(true, ApiConstant.StatusCode.OK, "修改成功");
    }

    @GetMapping(value = "/find/{id}")
    @ApiOperation(value = "根据id查询作者接口", notes = "根据id查询作者接口")
    public JXResult findById(@PathVariable @ApiParam(value = "id") Integer id) {
        UserAuthor userAuthor = userAuthorService.findById(id);
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", userAuthor);
    }

    @GetMapping(value = "/list/{pageIndex}/{pageSize}")
    @ApiOperation(value = "分页获取作者列表", notes = "分页获取作者列表")
    public JXResult getUserAuthorsByPage(
            @PathVariable @ApiParam(value = "第几页") Integer pageIndex
            , @PathVariable @ApiParam(value = "每页显示数量") Integer pageSize
            , @RequestParam(value = "nikeName", required = false) @ApiParam(value = "笔名") String nikeName) {

        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", userAuthorService.getUserAuthorsByPage(pageIndex, pageSize, nikeName));
    }

    @PostMapping(value = "/login")
    @ApiOperation(value = "作者或者管理员登录接口", notes = "作者或者管理员登录接口")
    public JXResult login(@RequestParam(value = "username") @ApiParam(value = "用户名", required = true) String username,
                          @RequestParam(value = "password") @ApiParam(value = "密码", required = true) String password) {
        Map<String, Object> login = userAuthorService.login(username, password);
        boolean flag = (boolean) login.get("flag");
        if (flag) {
            return new JXResult(true, ApiConstant.StatusCode.OK, "登录成功", login);
        }
        return new JXResult(false, ApiConstant.StatusCode.LOGINERROR, "登录失败,用户名或密码错误...");
    }

    @GetMapping(value = "/judge/{username}")
    @ApiOperation(value = "判断用户名是否可以被注册", notes = "判断用户名是否可以被注册")
    public JXResult judgeUsername(@ApiParam(value = "用户名") @PathVariable String username) {
        return new JXResult(true, ApiConstant.StatusCode.OK, "请求成功", userAuthorService.judgeUsername(username));
    }

    @PostMapping(value = "/smsCaptcha")
    @ApiOperation(value = "短信注册和登录接口", notes = "短信注册和登录接口")
    public JXResult smsCaptcha(HttpServletRequest request, @RequestParam(value = "phoneNum") @ApiParam(value = "手机号", required = true) String phoneNum,
                               @RequestParam(value = "smsCode") @ApiParam(value = "短信内容", required = true) String smsCode) {
        String message = "登录成功";

        //验证短信是否正确
        /*String s = redisService.get(phoneNum);*//*phoneNum, messageCode;*/
        /*String s=request.getSession().getAttribute(phoneNum).toString();*/
        /*Cookie[] cookies = request.getCookies();
        String msgcode="";
        for(Cookie cookie : cookies){
            if(cookie.getName().equals(phoneNum)){
                String loginInfo = cookie.getValue();
                System.out.println("拿到了cookie:"+loginInfo);
                msgcode=loginInfo;
                *//*String username = loginInfo.split(",")[0];
                String password = loginInfo.split(",")[1];
                request.setAttribute("username", username);
                request.setAttribute("password", password);*//*
            }
        }*/
        /*if (!StringUtils.equals(msgcode,smsCode)){
            return new JXResult(false,ApiConstant.StatusCode.LOGINERROR,"短信验证码错误!");
        }*/
        //如果没有用户,并且验证码正确:
        if (null == userAuthorService.getUserByPhone(phoneNum)) {
            System.out.println("还没有这个用户,创建");
            UserAuthor userAuthor = new UserAuthor();
            userAuthor.setBankName("");
            userAuthor.setBankNum("");
            userAuthor.setCardId("");
            userAuthor.setEmile("");
            userAuthor.setHeadImage("");
            userAuthor.setNickname("新建手机用户"+phoneNum);
            userAuthor.setPassword(phoneNum + smsCode);//用户密码
            userAuthor.setPhoneNum(phoneNum);
            userAuthor.setQqNum("");
            userAuthor.setRealName("");
            userAuthor.setSex(0);
            userAuthor.setUsername(phoneNum);//用户名是手机号

            userAuthorService.register(userAuthor);//进行注册
            message = "注册成功!您的密码是:" + phoneNum + smsCode + "请尽快完善作者信息";
        }

        //根据手机号拿取用户信息
        UserAuthor phoneUser = userAuthorService.getUserByPhone(phoneNum);
        String username = phoneUser.getUsername();
        System.out.println("拿到的用户是:" + username);
        String password = phoneUser.getPassword();

        //如果已经有了手机用户,就直接登录
        Map<String, Object> login = userAuthorService.login(username, password);
        boolean flag = (boolean) login.get("flag");
        if (flag) {
            return new JXResult(true, ApiConstant.StatusCode.OK, message, login);
        }
        return new JXResult(false, ApiConstant.StatusCode.LOGINERROR, "登录失败,用户名或密码错误...");
    }


    @PostMapping(value = "/messageCode")
    public @ResponseBody
    JXResult messageCode(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam(value = "phoneNum", required = true) String phoneNum) throws Exception {
        System.out.println("拿到的手机号是:" + phoneNum);//打印
        Map<String, Object> retMap = new HashMap<String, Object>();

        //生成一个随机数字
        String messageCode = this.getRandomNumber(4);

        //发送短信的内容=短信签名+短信正文(包含：一个随机数字)
        String content = "【凯信通】您的验证码是：" + messageCode;
        System.out.println(content);//打印
        //准备发送短信的参数
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("appkey", "91ee6c9e111780bd926f3e1e5c269be8");
        paramMap.put("mobile", phoneNum);
        paramMap.put("content", content);

        //发送短信：调用互联网接口发送
//        String jsonString = HttpClientUtils.doPost("https://way.jd.com/kaixintong/kaixintong", paramMap);
       // String jsonString = HttpClientUtilsGX.doPost("https://way.jd.com/kaixintong/kaixintong", paramMap);
        String jsonString = "{\"code\":\"10000\",\"charge\":false,\"remain\":0,\"msg\":\"查询成功\",\"result\":\"<?xml version=\\\"1.0\\\" encoding=\\\"utf-8\\\" ?><returnsms>\\n <returnstatus>Success</returnstatus>\\n <message>ok</message>\\n <remainpoint>-616780</remainpoint>\\n <taskID>78950259</taskID>\\n <successCounts>1</successCounts></returnsms>\"}";

        //解析json格式的字符串
        //将json字符串转换为json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        //获取通信标识
        String code = jsonObject.getString("code");
        System.out.println("拿到的code是:" + code);
        System.out.println("扣费吗:" + jsonObject.getBoolean("charge"));
        System.out.println("剩余次数:" + jsonObject.getLong("remain"));
        System.out.println("返回信息:" + jsonObject.getString("msg"));

        //判断通信结果
        if (StringUtils.equals("10000", code)) {

            //获取result的值,result是一个xml格式的字符串
            String result = jsonObject.getString("result");

            //使用dom4j+xpath解析xml格式的字符串
            //将xml格式的字符串转换为dom对象
            Document document = DocumentHelper.parseText(result);

            //获取returnstatus
            Node node = document.selectSingleNode("//returnstatus");

            //获取节点对象的文本内容
            String returnStatus = node.getText();

            //判断短信是否发送成功
            if (StringUtils.equals(returnStatus, "Success")) {

                //将生成的验证码存放到redis缓存中
                /*redisService.put(phoneNum, messageCode);*/
               /* request.getSession().setAttribute(phoneNum, messageCode);
                System.out.println("session中保存了:"+request.getSession().getAttribute(phoneNum));*/
                Cookie userCookie = new Cookie(phoneNum, messageCode);
                userCookie.setMaxAge(10);   //存活期为一个月 30*24*60*60   10就是一分钟
                userCookie.setPath("/");
                response.addCookie(userCookie);
                return new JXResult(true, ApiConstant.StatusCode.OK, messageCode);
            } else {
                return new JXResult(false, ApiConstant.StatusCode.REMOTEERROR, "短信发送失败，请稍后重试...");
            }

        } else {

            return new JXResult(false, ApiConstant.StatusCode.ERROR, jsonObject.getString("msg"));
        }

    }

    @GetMapping(value = "/AuthweiXin")
    @ApiOperation(value = "微信登录作者或者管理员接口", notes = "微信登录作者或者管理员接口")
    public JXResult AuthweiXin(@RequestParam(value = "code") @ApiParam(value = "微信返回的code", required = true) String code) throws Exception {
        String appid ="wx9013ed93344e6cbe";//"wx2e9c2975c24f2f3c";//报联文学的
        String secret = "ca110c2b36cf7ba789dcca6825268393";//"4222adfc8b18a1361d56280c6d74017f";
        System.out.println("接收到的code是:" + code);

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

        System.out.println(token);
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
        System.out.println(userInfo);
        JSONObject userJson = JSONObject.parseObject(userInfo);

        //检查微信中有没有这个用户,没有就创建一个作者和微信用户,并为他登录
        WxToAuthuser wxToAuthuser= weiXinUserService.getAuthorUserByUnionid(userJson.getString("unionid"));
       if (null==wxToAuthuser){
           System.out.println("还没有这个用户,创建");
           UserAuthor userAuthor = new UserAuthor();
           userAuthor.setBankName("");
           userAuthor.setBankNum("");
           userAuthor.setCardId("");
           userAuthor.setEmile("");
           userAuthor.setHeadImage("");
           userAuthor.setNickname("微信用户"+userJson.getString("nickname"));
           userAuthor.setPassword(userJson.getString("nickname") + "91yuedu");//用户密码
           userAuthor.setPhoneNum("");
           userAuthor.setQqNum("");
           userAuthor.setRealName("");
           userAuthor.setSex(0);
           userAuthor.setUsername(userJson.getString("nickname"));//用户名是微信名

           userAuthorService.register(userAuthor);//进行注册
           System.out.println(userJson.getString("nickname"));
           //注册完,从数据库中获取这个作者,把他的id给微信对象
          /* UserAuthor userAuthor1 = userAuthorService.getAuthUserByOneAuthUser(userAuthor);*/
           UserAuthor userAuthor1 = userAuthorService.getAuthUserByUsernameAndPassword(userAuthor.getUsername(),userAuthor.getPassword());
           System.out.println(userAuthor1.getNickname());
           wxToAuthuser = new WxToAuthuser();
           wxToAuthuser.setAppid(appid);

           wxToAuthuser.setAuthorid(userAuthor1.getId());
           wxToAuthuser.setNickname(userAuthor1.getNickname());
           wxToAuthuser.setOpenid(userJson.getString("openid"));
           wxToAuthuser.setUnionid(userJson.getString("unionid"));
           //保存微信对象
           int flag=weiXinUserService.addWxToAuthorUser(wxToAuthuser);
       }

       //走到这就是有微信用户了,拿到这个微信用户,获取authid字段,并完成登录
        String message = "微信登录成功";
        Integer authorid = wxToAuthuser.getAuthorid();
        UserAuthor byId = userAuthorService.findById(authorid);
        Map<String, Object> login = userAuthorService.login(byId.getUsername(), byId.getPassword());
        boolean flag = (boolean) login.get("flag");
        if (flag) {
            return new JXResult(true, ApiConstant.StatusCode.OK, message, login);
        }
        return new JXResult(false, ApiConstant.StatusCode.LOGINERROR, "登录失败,用户名或密码错误...");
        /*WxToAuthuser have = weiXinUserService.getHaveUserByWxToAuxthor("fsaf");
        System.out.println(have.getNickname());*/
    }

    @GetMapping(value = "/AuthQQ")
    @ApiOperation(value = "QQ登录作者或者管理员接口", notes = "QQ登录作者或者管理员接口")
    public JXResult AuthQQ(@RequestParam(value = "code") @ApiParam(value = "QQ返回的code", required = true) String code) throws Exception {
        String client_id = "101888327";//报联文学的  101888327   阅文的 wx2e9c2975c24f2f3c
        String client_secret = "c08e40c4d3df146855cbf65aef1e789b";//报联的c08e40c4d3df146855cbf65aef1e789b  阅文4222adfc8b18a1361d56280c6d74017f
        String redirect_uri = "http%3A%2F%2Fadmin.91yuedu.com%2F%23%2Flogin";//阅文https%3A%2F%2Fptlogin.qidian.com%2Flogin%2Fqqconnectcallback%3Freturnurl%3Dhttps%253A%252F%252Fwrite.qq.com%253Fartidx%253D0%26appid%3D34%26areaid%3D1006%26jumpdm%3Dyuewen%26popup%3D1%26ajaxdm%3Dyuewen%26target%3Dtop%26ticket%3D1%26ish5%3D0%26auto%3D1%26autotime%3D7
        System.out.println("接收到的code是:" + code);//打印code

        //通过client_id和client_secret获取token
        String url = "https://graph.qq.com/oauth2.0/token?" + "grant_type=authorization_code" +
                "client_id=" + client_id + "&client_secret=" + client_secret + "&code=" + code + "&redirect_uri=" + redirect_uri;
        //String token = HttpClientUtilsGX.doGet(url);
        String token="access_token=FE04************************CCE2&expires_in=7776000&refresh_token=88E4************************BE14";
        //从返回数据中拿到access_token
        int access_tokenIndex = token.indexOf("access_token=");
        int and = token.indexOf("&");
        String access_token = token.substring(access_tokenIndex, and);
        System.out.println(access_token);
        /**/

        //这一步获取openid

        String getopenidUrl="https://graph.qq.com/oauth2.0/me?access_token="+access_token;
        //String qqResul = HttpClientUtilsGX.doGet(getopenidUrl);
        String qqResul="callback( {\"client_id\":\"YOUR_APPID\",\"openid\":\"YOUR_OPENID\"} )";
        String qqJson = getQQJson(qqResul);

        System.out.println(qqJson);
        JSONObject tokenJson = JSONObject.parseObject(qqJson);
        String openid = tokenJson.getString("openid");//授权用户唯一标识

        //可以获取用户信息了
        String userInfoUrl = "https://graph.qq.com/user/get_user_info?" +
                "access_token=" + access_token +
                "&oauth_consumer_key=" + client_id + "&openid=" + openid;
        String userInfo = HttpClientUtilsGX.doGet(userInfoUrl);
        String okString="{\n" +
                "        \"ret\":0,\n" +//0: 正确返回
                "        \"msg\":\"\",\n" +//如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码。
                "        \"nickname\":\"Peter\",\n" +//用户在QQ空间的昵称。
                "        \"figureurl\":\"http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/30\",\n" +//大小为30×30像素的QQ空间头像URL。
                "        \"figureurl_1\":\"http://qzapp.qlogo.cn/qzapp/111111/942FEA70050EEAFBD4DCE2C1FC775E56/50\",\n" +
                "        \"figureurl_2\":\"http://api.baiwancangshu.com/Uploads/books/20180331/170925nwhv970k.jpg\",\n" +
                "        \"figureurl_qq_1\":\"http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/40\",\n" +
                "        \"figureurl_qq_2\":\"http://q.qlogo.cn/qqapp/100312990/DE1931D5330620DBD07FB4A5422917B6/100\",\n" +
                "        \"gender\":\"男\",\n" +//性别。 如果获取不到则默认返回"男"
                "        \"is_yellow_vip\":\"1\",\n" +
                "        \"vip\":\"1\",\n" +
                "        \"yellow_vip_level\":\"7\",\n" +
                "        \"level\":\"7\",\n" +
                "        \"is_yellow_year_vip\":\"1\"\n" +
                "        }";

        JSONObject userJson = JSONObject.parseObject(okString);//userInfo
        //检查QQ中有没有这个用户,没有就创建一个作者和QQ用户,并为他登录
        QqToAuthuser qqToAuthuser= qqUserService.getAuthorUserByOpenid(userJson.getString("openid"));


        if (null==qqToAuthuser){
            System.out.println("还没有这个用户,创建");
            UserAuthor userAuthor = new UserAuthor();
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
            userAuthor.setBankName("");
            userAuthor.setBankNum("");
            userAuthor.setCardId("");
            userAuthor.setEmile("");
            userAuthor.setHeadImage(headImage);
            userAuthor.setNickname("QQ用户"+userJson.getString("nickname"));
            userAuthor.setPassword(userJson.getString("nickname") + "91yuedu");//用户密码
            userAuthor.setPhoneNum("");
            userAuthor.setQqNum("");
            userAuthor.setRealName("");
            String gender = userJson.getString("gender");
            int sex = 1;
            switch (gender) {
                case "男":
                    break;
                case "女":
                    sex = 2;
                    break;
            }
            userAuthor.setSex(sex);
            userAuthor.setUsername(userJson.getString("nickname"));//用户名是QQ名

            userAuthorService.register(userAuthor);//进行注册
            System.out.println(userJson.getString("nickname"));
            //注册完,从数据库中获取这个作者,把他的id给QQ对象
            UserAuthor userAuthor1 = userAuthorService.getAuthUserByUsernameAndPassword(userAuthor.getUsername(),userAuthor.getPassword());
            System.out.println(userAuthor1.getNickname());
            qqToAuthuser = new QqToAuthuser();

            qqToAuthuser.setAuthorid(userAuthor1.getId());
            qqToAuthuser.setNickname(userAuthor1.getNickname());
            qqToAuthuser.setClientid(client_id);
            qqToAuthuser.setOpenid(openid);
            //保存QQ对象
            int flag=qqUserService.addQQToAuthorUser(qqToAuthuser);
        }

        //走到这就是有微信用户了,拿到这个微信用户,获取authid字段,并完成登录
        String message = "QQ登录成功";
        Integer authorid = qqToAuthuser.getAuthorid();
        UserAuthor byId = userAuthorService.findById(authorid);
        Map<String, Object> login = userAuthorService.login(byId.getUsername(), byId.getPassword());
        boolean flag = (boolean) login.get("flag");
        if (flag) {
            return new JXResult(true, ApiConstant.StatusCode.OK, message, login);
        }
        return new JXResult(false, ApiConstant.StatusCode.LOGINERROR, "登录失败,用户名或密码错误...");


    }

    @GetMapping(value = "/AuthWeiBo")
    @ApiOperation(value = "微博登录作者或者管理员接口", notes = "微博登录作者或者管理员接口")
    public void AuthWeiBo(@RequestParam(value = "code") @ApiParam(value = "微博返回的code", required = true) String code) throws Exception {
        String Key = "2200920757";//报联文学的
        String Secret = "7bd99332d17376f05d76b5fcf58cc2c3";
        String redirect_uri = "";
        System.out.println("接收到的code是:" + code);//打印code


        //通过client_id和client_secret获取token
        String url = "https://api.weibo.com/oauth2/access_token?" +
                "client_id=" + Key +
                "&client_secret=" + Secret + "&grant_type=authorization_code&" +
                "redirect_uri=" + redirect_uri + "&code=" + code;
        String token = HttpClientUtilsGX.doGet(url);
        /*{
    "access_token": "SlAV32hkKG",
    "remind_in": 3600,
    "expires_in": 3600
}*/
        //从返回数据中拿到access_token
        JSONObject jsonObject = JSONObject.parseObject(token);
        String access_token = jsonObject.getString("access_token");
        System.out.println(access_token);//打印


        //这一步根据access_token获取用户基本信息
        String userInfoUrl = "https://api.weibo.com/2/users/show.json?access_token=" + access_token;
        String userInfo = HttpClientUtilsGX.doGet(userInfoUrl);
        JSONObject userInfoJson = JSONObject.parseObject(userInfo);
        String id = userInfoJson.getString("id");
        String screen_name = userInfoJson.getString("screen_name");
        String head_image = "";
        if (userInfoJson.getString("avatar_hd").length() > 1) {
            head_image = userInfoJson.getString("avatar_hd");
        } else if (userInfoJson.getString("avatar_large").length() > 1) {
            head_image = userInfoJson.getString("avatar_large");
        } else if (userInfoJson.getString("profile_image_url").length() > 1) {
            head_image = userInfoJson.getString("profile_image_url");
        }

        //从微博保存的用户中,获取用户,如果不存在,就新建一个微博用户,并注册一个作者

        //如果微博用户存在,就根据微博用户信息,获取作者的id,根据作者id拿取作者对象

        //走到这里,就一定是有一个作者对象了,用这个作者对象给用户完成登录

        //从用户信息查询是否存在这个用户
        //如果是第一次,就为他注册AuthUser并登录
        String gender = userInfoJson.getString("gender");
        int sex = 1;
        switch (gender) {
            case "m":
                return;
            case "f":
                sex = 2;
                return;
        }
        //如果已经有了这个用户,就直接登录

    }

    private String getRandomNumber(int count) {
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < count; i++) {
            int index = (int) Math.round(Math.random() * 9);
            String number = arr[index];
            sb.append(number);
        }

        return sb.toString();
    }

    public static String getQQJson(String qqString) {
        if (StringUtils.equals("callback", qqString.substring(0, 8))) {
            return qqString.substring(9, qqString.length() - 3);
        }
        return "{\"message\":\"返回错误值\",\"messageCont\":\"+qqString+\"}";
    }
}
