package hr.fer.oprpp1;

public class Util {

  public static byte[] hexToBuff(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2)
      data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
        + Character.digit(s.charAt(i+1), 16));
    return data;
  }

  private static final String DIGITS = "0123456789abcdef";
  static String buffToHex(byte[] raw) {
    final StringBuilder hex = new StringBuilder(2 * raw.length);
    for (final byte b : raw) {
      hex.append(DIGITS.charAt((b & 0xF0) >> 4)).append(DIGITS.charAt((b & 0x0F)));
    }
    return hex.toString();
  }
}
