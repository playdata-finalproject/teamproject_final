package com.finalproject.shelter.businessLayer.settings.form.signup;


import com.finalproject.shelter.domainModelLayer.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object object, Errors errors) {
        // TODO username, email, nickname
        SignUpForm signUpForm = (SignUpForm) object;
        if (accountRepository.existsByUsername(signUpForm.getUsername())) {
            errors.rejectValue("username", "invalid.username", new Object[]{signUpForm.getUsername()}, "이미 사용중인 사용자 입니다.");
        }

        if (accountRepository.existsByEmail(signUpForm.getEmail())) {
            errors.rejectValue("email", "invalid.email", new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일 입니다.");
        }
//        if (accountRepository.existsByNickname(signUpForm.getNickname())) {
//            errors.rejectValue("nickname", "invalid.nickname", new Object[]{signUpForm.getNickname()}, "이미 사용중인 닉네임 입니다.");
//        }

        if (accountRepository.existsByIdentity(signUpForm.getIdentity())) {
            errors.rejectValue("identity", "invalid.identity", new Object[]{signUpForm.getIdentity()}, "이미 사용중인 닉네임입니다.");
        }

    }

}