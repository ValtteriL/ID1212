package se.kth.id1212.appserv.bank.presentation.error;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * Contains all exception handling methods.
 */
@Controller
@ControllerAdvice
public class ExceptionHandlers implements ErrorController {
    public static final String ERROR_PAGE_URL = "error";
    public static final String ERROR_TYPE_KEY = "errorType";
    public static final String GENERIC_ERROR = "Operation Failed";
    public static final String HTTP_404 = "The page could not be found";
    public static final String HTTP_404_INFO = "Sorry, but there is no such page. We would like to fix this error, please tell us what you where trying to do.";
    static final String ERROR_PATH = "failure";


    /**
     * The most generic exception handler, will be used if there is not a more specific handler for the exception that
     * shall be handled.
     *
     * @return The generic error page.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception, Model model) {
        model.addAttribute(ERROR_TYPE_KEY, GENERIC_ERROR);
        return ERROR_PAGE_URL;
    }

    // This method is never called. Could that be a bug in spring? I can not
    // find any call to an appropriate method named getErrorPath anywhere in
    // spring's source code. The path is instead set in application.properties,
    // in the property server.error.path. For this to work, there
    // must also be the property setting server.error.whitelabel.enabled=false.
    @Override
    public String getErrorPath() {
        return "/" + ERROR_PATH;
    }
}
