package com.mychaincode.server;

import com.mychaincode.server.utils.EmailUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailUtilTest {
    @Autowired
    EmailUtil emailUtil;

    @Test
    public void sendTest() throws MailException{
        emailUtil.sendMail("1947113120@qq.com","测试邮件","收到了吗？");
    }
}
