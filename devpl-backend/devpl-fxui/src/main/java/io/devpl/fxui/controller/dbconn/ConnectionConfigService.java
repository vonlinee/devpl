package io.devpl.fxui.controller.dbconn;

import io.devpl.fxui.model.ConnectionConfig;

/**
 * 连接信息管理Service
 */
public interface ConnectionConfigService {

    /**
     * 保存连接信息
     *
     * @param connectionConfig 连接信息
     * @return 是否成功
     */
    boolean save(ConnectionConfig connectionConfig);
}
