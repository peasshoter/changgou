package com.changgou.canal;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.changgou.item.feign.PageFeign;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@CanalEventListener
public class CanalDataEventListener {
    @Autowired
    private ContentFeign contentFeign;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private PageFeign pageFeign;

//    @InsertListenPoint
//    public void onEventInsert(CanalEntry.EntryType entryType, CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            System.out.println("增加列名" + column.getName() + "数据:" + column.getValue());
//        }
//    }
//
//    @UpdateListenPoint
//    public void onEventUpdate(CanalEntry.EntryType entryType, CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            System.out.println("修改列名" + column.getName() + "数据:" + column.getValue());
//        }
//    }
//
//    @DeleteListenPoint
//    public void onEventDelete(CanalEntry.EntryType entryType, CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
//            System.out.println("删除列名" + column.getName() + "数据:" + column.getValue());
//        }
//    }

    @ListenPoint(destination = "example",
            schema = {"changgou_content", "changgou_goods"},
            table = {"tb_content", "tb_content_category", "tb_spu"},
            eventType = {
                    CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.DELETE,
                    CanalEntry.EventType.INSERT})
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String categoryId = getColumnValue(eventType, rowData);
        Result<List<Content>> byCategory = contentFeign.findByCategory(Long.valueOf(categoryId));
        List<Content> data = byCategory.getData();
        System.out.println(data);
        redisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(data));

    }

    public void onEventSPUUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        Long spuId = Long.valueOf("");
        if (CanalEntry.EventType.DELETE == eventType) {
            for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
                if (column.getName().equalsIgnoreCase("id")) {
                    spuId = Long.valueOf(column.getValue());
                    break;
                }
            }
        } else {
            for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
                if (column.getName().equalsIgnoreCase("id")) {
                    spuId = Long.valueOf(column.getValue());
                    break;
                }
            }
        }
        pageFeign.createHtml(spuId);
    }

    private String getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String categoryId = "";
        if (CanalEntry.EventType.DELETE == eventType) {
            for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
                if (column.getName().equalsIgnoreCase("category_Id")) {
                    categoryId = column.getValue();
                    return categoryId;
                }
            }
        } else {
            for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
                if (column.getName().equalsIgnoreCase("category_Id")) {
                    categoryId = column.getValue();
                    return categoryId;
                }
            }
        }
        return categoryId;
    }
}
