package hr.fer.oprpp1;

import hr.fer.zemris.java.hw06.shell.Prompt;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;

public class Crypto {

  public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException {
//    Prompt prompt = new Prompt("crypto > "); // interactive mode
//    prompt.registerAction("checksha", new CheckSHAAction("SHA-256"));
//    prompt.registerAction("encrypt", new CipherAction("AES/CBC/PKCS5Padding", Cipher.ENCRYPT_MODE));
//    prompt.registerAction("decrypt", new CipherAction("AES/CBC/PKCS5Padding", Cipher.DECRYPT_MODE));
//    prompt.setExitAction("exit");
//    if (!(args.length > 0))
//      throw new IllegalArgumentException("Expected one of following arguments: " +
//        String.join(", ", prompt.getActions().keySet()));

//    prompt.setInputStream(new ByteArrayInputStream(String.join(" ", args).getBytes(StandardCharsets.UTF_8)));
//    prompt.setInputStream(System.in);
//    prompt.executeCommand();

    // encrypt /Users/bernard/Downloads/tus_module.zip /Users/bernard/Downloads/tus_module.zip.crypt
    // decrypt /Users/bernard/Downloads/tus_module.zip.crypt /Users/bernard/Downloads/tus_module.orig.zip
    // e52217e3ee213ef1ffdee3a192e2ac7e
    // 000102030405060708090a0b0c0d0e0f
    // checksha /Users/bernard/Downloads/tus_module.zip.crypt
    // cd9e01bb28967a5ad81390eb85d2eef31810c7ec7e7a5c0d3d303aefc6a055ef
    // decrypt
  }
}
