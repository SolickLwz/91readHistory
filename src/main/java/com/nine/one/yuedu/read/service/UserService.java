package com.nine.one.yuedu.read.service;

import com.nine.one.yuedu.read.config.JXResult;
import com.nine.one.yuedu.read.entity.User;

import java.util.Map;

public interface UserService {
    JXResult login(String username, String password);

    JXResult judgment(Integer bookId, Integer chapterId, Integer clientUserId,String isTruee);

    void register(User userByRegister);

    User getClientUserByUser(User userByRegister);

    User findByUid(Integer uid);

    void updateClientUser(User clientUser);

    boolean judgeClientUsername(String username);

    JXResult reminder(String author, String bookName, Long reminderVal, Integer clientUid);
}
