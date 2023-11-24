package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.generator.dao.TableFileGenerationMapper;
import io.devpl.generator.entity.TableFileGeneration;
import io.devpl.generator.service.TableFileGenerationService;
import org.springframework.stereotype.Service;

@Service
public class TableFileGenerationServiceImpl extends ServiceImpl<TableFileGenerationMapper, TableFileGeneration> implements TableFileGenerationService {
}
