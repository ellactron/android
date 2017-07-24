package com.ellactron.configuration;

import android.content.Context;

import com.ellactron.activities.R;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

/**
 * Created by ji.wang on 2017-07-24.
 */

public class AppConfiguration {
    static private AppConfiguration appConfiguration;

    public static synchronized AppConfiguration CreateConfiguration(Context context) {
        if (null == appConfiguration) {
            appConfiguration = new AppConfiguration(context);
        }

        return appConfiguration;
    }

    private Context context;

    private AppConfiguration(Context context) {
        this.context = context;
    }

    final static private String KEY_PASSPHASE = "pa55w0rd";

    public static String getPrivateKeyPassphase() {
        return KEY_PASSPHASE;
    }

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

    public static byte[] getPrivateKey() throws UnsupportedEncodingException {
        return Base64.decodeBase64(PRIVATE_KEY.getBytes("UTF-8"));
    }

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

    public static byte[] getCertificates() {
        return CLIENT_CERTIFICATE.getBytes();
    }

    final static String PROTOCOL_SCHEME = "https://";

    final static String USER_SERVICE_PORT = "8443";

    public String getUserServiceBaseUrl() {
        return PROTOCOL_SCHEME + context.getString(R.string.hostname) + ":" + USER_SERVICE_PORT;
    }

    final static String UI_SERVICE_PORT = "8444";

    public String getUIServiceBaseUrl() {
        return PROTOCOL_SCHEME + context.getString(R.string.hostname) + ":" + UI_SERVICE_PORT;
    }
}
