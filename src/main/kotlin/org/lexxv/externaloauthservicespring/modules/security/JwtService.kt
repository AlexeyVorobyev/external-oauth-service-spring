package org.lexxv.externaloauthservicespring.modules.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import java.io.File
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*


@Service
class JwtService {
    private val publicKey: PublicKey = loadPublicKey()

    private fun loadPublicKey(): PublicKey {
        val file = File("src/main/resources/keys/rsa.key.pub")
        val publicKeyPEM = file.readText()
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\n", "")
        val keyBytes = Base64.getDecoder().decode(publicKeyPEM)
        val keySpec = X509EncodedKeySpec(keyBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(keySpec)
    }

    fun validateAccessToken(accessToken: String): Boolean {
        return validateToken(accessToken, publicKey)
    }

    private fun validateToken(token: String, secret: PublicKey): Boolean {
        Jwts.parser()
            .verifyWith(secret)
            .build()
            .parse(token)
        return true
    }

    fun getAccessClaims(token: String): Claims {
        return getClaims(token, publicKey)
    }

    private fun getClaims(token: String, secret: PublicKey): Claims {
        return Jwts.parser()
            .verifyWith(secret)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}