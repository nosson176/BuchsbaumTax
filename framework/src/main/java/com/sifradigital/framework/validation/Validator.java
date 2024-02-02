package com.sifradigital.framework.validation;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.validator.routines.UrlValidator;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Validator {

    private final List<ValidationResult> results = new ArrayList<>();

    // Required string
    public Validator required(String s) {
        return required(s, "Missing required field");
    }

    public Validator required(String s, String message) {
        return validate(s != null && !s.isEmpty(), message);
    }


    // Required object
    public Validator required(Object o) {
        return required(o, "Missing required field");
    }

    public Validator required(Object o, String message) {
        return validate(o != null, message);
    }


    // Object in set
    public Validator inSet(Object o, Set<?> set) {
        return inSet(o, set, "Value is not in allowed values");
    }

    public Validator inSet(Object o, Set<?> set, String message) {
        return validate(set.contains(o), message);
    }


    // Object not in set
    public Validator notInSet(Object o, Set<?> set) {
        return notInSet(o, set, "Value is not allowed");
    }

    public Validator notInSet(Object o, Set<?> set, String message) {
        return validate(!set.contains(o), message);
    }


    // Non-zero integer
    public Validator nonZero(int i) {
        return nonZero(i, "Field must not be zero");
    }

    public Validator nonZero(int i, String message) {
        return validate(i != 0, message);
    }


    // Greater than
    public Validator greaterThan(int i, int limit) {
        return greaterThan(i, limit, "Field must be greater than " + limit);
    }

    public Validator greaterThan(int i, int limit, String message) {
        return validate(i > limit, message);
    }


    // Less than
    public Validator lessThan(int i, int limit) {
        return lessThan(i, limit, "Field must be less than " + limit);
    }

    public Validator lessThan(int i, int limit, String message) {
        return validate(i < limit, message);
    }


    // Minimum value
    public Validator min(int i, int min) {
        return min(i, min, "Field must be at least " + min);
    }

    public Validator min(int i, int min, String message) {
        return validate(i >= min, message);
    }


    // Maximum value
    public Validator max(int i, int max) {
        return max(i, max, "Field must be no more than " + max);
    }

    public Validator max(int i, int max, String message) {
        return validate(i <= max, message);
    }


    // Integer equals
    public Validator equals(int i, int compare) {
        return equals(i, compare, "Field must equal " + compare);
    }

    public Validator equals(int i, int compare, String message) {
        return validate(i == compare, message);
    }


    // Integer in range
    public Validator range(int i, int min, int max) {
        return range(i, min, max, "Field must be between " + min + " and " + max);
    }

    public Validator range(int i, int min, int max, String message) {
        return validate(i >= min && i <= max, message);
    }


    // Non-zero float
    public Validator nonZero(double d) {
        return nonZero(d, "Field must not be zero");
    }

    public Validator nonZero(double d, String message) {
        return validate(d != 0, message);
    }


    // Greater than
    public Validator greaterThan(double d, double limit) {
        return greaterThan(d, limit, "Field must be greater than " + limit);
    }

    public Validator greaterThan(double d, double limit, String message) {
        return validate(d > limit, message);
    }


    // Less than
    public Validator lessThan(double d, double limit) {
        return lessThan(d, limit, "Field must be less than " + limit);
    }

    public Validator lessThan(double d, double limit, String message) {
        return validate(d < limit, message);
    }


    // Minimum value
    public Validator min(double d, double min) {
        return min(d, min, "Field must be at least " + min);
    }

    public Validator min(double d, double min, String message) {
        return validate(d >= min, message);
    }


    // Maximum value
    public Validator max(double d, double max) {
        return max(d, max, "Field must be no more than " + max);
    }

    public Validator max(double d, double max, String message) {
        return validate(d <= max, message);
    }


    // Float equals
    public Validator equals(double d, double compare) {
        return equals(d, compare, "Field must equal " + compare);
    }

    public Validator equals(double d, double compare, String message) {
        return validate(d == compare, message);
    }


    // Float equals
    public Validator impreciseEquals(double d, double compare) {
        return impreciseEquals(d, compare, "Field must equal " + compare);
    }

    public Validator impreciseEquals(double d, double compare, String message) {
        return validate(Math.abs(d - compare) <= 0.00000001, message);
    }


    // Float in range
    public Validator range(double d, double min, double max) {
        return range(d, min, max, "Field must be between " + min + " and " + max);
    }

    public Validator range(double d, double min, double max, String message) {
        return validate(d >= min && d <= max, message);
    }


    // Valid email address
    public Validator email(String s) {
        return email(s, "Invalid email");
    }

    public Validator email(String s, String message) {
        return validate(EmailValidator.getInstance().isValid(s), message);
    }


    // Phone number, defaults to possible
    public Validator phone(String s) {
        return phonePossible(s, "Invalid phone number");
    }

    public Validator phone(String s, String message) {
        return phonePossible(s, message);
    }


    // Possible phone number
    public Validator phonePossible(String s) {
        return phonePossible(s, "Invalid phone number");
    }

    public Validator phonePossible(String s, String message) {
        return validate(validatePhoneNumber(s, PhoneNumberUtil.Leniency.POSSIBLE, ""), message);
    }


    // Valid phone number
    public Validator phoneValid(String s) {
        return phoneValid(s, "Invalid phone number");
    }

    public Validator phoneValid(String s, String message) {
        return validate(validatePhoneNumber(s, PhoneNumberUtil.Leniency.VALID, ""), message);
    }


    // Phone number for country code
    public Validator phoneForCountry(String s, String code) {
        return phoneForCountry(s, code, "Invalid phone number");
    }

    public Validator phoneForCountry(String s, String code, String message) {
        return validate(validatePhoneNumber(s, PhoneNumberUtil.Leniency.POSSIBLE, code), message);
    }

    private boolean validatePhoneNumber(String s, PhoneNumberUtil.Leniency leniency, String code) {
        if (s == null) {
            return false;
        }
        s = s.trim();
        if (!s.startsWith("+") && code.equals("")) {
            s = "+" + s;
        }
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber number = phoneNumberUtil.parse(s, code);
            return leniency == PhoneNumberUtil.Leniency.VALID ? phoneNumberUtil.isValidNumber(number) : phoneNumberUtil.isPossibleNumber(number);
        }
        catch (NumberParseException ignore) {
            return false;
        }
    }


    // Country
    public Validator isoCountry(String s) {
        return isoCountry(s, "Invalid country code");
    }

    public Validator isoCountry(String s, String message) {
        return validate(validateISOCountry(s), message);
    }

    private boolean validateISOCountry(String s) {
        if (s == null || s.length() != 2) {
            return false;
        }
        String[] codes = Locale.getISOCountries();
        for (String code : codes) {
            if (code.equals(s))
                return true;
        }
        return false;
    }

    // Language
    public Validator isoLanguage(String s) {
        return isoLanguage(s, "Invalid language code");
    }

    public Validator isoLanguage(String s, String message) {
        return validate(validateISOLanguage(s), message);
    }

    private boolean validateISOLanguage(String s) {
        if (s == null || s.length() != 2) {
            return false;
        }
        String[] codes = Locale.getISOLanguages();
        for (String code : codes) {
            if (code.equals(s))
                return true;
        }
        return false;
    }


    // Valid URL
    public Validator url(String s) {
        return url(s, "Invalid URL");
    }

    public Validator url(String s, String message) {
        return validate(UrlValidator.getInstance().isValid(s), message);
    }


    // Valid credit card
    public Validator creditCard(String s) {
        return creditCard(s, "Invalid credit card");
    }

    public Validator creditCard(String s, String message) {
        return validate(CreditCardValidator.genericCreditCardValidator().isValid(s), message);
    }


    // Minimum length
    public Validator minLength(String s, int length) {
        return minLength(s, length, "Field must be at least " + length + " chars long");
    }

    public Validator minLength(String s, int length, String message) {
        return validate(s != null && s.length() >= length, message);
    }


    // Maximum length
    public Validator maxLength(String s, int length) {
        return maxLength(s, length, "Field must be no more than " + length + " chars long");
    }

    public Validator maxLength(String s, int length, String message) {
        return validate(s != null && s.length() <= length, message);
    }


    // Exact length
    public Validator length(String s, int length) {
        return length(s, length, "Field must be exactly " + length + " chars long");
    }

    public Validator length(String s, int length, String message) {
        return validate(s != null && s.length() == length, message);
    }


    // String equals
    public Validator equals(String s, String compare) {
        return equals(s, compare, "Field must equal " + compare);
    }

    public Validator equals(String s, String compare, String message) {
        return validate(compare.equals(s), message);
    }


    // Case-insensitive string equals
    public Validator equalsIgnoreCase(String s, String compare) {
        return equalsIgnoreCase(s, compare, "Field must equal " + compare);
    }

    public Validator equalsIgnoreCase(String s, String compare, String message) {
        return validate(s != null && s.equalsIgnoreCase(compare), message);
    }


    // String contains
    public Validator contains(String s, String contains) {
        return contains(s, contains, "Field must contain " + contains);
    }

    public Validator contains(String s, String contains, String message) {
        return validate(s != null && s.contains(contains), message);
    }


    // String starts with
    public Validator startsWith(String s, String prefix) {
        return startsWith(s, prefix, "Field must start with " + prefix);
    }

    public Validator startsWith(String s, String prefix, String message) {
        return validate(s != null && s.startsWith(prefix), message);
    }


    // String ends with
    public Validator endsWith(String s, String suffix) {
        return endsWith(s, suffix, "Field must end with " + suffix);
    }

    public Validator endsWith(String s, String suffix, String message) {
        return validate(s != null && s.endsWith(suffix), message);
    }


    // String matches regex
    public Validator regex(String s, String regex) {
        return regex(s, regex, "Field must match " + regex);
    }

    public Validator regex(String s, String regex, String message) {
        return validate(s != null && s.matches(regex), message);
    }


    // Date before
    public Validator before(Date date, Date compare) {
        return before(date, compare, "Date must be before " + compare);
    }

    public Validator before(Date date, Date compare, String message) {
        return validate(date != null && date.before(compare), message);
    }


    // Date after
    public Validator after(Date date, Date compare) {
        return after(date, compare, "Date must be after " + compare);
    }

    public Validator after(Date date, Date compare, String message) {
        return validate(date != null && date.after(compare), message);
    }


    // Date in range
    public Validator range(Date date, Date min, Date max) {
        return range(date, min, max,"Date must be between " + min + " and " + max);
    }

    public Validator range(Date date, Date min, Date max, String message) {
        return validate(date != null && (date.after(min) || date.equals(min)) && (date.before(max) || date.equals(max)), message);
    }


    // List not empty
    public Validator notEmpty(List<?> list) {
        return notEmpty(list, "List must not be empty");
    }

    public Validator notEmpty(List<?> list, String message) {
        return validate(list != null && !list.isEmpty(), message);
    }


    // List min length
    public Validator minLength(List<?> list, int min) {
        return minLength(list, min, "List must not be empty");
    }

    public Validator minLength(List<?> list, int min, String message) {
        return validate(list != null && list.size() >= min, message);
    }


    // Custom
    public <T> Validator custom(T t, Predicate<T> predicate) {
        return custom(t, predicate, "Invalid field");
    }

    public <T> Validator custom(T t, Predicate<T> predicate, String message) {
        return validate(predicate.test(t), message);
    }

    public <T> Validator satisfies(T t, Predicate<T> predicate) {
        return satisfies(t, predicate, "Invalid field");
    }

    public <T> Validator satisfies(T t, Predicate<T> predicate, String message) {
        return validate(predicate.test(t), message);
    }



    private Validator validate(boolean valid, String message) {
        results.add(new ValidationResult(valid, message));
        return this;
    }

    public ValidationResult validate() {
        boolean valid = results.stream().allMatch(ValidationResult::isValid);
        String message = results.stream()
                .map(ValidationResult::getMessage)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(", "));
        return new ValidationResult(valid, message);
    }

    public void validateAndGuard() {
        ValidationResult result = validate();
        if (!result.isValid()) {
            throw new WebApplicationException(result.getMessage(), Response.Status.BAD_REQUEST);
        }
    }
}
