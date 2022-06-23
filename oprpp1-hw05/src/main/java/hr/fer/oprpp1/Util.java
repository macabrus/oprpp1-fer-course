package hr.fer.oprpp1;

public class Util {

  private static final String DIGITS = "0123456789abcdef";
  private static final String DIGITS_CAPITALIZED = "0123456789ABCDEF";

  public static byte[] hexToBuff(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2)
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
        + Character.digit(s.charAt(i+1), 16));
    return data;
  }


  public static String buffToHex(byte[] raw) {
    final StringBuilder hex = new StringBuilder(2 * raw.length);
    for (final byte b : raw) {
      hex.append(DIGITS.charAt((b & 0xF0) >> 4)).append(DIGITS.charAt((b & 0x0F)));
    }
    return hex.toString();
  }

  public static String byteToHex(byte b) {
    return String.valueOf(DIGITS_CAPITALIZED.charAt((b & 0xF0) >> 4)) + DIGITS_CAPITALIZED.charAt((b & 0x0F));
  }
}
