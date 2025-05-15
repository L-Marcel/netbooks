package app.netbooks.backend.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

import app.netbooks.backend.errors.CannotConvertUuid;

public class UUIDUtils {
    public static byte[] toBytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    };

    public static UUID fromBytes(byte[] bytes) {
        if (bytes == null || bytes.length != 16)
            throw new CannotConvertUuid();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long high = buffer.getLong();
        long low = buffer.getLong();
        return new UUID(high, low);
    };
};
