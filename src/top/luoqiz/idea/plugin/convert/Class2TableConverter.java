package top.luoqiz.idea.plugin.convert;

import com.intellij.psi.*;
import top.luoqiz.idea.plugin.model.Column;
import top.luoqiz.idea.plugin.model.Table;
import top.luoqiz.idea.plugin.utils.HumpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 将类的数据类型转为sql的数据类型
 *
 * @author luoqiz
 */
public class Class2TableConverter {

    public static final String DEFAULT_TYPE = "VARCHAR(512)";

    private String prefix = "";

    private static Map<PsiPrimitiveType, String> psiPrimitiveTypeMap;

    private static Map<String, String> classTypeMap;

    static {
        psiPrimitiveTypeMap = new HashMap<>();
        psiPrimitiveTypeMap.put(PsiType.BOOLEAN, "TINYINT");
        psiPrimitiveTypeMap.put(PsiType.BYTE, "SMALLINT");
        psiPrimitiveTypeMap.put(PsiType.CHAR, "SMALLINT");
        psiPrimitiveTypeMap.put(PsiType.DOUBLE, "DOUBLE");
        psiPrimitiveTypeMap.put(PsiType.FLOAT, "FLOAT");
        psiPrimitiveTypeMap.put(PsiType.SHORT, "SMALLINT");
        psiPrimitiveTypeMap.put(PsiType.INT, "INT");
        psiPrimitiveTypeMap.put(PsiType.LONG, "BIGINT");
        classTypeMap = new HashMap<>();
        classTypeMap.put("java.lang.String", DEFAULT_TYPE);
        classTypeMap.put("java.lang.Boolean", "TINYINT");
        classTypeMap.put("java.math.BigDecimal", "DECIMAL(10,2)");
        classTypeMap.put("java.util.Date", "DATETIME");
        classTypeMap.put("java.time.LocalDate", "DATE");
        classTypeMap.put("java.time.LocalDateTime", "DATETIME");
    }

    /**
     * 将类文件转为列信息
     *
     * @param psiClass
     * @return
     */
    public Table convert(PsiClass psiClass) {
        Table table = new Table();
        table.setName(prefix + HumpUtil.humpToUnderline(psiClass.getName()));

        String separator = "\\n+";

        for (PsiField psiField : psiClass.getFields()) {
            if (psiField.getModifierList().hasExplicitModifier("static")) {
                continue;
            }
            // 创建列信息实体类
            Column column = new Column();
            column.setName(HumpUtil.humpToUnderline(psiField.getName(), false));

            // 注释信息
            StringBuilder commentAccum = new StringBuilder();
            if (psiField.getDocComment() != null) {
                for (PsiElement psiElement : psiField.getDocComment().getDescriptionElements()) {
                    commentAccum.append(psiElement.getText());
                }
                column.setComment(commentAccum.toString().replaceAll(separator, "").trim());
            }

            if (psiField.getType() instanceof PsiPrimitiveType) {
                column.setType(psiPrimitiveTypeMap.get(psiField.getType()));
            } else if (psiField.getType() instanceof PsiClassType) {
                column.setType(classTypeMap.get(psiField.getType().getCanonicalText()));
            }
            if (column.getType() == null) {
                column.setType(DEFAULT_TYPE);
            }

            for (String s : commentAccum.toString().split(separator)) {
                if (s.trim().toLowerCase().startsWith("primary")) {
                    column.setPrimaryKey(true);
                }
                if (s.trim().toLowerCase().startsWith("max")) {
                    Long length = Long.parseLong(s.trim().split("=")[1].trim());
                    if (column.getType().equalsIgnoreCase(DEFAULT_TYPE)) {
                        if (length > 65535L) {
                            column.setType("text");
                        } else if (length > 4L) {
                            column.setType(String.format("VARCHAR(%s)", length));
                        } else {
                            column.setType(String.format("CHAR(%s)", length));
                        }
                    }
                }
                if (s.trim().toLowerCase().startsWith("required")) {
                    column.setNullable(!Boolean.parseBoolean(s.trim().split("=")[1].trim()));
                }
            }
            table.getColumns().add(column);
        }
        return table;
    }

    public static void main(String[] args) {
        PsiType a = PsiType.CHAR;
        System.out.println(a.getCanonicalText());
    }

}
