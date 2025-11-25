package br.com.alunoonline.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe utilitária para gerar hashes BCrypt de senhas
 * Usado apenas para gerar senhas para os scripts de migração Flyway
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Senha padrão para todos os usuários de teste: "senha123"
        String senha = "senha123";
        
        System.out.println("Gerando hash BCrypt para senha: " + senha);
        System.out.println("Hash: " + encoder.encode(senha));
        System.out.println("\nCopie este hash para usar nos scripts SQL de migração");
    }
}

