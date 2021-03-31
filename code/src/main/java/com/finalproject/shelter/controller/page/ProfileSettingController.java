package com.finalproject.shelter.controller.page;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.shelter.model.entity.Account;
import com.finalproject.shelter.model.entity.CurrentUser;
import com.finalproject.shelter.repository.AccountRepository;
import com.finalproject.shelter.service.AccountService;
import com.finalproject.shelter.settings.form.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

@Controller
//@RequestMapping("settings")
@RequiredArgsConstructor
public class ProfileSettingController {
    public static final String SETTINGS_PASSWORD_VIEW_NAME = "account/password";
    public static final String SETTINGS_PASSWORD_URL = "/settings/password";
    public static final String SETTINGS_ACCOUNT_VIEW_NAME = "account/account";
    public static final String SETTINGS_ACCOUNT_URL = "/settings/account";
    public static final String SETTING_DELETE_URL = "settings/delete";
    public static final String SETTING_DELETE_VIEW_NAME = "account/delete";
    public static final String REDIRECT_HOME = "redirect:"+"/main";
    public static final String HOME = "/main";
    private final AccountService accountService;
    private final IdentityFormValidator identityFormValidator;
    private final ModelMapper modelMapper;

    @InitBinder("passwordForm")
    public void passwordFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new PasswordFormValidator());
    }

    @InitBinder("identityForm")
    public void identityFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(identityFormValidator);
    }

    @InitBinder("deleteForm")
    public void deleteFormInitBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(new DeleteFormValidator());
    }

    @GetMapping(SETTINGS_PASSWORD_URL)
    public String updatePasswordForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute("passwordForm",new PasswordForm());
        return SETTINGS_PASSWORD_VIEW_NAME;
    }

    @PostMapping(SETTINGS_PASSWORD_URL)
    public String updatePassword(@CurrentUser Account account, @Valid PasswordForm passwordForm, Errors errors,
                                 Model model, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_PASSWORD_VIEW_NAME;
        }

        accountService.updatePassword(account, passwordForm);
        redirectAttributes.addFlashAttribute("message", "패스워드를 변경하였습니다.");
        return REDIRECT_HOME;
    }
    @GetMapping(SETTINGS_ACCOUNT_URL) //닉네임 수정 버튼
    public String updateAccountForm(@CurrentUser Account account, Model model) {
        model.addAttribute(account);
        model.addAttribute("identityForm",new IdentityForm(account.getIdentity())); //이부분이 중요
        return SETTINGS_ACCOUNT_VIEW_NAME;
    }

    @PostMapping(SETTINGS_ACCOUNT_URL)  //닉네임 수정 완료
    public String updateAccount(@CurrentUser Account account, @Valid IdentityForm identityForm, Errors errors,
                                Model model, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTINGS_ACCOUNT_VIEW_NAME;
        }
        accountService.updateIdentity(account, identityForm.getIdentity());
        attributes.addFlashAttribute("message", "닉네임을 수정했습니다.");
        return REDIRECT_HOME;
    }

    @GetMapping(SETTING_DELETE_URL)
    public String deleteAccountForm(@CurrentUser Account account, Model model){
        model.addAttribute(account);
        model.addAttribute("deleteForm",new DeleteForm());
        return SETTING_DELETE_VIEW_NAME;
    }
    @PostMapping(SETTING_DELETE_URL)
    public String deleteAccount(@CurrentUser Account account, @Valid DeleteForm deleteForm, Errors errors,
                                 Model model, RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            model.addAttribute(account);
            return SETTING_DELETE_VIEW_NAME;
        }
        accountService.deleteAccount(account);

        redirectAttributes.addFlashAttribute("message", "회원을 탈퇴하였습니다.");
        return REDIRECT_HOME;
    }

}
