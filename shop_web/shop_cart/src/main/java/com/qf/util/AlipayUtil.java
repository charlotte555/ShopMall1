package com.qf.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

public class AlipayUtil {

    private static AlipayClient alipayClient;

    public static final String ALIPAY_PUBLICK_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA9Ew70+n/IEePrVL/ezxv\n" +
            "LGLo8r4pcL4LnyJKLzyCCN3QQRXljtFfSCzTpR96jNx/G57eGVEnRvk8MIvpIdek\n" +
            "SpqIH8gYPbb9bW0bLmIKvfcRhNNiKapo3SDQ5IzZ9THAk9+sFsCQe5rdU+yEOXFg\n" +
            "+6q8Ej62P5myvxtnpfFYWEc0Q1BIipNMkzmGL1kbLAF7HwrPH1MJgTNpKRpjKv1e\n" +
            "SCBtMnBotrDNtHPgGjAqRBLp32hGFgpmtfeVJN7nlupNZbpH2BZGKVK73fNbvFgl\n" +
            "UlVPB+j53xjx2B05cE9eqxCvODRbObSm9l/2GV7w+r2Jcb5Vm2Qf10Alyi3U9du8\n" +
            "vQIDAQAB";

    static{
        alipayClient = new DefaultAlipayClient(
                "https://openapi.alipaydev.com/gateway.do",
                "2016073000127352",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQD0TDvT6f8gR4+t\n" +
                        "Uv97PG8sYujyvilwvgufIkovPIII3dBBFeWO0V9ILNOlH3qM3H8bnt4ZUSdG+Tww\n" +
                        "i+kh16RKmogfyBg9tv1tbRsuYgq99xGE02IpqmjdINDkjNn1McCT36wWwJB7mt1T\n" +
                        "7IQ5cWD7qrwSPrY/mbK/G2el8VhYRzRDUEiKk0yTOYYvWRssAXsfCs8fUwmBM2kp\n" +
                        "GmMq/V5IIG0ycGi2sM20c+AaMCpEEunfaEYWCma195Uk3ueW6k1lukfYFkYpUrvd\n" +
                        "81u8WCVSVU8H6PnfGPHYHTlwT16rEK84NFs5tKb2X/YZXvD6vYlxvlWbZB/XQCXK\n" +
                        "LdT127y9AgMBAAECggEAGrwNAA/nf5zVOF6Q3ueqMu1QGOB+oHvrwap6+9hfUqET\n" +
                        "nUvMJ2j6SGe/cZ6URr4Kmvczzwl2hunXkGh/RtjWfeOTagb3wCk6arM5+kV4b3mO\n" +
                        "TT7kyzJ+71QcCCbx40dij8w2hPbl2OEFv51uiurBdkkk88oxH5yuVwOkwCA9Tsul\n" +
                        "S5Jb95u2U8hbHpcAXb2Zt70G7UdiyaWArih+DnyPsfeLpzbBHxcnXfvfVTdstJYH\n" +
                        "vS2sIeQCDp3OO/XLyp/0Vgj8W2V8KVFG3zbRcJbUYZA4eURjDVERVO5ngPbMjpGY\n" +
                        "ASzuLITmnhbq3dWLs8dNE2bDXGsOiG1iFZ6m6ghc8QKBgQD8e/oJheYNcn+edBvC\n" +
                        "hpXZk6OVkt5M/Z3Twwt5aYnZL4GOWPTdEZEw0GpVfwZsv5H/1b7K4Q5dW84+cfKt\n" +
                        "eLBSQI4ppGYRgVMjBBoiKRXi2fdh25Njax3E51lJWbAEXGg7efS3P0cQM0ufRq5U\n" +
                        "VDJP3kWmZUOdLVp3vgMqLgDzswKBgQD3sxMoPxWMPuY0lVPxP7FTZcE45S5YP1OJ\n" +
                        "j2v/EOKF3NLtK+wKyzw5MQV/JSFj/uizqvNAHNmhB+0y8QemL4L6N6+sZQYH3eq+\n" +
                        "q2KyPkvvEvkyFONathU1lBy62tv6mPfjJ0yoESmoXBUE14eUdwjWrXZxtBz6eO1x\n" +
                        "Zxbx3AMVzwKBgQDGLsJbC0FCuOc3h0fq7x1chajX1UqtAD4MZILzMEZFtiZph6Xc\n" +
                        "iNeAi3eL2INAhTairDJwqStPWKqRFXw5cgxk48szIJoFj4+kYqnouns1b4XgyCvu\n" +
                        "07uwPTA2hTHGmooudVIDLKYpLzpisWZRRy2ex4fATqg+DcV/TCTLzQRuRQKBgFPl\n" +
                        "7dk19OB59ZNd+P6/6horXxUcW2Q8ntj7IUNVvsRElZULyXmmeRGODySPuQ2bq8of\n" +
                        "XyEaJISsoKUeis65TQ3firtDxJUpj3dhqZ5iB2pE7O8PsCprfieVsV2A//2TjnL8\n" +
                        "PrS2i3uhJOVs81lQMEg2nOXKcf5Ms3Az0Mu/D7R1AoGAYTO5Rcv1Ab9jI3L9iWg2\n" +
                        "F80taU0PMq2SAXo+xhE2PLu4pzfdMxgLKU21NKOMOSxwkFN0fkEvMI6YSGVCyXUd\n" +
                        "UjEE/3ypfJlO2jqpQ0OCveCcWguomhbbarcubDTXS48WLCvLwxo7WGVNB/kdTWh3\n" +
                        "nY1ePNi6X6Lj6apY50etzBE=",
                "json",
                "UTF-8",
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB",
                "RSA"); //获得初始化的AlipayClient
    }

    public static AlipayClient getAlipayClient(){
       return alipayClient;
    }
}
