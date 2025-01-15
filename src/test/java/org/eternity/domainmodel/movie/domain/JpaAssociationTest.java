package org.eternity.domainmodel.movie.domain;

import jakarta.persistence.EntityManager;
import org.eternity.domainmodel.generic.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
public class JpaAssociationTest {
	@Autowired
	private EntityManager em;

	@Test
	public void mapping() {
		Movie movie = new Movie("한산", 120, Money.wons(10000));
		Screening screening = new Screening(movie,1 , LocalDateTime.of(2024, 12, 9, 9, 0));

		em.persist(movie);
		em.persist(screening);

		em.flush();
		em.clear();

		Screening loadedScreening = em.find(Screening.class, screening.getId());

		assertThat(loadedScreening.getFixedFee()).isEqualTo(Money.wons(10000));
	}

	@Test
	public void change_association() {
		Movie movie = new Movie("한산", 120, Money.wons(10000));
		Screening screening = new Screening(movie,1 , LocalDateTime.of(2024, 12, 9, 9, 0));

		em.persist(movie);
		em.persist(screening);

		em.flush();
		em.clear();

		Movie movieToChange = new Movie("아바타", 120, Money.wons(20000));
		Screening loadedScreening = em.find(Screening.class, 1L);
		loadedScreening.changeMovie(movieToChange);

		em.persist(movieToChange);

		em.flush();
		em.clear();

		loadedScreening = em.find(Screening.class, screening.getId());

		assertThat(loadedScreening.getFixedFee()).isEqualTo(Money.wons(20000));
	}

	@Test
	public void association_owner_not_mapped() {
		Movie movie = new Movie("한산", 120, Money.wons(10000));
		Screening screening = new Screening(movie,1 , LocalDateTime.of(2024, 12, 9, 9, 0));

		em.persist(movie);
		em.persist(screening);

		em.flush();
		em.clear();

		Movie movieToChange = new Movie("아바타", 120, Money.wons(20000));
		Screening loadedScreening = em.find(Screening.class, 1L);
		movieToChange.addScreening(loadedScreening);

		em.persist(movieToChange);

		em.flush();
		em.clear();

		loadedScreening = em.find(Screening.class, screening.getId());

		assertThat(loadedScreening.getMovie().getTitle()).isEqualTo("한산");
	}
}
