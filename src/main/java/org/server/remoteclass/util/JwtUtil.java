package org.server.remoteclass.util;

import io.jsonwebtoken.Jwts;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    Environment env;

    public JwtUtil(Environment env){
        this.env = env;
    }
}
