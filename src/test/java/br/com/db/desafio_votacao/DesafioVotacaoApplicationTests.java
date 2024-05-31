package br.com.db.desafio_votacao;

import br.com.db.desafio_votacao.api.v1.dto.mapper.AssociadoMapper;
import br.com.db.desafio_votacao.api.v1.dto.mapper.PautaMapper;
import br.com.db.desafio_votacao.api.v1.dto.mapper.SessaoVotacaoMapper;
import br.com.db.desafio_votacao.api.v1.dto.mapper.VotoMapper;
import br.com.db.desafio_votacao.service.AssociadoServiceV1Impl;
import br.com.db.desafio_votacao.service.PautaServiceV1Impl;
import br.com.db.desafio_votacao.service.SessaoVotacaoServiceV1Impl;
import br.com.db.desafio_votacao.service.VotoServiceV1Impl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class DesafioVotacaoApplicationTests {
	@Autowired
	private AssociadoServiceV1Impl associadoServiceV1;

	@MockBean
	private AssociadoMapper associadoMapper;

	@Autowired
	private PautaServiceV1Impl pautaServiceV1;

	@MockBean
	private PautaMapper pautaMapper;

	@Autowired
	private SessaoVotacaoServiceV1Impl sessaoVotacaoServiceV1;

	@MockBean
	private SessaoVotacaoMapper sessaoVotacaoMapper;

	@Autowired
	private VotoServiceV1Impl votoServiceV1;

	@MockBean
	private VotoMapper votoMapper;


	@Test
	void contextLoads() {
	}

}
