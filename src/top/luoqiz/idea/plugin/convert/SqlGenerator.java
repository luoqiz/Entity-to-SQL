package top.luoqiz.idea.plugin.convert;

import org.apache.velocity.VelocityContext;
import top.luoqiz.idea.plugin.model.Table;
import top.luoqiz.idea.plugin.utils.VelocityUtils;

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
        sb.append(String.format("\t`id` bigint(20) NOT NULL,\n"));

        table.getColumns().forEach(column -> {
            sb.append(String.format("\t`%s` %s NOT NULL COMMENT '%s',\n",
                    column.getName(),
                    column.getType(),
                    column.getComment() == null ? "" : column.getComment()
                    )
            );

        });

        sb.append(String.format("\t`create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,\n"));
        sb.append(String.format("\t`update_time` timestamp NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"));
        sb.append(String.format("\tPRIMARY KEY (`id`) USING BTREE\n"));
        sb.append(String.format(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;"));
        return sb.toString();
    }

}
