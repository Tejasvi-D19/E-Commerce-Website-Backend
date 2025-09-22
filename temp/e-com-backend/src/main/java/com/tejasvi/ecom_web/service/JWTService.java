package com.tejasvi.ecom_web.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;



@Service
public class JWTService {
	
	@Value("${jwt.secretKey}")
	private String secretKey;
	
//	public JWTService() {
//			
//			try {
//				KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//				SecretKey key = keyGen.generateKey();
//				secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
//				
//				System.out.println("Base64 encoded secrete key ---- " + secretKey);
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			}
//			
//		}
//	
	
	public String getJWT(String userName) {
		Map<String, Object> claims = new HashMap<>();
		
		String token =  Jwts.builder()
				.claims()
				.add(claims)
				.subject(userName)
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 12))
				.and()
				.signWith(getKey())
				.compact();
		System.out.println("Secret key at generation = "+getKey());
		
		return token;
		
	}
	
	private SecretKey getKey() {
		
		//System.out.println("Base64 encoded secrete key ---- " + secretKey);
		byte[] keyByte = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyByte);
		
		//return secretKey;
	}


	
	
	public String extractUserName(String token) {
		System.out.println("Entered inside extractUserName()");
	    String username =  extractClaim(token, Claims::getSubject);
	    System.out.println("User name inside extractUSerName Method = "+username);
	    return username;
	}

	
	// ---------- Helper methods ----------

	private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		System.out.println("Entered in ectractClaim");
	    final Claims claims = extractAllClaims(token);
	    return claimsResolver.apply(claims);
	}

	/** Parse the token and return all claims (signature is validated here). */
	private Claims extractAllClaims(String token) {
		
		System.out.println("Secret key at validation inside extract All claims= "+getKey());
		
	    Claims clm = Jwts.parser()
	            .verifyWith(getKey())
	            .build()
	            .parseSignedClaims(token)   
	            .getPayload();
	    
	    
	    System.out.println();
	    System.out.println("Extracte claims form the token = ");
	    clm.forEach((key, value) -> {
            System.out.println(key + " : " + value);
        });
	    
	    return clm;
	}
	
	
	

	
	
	/**
	 * Validate token for a given UserDetails:
	 * - verifies signature (during parse)
	 * - checks token is not expired
	 * - checks username matches
	 *
	 * Returns false if token is invalid, expired, or parsing throws.
	 */
	public boolean validate(String token, UserDetails userDetails) {
	    try {
	    	
	        final String username = extractUserName(token);
	        System.out.println();
	        System.out.println("User name extracted inside the validation = "+ username);
	        System.out.println();
	        System.out.println("User Details extracted using user name inside validation");
			System.out.println("Username: " + userDetails.getUsername());
			System.out.println("Password: " + userDetails.getPassword());
			System.out.println("Authorities: " + userDetails.getAuthorities());
	        return (username != null
	                && username.equals(userDetails.getUsername())
	                && !isTokenExpired(token));
	    } catch (JwtException | IllegalArgumentException ex) {
	        // JwtException covers signature exceptions, malformed tokens, expired tokens (some variants).
	        // In production, log the exception (but never log the token itself).
	        return false;
	    }
	}

	private Date extractExpiration(String token) {
	    return extractClaim(token, Claims::getExpiration);
	}

	private boolean isTokenExpired(String token) {
	    Date expiration = extractExpiration(token);
	    return expiration != null && expiration.before(new Date());
	}



	
	
	
}
