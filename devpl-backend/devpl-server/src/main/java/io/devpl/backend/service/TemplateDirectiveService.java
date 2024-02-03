package io.devpl.backend.service;

import io.devpl.backend.domain.param.CustomTemplateDirectiveParam;
import io.devpl.backend.entity.CustomDirective;

import java.util.List;

public interface TemplateDirectiveService {

    boolean addCustomDirective(CustomTemplateDirectiveParam param);

    List<CustomDirective> listCustomDirectives(CustomTemplateDirectiveParam param);

    boolean removeCustomDirective(CustomTemplateDirectiveParam param);
}
