package ZeusFit.example.demo;

import ZeusFit.model.Corso;
import ZeusFit.model.Prenotazione;
import ZeusFit.model.Ruolo;
import ZeusFit.model.Utente;
import ZeusFit.repository.AbbonamentoRepository;
import ZeusFit.repository.CorsoRepository;
import ZeusFit.repository.PrenotazioneRepository;
import ZeusFit.repository.UtenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



import java.sql.Date;
import java.sql.Time;
import java.util.Collection;
import java.util.HashSet;

@Component
public class BootStrapData implements CommandLineRunner {

    private final UtenteRepository utenteRepository;
    private final PrenotazioneRepository prenotazioneRepository;
    private final CorsoRepository corsoRepository;
    private final AbbonamentoRepository abbonamentoRepository;


    public BootStrapData(UtenteRepository utenteRepository, PrenotazioneRepository prenotazioneRepository, CorsoRepository corsoRepository, AbbonamentoRepository abbonamentoRepository) {
        this.utenteRepository = utenteRepository;
        this.prenotazioneRepository = prenotazioneRepository;
        this.corsoRepository = corsoRepository;
        this.abbonamentoRepository = abbonamentoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Sono entrato");


        /*Time tempo = new Time(17,30,00);
        String data1 = "21/09/1998";
        String data2 = "22/3/1995";
        Time p1 = new Time(15,00,00);
        Time p2 = new Time(14,30,00);
        Ruolo r1 = new Ruolo("ADMIN");
        Ruolo r2 = new Ruolo("UTENTE");
        Collection collection = new HashSet();
        collection.add(r2);


        Utente Salvatore = new Utente("SNTSVT98P21E791U","Salvatore","Santella",data1,"M","zeus2115@gmail.com","Via Ponte dei cani","3314027048","waka",150,collection,"UTENTE");
        Utente Alex = new Utente("ALXTST98P54E897H","Alessandro","Testa",data2,"M","aletesta@hotmail.it","Via pozzuoli","3345685234","strong",200, collection,"UTENTE");

        Corso PL = new Corso("Powerlifting","Lunedi",tempo,120,3,6);

        Prenotazione P1 = new Prenotazione(p1,"Valida");
        Prenotazione P2 = new Prenotazione(p2,"Valida");

        Salvatore.getPrenotazioni().add(P1);
        P1.setUtente(Salvatore);


        Alex.getPrenotazioni().add(P2);
        P2.setUtente(Alex);


        utenteRepository.save(Salvatore);
        utenteRepository.save(Alex);
        corsoRepository.save(PL);
        prenotazioneRepository.save(P1);
        prenotazioneRepository.save(P2);

        System.out.println("Started in Bootstrap");
        System.out.println("Numero di Corsi: " +corsoRepository.count());
        System.out.println("Prenotazioni Totali: " + prenotazioneRepository.count());

         */

    }
}
