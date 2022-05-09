package pl.adibak.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ModelAttribute
    @ExceptionHandler(value = CommonException.class)
    ModelAndView handleException(CommonException e) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        if (e.getType() != null) {
            modelAndView.addObject("message", e.getType().getMessage());
        }
        return modelAndView;
    }

}
