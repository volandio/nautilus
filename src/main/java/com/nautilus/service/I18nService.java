package com.nautilus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Service
public class I18nService {

    private final Locale locale = LocaleContextHolder.getLocale();
    private final MessageSource source;
    private MessageSourceAccessor accessor;

    @Autowired
    public I18nService(@Qualifier("messageSource") MessageSource source) {
        this.source = source;
    }

    @PostConstruct
    public void init() {
        accessor = new MessageSourceAccessor(source, locale);
    }

    public String getMessage(String code) {
        try {
            return accessor.getMessage(code);
        } catch (NoSuchMessageException e) {
            return code + "_MISSING!!!";
        }
    }
}
