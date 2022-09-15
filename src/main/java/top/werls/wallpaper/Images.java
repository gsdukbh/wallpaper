package top.werls.wallpaper;

import java.util.Date;
import com.alibaba.fastjson.annotation.JSONField;

public class Images {
    /**
     * 日期
     */
    @JSONField(format = "yyyy-MM-dd")
    private Date endDate;

    /**
     * url
     */
    private String url;
    /**
     * 版权信息
     */
    private String copyright;

    private String copyrightCN;
    /**
     * hash 值
     */
    private String hash;

    private Date utcDate;

    private String urlForeign;

    private String fileName4k;

    private String fileName;

    @Override
    public String toString() {
        return "Images{" +
                "endDate=" + endDate +
                ", url='" + url + '\'' +
                ", copyright='" + copyright + '\'' +
                ", copyrightCN='" + copyrightCN + '\'' +
                ", hash='" + hash + '\'' +
                ", utcDate=" + utcDate +
                ", urlForeign='" + urlForeign + '\'' +
                ", fileName4k='" + fileName4k + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }

    public String getFileName4k() {
        return fileName4k;
    }

    public void setFileName4k(String fileName4k) {
        this.fileName4k = fileName4k;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getUtcDate() {
        return utcDate;
    }

    public void setUtcDate(Date utcDate) {
        this.utcDate = utcDate;
    }

    public String getUrlForeign() {
        return urlForeign;
    }

    public void setUrlForeign(String urlForeign) {
        this.urlForeign = urlForeign;
    }

    public String getCopyrightCN() {
        return copyrightCN;
    }

    public void setCopyrightCN(String copyrightCN) {
        this.copyrightCN = copyrightCN;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Images() {
    }
}
