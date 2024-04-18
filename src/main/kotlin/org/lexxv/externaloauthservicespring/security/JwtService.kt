package org.lexxv.externaloauthservicespring.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import java.io.File
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

/**
 * Сервис обработки авторизационного токена
 *
 * @author alexey vorobyev <mister.alex49@yandex.ru>
 * */
@Service
class JwtService {
    /**
     * Загрузка RSA ключа из директории
     * */
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
        val publicKey: PublicKey = loadPublicKey()
        return validateToken(accessToken, publicKey)
    }

    private fun validateToken(token: String, secret: PublicKey): Boolean {
        Jwts.parser()
            .verifyWith(secret)
            .build()
            .parse(token)
        return true
    }

    fun getClaims(token: String): Claims {
        return Jwts.parser()
            .build()
            .parseSignedClaims(token)
            .payload
    }
}