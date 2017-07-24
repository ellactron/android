package com.ellactron.http.security;

import org.apache.commons.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by ji.wang on 2017-07-21.
 */

public class HttpSecurityContext {
    final static private String KEY_TYPE = "RSA";
    final static private String KEY_PASSPHASE = "pa55w0rd";

    /**
     * Key Algorithm: RSA
     * Key length: 1024
     * Export format: PKCS8
     * Keystore encrypt algorithm: PBEWITHSHAAND2-KEYTRIPLEDES-CBC
     */
    final private static String PRIVATE_KEY =
            "MIICojAcBgoqhkiG9w0BDAEEMA4ECGsjDOXAu8W+AgICmwSCAoBIMkxdZEgh7Eod\n" +
                    "X2+3CifwEpUEKEJXMJj7uYE8qEzRkG/59UiWMpljij6e5mLq4+0s5ftnADYE09SB\n" +
                    "PMgtWWY8MHrkku6zs39QPhumxfYMK+aHfnosDEjJzW2+b3b9pQNVJf/VwNz1r9h/\n" +
                    "wYIxPJAeZrcNDh9qJj2fYK6L810hiSkWbbw9pmnG/wS7xM0wS3UXH7jlrqdQF33R\n" +
                    "du2s3zhFw1aGMgSPZsUmDr1i4kU0Mx5mwK4n8xgK4A3hWuWRBPQ7qghMAf+bxLew\n" +
                    "4DVEGUCUq/x7t6FvBUfLcyP0zGEqaU2IXEGn/oEsorQ6uGUcf1w7Z1PWkqhe+Ha9\n" +
                    "jdGnR/nfFikRWqbbrI6n+6/7dYuUNEsYmnq7rqpmraE+/Bk92Z0nCIzReLcc4q6Z\n" +
                    "aYRJPMVvVsTNTRvGDrf3Wazr5CAhZXqPJnCcYxz0wreVRNQK/qzHSCIgo1airg0F\n" +
                    "3NiqJT1etLagu3uMESnKuSHL38RTb4xGwv5iznQeMP/gQsKR2YEKBlH66LzQEzGN\n" +
                    "elOk461Q5s1ZMawC4WPf5rv5jgINGVEyawBrOz3mJ/2SbpVj0WjpX0syH95EwHI6\n" +
                    "h14ClP2R7+Bgzmwg5oy8fJVp6WWiTQLbm18wdvJHLl5PyM+m5iJvCbe6m3j0rGP0\n" +
                    "ny/XkhmrkY7grfvy/G+7Ba4uhXOEwMbO9wc8z10+NWRffZWzDAF+5eClCevISxH5\n" +
                    "aOnUZXZDx8I3ZmDg3XXML3PuKIIpXyrryPEFyg5KszjTXFUmZfMdZTKDlMzDQynP\n" +
                    "TGB+NJxx70NbstF5QdGm0IKwrVddICgtuRzE48fBYHNoBnXxDZ8Id4elCxd6LbLA\n" +
                    "jz2O2/0c";

