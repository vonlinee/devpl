package io.devpl.backend.service;

import io.devpl.backend.domain.param.CustomTemplateDirectiveParam;

public interface TemplateDirectiveService {

    boolean addCustomDirective(CustomTemplateDirectiveParam param);
}
