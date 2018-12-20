package se.kth.id1212.appserv.conversion.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import se.kth.id1212.appserv.conversion.Integration.*;
import se.kth.id1212.appserv.conversion.Model.*;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;

// register user

@Controller
@Scope("session")
public class AccountController {

    private static final String REGISTER_FORM_OBJ_NAME = "accountCreateForm";
    static final String LOGIN_PAGE_URL = "login";
    static final String REGISTER_PAGE_URL = "register";

    @Autowired
    private MyAccountService service;

    // autowire this controller
    @Autowired
    public AccountController() {
    }


    // GET register page accessed
    @GetMapping("/" + REGISTER_PAGE_URL)
    public String getRegister(AccountCreateForm accountCreateForm, Model model) {
        return REGISTER_PAGE_URL;
    }

    // POST register page submitted
    @PostMapping("/" + REGISTER_PAGE_URL)
    public String postRegister(@ModelAttribute(REGISTER_FORM_OBJ_NAME) @Valid AccountCreateForm accountCreateForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return showRegisterPage(model, accountCreateForm);
        }

        User acc = service.findAccount(accountCreateForm.getUsername());
        if (acc != null) {
            // username already exists!
            bindingResult.rejectValue("username", "error.user", "Username already exists.");
            return showRegisterPage(model, accountCreateForm);
        }

        // create new account
        service.newAcc(accountCreateForm.getUsername(), accountCreateForm.getPassword());
        return "redirect:" + LOGIN_PAGE_URL;
    }

    private String showRegisterPage(Model model, AccountCreateForm accountCreateForm) {
        if (accountCreateForm != null) {
            model.addAttribute(REGISTER_FORM_OBJ_NAME, accountCreateForm);
        }
        return REGISTER_PAGE_URL;
    }
}