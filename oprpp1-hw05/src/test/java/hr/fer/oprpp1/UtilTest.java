package hr.fer.oprpp1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {

  @Test
  void hexToBuff() {
    assertArrayEquals(new byte[] {10}, Util.hexToBuff("0a"));
  }

  @Test
  void buffToHex() {
    assertEquals("ffffff", Util.buffToHex(new byte[] {-1, -1, -1}));
  }

  @Test
  void byteToHex() {
    assertEquals("0F", Util.byteToHex((byte) 15));
  }
}