package io.github.geniusay.service;

import io.github.geniusay.pojo.DO.AnswerQuestion;

import java.util.List;

public interface FAQService {
    List<AnswerQuestion> getFAQ();

    void addFAQ(AnswerQuestion answerQuestion);
}
