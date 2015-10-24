package org.ikernits.sample.crypto;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Created by ikernits on 24/10/15.
 */
public class TestCrypto {

    private static byte[] extractKeyFromPem(InputStream pemKeyStream,
                                            String startMarker, String endMarker) throws IOException, InvalidKeyException {
        BufferedReader br = new BufferedReader(new InputStreamReader(pemKeyStream));
        StringBuilder base64Pem = new StringBuilder();

        boolean startLineValid = false;
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if (line.contains(startMarker)) {
                startLineValid = true;
                break;
            }
        }
        if (!startLineValid) {
            throw new InvalidKeyException("failed to detect start of PEM key: '" + startMarker + "'");
        }

        boolean endLineValid = false;
        for (String line = br.readLine(); line != null; line = br.readLine()) {
            if (line.contains(endMarker)) {
                endLineValid = true;
                break;
            }
            base64Pem.append(line);
        }

        if (!endLineValid) {
            throw new InvalidKeyException("failed to detect end of PEM key: '" + endMarker + "'");
        }

        return Base64.getDecoder().decode(base64Pem.toString());
    }

    public static void main(String[] args) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        Security.addProvider(new BouncyCastleProvider());
        PEMReader pemReader = new PEMReader(new FileReader("../crypto/mykey.pub.pem"));
        PublicKey keyPair = (PublicKey)pemReader.readObject();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair);
        cipher.update("test string".getBytes());
        byte[] encrypted = cipher.doFinal();

        FileUtils.writeByteArrayToFile(new File("../crypto/encrypted.dat"), encrypted);
    }
}
