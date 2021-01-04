package top.luoqiz.idea.plugin;

import com.intellij.ide.highlighter.JavaClassFileType;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import top.luoqiz.idea.plugin.convert.Class2TableConverter;
import top.luoqiz.idea.plugin.convert.SqlGenerator;
import top.luoqiz.idea.plugin.model.Table;
import top.luoqiz.idea.plugin.utils.LogUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * @author luoqiz
 */
public class EntityToSQL extends AnAction {

    private Class2TableConverter class2TableConverter = new Class2TableConverter();
    private SqlGenerator sqlGenerator = new SqlGenerator();

    @Override
    public void actionPerformed(AnActionEvent e) {

        PsiElement data = CommonDataKeys.PSI_ELEMENT.getData(e.getDataContext());
        Table table = class2TableConverter.convert((PsiClass) data);
        String createSql = sqlGenerator.generateByString(table);
        LogUtils.showInfo("生成成功,sql已复制到剪切板");
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection(createSql);
        clip.setContents(tText, null);
        LogUtils.showInfo(createSql);
    }

    @Override
    public void update(AnActionEvent actionEvent) {
        super.update(actionEvent);

        boolean visible = isActionAvailable(actionEvent);

        Presentation presentation = actionEvent.getPresentation();
        presentation.setVisible(visible);
        presentation.setEnabled(visible);
    }

    /**
     * 功能是否可用
     * @param e
     * @return
     */
    private boolean isActionAvailable(AnActionEvent e) {
        VirtualFile file = getVirtualFiles(e);
        if (AnAction.getEventProject(e) != null && file != null) {
            FileType fileType = file.getFileType();
            return JavaFileType.INSTANCE.equals(fileType) || JavaClassFileType.INSTANCE.equals(fileType);
        }
        return false;
    }

    private VirtualFile getVirtualFiles(AnActionEvent e) {
        return PlatformDataKeys.VIRTUAL_FILE.getData(e.getDataContext());
    }
}
