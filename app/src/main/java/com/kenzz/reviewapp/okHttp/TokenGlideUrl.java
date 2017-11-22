package com.kenzz.reviewapp.okHttp;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;

import java.net.URL;

/**
 * Created by ken.huang on 11/21/2017.
 *  自定义GlideUrl
 */

public class TokenGlideUrl extends GlideUrl {
    public TokenGlideUrl(URL url) {
        super(url);
    }

    public TokenGlideUrl(String url) {
        super(url);
    }

    public TokenGlideUrl(URL url, Headers headers) {
        super(url, headers);
    }

    public TokenGlideUrl(String url, Headers headers) {
        super(url, headers);
    }

    @Override
    public String getCacheKey() {
        String superKey = super.getCacheKey();
        superKey = removeToken(superKey);
        return superKey;
    }

    /**
     * 去掉含有Token的url, 因为Token是会变的，所以这会导致相同的图片
     * 多次缓存
     * @param superKey eg: http://www.baidu.com/124654JJJIII/sss.jpg&Token=124654jiouiou
     * @return
     */
    private String removeToken(String superKey) {
        return null;
    }
}
