import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

public class test {
    @Test
    public void test() {
        JwtBuilder builder = Jwts.builder().setId("000").setSubject("a").setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "itcast");
        System.out.println(builder.compact());
    }

    @Test
    public void testParseJwt() {
        String compactJwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwMDAiLCJzdWIiOiJhIiwiaWF0IjoxNjMyODQwNjg2fQ.WLiUckbymSYtqTUs2igfg8bYoOHJYy9HCPzgQF5ZYCA";
        Claims claims = Jwts.parser().
                setSigningKey("itcast").
                parseClaimsJws(compactJwt).
                getBody();
        System.out.println(claims);
    }
}
