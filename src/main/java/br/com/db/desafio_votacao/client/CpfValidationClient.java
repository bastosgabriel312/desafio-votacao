package br.com.db.desafio_votacao.client;

public interface CpfValidationClient {
    boolean isCpfValid(String cpf);
    String checkVotingEligibility(String cpf);
}