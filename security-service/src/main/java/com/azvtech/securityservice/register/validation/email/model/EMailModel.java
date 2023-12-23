package com.azvtech.securityservice.register.validation.email.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author Fellipe Toledo
 */
@Getter
@Setter
public class EMailModel {
    String to;
    String from;
    String subject;
    String content;
    private Map< String, Object > model;

}
