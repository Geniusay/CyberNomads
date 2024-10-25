package io.github.geniusay.deploy;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.github.geniusay.CyberNomadsApplication;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Scanner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CyberNomadsApplication.class)
public class JasyptUtil {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Value("${spring.datasource.username}")
    private String pwd;


    /**
     * 加密，输入空退出
     */
    @Test
    public void encrypt(){
        Scanner scanner = new Scanner(System.in);
        String temp;
        System.out.println("输入加密密匙:");
        while (StringUtils.isBlank(temp=scanner.nextLine())){
            System.out.println("加密后："+stringEncryptor.encrypt(temp));
            System.out.println("输入加密密匙：");
        }
    }

    /**
     * 解密，输入空退出
     */
    @Test
    public void decrypt(){
        Scanner scanner = new Scanner(System.in);
        String temp;
        System.out.println("输入解密密匙:");
        while (StringUtils.isBlank(temp=scanner.nextLine())){
            System.out.println("解密后："+stringEncryptor.decrypt(temp));
            System.out.println("输入解密密匙：");
        }
    }
}
