package org.d3op.registration.validator;

import org.apache.commons.validator.routines.EmailValidator;
import org.d3op.registration.dao.AppUserDAO;
import org.d3op.registration.formbean.AppUserForm;
import org.d3op.registration.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class AppUserValidator implements Validator {

	private EmailValidator emailValidator = EmailValidator.getInstance();

	@Autowired
	private AppUserDAO appUserDAO;

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == AppUserForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		AppUserForm appUserForm = (AppUserForm) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty.appUserForm.userName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty.appUserForm.firstName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty.appUserForm.lastName");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.appUserForm.email");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.appUserForm.password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.appUserForm.confirmPassword");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "NotEmpty.appUserForm.gender");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "countryCode", "NotEmpty.appUserForm.countryCode");

		if (!this.emailValidator.isValid(appUserForm.getEmail())) {
			errors.rejectValue("email", "Pattern.appUserForm.email");
		} else if (appUserForm.getUserId() == null) {
			AppUser dbUser = appUserDAO.findAppUserByEmail(appUserForm.getEmail());
			if (dbUser != null) {
				errors.rejectValue("email", "Duplicate.appUserForm.email");
			}
		}

		if (!errors.hasFieldErrors("userName")) {
			AppUser dbUser = appUserDAO.findAppUserByUserName(appUserForm.getUserName());
			if (dbUser != null) {
				errors.rejectValue("userName", "Duplicate.appUserForm.userName");
			}
		}

	}

}
