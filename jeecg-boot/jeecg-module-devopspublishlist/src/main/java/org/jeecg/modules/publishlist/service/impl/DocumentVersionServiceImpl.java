package org.jeecg.modules.publishlist.service.impl;



import org.jeecg.modules.publishlist.entity.DocumentVersion;
import org.jeecg.modules.publishlist.mapper.DocumentVersionMapper;
import org.jeecg.modules.publishlist.service.IDocumentVersionService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 文档版本
 * @Author: jeecg-boot
 * @Date:   2023-06-08
 * @Version: V1.0
 */
@Service
public class DocumentVersionServiceImpl extends ServiceImpl<DocumentVersionMapper, DocumentVersion> implements IDocumentVersionService {

}
