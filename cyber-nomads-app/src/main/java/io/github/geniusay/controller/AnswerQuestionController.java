package io.github.geniusay.controller;

import io.github.common.web.Result;
import io.github.geniusay.service.FAQService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/faq")
public class AnswerQuestionController {

    @Resource
    private FAQService faqService;

    @GetMapping("/get")
    public Result<?> getFAQ(){
        return Result.success(faqService.getFAQ());
    }
}
