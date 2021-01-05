package top.luoqiz.idea.plugin.convert;

import org.apache.velocity.VelocityContext;
import top.luoqiz.idea.plugin.model.Column;
import top.luoqiz.idea.plugin.model.Table;
import top.luoqiz.idea.plugin.utils.VelocityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author luoqiz
 */
public class SqlGenerator {

    public String generate(Table table) {
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("table", table);
        return VelocityUtils.render("create_table.vm", velocityContext);
    }

    public String generateByString(Table table) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("CREATE TABLE `%s` (\n", table.getName()));

        // 主键名称临时保存
        List<String> primaryKeyList = new ArrayList();

        // create_time 字段是否存在
        Boolean createTimeFiled = false;
        String createTimeFiledName = "create_time";

        // update_time 字段是否存在
        boolean updateTimeFiled = false;
        String updateTimeFiledName = "update_time";

        for (Column column : table.getColumns()) {
            sb.append(String.format("\t`%s` %s %s COMMENT '%s',\n",
                    column.getName(),
                    column.getType(),
                    column.isPrimaryKey() ? "NOT NULL" : column.isNullable() ? "Null" : "NOT NULL",
                    column.getComment() == null ? "" : column.getComment()
                    )
            );
            if (column.isPrimaryKey()) {
                primaryKeyList.add(String.format("`%s`", column.getName()));
            }
            if (column.getName().trim().equalsIgnoreCase(createTimeFiledName)) {
                createTimeFiled = true;
            }
            if (column.getName().trim().equalsIgnoreCase(updateTimeFiledName)) {
                updateTimeFiled = true;
            }
        }

        if (!createTimeFiled) {
            sb.append(String.format("\t`%s` timestamp NULL DEFAULT CURRENT_TIMESTAMP,\n", createTimeFiledName));
        }

        if (!updateTimeFiled) {
            sb.append(String.format("\t`%s` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n", updateTimeFiledName));
        }

        if (!primaryKeyList.isEmpty()) {
            sb.append(String.format("\tPRIMARY KEY (%s) USING BTREE\n", String.join(",", primaryKeyList)));
        }

        sb.append(String.format(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"));
        return sb.toString();
    }

}
