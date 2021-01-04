package top.luoqiz.idea.plugin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 读取的类文件信息
 *
 * @author luoqiz
 */
public class Table {

    private List<Column> columns = new ArrayList<>();

    private String name;

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
