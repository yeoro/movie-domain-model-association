package org.eternity.domainmodel.movie.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.eternity.domainmodel.generic.Money;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Screening {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int sequence;
    private LocalDateTime screeningTime;

    @ManyToOne
    @JoinColumn(name= "MOVIE_IE")
    private Movie movie;

    public Screening(Movie movie, int sequence, LocalDateTime screeningTime) {
        this.movie = movie;
        this.movie.addScreening(this);
        this.sequence = sequence;
        this.screeningTime = screeningTime;
    }

    public void changeMovie(Movie movie) {
        if (this.movie != null) {
            this.movie.removeScreening(this);
        }

        this.movie = movie;
        this.movie.addScreening(this);
    }

    public Money getFixedFee() {
        return movie.getFee();
    }


}
