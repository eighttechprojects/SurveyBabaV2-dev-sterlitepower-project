package com.surveybaba.DTO;

import java.io.Serializable;

public class PathVO implements Serializable {

    private String localPath = "";
    private String serverPath = "";

    public PathVO(String localPath, String serverPath) {
        this.localPath = localPath;
        this.serverPath = serverPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getServerPath() {
        return serverPath;
    }
}
