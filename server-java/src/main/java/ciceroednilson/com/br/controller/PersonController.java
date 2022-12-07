package ciceroednilson.com.br.controller;

import ciceroednilson.com.br.entity.PayloadJwt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final String DELIMITER = "\\.";

    private final Base64.Decoder decoder =  Base64.getDecoder();

    private final ObjectMapper mapper = new ObjectMapper();

    @PostMapping
    public @ResponseBody ResponseEntity<String> save(HttpEntity<String> httpEntity) throws JsonProcessingException {
        final String hashSha256 = Hashing.sha256().hashString(httpEntity.getBody(), StandardCharsets.UTF_8).toString();
        final String httpJwtHeader = jwtHeader(httpEntity);
        final String[] jwt = httpJwtHeader.split(DELIMITER);
        final String headerJWT = jwt[0];
        final String bodyJWT = jwt[1];
        final String signatureJWT = jwt[2];
        final PayloadJwt payloadJwt =  payloadJWT(bodyJWT);
        System.out.println(hashSha256);
        System.out.println(headerJWT);
        System.out.println(signatureJWT);
        System.out.println(hashSha256.equals(payloadJwt.getBody()));
        return ResponseEntity.ok(payloadJwt.getBody());
    }

    private PayloadJwt payloadJWT(final String bodyJWT) throws JsonProcessingException {
        final String payload = decodeBodyJWT(bodyJWT);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        return mapper.readValue(payload, PayloadJwt.class);
    }

    private String jwtHeader(HttpEntity<String> httpEntity) {
        return httpEntity.getHeaders().get("authentication").get(0);
    }

    private String decodeBodyJWT(final String bodyJWT) {
        return new String(decoder.decode(bodyJWT), StandardCharsets.UTF_8);
    }

    private String decodeHeaderJWT(final String headerJWT) {
        return new String(decoder.decode(headerJWT), StandardCharsets.UTF_8);
    }

}
