package top.luoqiz.idea.plugin.utils;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepositoryImpl;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.velocity.runtime.resource.loader.StringResourceLoader.REPOSITORY_NAME_DEFAULT;

/**
 * 读取文件
 *
 * @author luoqiz
 */
public class ResourceLoader {

    private StringResourceRepositoryImpl repo;

    public ResourceLoader() {
        repo = new StringResourceRepositoryImpl();
        StringResourceLoader.setRepository(REPOSITORY_NAME_DEFAULT, repo);
    }

    public void init(String file) throws IOException {
        if (repo.getStringResource(file) == null) {
            repo.putStringResource(file, loadFile(file));
        }
    }

    @NotNull
    private String loadFile(String fileName) throws IOException {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return new String(IOUtils.readFully(inputStream, 0), "UTF-8");
    }

}
