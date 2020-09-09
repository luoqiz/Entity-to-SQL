package top.luoqiz.idea.plugin.convert;

import com.intellij.psi.*;
import top.luoqiz.idea.plugin.model.Column;
import top.luoqiz.idea.plugin.model.Table;
import top.luoqiz.idea.plugin.utils.HumpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author luoqiz
 */
public class Class2TableConverter {

    public static final String DEFAULT_TYPE = "VARCHAR(512)";

    private String prefix = "";

    private String primaryKey = "id";

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
        classTypeMap.put("java.lang.String", "VARCHAR(255)");
        classTypeMap.put("java.lang.Boolean", "TINYINT");
        classTypeMap.put("java.math.BigDecimal", "DECIMAL(10,2)");
        classTypeMap.put("java.util.Date", "DATETIME");
        classTypeMap.put("java.time.LocalDate", "DATE");
        classTypeMap.put("java.time.LocalDateTime", "DATETIME");
    }

    public Table convert(PsiClass psiClass) {
        Table table = new Table();
        table.setName(prefix + psiClass.getName());
        for (PsiField psiField : psiClass.getFields()) {
            if (psiField.getModifierList().hasExplicitModifier("static")) {
                continue;
            }
            Column column = new Column();
            if (psiField.getDocComment() != null) {
                StringBuilder commentAccum = new StringBuilder();
                for (PsiElement psiElement : psiField.getDocComment().getDescriptionElements()) {
                    commentAccum.append(psiElement.getText());
                }
                column.setComment(commentAccum.toString().replaceAll("\\n+", "").trim());
            }
            column.setName(HumpUtil.humpToUnderline(psiField.getName(), false));
            if (psiField.getType() instanceof PsiPrimitiveType) {
                column.setType(psiPrimitiveTypeMap.get(psiField.getType()));
            } else if (psiField.getType() instanceof PsiClassType) {
                column.setType(classTypeMap.get(((PsiClassType) psiField.getType()).getCanonicalText()));
            }
            if (column.getType() == null) {
                column.setType(DEFAULT_TYPE);
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
