package com.sprint.mission.discodeit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String nullPointerExceptionHandler(NullPointerException exception) {

        System.out.println("값이 없습니다.");
        return exception.getMessage();
    }

    @ExceptionHandler({
            MemberRegistException.class,
            EmailRegistException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String userExceptionHandler(Model model, MemberRegistException exception) {

        System.out.println("회원가입 실패");
        model.addAttribute("exception", exception);

        return exception.getMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String nullPointerExceptionHandler(Exception exception) {

        System.out.println("오류");
        return exception.getMessage();
    }

}