    final private static String CLIENT_CERTIFICATE =
            "-----BEGIN CERTIFICATE-----\n" +
                    "MIIC6jCCAdKgAwIBAgIEWXVfpDANBgkqhkiG9w0BAQsFADB2MQswCQYDVQQGEwJD\n" +
                    "QTEQMA4GA1UECAwHT250YXJpbzEQMA4GA1UEBwwHVG9yb250bzEVMBMGA1UECgwM\n" +
                    "TmV3QmVlIEx0ZC4sMQswCQYDVQQLDAJDQTEfMB0GA1UEAwwWTmV3QmVlIEludGVy\n" +
                    "bWVkaWF0ZSBDQTAeFw0xNzA3MjQwMjQ3NDNaFw0yMjA3MjQwMjQ3NDNaMGMxCzAJ\n" +
                    "BgNVBAYTAkNBMQswCQYDVQQIDAJPTjEQMA4GA1UEBwwHVG9yb250bzEQMA4GA1UE\n" +
                    "CgwHTmV3QmVlbTESMBAGA1UECwwJRWxsYWN0cm9uMQ8wDQYDVQQDDAZjbGllbnQw\n" +
                    "gZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAN3KTpxTDPaRnddt5pKApRh5U43I\n" +
                    "gGSOsCwNy6DTACRFO4HOmzhhwNXw+CDr/YZebRN6bKbZKMvZS0Qvog1Bko8V2tHq\n" +
                    "FaMl5ffP2M3lvWACmjx/4dlgLbVAaQCORMO4YoqyqZ6s3OJ75lzXmHFCrfDCI8zk\n" +
                    "PDikhKwbBRnXPU01AgMBAAGjFzAVMBMGA1UdJQQMMAoGCCsGAQUFBwMCMA0GCSqG\n" +
                    "SIb3DQEBCwUAA4IBAQCkyKhSMAD57P4yMNOaqK4OSlW1RerWvy0nu9+H1Blrnp/Z\n" +
                    "8WwGf7IJxdBdmi9C8AixbZe6826tk9HDpAnxWIz+trt8O1nQVs/r3uOSSU1BGwm4\n" +
                    "+gl1g+Hm+7uS8WxDBT2Ql29cxJSxoYky0Ko5JxKplJx8XmMTTIxh9/0ARd7fjkNX\n" +
                    "qJJAdOqI4dVTk0TZjCic/C6s6MpU5Fz3DGZkiN/KpgbA1EyIG9g8G9i1OZNnyUZy\n" +
                    "laxqBxx7FtPx1zjf1l0DR+GeCo+5nPqOu2Dd1QAA6ID3i4QKLawikhgmHR1reULH\n" +
                    "SAmGWfdt5M2orWxWGGjdldGrIhFPz9cEkH3grWKy\n" +
                    "-----END CERTIFICATE-----\n" +
                    "-----BEGIN CERTIFICATE-----\n" +
                    "MIIDdTCCAl2gAwIBAgIEWRylBzANBgkqhkiG9w0BAQsFADBuMQswCQYDVQQGEwJD\n" +
                    "QTEQMA4GA1UECAwHT250YXJpbzEQMA4GA1UEBwwHVG9yb250bzEVMBMGA1UECgwM\n" +
                    "TmV3QmVlIEx0ZC4sMQswCQYDVQQLDAJDQTEXMBUGA1UEAwwOTmV3QmVlIFJvb3Qg\n" +
                    "Q0EwHhcNMTcwNTE3MTkzMjA2WhcNMjcwNTE3MTkzMjA2WjB2MQswCQYDVQQGEwJD\n" +
                    "QTEQMA4GA1UECAwHT250YXJpbzEQMA4GA1UEBwwHVG9yb250bzEVMBMGA1UECgwM\n" +
                    "TmV3QmVlIEx0ZC4sMQswCQYDVQQLDAJDQTEfMB0GA1UEAwwWTmV3QmVlIEludGVy\n" +
                    "bWVkaWF0ZSBDQTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAOeZT8dn\n" +
                    "Y8xTGB2BzKzl+grTzKTwwa/nxAGxf0GeH5ARyegEQFteHMFrdsyuq2JGoIBjcDQD\n" +
                    "65WBTv6ZsetD0tmysR7S/SILEsr3Gdg9iipdwhSfCkb7CBw/tyY51YLPgMsLc8qg\n" +
                    "SOsYZnJRVFcCbFY2IfJrUeUrqTK7mb5kf2K9JmNWdbZn02aKuQ4Fq7eNlJr/GgB+\n" +
                    "So2tgwb7+CbEqSOBgBKw6ThaBmtFa4UmnhX3C3AkVxc1ZznayEf+xQj+vWL+AdJV\n" +
                    "+AdNI3LcFzWTIyusE+Ur0eHKv/RdXF5toHR6iKOA59CWOD+L4ocv8aG8KmX242D2\n" +
                    "cnfcI79Ti6JeKlkCAwEAAaMTMBEwDwYDVR0TAQH/BAUwAwEB/zANBgkqhkiG9w0B\n" +
                    "AQsFAAOCAQEAJBXdz1//fuC9YXPzODqh5WTZSkY5gHCA2aFVIXmdAJnFt6/nyHw5\n" +
                    "rvHqdFOg5SOc1THQoUeWRTFTo73jbf9BekHM1SekrB2ZK8PTe9OXmMU9Ssomct7V\n" +
                    "CM0MdLzQERn1ZW/GIJvb38LUrObs25TQHzGq2diBxG0RUfszjA0UvZCycJzDWvBd\n" +
                    "UVAmScGrGA1+ZKUh+TjftykIxNsbj4+uZNhYCNEsiii+Fi+5ka3G3XoGzwNtnhJl\n" +
                    "H9z0cZ6WqJuXZRBeuELYeiqoLquE6H76vOSFFENOlM0ZQR7aU/O5IzGwt3ZDQxV/\n" +
                    "Tr09FyTBAMXSKK5U4n65KZOVXviW2411Qw==\n" +
                    "-----END CERTIFICATE-----\n" +
                    "-----BEGIN CERTIFICATE-----\n" +
                    "MIIDbTCCAlWgAwIBAgIEWRykuzANBgkqhkiG9w0BAQsFADBuMQswCQYDVQQGEwJD\n" +
                    "QTEQMA4GA1UECAwHT250YXJpbzEQMA4GA1UEBwwHVG9yb250bzEVMBMGA1UECgwM\n" +
                    "TmV3QmVlIEx0ZC4sMQswCQYDVQQLDAJDQTEXMBUGA1UEAwwOTmV3QmVlIFJvb3Qg\n" +
                    "Q0EwHhcNMTcwNTE3MTkzMTA1WhcNMzcwNTE3MTkzMTA1WjBuMQswCQYDVQQGEwJD\n" +
                    "QTEQMA4GA1UECAwHT250YXJpbzEQMA4GA1UEBwwHVG9yb250bzEVMBMGA1UECgwM\n" +
                    "TmV3QmVlIEx0ZC4sMQswCQYDVQQLDAJDQTEXMBUGA1UEAwwOTmV3QmVlIFJvb3Qg\n" +
                    "Q0EwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDOqFqpA9LgC59EVsqW\n" +
                    "Qv/I5O4xhFtycgLBrUkJHaRyN3DMG54Zq9lPXZ5JiICT2SOE9XnQ1rGNH0qzZfH7\n" +
                    "tfxUcXfsNr+TlAsSxAZ/tS+Y6BrnLeSeO9tNWXYHHUVhSbBeS+7Zzakb9x+Qa4eq\n" +
                    "zKoqNzA+EBsuJy1pgIrRKq++KwaTWIPKvV1ygFbae1qq17u8MEOIsvE72Y2pKH+c\n" +
                    "hbc6a3ZCSHJpgP47O28TQxF+15M8wr2BkSjKKbXr8jcUHE2n81acxLP07V+g2cQB\n" +
                    "E9SeRYar2LN7Q8+baEqWZZdX9G+scoJiNrTvcAlXH+XsovaK6J5QhGhgLovOpe3j\n" +
                    "u75tAgMBAAGjEzARMA8GA1UdEwEB/wQFMAMBAf8wDQYJKoZIhvcNAQELBQADggEB\n" +
                    "AA8Zdx5u5I2k4O5GsxQK4TKi5vALsFUKawmz3KPJPTncpQDjfb0Fh0Tg0vmmrD/V\n" +
                    "CUV5SGA8kOGuYOXb0qHngNZoIQMKK+E4Vs0T7qXAgiPYVy3qlhVrhMbFMZFG27U3\n" +
                    "daMRpZnN5mAi7Is9ld9AQSxMS4A9d7YsoPddQuTw63Q4h2oclcxkLc8o5BRVJ6xm\n" +
                    "KhKld98kNogEoBqGZnJZwpKaq+tx8trA+DqcS50xo7N3kSlPQcbwsFZTAVREKR35\n" +
                    "AQ4h3C9hgRTw/QA1xqrMtBkdfFHttfvrsmecdgr5y6yG2SMnK6gHbO9sTsb9iEsN\n" +
                    "v8JwQxMAMbM+qE9ztwwTNqs=\n" +
                    "-----END CERTIFICATE-----\n";

