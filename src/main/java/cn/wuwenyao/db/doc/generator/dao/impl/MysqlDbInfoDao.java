package cn.wuwenyao.db.doc.generator.dao.impl;

import cn.wuwenyao.db.doc.generator.entity.TableFieldInfo;
import cn.wuwenyao.db.doc.generator.entity.TableInfo;
import cn.wuwenyao.db.doc.generator.entity.TableKeyInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/***
 * 获取mysql数据库信息
 *
 * @author wwy
 *
 */

public final class MysqlDbInfoDao extends AbstractDbInfoDao {

    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(MysqlDbInfoDao.class);

    @Override
    public String databaseName() {
        String databaseName = jdbcTemplate.queryForObject("select database()", String.class);
        return databaseName;
    }

    @Override
    public List<TableInfo> tableInfoList() {
        //查询表信息
        List<TableInfo> tableInfos = jdbcTemplate.query(
                "select table_name,table_comment from information_schema.tables where table_schema = database()",
                new TableInfoRowMapper());
        if (CollectionUtils.isEmpty(tableInfos)) {
            return tableInfos;
        }
        CountDownLatch countDownLatch = new CountDownLatch(tableInfos.size());
        ExecutorService executor = new ThreadPoolExecutor(tableInfos.size(), tableInfos.size(),
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
        tableInfos.stream().forEach((tableInfo) -> {
            executor.execute(new GetTableInfoTask(tableInfo, countDownLatch, jdbcTemplate));
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        return tableInfos;
    }

    /****
     * 任务-获取表信息
     */
    public static class GetTableInfoTask implements Runnable {

        private TableInfo tableInfo;

        private CountDownLatch countDownLatch;

        private JdbcTemplate jdbcTemplate;

        public GetTableInfoTask(TableInfo tableInfo, CountDownLatch countDownLatch, JdbcTemplate jdbcTemplate) {
            this.tableInfo = tableInfo;
            this.countDownLatch = countDownLatch;
            this.jdbcTemplate = jdbcTemplate;
        }


        @Override
        public void run() {
            Object[] params = new Object[]{tableInfo.getTableName()};
            //查询列信息
            List<TableFieldInfo> fields = jdbcTemplate.query(
                    "select COLUMN_NAME, COLUMN_COMMENT,COLUMN_DEFAULT,IS_NULLABLE,COLUMN_TYPE,COLUMN_KEY,EXTRA from information_schema.columns where table_schema =database() and table_name = ?",
                    params, new TableFieldInfoRowMapper());
            tableInfo.setFields(fields);
            //查询索引信息
            List<Map<String, Object>> rawKeyInfos = jdbcTemplate.query("show keys from " + tableInfo.getTableName(),
                    new ColumnMapRowMapper());
            Map<String, TableKeyInfo> keyMap = new HashMap<>(5);
            for (Map<String, Object> rawKeyInfo : rawKeyInfos) {
                TableKeyInfo tableKeyInfo = keyMap.get(rawKeyInfo.get("Key_name").toString());
                String columnName = rawKeyInfo.get("Column_name").toString();
                if (tableKeyInfo == null) {
                    tableKeyInfo = new TableKeyInfo();
                    ArrayList<String> columns = new ArrayList<>();
                    columns.add(columnName);
                    tableKeyInfo.setColumns(columns);
                    tableKeyInfo.setIndexComment(rawKeyInfo.get("Index_comment").toString());
                    tableKeyInfo.setIndexType(rawKeyInfo.get("Index_type").toString());
                    tableKeyInfo.setName(rawKeyInfo.get("Key_name").toString());
                    tableKeyInfo.setUnique(rawKeyInfo.get("Non_unique").toString().equals("0"));
                } else {
                    tableKeyInfo.getColumns().add(columnName);
                }
                keyMap.put(rawKeyInfo.get("Key_name").toString(), tableKeyInfo);
            }
            tableInfo.setKeys(new ArrayList<>(keyMap.values()));
            logger.info("表：{}信息查询完毕", tableInfo.getTableName());
            countDownLatch.countDown();
        }

    }

    /***
     * 表信息RowMapper
     */
    public static class TableInfoRowMapper implements RowMapper<TableInfo> {

        @Override
        public TableInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            String tableName = rs.getString(1);
            String remark = rs.getString(2);
            TableInfo tableInfo = new TableInfo();
            tableInfo.setTableRemark(remark);
            tableInfo.setTableName(tableName);
            return tableInfo;
        }

    }

    /***
     * 列信息RowMapper
     */
    public static class TableFieldInfoRowMapper implements RowMapper<TableFieldInfo> {

        @Override
        public TableFieldInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            TableFieldInfo tableFieldInfo = new TableFieldInfo();
            tableFieldInfo.setDefaultValue(rs.getString("COLUMN_DEFAULT"));
            tableFieldInfo.setExtra(rs.getString("EXTRA"));
            tableFieldInfo.setField(rs.getString("COLUMN_NAME"));
            tableFieldInfo.setKey(rs.getString("COLUMN_KEY"));
            tableFieldInfo.setNullAble(rs.getString("IS_NULLABLE"));
            tableFieldInfo.setType(rs.getString("COLUMN_TYPE"));
            tableFieldInfo.setRemark(rs.getString("COLUMN_COMMENT"));
            if (tableFieldInfo.getDefaultValue() == null) {
                tableFieldInfo.setDefaultValue("无");
            }
            return tableFieldInfo;
        }

    }

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
