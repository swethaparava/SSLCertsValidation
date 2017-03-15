package com.Util;

/**
 * Created by sparava on 16/3/17.
 */

import javax.net.ssl.*;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class CheckCertsUtil {

  public static void main(String[] args) throws Exception {
    checkCerts("https://github.com");
  }

  public static String checkCerts(String urlString) throws Exception {
    boolean isValid = false;
    Date validUntil = null;
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
    SSLContext.setDefault(ctx);

    URL url = new URL(urlString);
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    conn.setHostnameVerifier(new HostnameVerifier() {
      @Override
      public boolean verify(String arg0, SSLSession arg1) {
        return true;
      }
    });
    int responseCode = conn.getResponseCode();
    Certificate[] certs = conn.getServerCertificates();
    for (Certificate cert : certs) {
      X509Certificate cert1 = (X509Certificate) cert;
      if (cert1.getNotAfter().getTime() > new Date().getTime()) {
        isValid = true;
        validUntil = cert1.getNotAfter();
      }
    }
    conn.disconnect();
    String res;
    if (isValid) {
      res = "Valid until :" + (validUntil != null ? validUntil.toString() : "");
    } else {
      res = "Certificate Expired";
    }
    return res;
  }

  private static class DefaultTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
  }
}
