package com.nine.one.yuedu.read.service.impl;

import com.nine.one.yuedu.read.config.ApiConstant;
import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.*;
import com.nine.one.yuedu.read.mapper.*;
import com.nine.one.yuedu.read.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:李王柱
 * 2020/11/11
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ChapterInfoMapper chapterInfoMapper;
    @Autowired
    private BuyChapterRecordMapper buyChapterRecordMapper;
    @Autowired
    private ReminderMapper reminderMapper;
    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public JXResult login(String username, String password) {

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", username).andEqualTo("password", password);
        User user = userMapper.selectOneByExample(example);
        if (null != user && user.getStatus() == 1) {
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("flag", true);
            resultMap.put("clientUser", user);
            return new JXResult(true, ApiConstant.StatusCode.OK, "登录成功", resultMap);
        }
        return new JXResult(false, ApiConstant.StatusCode.LOGINERROR, "登录失败,用户名或密码错误...");
    }

    @Override
    public JXResult judgment(Integer bookId, Integer chapterId, Integer clientUserId, String isTrue) {
        //1.直接返回,2.章节收费xx金币,是否继续? 3.金币余额不足,是否前往充值?
        //获取章节对象,如果这章节免费直接返回;如果用户设置了默认扣费,并且余额充足,在扣费后返回;
        ChapterInfo chapterInfo = chapterInfoMapper.selectOneByBookIdAndChapterId(bookId, chapterId);
        Byte isFree = chapterInfo.getIsFree();
        if (1 == isFree) {
            return new JXResult(true, ApiConstant.StatusCode.OK, "成功");
        }
        //到这里就是收费章节了
        if (0==clientUserId){
            return new JXResult(false, ApiConstant.StatusCode.LOGINERROR, "游客您好,此章节需要登录后阅读..");
        }

        BuyChapterRecord buyChapterRecord = new BuyChapterRecord();
        User user = userMapper.selectByPrimaryKey(clientUserId);
        Long price = getPrice(chapterInfo.getWords());
        //如果用户购买过章节,直接返回
        buyChapterRecord.setBookid(bookId);
        buyChapterRecord.setChapterid(chapterId);
        buyChapterRecord.setUserid(clientUserId);
        if (null != buyChapterRecordMapper.selectOne(buyChapterRecord)) {
            return new JXResult(true, ApiConstant.StatusCode.OK, "成功");
        }
        //走到这就是没买过
        //只有在前端没有确认的情况,并且用户没有设置自动扣费
        if (StringUtils.equals("no", user.getAuto()) && StringUtils.equals("no", isTrue)) {
            return new JXResult(false, ApiConstant.StatusCode.ACCESSERROR, "本章价格：" + price + "阅币","账户剩余："+user.getBalance()+"阅币");
        }
        //准备扣费
        if (user.getBalance() < price) {//如果用户余额不足,提示充值
            return new JXResult(false, ApiConstant.StatusCode.ERROR, "阅币不足..是否前往充值?");
        }
        buyChapterRecord.setBuytime(new Date());
        buyChapterRecordMapper.insertSelective(buyChapterRecord);
        Long thatPrice = user.getBalance() - price;
        user.setBalance(thatPrice);
        userMapper.updateByPrimaryKeySelective(user);
        return new JXResult(true, ApiConstant.StatusCode.OK, "成功");
    }

    @Override
    public void register(User userByRegister) {
        userByRegister.setCreateTime(new Date());
        userByRegister.setUpdateTime(new Date());
        userByRegister.setStatus(1);
        userMapper.insertSelective(userByRegister);
    }

    @Override
    public User getClientUserByUser(User userByRegister) {
        System.out.println("controller取到的登录名:"+userByRegister.getUsername());
        System.out.println("controller取到的登录密码:"+userByRegister.getPassword());
        /*User user = userMapper.selectOne(userByRegister);
        System.out.println("取到的controller用户对象:"+user);*/

       /* User userByTest = new User();
        userByTest.setUsername(userByRegister.getUsername());
        userByTest.setPassword(userByRegister.getPassword());*/
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username",userByRegister.getUsername()).andEqualTo("password",userByRegister.getPassword());
        User user2 = userMapper.selectOneByExample(example);
        System.out.println("取到的账号密码用户:"+user2.getId());
        return user2;
    }

    @Override
    public User findByUid(Integer uid) {
        return userMapper.selectByPrimaryKey(uid);
    }

    @Override
    public void updateClientUser(User clientUser) {
        clientUser.setUpdateTime(new Date());
        userMapper.updateByPrimaryKeySelective(clientUser);
    }

    @Override
    public boolean judgeClientUsername(String username) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username", username);
        List<User> clientUsers = userMapper.selectByExample(example);
        if (clientUsers != null && clientUsers.size() > 0) {
            return false;
        }
        return true;
    }

    @Override
    public JXResult reminder(String author, String bookName, Long reminderVal, Integer clientUid) {
        //取出用户,如果用户余额低于打赏金额:
        User user = userMapper.selectByPrimaryKey(clientUid);
        Long balance = user.getBalance();
        if (balance<reminderVal){
            return new JXResult(false, ApiConstant.StatusCode.ACCESSERROR,"您账户的余量是:"+balance+",不足以支出..");
        }
        //余额足够,生成催更记录
        Reminder reminderByInsert = new Reminder();
        reminderByInsert.setAuthor(author);
        reminderByInsert.setBookname(bookName);
        reminderByInsert.setUid(clientUid);
        reminderByInsert.setReminderval(reminderVal);
        reminderByInsert.setCreagetime(new Date());
        reminderMapper.insertSelective(reminderByInsert);
        //从用户余额中扣除,
        user.setBalance(balance-reminderVal);
        userMapper.updateByPrimaryKeySelective(user);

        //生成一个管理后台通知对象,通知作者,这部作品得到了打赏催更
        Notice notice = new Notice();
        notice.setAuthornickname(author);
        notice.setCraeatetime(new Date());
        notice.setHead("读者催更");
        notice.setContent("大大您好、您的《"+bookName+"》收到了用户的催更~###您今天更新的章节越多,能得到的奖励也越多!###请加油,回应读者的支持吧!");
        noticeMapper.insertSelective(notice);

        return new JXResult(true, ApiConstant.StatusCode.OK,"成功!感谢您对"+bookName+"的支持!"+author+"将会收到您的催更~");
    }

    public static Long getPrice(Integer chapterWords) {
        if (chapterWords <= 500) {
            return 15L;
        }
        if (chapterWords < 2000) {
            return 30L;
        }
        if (chapterWords < 3000) {
            return 60L;
        }
        if (chapterWords < 4000) {
            return 90L;
        }
        if (chapterWords < 5000) {
            return 120L;
        }
        return chapterWords / 100 * 3L;
    }
}
