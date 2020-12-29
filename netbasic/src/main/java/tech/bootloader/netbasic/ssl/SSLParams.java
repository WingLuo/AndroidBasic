package tech.bootloader.netbasic.ssl;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Administrator on 2017/7/7.
 */

public abstract class SSLParams {
    SSLParams(){

    }
     public SSLSocketFactory sSLSocketFactory;
     public X509TrustManager trustManager;
}
