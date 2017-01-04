package dev.wolveringer.bungeeutil.profile;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UUIDFetcher implements Callable<Map<String, UUID>> {

	private static LoadingCache<String, UUID> uuidCache = CacheBuilder.newBuilder().maximumSize(500).expireAfterWrite(4, TimeUnit.HOURS).build(new CacheLoader<String, UUID>() {
		@Override
		public UUID load(String name) throws Exception {
			return new UUIDFetcher(Arrays.asList(name)).call().get(name);
		};
	});

    private static final double PROFILES_PER_REQUEST = 100;
    private static final String PROFILE_URL = "https://api.mojang.com/profiles/minecraft";
    private static HttpURLConnection createConnection() throws Exception {
        URL url = new URL(PROFILE_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        return connection;
    }
    public static UUID fromBytes(byte[] array) {
        if (array.length != 16) {
            throw new IllegalArgumentException("Illegal byte array length: " + array.length);
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        long mostSignificant = byteBuffer.getLong();
        long leastSignificant = byteBuffer.getLong();
        return new UUID(mostSignificant, leastSignificant);
    }
    private static UUID getUUID(String id) {
        return UUID.fromString(id.substring(0, 8) + "-" + id.substring(8, 12) + "-" + id.substring(12, 16) + "-" + id.substring(16, 20) + "-" +id.substring(20, 32));
    }

    public static UUID getUUIDOf(String name) throws Exception {
        return uuidCache.get(name);
    }

    public static byte[] toBytes(UUID uuid) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        return byteBuffer.array();
    }

    private static void writeBody(HttpURLConnection connection, String body) throws Exception {
        OutputStream stream = connection.getOutputStream();
        stream.write(body.getBytes());
        stream.flush();
        stream.close();
    }

    private final JsonParser jsonParser = new JsonParser();

    private final List<String> names;

    private final boolean rateLimiting;

    public UUIDFetcher(List<String> names) {
        this(names, true);
    }

    public UUIDFetcher(List<String> names, boolean rateLimiting) {
        this.names = ImmutableList.copyOf(names);
        this.rateLimiting = rateLimiting;
    }

    @Override
	public Map<String, UUID> call() throws Exception {
        Map<String, UUID> uuidMap = new HashMap<String, UUID>();
        int requests = (int) Math.ceil(this.names.size() / PROFILES_PER_REQUEST);
        for (int i = 0; i < requests; i++) {
            HttpURLConnection connection = createConnection();
            JSONArray obj = new JSONArray();
            for(String s : this.names.subList(i * 100, Math.min((i + 1) * 100, this.names.size()))) {
				obj.put(s);
			}
            String body = obj.toString();
            writeBody(connection, body);
            JsonArray array = (JsonArray) this.jsonParser.parse(new InputStreamReader(connection.getInputStream()));
            for (Object profile : array) {
            	JsonObject jsonProfile = (JsonObject) profile;
                String id = jsonProfile.get("id").getAsString();
                String name = jsonProfile.get("name").getAsString();
                UUID uuid = UUIDFetcher.getUUID(id);
                uuidMap.put(name, uuid);
            }
            if (this.rateLimiting && i != requests - 1) {
                Thread.sleep(100L);
            }
        }
        return uuidMap;
    }
}