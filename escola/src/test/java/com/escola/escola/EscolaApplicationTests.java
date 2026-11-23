package com.escola.escola;

import com.escola.escola.enums.UserChapaRole;
import com.escola.escola.models.Chapa;
import com.escola.escola.models.User;
import com.escola.escola.models.UsersChapa;
import com.escola.escola.repository.ChapaRepository;
import com.escola.escola.repository.UserRepository;
import com.escola.escola.repository.UsersChapaRepository;
import com.escola.escola.repository.VotoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class EscolaApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ChapaRepository chapaRepository;

	@Autowired
	private UsersChapaRepository usersChapaRepository;

	@Autowired
	private VotoRepository votoRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Test
	void contextLoadsAndStudentsAreImported() {
		assertThat(userRepository.count()).isGreaterThan(100);
		assertThat(userRepository.findByRa("RA")).isEmpty();
	}

	@Test
	void studentCanLoginWithEurinuAndEurinyPasswordsAndWithOrWithoutLeadingZeros() throws Exception {
		User sampleStudent = userRepository.findByRa("000115778975").orElseThrow();
		assertThat(passwordEncoder.matches("Eurinu@2026", sampleStudent.getPassword())).isTrue();
		assertThat(passwordEncoder.matches("Euriny@2026", sampleStudent.getPassword())).isTrue();

		// Login com RA completo e senha Eurinu@2026
		mockMvc.perform(formLogin("/login").user("000115778975").password("Eurinu@2026"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/home"))
				.andExpect(authenticated().withUsername("000115778975"));

		// Login com RA sem zeros à esquerda e senha Euriny@2026
		mockMvc.perform(formLogin("/login").user("115778975").password("Euriny@2026"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/home"))
				.andExpect(authenticated().withUsername("000115778975"));
	}

	@Test
	void fullElectionCycleAdminCreatesEditsChapaManagesMembersAndStudentVotes() throws Exception {
		User aluno1 = userRepository.findByRa("000115778975").orElseThrow();
		User aluno2 = userRepository.findByRa("000120066425").orElseThrow();

		// 1. Admin cria uma nova chapa
		mockMvc.perform(post("/chapas/salvar")
						.with(csrf())
						.with(user("emmanuel").roles("ADMIN"))
						.param("name", "Chapa União Estudantil")
						.param("number", "10")
						.param("description", "Unidos pela escola")
						.param("objective", "Melhorar quadra e eventos culturais"))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("mensagemSucesso"));

		List<Chapa> chapas = chapaRepository.findAll();
		Chapa novaChapa = chapas.stream()
				.filter(c -> "Chapa União Estudantil".equals(c.getName()))
				.findFirst()
				.orElseThrow();

		// 2. Admin gerencia os membros da chapa
		mockMvc.perform(get("/chapas/" + novaChapa.getId() + "/membros")
						.with(user("emmanuel").roles("ADMIN")))
				.andExpect(status().isOk())
				.andExpect(view().name("chapas/gerenciarMembros"));

		mockMvc.perform(post("/chapas/" + novaChapa.getId() + "/membros")
						.with(csrf())
						.with(user("emmanuel").roles("ADMIN"))
						.param("membros[0].role", UserChapaRole.ROLE_PRESIDENTE.name())
						.param("membros[0].userId", aluno1.getId().toString())
						.param("membros[1].role", UserChapaRole.ROLE_VICE_PRESIDENTE.name())
						.param("membros[1].userId", aluno2.getId().toString()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/chapas"))
				.andExpect(flash().attributeExists("mensagemSucesso"));

		List<UsersChapa> membrosSalvos = usersChapaRepository.findByChapaId(novaChapa.getId());
		assertThat(membrosSalvos).hasSize(2);

		// 3. Admin visualiza página de edição e atualiza informações da chapa
		mockMvc.perform(get("/chapas/" + novaChapa.getId())
						.with(user("emmanuel").roles("ADMIN")))
				.andExpect(status().isOk())
				.andExpect(view().name("chapas/details"));

		mockMvc.perform(post("/chapas/" + novaChapa.getId() + "/salvar")
						.with(csrf())
						.with(user("emmanuel").roles("ADMIN"))
						.param("name", "Chapa União Renovada")
						.param("number", "15")
						.param("description", "Slogan atualizado")
						.param("objective", "Objetivos atualizados"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/chapas"))
				.andExpect(flash().attributeExists("mensagemSucesso"));

		Chapa chapaAtualizada = chapaRepository.findById(novaChapa.getId()).orElseThrow();
		assertThat(chapaAtualizada.getName()).isEqualTo("Chapa União Renovada");
		assertThat(chapaAtualizada.getNumber()).isEqualTo(15);

		// 4. Aluno acessa a urna (/votos/votar) e vota na chapa
		mockMvc.perform(get("/votos/votar")
						.with(user(aluno1.getRa()).roles("COMUM")))
				.andExpect(status().isOk())
				.andExpect(view().name("votar"))
				.andExpect(model().attributeExists("listaChapas"));

		mockMvc.perform(post("/votos")
						.with(csrf())
						.with(user(aluno1.getRa()).roles("COMUM"))
						.param("chapa.id", novaChapa.getId().toString()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/votos/votar"))
				.andExpect(flash().attributeExists("mensagemSucesso"));

		assertThat(votoRepository.countVotos(novaChapa.getId())).isEqualTo(1L);

		// 5. Aluno tenta votar novamente e é bloqueado
		mockMvc.perform(post("/votos")
						.with(csrf())
						.with(user(aluno1.getRa()).roles("COMUM"))
						.param("chapa.id", novaChapa.getId().toString()))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/votos/votar"))
				.andExpect(flash().attributeExists("mensagemErro"));

		assertThat(votoRepository.countVotos(novaChapa.getId())).isEqualTo(1L);

		// 6. Admin consulta painel de apuração (/votos/chapas)
		mockMvc.perform(get("/votos/chapas")
						.with(user("emmanuel").roles("ADMIN")))
				.andExpect(status().isOk())
				.andExpect(view().name("chapas/contagemVotos"))
				.andExpect(model().attributeExists("resultadoVotacao"))
				.andExpect(model().attributeExists("totalGeralVotos"));
	}
}
