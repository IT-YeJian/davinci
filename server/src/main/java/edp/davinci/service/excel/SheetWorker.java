/*
 * <<
 *  Davinci
 *  ==
 *  Copyright (C) 2016 - 2019 EDP
 *  ==
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  >>
 *
 */

package edp.davinci.service.excel;

import com.google.common.base.Stopwatch;
<<<<<<< HEAD
import com.google.common.collect.Maps;
import edp.core.enums.DataTypeEnum;
=======
>>>>>>> 7958af50c93c4e3a7d841b0169fec6aba1af2411
import edp.core.model.QueryColumn;
import edp.core.utils.CollectionUtils;
import edp.core.utils.MD5Util;
import edp.core.utils.SqlUtils;
import edp.davinci.core.enums.ActionEnum;
import edp.davinci.core.utils.SqlParseUtils;
import edp.davinci.dto.cronJobDto.MsgMailExcel;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSetMetaData;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static edp.core.consts.Consts.QUERY_META_SQL;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author daemon
 * @Date 19/5/28 18:23
 * To change this template use File | Settings | File Templates.
 */
public class SheetWorker<T> extends AbstractSheetWriter implements Callable {
    
    private SheetContext context;

    private int maxRows = 1000000;

    public SheetWorker(SheetContext context) {
        this.context = context;
    }

