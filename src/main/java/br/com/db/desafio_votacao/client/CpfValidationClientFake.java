package br.com.db.desafio_votacao.client;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class CpfValidationClientFake implements CpfValidationClient {

    private final Random random = new Random();

    @Override
    public boolean isCpfValid(String cpf) {
        // Simula a validação de um CPF, retornando true ou false aleatoriamente
        return random.nextBoolean();
    }

    @Override
    public String checkVotingEligibility(String cpf) {
        if (!isCpfValid(cpf)) {
            return "INVALID_CPF";
        }
        // Simula a verificação de elegibilidade para votação, retornando ABLE_TO_VOTE ou UNABLE_TO_VOTE aleatoriamente
        return random.nextBoolean() ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE";
    }
}