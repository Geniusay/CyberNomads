package io.geniusay.github.cache;

import io.github.geniusay.CyberNomadsApplication;
import io.github.geniusay.pojo.DO.AnswerQuestion;
import io.github.geniusay.service.FAQService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CyberNomadsApplication.class)
public class XiaochunTest {

    @Resource
    private FAQService faqService;

    @Test
    public void test() {
        faqService.addFAQ(AnswerQuestion.builder()
                        .question("123")
                        .answer("123")
                        .englishQuestion("123")
                        .englishAnswer("123")
                        .build());
    }
}
