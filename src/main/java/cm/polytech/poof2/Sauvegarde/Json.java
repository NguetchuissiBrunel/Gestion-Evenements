package cm.polytech.poof2.Sauvegarde;

import cm.polytech.poof2.classes.Evenement;

import cm.polytech.poof2.classes.Participant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json implements Persistance {
    private final ObjectMapper objectMapper;

    public Json() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);


    }

    @Override
    public void saveEvenements(Map<String,Evenement> evenements) throws IOException {
        objectMapper.writerFor(new TypeReference<Map<String, Evenement>>() {})
                .withAttribute("type", "Evenement")
                .writeValue(new File("evenements.json"), evenements);
    }

    @Override
    public Map<String, Evenement> loadEvenements() throws IOException {
        File file = new File("evenements.json");
        if (!file.exists()) return new HashMap<>();
        return objectMapper.readValue(file, new TypeReference<Map<String, Evenement>>() {});
    }

    @Override
    public void saveOrganisateurs(Map<String,Participant> organisateurs) throws IOException {
        objectMapper.writerFor(new TypeReference<Map<String, Participant>>() {})
                .withAttribute("type", "Participant")
                .writeValue(new File("organisateurs.json"), organisateurs);
    }

    @Override
    public Map<String, Participant> loadOrganisateurs() throws IOException {
        File file = new File("organisateurs.json");
        if (!file.exists()) return new HashMap<>();
        return objectMapper.readValue(file, new TypeReference<Map<String, Participant>>() {});
    }

    @Override
    public void saveParticipants(List<Participant> participants) throws IOException {
        objectMapper.writerFor(new TypeReference<List<Participant>>() {})
                .withAttribute("type", "Participant")
                .writeValue(new File("participants.json"), participants);
    }

    @Override
    public List<Participant> loadParticipants() throws IOException {
        File file = new File("participants.json");
        if (!file.exists()) return new ArrayList<>();
        return objectMapper.readValue(file, new TypeReference<List<Participant>>() {});
    }
}