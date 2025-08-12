package org.knowm.xchange.bitz.service;

import jakarta.ws.rs.FormParam;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.stream.Collectors;
import si.mazi.rescu.ParamsDigest;
import si.mazi.rescu.RestInvocation;

public class BitZDigest implements ParamsDigest {

  private final MessageDigest md5;

  public BitZDigest() throws NoSuchAlgorithmException {
    this.md5 = MessageDigest.getInstance("MD5");
  }

  public static BitZDigest createInstance() {
    try {
      return new BitZDigest();
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Unable to load MD5 digest", e);
    }
  }

  @Override
  public String digestParams(RestInvocation restInvocation) {
    // Get Parameters
    Map<String, String> params = restInvocation.getParamsMap().get(FormParam.class).asHttpHeaders();

    // Order By Key Alphabetically, Concancecate Values
    byte[] unsigned =
        params.entrySet().stream()
            .sorted(Map.Entry.<String, String>comparingByKey())
            .filter(e -> !e.getKey().equalsIgnoreCase("sign"))
            .map(e -> e.getValue())
            .collect(Collectors.joining())
            .getBytes(StandardCharsets.UTF_8);

    byte[] digest = md5.digest(unsigned);
    StringBuilder hex = new StringBuilder(digest.length * 2);
    for (byte b : digest) {
      hex.append(String.format("%02x", b));
    }
    return hex.toString();
  }
}
