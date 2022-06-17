package ZeusFit.controller;

import ZeusFit.controller.dto.ListaPrenotazioniDto;
import ZeusFit.controller.dto.PrenotazioneDto;
import ZeusFit.controller.dto.SaldoDto;
import ZeusFit.model.Corso;
import ZeusFit.model.Lezione;
import ZeusFit.model.Prenotazione;
import ZeusFit.model.Utente;
import ZeusFit.repository.CorsoRepository;
import ZeusFit.repository.LezioneRepository;
import ZeusFit.repository.PrenotazioneRepository;
import ZeusFit.repository.UtenteRepository;
import ZeusFit.service.PrenotazioneService;
import ZeusFit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller

@RequestMapping("/homepage")
public class UserHomepageController {

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PrenotazioneService prenotazioneService;

    @Autowired
    private CorsoRepository corsoRepository;

    @Autowired
    private LezioneRepository lezioneRepository;

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @GetMapping
    public String showHomepage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("UTENTE"))) {

            String email = auth.getName();
            Utente utente = utenteRepository.findByEmail(email);

            ArrayList<Corso> listaCorsi = (ArrayList<Corso>) corsoRepository.findAll();

            //Nome utente e saldo mi servono per la navbar, la lista corsi per la visualizzazione dei corsi in homepage
            //Nome utente e saldo mi serviranno in tutte le pagine relative all'utente

            model.addAttribute("nome",utente.getNome());
            model.addAttribute("saldo",utente.getSaldo());
            model.addAttribute("corsi", listaCorsi);

            return "homepage";
        }
        else return "redirect:/login";
    }


    @GetMapping("/riepilogo-prenotazioni")
    public String viewprenotazioni(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("UTENTE"))) {

            //Cerco tutte le prenotazioni relative ad un determinato utente tramite l'id dell'utente

            String email = auth.getName();
            Utente utente = utenteRepository.findByEmail(email);
            ArrayList<Prenotazione> prenotazionissime = prenotazioneRepository.findByIdUtente(utente.getId());
            ArrayList<ListaPrenotazioniDto> prenotazioni = new ArrayList<ListaPrenotazioniDto>();

            ListaPrenotazioniDto prenotazione;
            for(int i = 0; i<prenotazionissime.size(); i++){
                prenotazione = new ListaPrenotazioniDto();
                prenotazione.setCorso(prenotazionissime.get(i).getLezione().getCorso().getNome());
                prenotazione.setGiorno(prenotazionissime.get(i).getLezione().getGiorno());
                prenotazione.setOrario(prenotazionissime.get(i).getLezione().getOrario());
                prenotazione.setSala(prenotazionissime.get(i).getLezione().getSala());
                prenotazione.setDurata(prenotazionissime.get(i).getLezione().getDurata());
                prenotazioni.add(prenotazione);
            }

            model.addAttribute("prenotazioni", prenotazioni);
            model.addAttribute("nome",utente.getNome());
            model.addAttribute("saldo",utente.getSaldo());
            return "riepilogo-prenotazioni";
        }
        else return "redirect:/login";
    }

    @GetMapping("/effettua-prenotazione/{id}/{idcorso}")
    public String prenotati(@PathVariable (value = "id") long id,@PathVariable (value ="idcorso") long idcorso){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("UTENTE"))) {

            String email = auth.getName();
            Utente utente = utenteRepository.findByEmail(email);
            long iduser = utente.getId();
            long idlez = id;
            Lezione lezione = lezioneRepository.findBy_Id(id);

            if (!prenotazioneService.prenotazione_exist(iduser, idlez)) {

                if (lezione.getNum_posti_disponibili() > 0) {

                    if (utente.getSaldo() > lezione.getCosto()) {

                        utente.setSaldo(utente.getSaldo() - lezione.getCosto());
                        lezione.setNum_posti_disponibili(lezione.getNum_posti_disponibili() - 1);
                        Prenotazione prenotazione = new Prenotazione(Calendar.getInstance().getTime().toString(), "valido");
                        utenteRepository.save(utente);
                        lezioneRepository.save(lezione);
                        System.out.println("Sono entrato nella zona critica");
                        prenotazione.setLezione(lezione);
                        prenotazione.setUtente(utente);
                        prenotazioneRepository.save(prenotazione);


                        return "redirect:/homepage/visualizza-lezioni-user/" + idcorso + "?success0";
                    } else
                        return "redirect:/homepage/visualizza-lezioni-user/" + idcorso + "?error3";
                } else
                    return "redirect:/homepage/visualizza-lezioni-user/" + idcorso + "?error2";

            } else return "redirect:/homepage/visualizza-lezioni-user/"+idcorso+"?error1";
        }
        else
            return "redirect:/login";
    }

    @GetMapping("/visualizza-lezioni-user/{id}")
    public String showlessons(@PathVariable (value="id") long id,Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("UTENTE"))){

            Corso corso = corsoRepository.findbyId(id);
            ArrayList<Lezione> lezioni = lezioneRepository.findByCorso(corso.getId());
            String email = auth.getName();
            Utente utente = utenteRepository.findByEmail(email);
            model.addAttribute("nome",utente.getNome());
            model.addAttribute("saldo",utente.getSaldo());
            model.addAttribute("corso",corso);
            model.addAttribute("lezioni",lezioni);
            model.addAttribute("id", id);

            return "visualizza-lezioni-user";
        }
        else
            return  "redirect:/login";
    }



    @GetMapping("/visualizza-abbonamenti")
    public String viewabbonamenti(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("UTENTE"))) {

            String email = auth.getName();
            Utente utente = utenteRepository.findByEmail(email);
            model.addAttribute("nome",utente.getNome());
            model.addAttribute("saldo",utente.getSaldo());

            //DA IMPLEMENTARE
            return "visualizza-abbonamenti";
        }
        else return "redirect:/login";
    }


    @ModelAttribute("ricaric")
    public SaldoDto saldoDto(){return new SaldoDto();}


    @GetMapping("/carica-saldo")
    public String carica_saldo(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("UTENTE"))) {

            String email = auth.getName();
            Utente utente = utenteRepository.findByEmail(email);
            model.addAttribute("nome",utente.getNome());
            model.addAttribute("saldo",utente.getSaldo());
            return "carica-saldo";
        }
        else return "redirect:/login";
    }

    @PostMapping("/carica-saldo")
    public String soldi(Model model,@ModelAttribute("ricaric") SaldoDto saldoDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("UTENTE"))) {

            // Prendo l'ammontare della ricarica inserito e lo aggiungo al saldo dell'utente
            String email = auth.getName();
            Utente utente = utenteRepository.findByEmail(email);
            model.addAttribute("nome",utente.getNome());
            model.addAttribute("saldo",utente.getSaldo());

            float temp = utente.getSaldo();
            utente.setSaldo(temp+saldoDto.getRicarica());
            utenteRepository.save(utente);

            return "redirect:/homepage/carica-saldo?success0";
        }
        else
            return "redirect:/login";
    }

}

