package com.example.smit_pt.validator;

import java.time.LocalDate;

import com.example.smit_pt.entity.FilterSettings;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, FilterSettings> {

	@Override
	public boolean isValid(FilterSettings filterSettings, ConstraintValidatorContext context) {
		if (filterSettings.getFrom() == null) {
			return true;
		}
		LocalDate now = LocalDate.now();
		LocalDate from = filterSettings.getFrom();
		LocalDate until;
		if (filterSettings.getUntil() == null) {
			until = null;
		} else {
			until = filterSettings.getUntil();
		}
		if (from.isBefore(now) || (until != null && until.isBefore(now))) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Dates cannot be in the past").addConstraintViolation();
			return false;
		}
		if (until != null && until.isBefore(from)) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("'Until' date cannot be before 'From' date")
					.addConstraintViolation();
			return false;
		}
		return true;
	}
}
