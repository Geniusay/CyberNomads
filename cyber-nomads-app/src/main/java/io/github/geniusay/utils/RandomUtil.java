package io.github.geniusay.utils;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.UUID;

public class RandomUtil {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final String BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateId() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return encodeBase62(byteBuffer.array()).substring(0, 20); // 限制为20位
    }

    // 将UUID转换为Base62编码
    public static String encodeBase62(byte[] uuidBytes) {
        BigInteger bigInt = new BigInteger(1, uuidBytes);
        StringBuilder sb = new StringBuilder();
        while (bigInt.compareTo(BigInteger.ZERO) > 0) {
            sb.append(BASE62.charAt(bigInt.mod(BigInteger.valueOf(62)).intValue()));
            bigInt = bigInt.divide(BigInteger.valueOf(62));
        }
        return sb.reverse().toString();
    }

    public static String generateRandomString(int len) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }
}