    public static void InitSSLContext()
            throws CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnrecoverableKeyException {
        InitSSLContext(getCertificateChainInput());
    }

    private static void InitSSLContext(BufferedInputStream input)
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, UnrecoverableKeyException {
        Certificate[] chainList = getCertificateChain(input);

        TrustManagerFactory tmf = getTrustManagerFactory(chainList);
        KeyManagerFactory kmf = getKeyManagerFactory(chainList);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        setDefaultSocketContext(context);
    }

    static private KeyManagerFactory getKeyManagerFactory(Certificate[] chainList) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException, InvalidKeySpecException, InvalidKeyException, NoSuchPaddingException, UnrecoverableKeyException {
        KeyStore clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        clientKeyStore.load(null, null);
        clientKeyStore.setCertificateEntry("certificate", chainList[0]);
        clientKeyStore.setKeyEntry("private-key",
                getPrivateKey(),
                KEY_PASSPHASE.toCharArray(),
                chainList);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, KEY_PASSPHASE.toCharArray());

        return keyManagerFactory;
    }

    static private PrivateKey getPrivateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] encoded = Base64.decodeBase64(PRIVATE_KEY.getBytes("UTF-8"));

        EncryptedPrivateKeyInfo ekey=new EncryptedPrivateKeyInfo(encoded);
        Cipher cip=Cipher.getInstance(ekey.getAlgName());
        PBEKeySpec pspec=new PBEKeySpec(KEY_PASSPHASE.toCharArray());
        SecretKeyFactory skfac= SecretKeyFactory.getInstance(ekey.getAlgName());
        Key pbeKey=skfac.generateSecret(pspec);
        AlgorithmParameters algParams=ekey.getAlgParameters();
        cip.init(Cipher.DECRYPT_MODE,pbeKey,algParams);
        PKCS8EncodedKeySpec pkcs8KeySpec=ekey.getKeySpec(cip);
        KeyFactory rsaKeyFac=KeyFactory.getInstance(KEY_TYPE);
        return (PrivateKey)rsaKeyFac.generatePrivate(pkcs8KeySpec);

        /*PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance(KEY_TYPE);
        PrivateKey privKey = kf.generatePrivate(keySpec);

        return privKey;*/
    }

    static private TrustManagerFactory getTrustManagerFactory(Certificate[] chainList)
            throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(null, null);

        ks.setCertificateEntry(Integer.toString(1), chainList[chainList.length-1]);
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        return tmf;
    }

    static private void setDefaultSocketContext(SSLContext context) {
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
    }

    private static Certificate[] getCertificateChain(BufferedInputStream input) throws CertificateException {
        Collection<? extends Certificate> chain = CertificateFactory.getInstance("X.509").generateCertificates(input);
        List list = new ArrayList(chain);
        Certificate[] certificates = new Certificate[list.size()];
        list.toArray(certificates);
        return certificates;
    }

    protected static BufferedInputStream getCertificateChainInput() {
        BufferedInputStream rootCaInputStream = new BufferedInputStream(
                new ByteArrayInputStream(CLIENT_CERTIFICATE.getBytes()));
        return rootCaInputStream;
    }
}
