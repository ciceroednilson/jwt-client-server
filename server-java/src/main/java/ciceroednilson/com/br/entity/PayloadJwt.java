package ciceroednilson.com.br.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayloadJwt {

    private String body;
    private int iat;
    private int exp;
    private String aud;
    private String iss;
    private String sub;
}
