package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jgci.springboot.app.account.entity.Account;
import com.jgci.springboot.app.account.models.Transaction;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }


    public static <T> T toObject(String json, Class<T> type) {
        try {
            JsonNode node = mapper.readTree(json);
            return mapper.treeToValue(node, type);
        } catch (Exception e) {
            throw new RuntimeException("Could not deserialize to " + type.getName() + e.getMessage());
        }

    }

    public static String getFileText(String name) throws IOException {
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
        return IOUtils.toString(reader);
    }

    public static Transaction generateValidTransaction() {
        return new Transaction("12345678",true, new BigDecimal(300));
    }

    public static ResponseEntity<Account> generateValidResponseEntity() {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(generateValidAccount());
    }

    public static Account generateValidAccount() {
        List<String> errors = new ArrayList<>();

        return new Account("12345678", new BigDecimal(500),2, errors);
    }
}
