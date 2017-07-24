package com.ellactron.http.security;

import com.ellactron.configuration.AppConfiguration;

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
                AppConfiguration.getPrivateKeyPassphase().toCharArray(),
                chainList);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(clientKeyStore, AppConfiguration.getPrivateKeyPassphase().toCharArray());

        return keyManagerFactory;
    }

    static private PrivateKey getPrivateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException {
        byte[] encoded = AppConfiguration.getPrivateKey();

        EncryptedPrivateKeyInfo ekey=new EncryptedPrivateKeyInfo(encoded);
        Cipher cip=Cipher.getInstance(ekey.getAlgName());
        PBEKeySpec pspec=new PBEKeySpec(AppConfiguration.getPrivateKeyPassphase().toCharArray());
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
                new ByteArrayInputStream(AppConfiguration.getCertificates()));
        return rootCaInputStream;
    }
}
