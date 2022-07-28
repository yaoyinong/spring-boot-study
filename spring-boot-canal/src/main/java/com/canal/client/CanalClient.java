package com.canal.client;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author yaoyinong
 * @date 2022/7/27 15:09
 * @description Java获取Canal的binlog
 */
@Slf4j
@Component
public class CanalClient implements ApplicationRunner {

    @Value("${canal-monitor-mysql.hostname}")
    String canalMonitorHost;

    @Value("${canal-monitor-mysql.port}")
    Integer canalMonitorPort;

    @Value("${canal-monitor-mysql.tableName}")
    String canalMonitorTableName;

    private static final int BATCH_SIZE = 10000;


    @Async("customExecutor")
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("尝试连接CanalServer...");
        CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress(canalMonitorHost, canalMonitorPort),
                "example",
                "",
                "");
        try {
            //打开连接
            connector.connect();
            log.info("连接CanalServer成功");
            log.info("数据库检测连接成功!，监控表:{}", canalMonitorTableName);
            //订阅数据库表
            connector.subscribe(canalMonitorTableName + "\\..*");
            //回滚到未进行ack的地方，下次fetch的时候，可以从最后一个没有ack的地方开始拿
            connector.rollback();
            for (; ; ) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(BATCH_SIZE);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    Thread.sleep(200);
                    continue;
                } else {
                    handleDataChange(message.getEntries());
                }
                // 提交确认
                connector.ack(batchId);
            }
        } catch (Exception e) {
            log.error("Canal连接已断开:{}", e.getMessage(), e);
        } finally {
            connector.disconnect();
        }
    }

    /**
     * 打印canal server解析binlog获得的实体类信息
     */
    private void handleDataChange(List<CanalEntry.Entry> entrys) {
        for (CanalEntry.Entry entry : entrys) {
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN || entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }
            //RowChange对象，包含了一行数据变化的所有特征
            CanalEntry.RowChange rowChange;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }
            CanalEntry.EventType eventType = rowChange.getEventType();
            log.info("Canal监测到{}--table:{}", eventType, entry.getHeader().getTableName());
            switch (eventType) {
                case DELETE:
//                    List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
//                    List<String> idList = rowDatasList.stream().map(r -> {
//                        Optional<CanalEntry.Column> id = r.getBeforeColumnsList().stream().filter(c -> c.getName().equals("id")).findFirst();
//                        return id.map(CanalEntry.Column::getValue).orElse(null);
//                    }).collect(Collectors.toList());
//                    idList.forEach(id -> log.info("删除ID：{}", id));
                    log.info("Canal-Delete-rawChange:{}------------entry:{}", rowChange, entry);
                    break;
                case INSERT:
                    log.info("Canal-Insert-rawChange:{}------------entry:{}", rowChange, entry);
                    break;
                case UPDATE:
                    log.info("Canal-Update-rawChange:{}------------entry:{}", rowChange, entry);
                    break;
                default:
                    break;
            }

        }
    }
}
