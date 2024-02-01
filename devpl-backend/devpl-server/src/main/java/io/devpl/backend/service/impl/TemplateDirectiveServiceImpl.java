package io.devpl.backend.service.impl;

import io.devpl.backend.domain.param.CustomTemplateDirectiveParam;
import io.devpl.backend.service.TemplateDirectiveService;
import org.springframework.stereotype.Service;

@Service
public class TemplateDirectiveServiceImpl implements TemplateDirectiveService {


    @Override
    public boolean addCustomDirective(CustomTemplateDirectiveParam param) {
        return false;
    }
}
