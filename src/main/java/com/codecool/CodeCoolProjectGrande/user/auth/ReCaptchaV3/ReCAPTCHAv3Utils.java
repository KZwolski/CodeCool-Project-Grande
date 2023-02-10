package com.codecool.CodeCoolProjectGrande.user.auth.ReCaptchaV3;

import com.codecool.CodeCoolProjectGrande.user.auth.ReCaptchaV3.ReCAPTCHAv3Exception;
import com.codecool.CodeCoolProjectGrande.user.auth.ReCaptchaV3.ReCAPTCHAv3Response;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class ReCAPTCHAv3Utils {

    private static ObjectMapper mapper = new ObjectMapper();

    @Value("${reCaptchaSecretKey}")
    private static final String SECRET_KEY = "6LcYmGwkAAAAAF3r0pZ0YvPh_kKJUxj4YszxaDy5";

    private ReCAPTCHAv3Utils() {
    }

    private static String readStream(InputStream stream) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                builder.append(line);
            }
        }
        return builder.toString();
    }

    private static ReCAPTCHAv3Response readObject(InputStream stream) throws IOException {
        String response = readStream(stream);
        return mapper.readValue(response, ReCAPTCHAv3Response.class);
    }

    public static ReCAPTCHAv3Response request(String token, String ip) throws ReCAPTCHAv3Exception {
        int retries = 3;
        int retryInterval = 5000; // 5 seconds
        while (retries > 0) {
            try {
                URL url = new URL("https://www.google.com/recaptcha/api/siteverify?secret="
                        + SECRET_KEY + "&response=" + token + "&remoteip=" + ip);
                System.out.println(url);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try {
                    System.out.println("1");
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    System.out.println("2");
                    ReCAPTCHAv3Response response = readObject(connection.getInputStream());
                    System.out.println(response.getAction());
                    if (response.getSuccess()) {
                        System.out.println("3");
                        return response;
                    } else if (response.getAction().contains("timeout-or-duplicate")) {
                        System.out.println("4");
                        retries--;
                        Thread.sleep(retryInterval);
                    } else {
                        System.out.println("5");
                        throw new ReCAPTCHAv3Exception(response.getErrors());
                    }
                } finally {
                    System.out.println("6");
                    connection.disconnect();
                }
            } catch (Exception e) {
                System.out.println("7");
                throw new ReCAPTCHAv3Exception("Verification failed", e);
            }
        }
        System.out.println("8");
        throw new ReCAPTCHAv3Exception("Verification failed after maximum number of retries");
    }
}