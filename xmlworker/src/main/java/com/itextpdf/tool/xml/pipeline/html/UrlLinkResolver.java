package com.itextpdf.tool.xml.pipeline.html;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class UrlLinkResolver {
    private String rootPath;

    public UrlLinkResolver(String localRootPath) {
        this.rootPath = localRootPath;
    }

    public UrlLinkResolver() {
    }

    public URL resolveUrl(String src) throws MalformedURLException {
        URL url;
        try {
            url = new URL(src);
        } catch (MalformedURLException e) {
            url = resolveLocalUrl(src);
        }
        return url;
    }

    public URL resolveLocalUrl(String src) throws MalformedURLException {
        String path;
        if (rootPath != null) {
            boolean rootSlashed = rootPath.endsWith("/");
            boolean srcSlashed = src.startsWith("/");
            if (rootSlashed && srcSlashed) {
                rootPath = rootPath.substring(0, rootPath.length() - 1);
            } else if (!rootSlashed && !srcSlashed) {
                rootPath += "/";
            }
            path = rootPath + src;
        } else {
            path = src;
        }

        File file = new File(path);
        if (file.exists())
            return file.toURI().toURL();
        return null;
    }

    public void setLocalRootPath(String rootPath) {
        this.rootPath = rootPath;
    }
}