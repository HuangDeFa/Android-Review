package com.kenzz.reader.http;

import java.io.File;

/**
 * Created by ken.huang on 11/27/2017.
 * DownloadManager
 */

public class DownloadManager {

    public static File downloadFile(String url,String fileName){
       ApiManager.getInstance().getService(DownloadService.class)
               .downloadFile(url,fileName);
       return null;
    }
}
