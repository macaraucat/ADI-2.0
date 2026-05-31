package cl.aduana.ms_risk.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.aduana.ms_risk.model.CriterioRiesgo;
import cl.aduana.ms_risk.model.CriterioRiesgo.NivelAlerta;
import cl.aduana.ms_risk.repository.CriterioRiesgoRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CriterioRiesgoRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            CriterioRiesgo c1 = new CriterioRiesgo();
            c1.setDescripcion("Sujeto con antecedentes penales");
            c1.setNivelAlerta(NivelAlerta.ALTO);

            CriterioRiesgo c2 = new CriterioRiesgo();
            c2.setDescripcion("Acompañante con antecedentes penales");
            c2.setNivelAlerta(NivelAlerta.ALTO);

            CriterioRiesgo c3 = new CriterioRiesgo();
            c3.setDescripcion("Patente con historial de infracciones");
            c3.setNivelAlerta(NivelAlerta.MEDIO);

            CriterioRiesgo c4 = new CriterioRiesgo();
            c4.setDescripcion("Tramite sin inconsistencias");
            c4.setNivelAlerta(NivelAlerta.BAJO);

            repository.saveAll(List.of(c1, c2, c3, c4));
        }
    }
}