    @Override
    public T call() {

        Stopwatch watch = Stopwatch.createStarted();
        Boolean rst = true;
        String md5 = null;
        Logger logger = context.getCustomLogger();
        boolean log = context.getCustomLogger() != null;

        try {

            interrupted(context);

            SqlUtils utils = context.getSqlUtils();
            JdbcTemplate template = utils.jdbcTemplate();
            propertiesSet(template);
            buildQueryColumn(template);
            super.init(context);
            super.writeHeader(context);
            template.setMaxRows(context.getResultLimit() > 0 && context.getResultLimit() <= maxRows ? context.getResultLimit() : maxRows);

            // special for mysql
            if(utils.getDataTypeEnum() == DataTypeEnum.MYSQL) {
            	template.setFetchSize(Integer.MIN_VALUE);
            }

            String sql = context.getQuerySql().get(context.getQuerySql().size() - 1);
            sql = SqlParseUtils.rebuildSqlWithFragment(sql);
            md5 = MD5Util.getMD5(sql, true, 16);
            Set<String> queryFromsAndJoins = SqlUtils.getQueryFromsAndJoins(sql);
            if (log) {
                logger.info("Task({}) sheet worker(name:{}, sheetNo:{}, sheetName:{}) query start sql:{}, md5:{}",
                        context.getTaskKey(), context.getName(), context.getSheetNo(), context.getSheet().getSheetName(), utils.formatSql(sql), md5);
            }

            final AtomicInteger count = new AtomicInteger(0);
<<<<<<< HEAD
            template.query(sql, rs -> {

                interrupted(context);

                Map<String, Object> dataMap = Maps.newHashMap();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    dataMap.put(SqlUtils.getColumnLabel(queryFromsAndJoins, rs.getMetaData().getColumnLabel(i)), rs.getObject(rs.getMetaData().getColumnLabel(i)));
                }
                writeLine(context, dataMap);
                count.incrementAndGet();
            });

            if (log) {
                logger.info("Task({}) sheet worker(name:{}, sheetNo:{}, sheetName:{}) query finish md5:{}, count:{}",
=======
//            template.query(sql, rs -> {
//                Map<String, Object> dataMap = Maps.newHashMap();
//                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
//                    dataMap.put(SqlUtils.getColumnLabel(queryFromsAndJoins, rs.getMetaData().getColumnLabel(i)), rs.getObject(rs.getMetaData().getColumnLabel(i)));
//                }
//                writeLine(context, dataMap);
//                count.incrementAndGet();
//            });

            List<Map<String, Object>> list = template.queryForList(sql);
            // 获取合并列
            List<Integer> listIt = new ArrayList<Integer>();
            for (int j = 0; j < context.getQueryColumns().size(); j++) {
                QueryColumn queryColumn = context.getQueryColumns().get(j);
                if(!queryColumn.getType().equals("value")){
                    listIt.add(j);
                }
            }
            int[] mergeIndex = new int[listIt.size()];
            int i = 0;
            for(Integer it: listIt){
                mergeIndex[i++] = it.intValue();
            }

            Map<String, List<Map<String, Object>>> map = new HashMap();
            map.put("合并单元格数据", list);
            createSheet(context.getSheet(),context.getQueryColumns(),map,mergeIndex);

            if (context.getCustomLogger() != null) {
                context.getCustomLogger().info("Task({}) sheet  worker(name:{}, sheetNo: {}, sheetName:{}) finish query md5:{}, count:{}",
>>>>>>> 7958af50c93c4e3a7d841b0169fec6aba1af2411
                        context.getTaskKey(), context.getName(), context.getSheetNo(), context.getSheet().getSheetName(), md5, count.get());
            }

            super.refreshHeightWidth(context);

        } catch (Exception e) {
            if (context.getWrapper().getAction() == ActionEnum.MAIL) {
                MsgMailExcel msg = (MsgMailExcel) context.getWrapper().getMsg();
                msg.setDate(new Date());
                msg.setException(e);
            }
            if (log) {
                logger.error("Task({}) sheet worker(name:{}, sheetNo:{}, sheetName:{}) query error md5:{}",
                        context.getTaskKey(), context.getName(), context.getSheetNo(), context.getSheet().getSheetName(), md5);
                logger.error(e.toString(), e);
            }
            rst = false;
        }

        Object[] args = {context.getTaskKey(), context.getName(), md5, rst, context.getWrapper().getAction(), context.getWrapper().getxId(),
                context.getWrapper().getxUUID(), context.getSheetNo(), context.getSheet().getSheetName(), context.getDashboardId(),
                context.getWidgetId(), watch.elapsed(TimeUnit.MILLISECONDS)};
        if (log) {
            logger.info(
                    "Task({}) sheet worker({}) complete md5={}, status={}, action={}, xid={}, xUUID={}, sheetNo={}, sheetName={}, dashboardId={}, widgetId={}, cost={}ms",
                    args);
        }

        return (T) rst;
    }

    private void interrupted(SheetContext context) {
        if (Thread.interrupted()) {
            Logger logger = context.getCustomLogger();
            boolean log = context.getCustomLogger() != null;
            if (log) {
                logger.error("Task({}) sheet worker(name:{}, sheetNo:{}, sheetName:{}) interrupted",
                        context.getTaskKey(), context.getName(), context.getSheetNo(), context.getSheet().getSheetName());
            }
            throw new RuntimeException("Task(" + context.getTaskKey() + ") sheet worker(name:" + context.getName() + ", " +
                    "sheetNo:" + context.getSheetNo() + ", sheetName:" + context.getSheet().getSheetName() + ") interrupted");
        }
    }

    private void propertiesSet(JdbcTemplate template) {
        if (CollectionUtils.isEmpty(context.getExecuteSql())) {
            return;
        }

        context.getExecuteSql().stream().filter(sql -> sql != null).forEach(sql -> {
            template.execute(sql);
        });
    }

    private void buildQueryColumn(JdbcTemplate template) {
        template.setMaxRows(1);
        String sql = context.getQuerySql().get(context.getQuerySql().size() - 1);
        sql = String.format(QUERY_META_SQL, sql);
        sql = SqlParseUtils.rebuildSqlWithFragment(sql);
        Set<String> queryFromsAndJoins = SqlUtils.getQueryFromsAndJoins(sql);
        template.query(sql, rs -> {
            ResultSetMetaData metaData = rs.getMetaData();
            List<QueryColumn> totalColumns = new ArrayList<>();
            List<QueryColumn> queryColumns = new ArrayList<>();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String label = SqlUtils.getColumnLabel(queryFromsAndJoins, metaData.getColumnLabel(i));
                totalColumns.add(new QueryColumn(label, metaData.getColumnTypeName(i)));
                if (!CollectionUtils.isEmpty(context.getExcludeColumns()) && context.getExcludeColumns().contains(label)) {
                    continue;
                }
                queryColumns.add(new QueryColumn(label, metaData.getColumnTypeName(i)));
            }
            if (CollectionUtils.isEmpty(totalColumns) || CollectionUtils.isEmpty(queryColumns)) {
                throw new IllegalArgumentException("Can not find any query column, widgetId=" + context.getWidgetId()
                        + ", sql=" + context.getQuerySql().get(context.getQuerySql().size() - 1));
            }
            context.setTotalColumns(totalColumns);
            context.setQueryColumns(queryColumns);
            return context;
        });
    }
}
